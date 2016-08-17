package be.miner.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class PluginLangFile extends PluginFile {

    public PluginLangFile(JavaPlugin plugin, String fileName) {
        this(plugin, fileName, null);
    }

    public PluginLangFile(JavaPlugin plugin, String fileName, String fromFileName) {
        setPlugin(plugin);
        setFile(new File(plugin.getDataFolder() + "/Lang", fileName));
        setFromFileFile(fromFileName);
        reload();
    }
}
