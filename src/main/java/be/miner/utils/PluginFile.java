package be.miner.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class PluginFile extends YamlConfiguration {
    private File file;
    private JavaPlugin plugin;
    private String fromFile;

    public PluginFile(JavaPlugin plugin, String fileName) {
        this(plugin, fileName, null);
    }

    public PluginFile(JavaPlugin plugin, String fileName, String fromFileName) {
        this.plugin = plugin;
        file = new File(plugin.getDataFolder(), fileName);
        fromFile = fromFileName;
        reload();
    }

    public PluginFile() {
    }

    protected void setFile(File file) {
        this.file = file;
    }

    protected void setFromFileFile(String fromFileName) {
        fromFile = fromFileName;
    }

    protected void setPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void reload() {

        //create folders and files
        if (!file.exists()) {
            try {
                boolean mkdirs = file.getParentFile().mkdirs();
                boolean newFile = file.createNewFile();
                //fill with default content if needed
                if (fromFile != null && plugin != null) {
                    InputStream in = plugin.getResource(fromFile);
                    if (in == null) {
                        throw new NullPointerException();
                    }
                    try (InputStreamReader reader = new InputStreamReader(in); PrintWriter writer = new PrintWriter(file)) {
                        int readBytes;
                        char[] buffer = new char[4096];
                        while ((readBytes = reader.read(buffer)) > 0) {
                            writer.write(buffer, 0, readBytes);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Console.log("An error occured while filling file " + file.getName());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Console.log("An error occured while creating file " + file.getName());
            }
        }

        //reload file content to memory
        try {
            load(file);
            if (fromFile != null && plugin != null) {
                try (InputStreamReader reader = new InputStreamReader(plugin.getResource(fromFile))) {
                    FileConfiguration defaultsConfig = YamlConfiguration.loadConfiguration(reader);

                    setDefaults(defaultsConfig);
                    options().copyDefaults(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    Console.log("An error occured while reloading file in memory " + file.getName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Console.log("An error occured while reloading file in memory " + file.getName());
        }
    }


    public void save() {
        try {
            options().indent(2);
            save(file);
        } catch (Exception e) {
            e.printStackTrace();
            Console.log("An error occured while saving file " + file.getName());
        }
    }
}
