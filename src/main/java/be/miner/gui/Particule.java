package be.miner.gui;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Particule {
    private Particle _particule;
    private double _red = 0.001;
    private double _green = 0.001;
    private double _blue = 0.001;
    private double _xOffset = 0;
    private double _yOffset = 0;
    private double _zOffset = 0;
    private int _particuleData;
    private int _particuleMetaData;

    public Particule(Particle particule, int particuleData, int particuleMetaData) {
        create(particule, 0, 0, 0, particuleData, particuleMetaData);
    }

    public Particule(Particle particule, int particuleData) {
        create(particule, 0, 0, 0, particuleData, 0);
    }

    public Particule(Particle particule) {
        create(particule, 0, 0, 0, 0, 0);
    }

    public Particule(Particle particule, Location location, float xOffset, float yOffset, float zOffset, int particuleData, int particuleMetaData) {
        create(particule, xOffset, yOffset, zOffset, particuleData, particuleMetaData);
    }

    public Particule(Particle particule, Location location, float xOffset, float yOffset, float zOffset, int particuleData) {
        create(particule, xOffset, yOffset, zOffset, particuleData, 0);
    }

    public Particule(Particle particule, float xOffset, float yOffset, float zOffset) {
        create(particule, xOffset, yOffset, zOffset, 0, 0);
    }

    public Particule(Particle colorableParticule, int R, int G, int B) {
        if (colorableParticule != Particle.SPELL_MOB && colorableParticule != Particle.SPELL_MOB_AMBIENT && colorableParticule != Particle.REDSTONE) {
            throw new IllegalArgumentException("This particule is not colorable");
        }
        _particule = colorableParticule;
        _red = map((double) R, 0, 255, 0.001, 1);
        _green = map((double) G, 0, 255, 0.001, 1);
        _blue = map((double) B, 0, 255, 0.001, 1);
    }

    public static void spawnColoredParticle(World world, Particle particule, Location location, int R, int G, int B) {
        double fR = map((double) R, 0, 255, 0.001, 1);
        double fG = map((double) G, 0, 255, 0.001, 1);
        double fB = map((double) B, 0, 255, 0.001, 1);
        world.spawnParticle(particule, location.getX(), location.getY(), location.getZ(), 0, fR, fG, fB, 1);
    }

    public static void spawnColoredParticle(Player player, Particle particule, Location location, int R, int G, int B) {
        double fR = map((double) R, 0, 255, 0.001, 1);
        double fG = map((double) G, 0, 255, 0.001, 1);
        double fB = map((double) B, 0, 255, 0.001, 1);
        player.spawnParticle(particule, location.getX(), location.getY(), location.getZ(), 0, fR, fG, fB, 1);
    }

    private static double map(double value, double fromOrigin, double toOrigin, double fromDest, double toDest) {
        return (value - fromOrigin) / (toOrigin - fromOrigin) * (toDest - fromDest) + fromDest;
    }

    private void create(Particle particule, float xOffset, float yOffset, float zOffset, int particuleData, int particuleMetaData) {
        _particule = particule;
        _particuleData = particuleData;
        _particuleMetaData = particuleMetaData;
        _xOffset = xOffset;
        _yOffset = yOffset;
        _zOffset = zOffset;
    }

    public void spawn(World world, Location location) {
        world.spawnParticle(_particule, location.getX(), location.getY(), location.getZ(), 0, _red, _green, _blue, 1);
    }

    //TODO: spawn normal particule

    public void spawn(Player player, Location location) {
        player.spawnParticle(_particule, location.getX(), location.getY(), location.getZ(), 0, _red, _green, _blue, 1);
    }
}