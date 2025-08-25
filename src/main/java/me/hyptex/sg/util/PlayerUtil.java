package me.hyptex.sg.util;

import lombok.experimental.UtilityClass;
import org.bukkit.GameMode;

@UtilityClass
public class PlayerUtil {

    public void resetPlayer(org.bukkit.entity.Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setSaturation(20.0f);
        player.setExp(0.0f);
        player.setLevel(0);
        player.setFireTicks(0);
        player.setFallDistance(0.0f);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        player.setGameMode(GameMode.SURVIVAL);
        player.closeInventory();
    }
}
