package me.hyptex.sg.game.loot;

import me.hyptex.sg.SG;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class LootManager {

    public final SG plugin;
    private final int maxItemsPerChest;
    private final List<Material> possibleItems;
    private final Random random;
    public HashMap<Location, Inventory> chestLoot;

    public LootManager(SG plugin) {
        this.chestLoot = new HashMap<>();
        this.plugin = plugin;
        this.maxItemsPerChest = plugin.getSettingsFile().getInt("game.items-per-chest", 5);
        this.possibleItems = new ArrayList<>(plugin.getLootFile().getStringList("items").stream().map(String::toUpperCase).map(Material::valueOf).toList());
        this.random = ThreadLocalRandom.current();

        this.plugin.getServer().getPluginManager().registerEvents(new LootListener(plugin), plugin);
    }


    public Inventory getOrCreate(Location loc, int size) {
        Inventory inventory = chestLoot.get(loc);
        if (inventory == null) {
            inventory = plugin.getServer().createInventory(null, size, "Loot");

            List<Integer> slots = new ArrayList<>(size);

            for (int i = 0; i < size; i++) slots.add(i);

            Collections.shuffle(slots, random);

            int itemsToAdd = Math.min(maxItemsPerChest, possibleItems.size());
            for (int i = 0; i < itemsToAdd; i++) {
                Material mat = possibleItems.get(random.nextInt(possibleItems.size()));
                int amount = random.nextInt(1, 4);
                ItemStack itemStack = new ItemStack(mat, Math.min(mat.getMaxStackSize(), amount));

                inventory.setItem(slots.get(i), itemStack);
            }

            chestLoot.put(loc, inventory);
        }
        return inventory;
    }
}
