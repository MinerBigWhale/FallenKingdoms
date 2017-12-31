package be.miner.gui;

import be.miner.Main;
import be.miner.data.Base;
import be.miner.data.Game;
import be.miner.utils.Console;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MainMenu {

    public static void openMenu(Player player) {
        IconMenu menu = Main.menu;
        menu.createIconMenu(ChatColor.GOLD + Main.getLangFile().getString("joinmenu.title"), 9);
        int index = -1;
        for (Base base : Game.getBases()) {
            index++;
            menu.setOption(index, new ItemStack(Material.WOOL, base.getPlayers().size()+1, base.getColorByte()), base.getName(), ChatColor.RESET + Main.getLangFile().getString("joinmenu.team") + base.getNameString());
        }
        menu.setOption(8, new ItemStack(Material.LAVA_BUCKET, 1), "Leave", ChatColor.RED + Main.getLangFile().getString("joinmenu.leave"));
        menu.open(player);
    }
}
