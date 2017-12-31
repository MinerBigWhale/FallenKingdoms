package be.miner.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class IconMenu implements org.bukkit.event.Listener {

    private String name;

    public String getName () {
        return name;
    }

    public int getSize () {
        return size;
    }

    public String[] getOptionNames () {
        return optionNames;
    }

    private int size;

    private String[] optionNames;
    private ItemStack[] optionIcons;

    public IconMenu createIconMenu(String name, int size) {
        this.name = name;
        this.size = size;
        optionNames = new String[size];
        optionIcons = new ItemStack[size];
        return this;
    }

    public IconMenu setOption(int position, ItemStack icon, String name, String... info) {
        optionNames[position] = name;
        optionIcons[position] = setItemNameAndLore(icon, name, info);
        return this;
    }

    public void open(Player player) {
        Inventory inventory = Bukkit.createInventory(player, size, name);
        for (int i = 0; i < optionIcons.length; i++) {
            if (optionIcons[i] != null) {
                inventory.setItem(i, optionIcons[i]);
            }
        }
        player.openInventory(inventory);
    }

    public void destroy() {
        optionNames = null;
        optionIcons = null;
    }


    private ItemStack setItemNameAndLore(ItemStack item, String name, String[] lore) {
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(name);
        im.setLore(Arrays.asList(lore));
        item.setItemMeta(im);
        return item;
    }

}