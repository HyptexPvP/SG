package me.hyptex.sg.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import lombok.RequiredArgsConstructor;
import me.hyptex.sg.game.Phase;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class ForceStartCommand extends BaseCommand {
    public final me.hyptex.sg.SG plugin;

    @CommandAlias("forcestart")
    @CommandPermission("sg.forcestart")
    public void forceStart(Player player) {
        if(plugin.getGameHandler().getProfiles().size() < 2) {
            player.sendMessage("Not enough players to start the game.");
            return;
        }

        if(plugin.getGameHandler().getPhase() != Phase.GAME) {
            player.sendMessage("Game is already in progress.");
            return;
        }

        plugin.getGameHandler().getLobbyScheduler().start();
        player.sendMessage("Game starting...");
    }
}
