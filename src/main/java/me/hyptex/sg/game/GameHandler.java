package me.hyptex.sg.game;

import lombok.Getter;
import lombok.Setter;
import me.hyptex.sg.SG;
import me.hyptex.sg.game.border.BorderManager;
import me.hyptex.sg.game.listener.GameListener;
import me.hyptex.sg.game.listener.ProfileListener;
import me.hyptex.sg.game.loot.LootManager;
import me.hyptex.sg.game.spawn.SpawnManager;
import me.hyptex.sg.game.task.DeathMatchScheduler;
import me.hyptex.sg.game.task.LobbyScheduler;
import me.hyptex.sg.game.task.WinnerTask;
import me.hyptex.sg.scoreboard.ScoreboardTask;
import me.hyptex.sg.util.CC;
import me.hyptex.sg.util.LocationSerializer;
import me.hyptex.sg.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Getter
public class GameHandler {

    public final BorderManager borderManager;
    public final SpawnManager spawnManager;
    private final SG plugin;
    private final LobbyScheduler lobbyScheduler;
    public final LootManager lootManager;
    public ConcurrentMap<UUID, Profile> profiles;
    public final ScoreboardTask scoreboardTask;
    private DeathMatchScheduler deathMatchScheduler;
    @Setter
    public Phase phase = Phase.IDLING;
    public final WinnerTask winnerTask;


    public GameHandler(SG plugin) {
        this.plugin = plugin;
        this.borderManager = new BorderManager(plugin);
        this.spawnManager = new SpawnManager(plugin);
        this.lootManager = new LootManager(plugin);
        this.scoreboardTask = new ScoreboardTask(plugin);
        this.lobbyScheduler = new LobbyScheduler(plugin, 2, 10);
        this.winnerTask = new WinnerTask(plugin);

        plugin.getServer().getPluginManager().registerEvents(new GameListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ProfileListener(plugin), plugin);
        if (plugin.getSettingsFile().getBoolean("game.auto-start")) {
            this.lobbyScheduler.start();
        }
        this.profiles = new ConcurrentHashMap<>();

        if(plugin.getSettingsFile().getString("border.center") != null) {
            this.borderManager.startBorder(
                    LocationSerializer.read(plugin.getSettingsFile().getString("border.center")),
                    plugin.getSettingsFile().getInt("border.radius", 100)
            );
        }

    }

    public void start() {
        this.lobbyScheduler.stop();
        this.phase = Phase.GAME;
        this.deathMatchScheduler = new DeathMatchScheduler(plugin);
    }

    public void end() {
        this.phase = Phase.DONE;
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().clear();
            player.setGameMode(org.bukkit.GameMode.ADVENTURE);
            PlayerUtil.resetPlayer(player);
        }
        List<Profile> winners = plugin.getGameHandler().getProfiles().values()
                .stream().filter(Profile::isAlive)
                .toList();

        Profile winner = winners.get(0);
        Player player = Bukkit.getPlayer(winner.getUuid());
        if (player != null) {
            Bukkit.broadcastMessage(CC.translate(plugin.getMessagesFile().getString("winner.winner-message")
                    .replace("%player%", player.getName())));

            player.sendTitle(CC.translate(plugin.getMessagesFile().getString("winner.winner-title")), CC.translate(plugin.getMessagesFile().getString("winner.winner-subtitle")));
        }
        profiles.clear();

        Bukkit.getScheduler().runTaskLater(plugin, Bukkit::shutdown, 5 * 20L);
    }


    public void teleport() {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

        if (players.size() > spawnManager.getLocations().size()) {
            throw new IllegalStateException("Not enough spawn locations for players");
        }

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            PlayerUtil.resetPlayer(player);

            Location loc = spawnManager.getLocations().get(i);

            player.teleport(loc);

        }
    }


    public void startDeathMatch() {
        borderManager.setDeathMatchBorder();
        List<Player> players = profiles.values().stream().filter(Profile::isAlive).map(profile -> Bukkit.getPlayer(profile.getUuid())).toList();

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            Location loc = spawnManager.getLocations().get(i);

            player.teleport(loc);
            player.sendTitle(CC.translate(plugin.getMessagesFile().getString("deathmatch.title")), CC.translate(plugin.getMessagesFile().getString("deathmatch.subtitle")));
        }




    }
}
