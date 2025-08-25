package me.hyptex.sg.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
@UtilityClass
public class CC {

    public static String translate(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
