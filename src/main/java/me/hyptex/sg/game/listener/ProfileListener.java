package me.hyptex.sg.game.listener;

import lombok.RequiredArgsConstructor;
import me.hyptex.sg.SG;
import me.hyptex.sg.game.Profile;
import me.hyptex.sg.util.PlayerUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class ProfileListener implements Listener {

    private final SG plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Profile profile = new Profile(event.getPlayer().getUniqueId());

        plugin.getGameHandler().getProfiles().put(event.getPlayer().getUniqueId(), profile);

        PlayerUtil.resetPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getGameHandler().getProfiles().remove(event.getPlayer().getUniqueId());
    }
}
