package be.miner.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class PluginFile extends YamlConfiguration {
    private File file;
    private String defaults;
    private JavaPlugin plugin;

    public PluginFile(JavaPlugin plugin, String fileName) {
        this(plugin, fileName, null);
    }

    public PluginFile(JavaPlugin plugin, String fileName, String defaultsName) {
        this.plugin = plugin;
        this.defaults = defaultsName;
        this.file = new File(plugin.getDataFolder(), fileName);
        reload();
    }

    public void reload() {
        if (!this.file.exists()) {
            try {
                boolean mkdirs = this.file.getParentFile().mkdirs();
                boolean newFile = this.file.createNewFile();
            } catch (IOException exception) {
                exception.printStackTrace();
                this.plugin.getLogger().severe("Error when creating file " + this.file.getName());
            }
        }

        try {
            load(this.file);

            if (this.defaults != null) {
                InputStreamReader reader = new InputStreamReader(this.plugin.getResource(this.defaults));
                FileConfiguration defaultsConfig = YamlConfiguration.loadConfiguration(reader);

                setDefaults(defaultsConfig);
                options().copyDefaults(true);

                reader.close();
                save();
            }
        } catch (IOException | InvalidConfigurationException exception) {
            exception.printStackTrace();
            this.plugin.getLogger().severe("Error when creating " + this.file.getName());
        }
    }


    public void save() {
        try {
            options().indent(2);
            save(this.file);
        } catch (IOException exception) {
            exception.printStackTrace();
            this.plugin.getLogger().severe("Erreur de la céation du document " + this.file.getName());
        }
    }
}
