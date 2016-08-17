package be.miner.utils;

import org.bukkit.Bukkit;
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
        setPlugin(plugin);
        setFile(new File(plugin.getDataFolder(), fileName));
        setFromFileFile(fromFileName);
        reload();
    }

    protected void setFile(File file){
        this.file = file;
    }
    protected void setFromFileFile(String fromFileName){
        this.fromFile = fromFileName;
    }
    protected void setPlugin(JavaPlugin plugin){
        this.plugin = plugin;
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
                if (fromFile != null && this.plugin != null) {
                    InputStream in = this.plugin.getResource(this.fromFile);
                    if (in == null) { throw new NullPointerException(); }
                    InputStreamReader reader = new InputStreamReader(in);
                    PrintWriter writer = new PrintWriter(this.file);
                    try{
                        int readBytes;
                        char[] buffer = new char[4096];
                        while ((readBytes = reader.read(buffer)) > 0) {
                            writer.write(buffer, 0, readBytes);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Bukkit.getConsoleSender().sendMessage(Prefix.getPrefix() + "An error occured while filling file " + this.file.getName());
                    } finally {
                        reader.close();
                        writer.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Bukkit.getConsoleSender().sendMessage(Prefix.getPrefix() + "An error occured while creating file " + this.file.getName());
            }
        }

        //reload file content to memory
        try {
            load(this.file);
            if (fromFile != null && this.plugin != null) {
                InputStreamReader reader = new InputStreamReader(this.plugin.getResource(this.fromFile));
                try {
                    FileConfiguration defaultsConfig = YamlConfiguration.loadConfiguration(reader);

                    setDefaults(defaultsConfig);
                    options().copyDefaults(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    Bukkit.getConsoleSender().sendMessage(Prefix.getPrefix() + "An error occured while reloading file in memory " + this.file.getName());
                } finally {
                    reader.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(Prefix.getPrefix() + "An error occured while reloading file in memory " + this.file.getName());
        }
    }


    public void save() {
        try {
            options().indent(2);
            save(this.file);
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(Prefix.getPrefix() + "An error occured while saving file " + this.file.getName());
        }
    }
}
