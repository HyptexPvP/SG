package me.hyptex.sg.game.loot;

import lombok.RequiredArgsConstructor;
import me.hyptex.sg.SG;
import me.hyptex.sg.game.Phase;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

@RequiredArgsConstructor
public class LootListener implements Listener {

    public final SG plugin;


    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (plugin.getGameHandler().getPhase() != Phase.GAME || event.getPlayer().getGameMode() != org.bukkit.GameMode.SURVIVAL) {
            event.setCancelled(true);
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block b = event.getClickedBlock();

        if (!(b.getState() instanceof Chest c)) return;
        event.setCancelled(true);

        Inventory inventory = c.getInventory();
        Location loc = (inventory.getHolder() instanceof DoubleChest dc) ? dc.getLocation()
                : c.getLocation();
        Inventory loot = plugin.getGameHandler().getLootManager().getOrCreate(loc, inventory.getSize());

        event.getPlayer().openInventory(loot);
    }
}
