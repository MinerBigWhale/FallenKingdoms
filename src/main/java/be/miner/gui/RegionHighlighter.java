package be.miner.gui;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.plugin.Plugin;

public class RegionHighlighter {
    private Plugin pl = Bukkit.getPluginManager().getPlugin("FallenKingdoms");
    private int _r = 0, _g = 0, _b = 0;
    private Location _center;
    private double _hLimit;
    private double _vLimit;
    private int _taskId;
    private int _blockId;
    private boolean _showState = false;
    private double _granularity = 0.5;
    private double _offset = 0.5;

    private double ax, ay, az;
    private double bx, by, bz;
    private double cx, cy, cz;
    private double dx, dy, dz;

    public RegionHighlighter(Location center, double horizontalLimit) {
        this(center, horizontalLimit, 0);
    }

    public RegionHighlighter(Location center, double horizontalLimit, double verticalLimit) {
        _center = center;
        _hLimit = horizontalLimit;
        _vLimit = verticalLimit;
        _showState = false;
    }

    public RegionHighlighter setColor(int R, int G, int B) {
        _r = R;
        _g = G;
        _b = B;
        return this;
    }

    public RegionHighlighter setColor(String color) {
        switch (color) {
            case "Blue":
                _r = 0;
                _g = 0;
                _b = 255;
                break;
            case "Red":
                _r = 255;
                _g = 0;
                _b = 0;
                break;
            case "Green":
                _r = 0;
                _g = 255;
                _b = 0;
                break;
            case "Yellow":
                _r = 255;
                _g = 255;
                _b = 0;
                break;
            default:
                throw new IllegalArgumentException("For custom color, use \"setColor(int R, int G, int B)\"");
        }
        return this;
    }


    public void showRegion() {
        if (_showState) throw new SecurityException("region is already shown");
        _showState = true;

        _taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(pl, () -> {
            ax = _center.getX() + _hLimit + 1.5;
            ay = _center.getY();
            az = _center.getZ() - _hLimit;
            bx = _center.getX() - _hLimit - 0.5;
            by = _center.getY();
            bz = _center.getZ() - _hLimit;
            cx = _center.getX() - _hLimit;
            cy = _center.getY();
            cz = _center.getZ() + _hLimit + 1.5;
            dx = _center.getX() - _hLimit;
            dy = _center.getY();
            dz = _center.getZ() - _hLimit - 0.5;
            for (double i = 0; i <= _hLimit * 2; i += _granularity) {
                az += _granularity;
                bz += _granularity;
                cx += _granularity;
                dx += _granularity;

                Particule.spawnColoredParticle(_center.getWorld(), Particle.REDSTONE, new Location(_center.getWorld(), ax, ay, az), _r, _g, _b);
                Particule.spawnColoredParticle(_center.getWorld(), Particle.REDSTONE, new Location(_center.getWorld(), bx, by, bz), _r, _g, _b);
                Particule.spawnColoredParticle(_center.getWorld(), Particle.REDSTONE, new Location(_center.getWorld(), cx, cy, cz), _r, _g, _b);
                Particule.spawnColoredParticle(_center.getWorld(), Particle.REDSTONE, new Location(_center.getWorld(), dx, dy, dz), _r, _g, _b);
            }
        }, 0L, 20L);
    }

    public void hideRegion() {
        Bukkit.getScheduler().cancelTask(_taskId);
        _showState = false;
        _taskId = 0;
    }

    public boolean isShown() {
        return _showState;
    }

}
