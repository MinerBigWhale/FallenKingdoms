package be.miner.utils;

import org.bukkit.Bukkit;

public class Console {
    public static void log (String message) {
        Bukkit.getConsoleSender().sendMessage(Prefix.getPrefix() + message);
    }
    public static void broadcast (String message) {
        Console.broadcast( message);
    }
}
