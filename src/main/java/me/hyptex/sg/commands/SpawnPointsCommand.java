package me.hyptex.sg.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.RequiredArgsConstructor;
import me.hyptex.sg.SG;
import org.bukkit.entity.Player;

@CommandAlias("spawnpoints|sps")
@CommandPermission("sg.admin")
@RequiredArgsConstructor
public class SpawnPointsCommand extends BaseCommand {

    public final SG plugin;


    @Subcommand("add")
    public void add(Player sender) {
        plugin.getGameHandler().getSpawnManager().addLocation(sender.getLocation());
        sender.sendMessage("Added spawn point at your location.");
    }

    @Subcommand("list")
    public void list(Player sender) {
        int i = 1;
        for (var loc : plugin.getGameHandler().getSpawnManager().getLocations()) {
            sender.sendMessage(i + ": " + loc.toString());
            i++;
        }
    }

    @Subcommand("delete|del|remove|rm")
    public void delete(Player sender, int index) {
        var locations = plugin.getGameHandler().getSpawnManager().getLocations();
        if (index < 1 || index > locations.size()) {
            sender.sendMessage("Invalid index.");
            return;
        }
        plugin.getGameHandler().getSpawnManager().removeLocation(index - 1);
        sender.sendMessage("Removed spawn point at index " + index);
    }
}
