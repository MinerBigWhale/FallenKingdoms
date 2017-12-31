package be.miner.data;

import be.miner.Main;
import be.miner.utils.PluginFile;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.ArrayList;

public class Game {

    private static Boolean _run = false;
    private static Boolean _pause = false;
    private static final ArrayList<Base> _bases = new ArrayList<>();
    private static final ArrayList<Material> _blocks = new ArrayList<>();

    public static Boolean isRunning() {
        return _run;
    }

    public static Boolean isPaused() {
        return _pause;
    }

    public static void start() {
        _run = true;
    }

    public static void stop() {
        _run = false;
    }

    public static void pause() {
        _pause = !_pause;
    }

    public static void pause(Boolean state) {
        _pause = state;
    }

    public static void addBase(Base base) {
        _bases.add(base);
    }

    public static ArrayList<Base> getBases() {
        return _bases;
    }

    public static ArrayList<String> getBasesName() {
        ArrayList<String> ret = new ArrayList<>();
        for (Base base : _bases)
            ret.add(base.getName());
        return ret;
    }

    public static Base getBase(String baseName) {
        for (Base base : _bases)
            if (base.getName().equalsIgnoreCase(baseName)) return base;
        return null;
    }

    public static Boolean hasBase(Base base) {
        return _bases.contains(base);
    }

    public static Boolean hasBase(String baseName) {
        for (Base base : _bases)
            if (base.getName().equalsIgnoreCase(baseName)) return true;
        return false;
    }

    public static void clearBases() {
        _bases.clear();
    }

    public static void addBlock(Material material) {
        Bukkit.broadcastMessage("Debug: addBlock( Material " + material.name() + ")");
        if (!_blocks.contains(material)) {
            _blocks.add(material);
            Bukkit.broadcastMessage("Debug: " + material.name() + " added");
            PluginFile fileBlock = Main.getBlockFile();
            fileBlock.set(material.name(), true);
            fileBlock.save();
        }
    }

    public static void addBlock(String materialName) {
        addBlock(Material.matchMaterial(materialName));
    }

    public static void removeBlock(Material material) {
        if (_blocks.contains(material)) {
            _blocks.remove(material);
            PluginFile fileBlock = Main.getBlockFile();
            fileBlock.set(material.name(), false);
            fileBlock.save();
        }
    }

    public static void removeBlock(String materialName) {
        removeBlock(Material.matchMaterial(materialName));
    }

    public static ArrayList<Material> getBlocks() {
        return _blocks;
    }

    public static Boolean hasBlock(Material material) {
        return _blocks.contains(material);
    }

    public static Boolean hasBlock(String materialName) {
        return _blocks.contains(Material.matchMaterial(materialName));
    }

    public static void clearBlocks() {
        _blocks.clear();
    }

    public static void clearAll() {
        _run = false;
        _pause = false;
        clearBases();
        clearBlocks();
    }
}
