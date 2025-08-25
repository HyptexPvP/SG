package me.hyptex.sg.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Name;
import co.aikar.commands.annotation.Subcommand;
import lombok.RequiredArgsConstructor;
import me.hyptex.sg.SG;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@CommandAlias("border")
@CommandPermission("sg.admin")
public class BorderCommand extends BaseCommand {

    private final SG plugin;

    @Subcommand("create")
    public void createBorder(Player player, @Name("radius") int radius) {
        plugin.getGameHandler().getBorderManager().startBorder(player.getLocation(), radius);
        player.sendMessage("Created a border with radius " + radius);
    }

    @Subcommand("delete")
    public void deleteBorder(Player player) {
        plugin.getGameHandler().getBorderManager().deleteBorder();
        player.sendMessage("Deleted the border");
    }

    @Subcommand("resize")
    public void resizeBorder(Player player, @Name("radius") int radius) {
        if (plugin.getGameHandler().getBorderManager().getBorder() != null) {
            plugin.getGameHandler().getBorderManager().getBorder().setSize(radius * 2.0);
            player.sendMessage("Resized the border to radius " + radius);
        } else {
            player.sendMessage("No border to resize");
        }
    }

    @Subcommand("info")
    public void borderInfo(Player player) {
        if (plugin.getGameHandler().getBorderManager().getBorder() != null) {
            double size = plugin.getGameHandler().getBorderManager().getBorder().getSize();
            player.sendMessage("Current border size: " + size + " (radius: " + (size / 2.0) + ")");
        } else {
            player.sendMessage("No border set");
        }
    }

}
