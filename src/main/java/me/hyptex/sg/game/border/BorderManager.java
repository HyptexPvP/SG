package me.hyptex.sg.game.border;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.hyptex.sg.SG;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;

@RequiredArgsConstructor
@Getter
public class BorderManager {

    private final SG plugin;
    public WorldBorder border;

    public void startBorder(Location center, double radius) {
        World w = center.getWorld();
        WorldBorder wb = w.getWorldBorder();
        this.border = wb;

        wb.setCenter(center);
        wb.setDamageBuffer(0.0);
        wb.setDamageAmount(1.0);
        wb.setWarningDistance(8);
        wb.setWarningTime(5);

        wb.setSize(radius * 2.0);
    }

    public void deleteBorder() {
        if (this.border != null) {
            this.border.reset();
        }
    }
}
