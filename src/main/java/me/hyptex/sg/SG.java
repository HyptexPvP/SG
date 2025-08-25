package me.hyptex.sg;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import me.hyptex.sg.commands.BorderCommand;
import me.hyptex.sg.commands.ForceStartCommand;
import me.hyptex.sg.commands.SpawnPointsCommand;
import me.hyptex.sg.game.GameHandler;
import me.hyptex.sg.util.ConfigFile;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class SG extends JavaPlugin {


    private ConfigFile settingsFile, lootFile, scoreboardFile, messagesFile;
    private GameHandler gameHandler;

    @Override
    public void onEnable() {
        this.settingsFile = new ConfigFile(this, "settings");
        this.lootFile = new ConfigFile(this, "loot");
        this.scoreboardFile = new ConfigFile(this, "scoreboard");
        this.messagesFile = new ConfigFile(this, "messages");
        this.gameHandler = new GameHandler(this);

        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new SpawnPointsCommand(this));
        manager.registerCommand(new ForceStartCommand(this));
        manager.registerCommand(new BorderCommand(this));


    }

    @Override
    public void onDisable() {

    }
}
