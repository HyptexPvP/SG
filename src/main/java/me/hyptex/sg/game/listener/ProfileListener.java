package me.hyptex.sg.game.listener;

import lombok.RequiredArgsConstructor;
import me.hyptex.sg.SG;
import me.hyptex.sg.game.Phase;
import me.hyptex.sg.game.Profile;
import me.hyptex.sg.util.CC;
import me.hyptex.sg.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class ProfileListener implements Listener {

    private final SG plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = new Profile(player.getUniqueId());

        plugin.getGameHandler().getProfiles().put(event.getPlayer().getUniqueId(), profile);

        if(plugin.getGameHandler().getSpawnManager().getLobby() != null) player.teleport(plugin.getGameHandler().getSpawnManager().getLobby());
        PlayerUtil.resetPlayer(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getGameHandler().getProfiles().remove(event.getPlayer().getUniqueId());

        if(plugin.getGameHandler().getPhase() == Phase.GAME || plugin.getGameHandler().getPhase() == Phase.DEATHMATCH) {
            Bukkit.broadcastMessage(CC.translate(plugin.getMessagesFile().getString("death.quit")));
        }
    }
}
