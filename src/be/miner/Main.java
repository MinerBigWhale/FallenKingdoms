package be.miner;

import be.miner.commands.FkCommand;
import be.miner.data.Timer;
import be.miner.events.*;
import be.miner.gui.IconMenu;
import be.miner.utils.PluginFile;
import be.miner.data.Game;
import be.miner.utils.PluginLangFile;
import be.miner.utils.Prefix;
import org.bukkit.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import be.miner.data.Base;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.sql.Time;

public class Main extends JavaPlugin {

    private static PluginFile fileConfig;
    private static PluginFile fileBlock;
    private static PluginLangFile fileText;
    private static Plugin pl = Bukkit.getPluginManager().getPlugin("FkPlugin");
    public static IconMenu menu;

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
        fileText = new PluginLangFile(this, this.getConfigFile().getString("lang") + ".yml");

        //print messages in Admin consoles
        Bukkit.getConsoleSender().sendMessage(Prefix.getPrefix() + ChatColor.DARK_RED + "[IMPORTANT] Ne pas oublier de configurer les coordonées des bases !");
        Bukkit.getConsoleSender().sendMessage(Prefix.getPrefix() + ChatColor.DARK_RED + "            Pour cela il vous faudrat éteindre le serveur !");
        Bukkit.getConsoleSender().sendMessage(Prefix.getPrefix() + ChatColor.YELLOW + "Developped by:" + ChatColor.WHITE + " MinerBigWhale, " + ChatColor.YELLOW + " inspired by:" + ChatColor.WHITE + " TeaB0" + ChatColor.YELLOW + "'s plugin");

        //Load Base objects
        for (String baseName: this.getConfigFile().getConfigurationSection("BaseList").getKeys(false)) {
            if (this.getConfigFile().getBoolean("BaseList." + baseName)) {
                Game.addBase(new Base(baseName));
            }
        }

        //Load Blocks
        for (String materialName: this.getBlockFile().getConfigurationSection("blocklist").getKeys(false)) {
            if (this.getBlockFile().getBoolean(materialName)) {
                Game.addBlock(materialName);
            }
        }

        Bukkit.getConsoleSender().sendMessage(Prefix.getPrefix() + ChatColor.GREEN + " [ON] Plugin switched on !");
    }

    public void onDisable() {
        //Clear Game's objects
        Game.clearAll();
        Timer.stopFk(this);
        Bukkit.getConsoleSender().sendMessage(Prefix.getPrefix() + ChatColor.RED + " [OFF] Plugin switched off !");
    }

    public static PluginFile getConfigFile(){ return fileConfig; }
    public static PluginFile getBlockFile(){ return fileBlock; }
    public static PluginFile getLangFile(){ return (PluginFile) fileText; }
}