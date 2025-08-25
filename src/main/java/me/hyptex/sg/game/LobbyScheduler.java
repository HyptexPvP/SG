package me.hyptex.sg.game;

import lombok.Getter;
import me.hyptex.sg.SG;
import me.hyptex.sg.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class LobbyScheduler {


    private final SG plugin;
    private final int minPlayers;
    private final int secondsTotal;

    @Getter
    public int secondsLeft = 0;
    private int taskId = -1;


    public LobbyScheduler(SG plugin, int minPlayers, int secondsTotal) {
        this.plugin = plugin;
        this.minPlayers = minPlayers;
        this.secondsTotal = secondsTotal;
    }

    public void start() {
        if (taskId != -1) return;
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::tick, 20L, 20L);
    }

    public void stop() {
        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
            taskId = -1;
        }
        this.plugin.getGameHandler().setPhase(Phase.IDLING);
        secondsLeft = 0;
    }

    private void tick() {
        int online = Bukkit.getOnlinePlayers().size();
        Phase phase = plugin.getGameHandler().getPhase();
        if (online < minPlayers) {
            if (phase == Phase.STARTING) {
                this.plugin.getGameHandler().setPhase(Phase.IDLING);
                secondsLeft = 0;
                say("Countdown cancelled. Not enough players.");
            }
            return;
        }

        if (phase == Phase.IDLING) {
            this.plugin.getGameHandler().setPhase(Phase.STARTING);
            secondsLeft = secondsTotal;
            say("Game starts in " + secondsLeft + "s");
            plugin.getGameHandler().teleport();
            return;
        }

        if (announceNow(secondsLeft)) say(secondsLeft + "...");
        secondsLeft--;

        if (secondsLeft <= 0) {
            secondsLeft = 0;
            say("Game is starting!");
            plugin.getGameHandler().start();
        }
    }

    private boolean announceNow(int s) {
        return s <= 5 || s == 10 || s == 15 || s == 30 || s % 60 == 0;
    }

    private void say(String msg) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle(CC.translate(msg), "");
        }
    }
}
