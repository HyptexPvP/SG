package me.hyptex.sg.scoreboard;

import fr.mrmicky.fastboard.FastBoard;
import me.hyptex.sg.SG;
import me.hyptex.sg.game.Phase;
import me.hyptex.sg.game.Profile;
import me.hyptex.sg.util.CC;
import me.hyptex.sg.util.ConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreboardTask extends BukkitRunnable {

    public SG plugin;
    public final ConfigFile scoreboardConfig;

    public ScoreboardTask(SG plugin) {
        this.plugin = plugin;

        this.scoreboardConfig = plugin.getScoreboardFile();
        plugin.getServer().getPluginManager().registerEvents(new ScoreboardListener(), plugin);

        this.runTaskTimer(plugin, 2L, 20L);
    }

    @Override
    public void run() {
        for (FastBoard board : ScoreboardListener.getBoards().values()) {
            if (board == null || board.getPlayer() == null) continue;

            Profile profile = plugin.getGameHandler().getProfiles().get(board.getPlayer().getUniqueId());

            board.updateTitle(CC.translate(scoreboardConfig.getString("title")));

            if(plugin.getGameHandler().getPhase() == Phase.IDLING) {
                board.updateLines(
                        scoreboardConfig.getStringList("lobby")
                                .stream()
                                .map(CC::translate)
                                .map(line -> line.replace("%players%", Bukkit.getOnlinePlayers().size() + "")
                                        .replace("%max_players%", Bukkit.getMaxPlayers() + "")
                                        .replace("%map%", plugin.getSettingsFile().getString("data.map")))
                                .toList()
                );
                continue;
            }
            if(plugin.getGameHandler().getPhase() == Phase.STARTING) {
                board.updateLines(
                        scoreboardConfig.getStringList("counting")
                                .stream()
                                .map(CC::translate)
                                .map(line -> line.replace("%players%", Bukkit.getOnlinePlayers().size() + "")
                                        .replace("%max_players%", Bukkit.getMaxPlayers() + "")
                                        .replace("%seconds%", plugin.getGameHandler().getLobbyScheduler().getSecondsLeft() + "")
                                        .replace("%map%", plugin.getSettingsFile().getString("data.map")))
                                .toList()
                );
                continue;
            }

            if(plugin.getGameHandler().getPhase() == Phase.DONE) {
                board.updateLines(
                        scoreboardConfig.getStringList("ending")
                                .stream()
                                .map(CC::translate)
                                .map(line -> line.replace("%players%", Bukkit.getOnlinePlayers().size() + "")
                                        .replace("%max_players%", Bukkit.getMaxPlayers() + "")
                                        .replace("%map%", plugin.getSettingsFile().getString("data.map")
                                                .replace("%winner%", plugin.getGameHandler().getProfiles().values().stream().filter(Profile::isAlive).findFirst().map(p -> {
                                                    if (Bukkit.getPlayer(p.getUuid()) != null) {
                                                        return Bukkit.getPlayer(p.getUuid()).getName();
                                                    }
                                                    return "Unknown";
                                                }).orElse("Unknown"))))
                                .toList()
                );
                continue;
            }

            if (profile.isAlive()) {
                board.updateLines(
                        scoreboardConfig.getStringList("in-game")
                                .stream()
                                .map(CC::translate)
                                .map(line -> line.replace("%kills%", profile.getKills() + "")
                                        .replace("%players%", plugin.getGameHandler().getProfiles().values().stream().filter(Profile::isAlive).count() + "")
                                        .replace("%max_players%", Bukkit.getMaxPlayers() + "")
                                        .replace("%map%", plugin.getSettingsFile().getString("data.map")))
                                .toList()
                );
            } else {
                board.updateLines(
                        scoreboardConfig.getStringList("spectating")
                                .stream()
                                .map(CC::translate)
                                .map(line -> line.replace("%kills%", profile.getKills() + "")
                                        .replace("%players%", plugin.getGameHandler().getProfiles().values().stream().filter(Profile::isAlive).count() + "")
                                        .replace("%max_players%", Bukkit.getMaxPlayers() + "")
                                        .replace("%map%", plugin.getSettingsFile().getString("data.map")))
                                .toList()
                );

            }
        }
    }
}
