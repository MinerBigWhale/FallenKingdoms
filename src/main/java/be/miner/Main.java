package be.miner;

import be.miner.commands.FkCommand;
import be.miner.data.Base;
import be.miner.data.Game;
import be.miner.data.Timer;
import be.miner.events.*;
import be.miner.gui.IconMenu;
import be.miner.utils.Message;
import be.miner.utils.PluginFile;
import be.miner.utils.PluginLangFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Main extends JavaPlugin {

    public static IconMenu menu;
    private static PluginFile fileConfig;
    private static PluginFile fileBlock;
    private static PluginLangFile fileText;
    private static final Plugin pl = Bukkit.getPluginManager().getPlugin("FallenKingdoms");

    public static PluginFile getConfigFile () {
        return fileConfig;
    }

    public static PluginFile getBlockFile () {
        return fileBlock;
    }

    public static PluginFile getLangFile () {
        return fileText;
    }

    public void onEnable () {
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

        //print messages in Admin consoles
        Message.log(ChatColor.DARK_RED + "[IMPORTANT] Do not forget to setup bases locations !");
        Message.log(ChatColor.DARK_RED + "            Reloading the plugin is needed after that !");
        Message.log(ChatColor.YELLOW + "Developped by:" + ChatColor.WHITE + " MinerBigWhale, " + ChatColor.YELLOW + " inspired by:" + ChatColor.WHITE + " TeaB0" + ChatColor.YELLOW + "'s plugin");

        Message.log(ChatColor.YELLOW + "Loading " + ChatColor.BLUE + "'config.yml'");
        fileConfig = new PluginFile(this, "config.yml", "config.yml");
        for (String baseName : fileConfig.getConfigurationSection("BaseList").getKeys(false)) {
            if (fileConfig.getBoolean("BaseList." + baseName)) {
                Game.addBase(new Base(baseName));
            }
        }

        ArrayList<String>  blocks = new ArrayList<String>();
        Message.log(ChatColor.YELLOW + "Loading " + ChatColor.BLUE + "'blocklist.yml'");
        fileBlock = new PluginFile(this, "blocklist.yml", "blocklist.yml");
        for (String materialName : fileBlock.getConfigurationSection("blocklist").getKeys(false)) {
            if (fileBlock.getBoolean("blocklist." + materialName)) {
                Game.addBlock(materialName);
                blocks.add(materialName);
            }
        }
        Message.log(ChatColor.GREEN + "Blocks added to the " + ChatColor.GRAY + "blacklist :");
        Message.log(ChatColor.YELLOW + "  " + String.join(", ",blocks));

        Message.log(ChatColor.YELLOW + "Loading " + ChatColor.BLUE + "'"+ fileConfig.getString("lang") + ".yml'");
        fileText = new PluginLangFile(this, fileConfig.getString("lang") + ".yml");

        Message.log(ChatColor.GREEN + " [ON] Plugin switched on !");
    }

    public void onDisable () {
        //Clear Game's objects
        Game.clearAll();
        if (Game.isRunning()) Timer.stopFk(this);
        Message.log(ChatColor.RED + " [OFF] Plugin switched off !");
    }
}