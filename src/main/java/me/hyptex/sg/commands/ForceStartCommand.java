package me.hyptex.sg.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ForceStartCommand extends BaseCommand {
    public final me.hyptex.sg.SG plugin;

    @CommandAlias("forcestart")
    public void forceStart() {
        plugin.getGameHandler().getLobbyScheduler().start();
    }
}
