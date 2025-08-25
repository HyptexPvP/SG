package me.hyptex.sg.game.task;

import me.hyptex.sg.SG;
import me.hyptex.sg.game.Phase;
import me.hyptex.sg.game.Profile;
import org.bukkit.scheduler.BukkitRunnable;

public class WinnerTask extends BukkitRunnable {

    private final SG plugin;

    public WinnerTask(SG plugin) {
        this.plugin = plugin;
        this.runTaskTimer(plugin, 20L, 20L);
    }


    @Override
    public void run() {
        if (plugin.getGameHandler().getProfiles().values().stream().filter(Profile::isAlive).toList().size() <= 1 && plugin.getGameHandler().getPhase() == Phase.GAME) {
            plugin.getGameHandler().end();
            this.cancel();
        }
    }
}
