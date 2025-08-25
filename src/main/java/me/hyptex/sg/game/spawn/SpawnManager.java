package me.hyptex.sg.game.spawn;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.hyptex.sg.SG;
import me.hyptex.sg.util.LocationSerializer;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
public class SpawnManager {

    @Getter
    private List<Location> locations;
    private final SG plugin;

    public SpawnManager(SG plugin) {
        this.plugin = plugin;
        this.locations = new ArrayList<>();

        plugin.getSettingsFile().getStringList("game.spawn-points").forEach(s -> {
            this.locations.add(LocationSerializer.read(s));
        });
    }

    public void addLocation(Location location) {
        locations.add(location);

        List<String> list = plugin.getSettingsFile().getStringList("game.spawn-points");
        list.add(LocationSerializer.write(location));
        plugin.getSettingsFile().set("game.spawn-points", list);
        plugin.getSettingsFile().save();
    }

    public void removeLocation(int index) {
        if (index < 0 || index >= locations.size()) return;

        locations.remove(index);

        List<String> list = plugin.getSettingsFile().getStringList("game.spawn-points");
        if (index < list.size()) list.remove(index);
        plugin.getSettingsFile().set("game.spawn-points", list);   // IMPORTANT
        plugin.getSettingsFile().save();
    }


}
