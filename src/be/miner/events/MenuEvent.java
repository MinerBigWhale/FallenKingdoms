package be.miner.events;

import be.miner.Main;
import be.miner.data.Base;
import be.miner.data.Game;
import be.miner.utils.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuEvent implements org.bukkit.event.Listener {
    @EventHandler
    void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getTitle().equals(Main.menu.name)) {
            e.setCancelled(true);
            int slot = e.getRawSlot();
            if (slot >= 0 && slot < Main.menu.size && Main.menu.optionNames[slot] != null) {
                Player player = (Player) e.getWhoClicked(); // The player that clicked the item
                String clicked = Main.menu.optionNames[slot]; // The item that was clicked
                for (Base base : Game.getBases()) {
                    if (!base.hasPlayer(player) && base.getName().equalsIgnoreCase(clicked)) {
                        base.addPlayer(player);
                    } else {
                        base.removePlayer(player);
                    }
                }
                final Player p = (Player) e.getWhoClicked();

                p.closeInventory();
                Main.menu.destroy();
            }
        }
    }
}
