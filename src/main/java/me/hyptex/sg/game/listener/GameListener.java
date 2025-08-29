package me.hyptex.sg.game.listener;

import lombok.RequiredArgsConstructor;
import me.hyptex.sg.SG;
import me.hyptex.sg.game.Phase;
import me.hyptex.sg.game.Profile;
import me.hyptex.sg.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.projectiles.ProjectileSource;

@RequiredArgsConstructor
public class GameListener implements Listener {

    private final SG plugin;

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (plugin.getGameHandler().getPhase() == Phase.STARTING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (plugin.getGameHandler().getPhase() == Phase.STARTING || plugin.getGameHandler().getPhase() == Phase.IDLING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent event) {
        if (plugin.getGameHandler().getPhase() == Phase.GAME || plugin.getGameHandler().getPhase() == Phase.DEATHMATCH) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Game in progress");
        }
    }


    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (plugin.getGameHandler().getPhase() == Phase.GAME || plugin.getGameHandler().getPhase() == Phase.DEATHMATCH) {
            Location location = event.getEntity().getLocation();

            location.getWorld().strikeLightningEffect(location);

            Player player = event.getEntity();
            Profile profile = plugin.getGameHandler().getProfiles().get(player.getUniqueId());
            profile.setAlive(false);

            player.spigot().respawn();

            player.setGameMode(GameMode.SPECTATOR);

            event.setDeathMessage(null);

            Player killer = player.getKiller();

            if (killer == null) {
                EntityDamageEvent last = player.getLastDamageCause();
                if (last instanceof EntityDamageByEntityEvent edbe) {
                    Entity damager = edbe.getDamager();
                    if (damager instanceof Player) {
                        killer = (Player) damager;
                    } else if (damager instanceof Projectile) {
                        ProjectileSource src = ((Projectile) damager).getShooter();
                        if (src instanceof Player) killer = (Player) src;
                    }
                }
            }

            if (killer != null && killer != player) {
                Profile killerProfile = plugin.getGameHandler().getProfiles().get(killer.getUniqueId());

                killerProfile.setKills(killerProfile.getKills() + 1);

                Bukkit.broadcastMessage(CC.translate(plugin.getMessagesFile().getString("death.player-killed")
                        .replace("%player%", player.getName())
                        .replace("%killer%", killer.getName())
                        .replace("%killer_kills%", killerProfile.getKills() + "")
                        .replace("%player_kills%", profile.getKills() + "")
                        .replace("%weapon%", pretty(killer.getItemInHand().getType().name()))));

            } else {
                switch (event.getEntity().getLastDamageCause().getCause()) {
                    case FALL -> Bukkit.broadcastMessage(CC.translate(plugin.getMessagesFile().getString("death.fell")
                            .replace("%player%", player.getName())
                            .replace("%player_kills%", profile.getKills() + "")));
                    case FIRE, FIRE_TICK, LAVA ->
                            Bukkit.broadcastMessage(CC.translate(plugin.getMessagesFile().getString("death.burned")
                                    .replace("%player%", player.getName())
                                    .replace("%player_kills%", profile.getKills() + "")));
                    default ->
                            Bukkit.broadcastMessage(CC.translate(plugin.getMessagesFile().getString("death.player-died")
                                    .replace("%player%", player.getName())));
                }

                player.sendTitle(CC.translate(plugin.getMessagesFile().getString("death.death-title")), CC.translate(plugin.getMessagesFile().getString("death.subtitle")));
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        player.setAllowFlight(true);
        player.setFlying(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) event.setCancelled(true);
    }

    private String pretty(String matName) {
        String lower = matName.toLowerCase(java.util.Locale.ROOT).replace('_', ' ');
        String[] parts = lower.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            if (p.isEmpty()) continue;
            sb.append(Character.toUpperCase(p.charAt(0))).append(p.substring(1)).append(' ');
        }
        return sb.toString().trim();
    }


}
