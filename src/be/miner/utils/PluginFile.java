package be.miner.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.Charset;

public class PluginFile extends YamlConfiguration {
    private File file;
    private JavaPlugin plugin;
    private String fromFile;

    public PluginFile(JavaPlugin plugin, String fileName) {
        this(plugin, fileName, null);
    }

    public PluginFile(JavaPlugin plugin, String fileName, String fromFileName) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), fileName);
        this.fromFile = fromFileName;
        reload();
    }

    public PluginFile() {
    }

    public void reload() {

        //create folders and files
        if (!this.file.exists()) {
            try {
                boolean mkdirs = this.file.getParentFile().mkdirs();
                boolean newFile = this.file.createNewFile();
                //fill with default content if needed
                if (fromFile != null) {
                    InputStreamReader reader = new InputStreamReader(this.plugin.getResource(this.fromFile));
                    PrintWriter writer = new PrintWriter(this.file);
                    try{
                        int readBytes;
                        char[] buffer = new char[4096];
                        while ((readBytes = reader.read(buffer)) > 0) {
                            writer.write(buffer, 0, readBytes);
                        }
                    } catch (Exception e) {
                        this.plugin.getLogger().severe("An error occured while filling file " + this.file.getName());
                    } finally {
                        reader.close();
                        writer.close();
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
                this.plugin.getLogger().severe("An error occured while creating file " + this.file.getName());
            }
        }

        //reload file content to memory
        try {
            load(this.file);
            InputStreamReader reader = new InputStreamReader(this.plugin.getResource(this.fromFile));
            try{
                FileConfiguration defaultsConfig = YamlConfiguration.loadConfiguration(reader);

                setDefaults(defaultsConfig);
                options().copyDefaults(true);
            } catch (Exception e) {
                this.plugin.getLogger().severe("An error occured while reloading file in memory " + this.file.getName());
            } finally {
                reader.close();
            }
        } catch (IOException | InvalidConfigurationException exception) {
            exception.printStackTrace();
            this.plugin.getLogger().severe("An error occured while reloading file in memory " + this.file.getName());
        }
    }


    public void save() {
        try {
            options().indent(2);
            save(this.file);
        } catch (IOException exception) {
            exception.printStackTrace();
            this.plugin.getLogger().severe("An error occured while saving file " + this.file.getName());
        }
    }
}
