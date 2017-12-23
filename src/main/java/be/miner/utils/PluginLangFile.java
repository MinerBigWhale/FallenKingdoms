package be.miner.utils;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

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
