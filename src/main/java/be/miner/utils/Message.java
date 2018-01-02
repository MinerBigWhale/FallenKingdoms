package be.miner.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Message {
    public static void log (String message) {
        Bukkit.getConsoleSender().sendMessage(Prefix.getPrefix() + message);
    }
    public static void broadcast (String message) {
        Bukkit.broadcastMessage(Prefix.getPrefix() + message);
    }
    public static void send (Player player, String message) { player.sendMessage(Prefix.getPrefix() + message); }
}
