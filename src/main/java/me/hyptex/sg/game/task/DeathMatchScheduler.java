package me.hyptex.sg.game.task;

import me.hyptex.sg.SG;
import me.hyptex.sg.game.Phase;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathMatchScheduler extends BukkitRunnable {

    private final SG plugin;

    public DeathMatchScheduler(SG plugin) {
        this.plugin = plugin;

        this.runTaskLater(plugin, 20L * 60L * 1L);
    }

    @Override
    public void run() {

        if(plugin.getGameHandler().getPhase() == Phase.GAME) {
            plugin.getGameHandler().setPhase(Phase.DEATHMATCH);
            plugin.getGameHandler().startDeathMatch();
        }
    }
}
