package me.hyptex.sg.game.loot;

import lombok.RequiredArgsConstructor;
import me.hyptex.sg.SG;
import me.hyptex.sg.game.Phase;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

@RequiredArgsConstructor
public class LootListener implements Listener {

    private final SG plugin;


    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block b = event.getClickedBlock();

        if (!(b.getState() instanceof Chest c)) return;

        if (plugin.getGameHandler().getPhase() != Phase.GAME && plugin.getGameHandler().getPhase() != Phase.DEATHMATCH) {
            return;
        }

        if (event.getPlayer().getGameMode() == GameMode.SPECTATOR) {
            event.setCancelled(true);
            return;
        }

        event.setCancelled(true);

        Inventory inventory = c.getInventory();
        Location loc = (inventory.getHolder() instanceof DoubleChest dc) ? dc.getLocation()
                : c.getLocation();
        Inventory loot = plugin.getGameHandler().getLootManager().getOrCreate(loc, inventory.getSize());


        event.getPlayer().openInventory(loot);
    }


}
