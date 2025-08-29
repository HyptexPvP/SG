package me.hyptex.sg.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import lombok.RequiredArgsConstructor;
import me.hyptex.sg.SG;
import me.hyptex.sg.util.LocationSerializer;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class SpawnCommand extends BaseCommand {

    private final SG plugin;

    @CommandAlias("setspawn")
    @CommandPermission("sg.admin")
    public void setSpawn(Player player) {
        plugin.getSettingsFile().set("lobby.spawn", LocationSerializer.write(player.getLocation()));
        plugin.getSettingsFile().save();

        player.sendMessage("Set lobby spawn to your current location.");
    }
}
