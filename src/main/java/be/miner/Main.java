package be.miner;

import be.miner.commands.FkCommand;
import be.miner.data.Base;
import be.miner.data.Game;
import be.miner.data.Timer;
import be.miner.events.*;
import be.miner.gui.IconMenu;
import be.miner.utils.Console;
import be.miner.utils.PluginFile;
import be.miner.utils.PluginLangFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static IconMenu menu;
    private static PluginFile fileConfig;
    private static PluginFile fileBlock;
    private static PluginLangFile fileText;
    private static Plugin pl = Bukkit.getPluginManager().getPlugin("FallenKingdoms");

    public static PluginFile getConfigFile() {
        return fileConfig;
    }

    public static PluginFile getBlockFile() {
        return fileBlock;
    }

    public static PluginFile getLangFile() { return fileText; }

    public void onEnable() {
        //handle commands
        CommandExecutor ce = new FkCommand();
        getCommand("fk").setExecutor(ce);

        //map events
        Bukkit.getPluginManager().registerEvents(new JoinEvent(), this);
        Bukkit.getPluginManager().registerEvents(new BlockEvent(), this);
        Bukkit.getPluginManager().registerEvents(new ChatEvent(), this);
        Bukkit.getPluginManager().registerEvents(new DamageEvent(), this);
        Bukkit.getPluginManager().registerEvents(new MoveEvent(), this);
        menu = new IconMenu();
        Bukkit.getPluginManager().registerEvents(new MenuEvent(), this);

        //Load lang files
        new PluginLangFile(this, "fr-be.yml", "fr-be.yml");
        new PluginLangFile(this, "en-uk.yml", "en-uk.yml");

        //Load files
        fileConfig = new PluginFile(this, "config.yml", "config.yml");
        fileBlock = new PluginFile(this, "blocklist.yml", "blocklist.yml");
        fileText = new PluginLangFile(this, getConfigFile().getString("lang") + ".yml");

        //print messages in Admin consoles
        Console.log(ChatColor.DARK_RED + "[IMPORTANT] Ne pas oublier de configurer les coordonées des bases !");
        Console.log(ChatColor.DARK_RED + "            Pour cela il vous faudrat éteindre le serveur !");
        Console.log(ChatColor.YELLOW + "Developped by:" + ChatColor.WHITE + " MinerBigWhale, " + ChatColor.YELLOW + " inspired by:" + ChatColor.WHITE + " TeaB0" + ChatColor.YELLOW + "'s plugin");

        //Load Base objects
        for (String baseName : getConfigFile().getConfigurationSection("BaseList").getKeys(false)) {
            if (getConfigFile().getBoolean("BaseList." + baseName)) {
                Game.addBase(new Base(baseName));
            }
        }

        //Load Blocks
        for (String materialName : getBlockFile().getConfigurationSection("blocklist").getKeys(false)) {
            if (getBlockFile().getBoolean(materialName)) {
                Game.addBlock(materialName);
            }
        }

        Console.log(ChatColor.GREEN + " [ON] Plugin switched on !");
    }

    public void onDisable() {
        //Clear Game's objects
        Game.clearAll();
        Timer.stopFk(this);
        Console.log(ChatColor.RED + " [OFF] Plugin switched off !");
    }
}