package be.miner.utils;

import net.minecraft.server.v1_10_R1.EnumParticle;
import net.minecraft.server.v1_10_R1.PacketPlayOutWorldParticles;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Particule {
    private PacketPlayOutWorldParticles _packet;

    public Particule(PacketPlayOutWorldParticles packet){
        _packet = packet;
    }

    public static Particule create(EnumParticle particule, Location location, int particuleData, int particuleMetaData) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particule, false, (float) location.getX(), (float) location.getY(), (float) location.getZ(), 0, 0, 0, particuleData, 1, particuleMetaData);
        return new Particule(packet);
    }
    public static Particule create(EnumParticle particule, Location location, int particuleData) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particule, false, (float) location.getX(), (float) location.getY(), (float) location.getZ(), 0, 0, 0, particuleData, 1, null);
        return new Particule(packet);
    }
    public static Particule create(EnumParticle particule, Location location) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particule, false, (float) location.getX(), (float) location.getY(), (float) location.getZ(), 0, 0, 0, 0, 1, null);
        return new Particule(packet);
    }

    public static Particule create(EnumParticle particule, Location location, float xOffset, float yOffset, float zOffset, int particuleData, int particuleMetaData) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particule, false, (float) location.getX() + xOffset, (float) location.getY() + yOffset, (float) location.getZ() + zOffset, 0, 0, 0, particuleData, 1, particuleMetaData);
        return new Particule(packet);
    }
    public static Particule create(EnumParticle particule, Location location, float xOffset, float yOffset, float zOffset, int particuleData) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particule, false, (float) location.getX() + xOffset, (float) location.getY() + yOffset, (float) location.getZ() + zOffset, 0, 0, 0, particuleData, 1, null);
        return new Particule(packet);
    }
    public static Particule create(EnumParticle particule, Location location, float xOffset, float yOffset, float zOffset) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particule, false, (float) location.getX() + xOffset, (float) location.getY() + yOffset, (float) location.getZ() + zOffset, 0, 0, 0, 0, 1, null);
        return new Particule(packet);
    }

    public static Particule createColored(EnumParticle particule, Location location, int R, int G, int B) {
        if (particule != EnumParticle.SPELL_MOB && particule != EnumParticle.SPELL_MOB_AMBIENT && particule != EnumParticle.REDSTONE) {
                throw new IllegalArgumentException("This particule is not colorable");
        }
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particule, false, (float) location.getX(), (float) location.getY(), (float) location.getZ(), (((float)R)/255 -1), ((float)G)/255, ((float)B)/255, 1, 0, null);
        return new Particule(packet);
    }


    public void sendAll(){
        for (Player player : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(_packet);
        }
    }
    public void send(Player player){
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(_packet);
    }
}
