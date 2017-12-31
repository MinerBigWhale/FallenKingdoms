package be.miner.events;

import be.miner.Main;
import be.miner.data.Base;
import be.miner.data.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuEvent implements org.bukkit.event.Listener {
    @EventHandler
    void onInventoryClick(InventoryClickEvent inventoryEvent) {
        if (inventoryEvent.getInventory().getTitle().equals(Main.menu.getName())) {
            inventoryEvent.setCancelled(true);
            int slot = inventoryEvent.getRawSlot();
            if (slot >= 0 && slot < Main.menu.getSize() && Main.menu.getOptionNames()[slot] != null) {
                Player player = (Player) inventoryEvent.getWhoClicked(); // The player that clicked the item
                String clicked = Main.menu.getOptionNames()[slot]; // The item that was clicked
                for (Base base : Game.getBases()) {
                    if (!base.hasPlayer(player) && base.getName().equalsIgnoreCase(clicked)) {
                        base.addPlayer(player);
                    } else {
                        base.removePlayer(player);
                    }
                }
                Player p = (Player) inventoryEvent.getWhoClicked();

                p.closeInventory();
                Main.menu.destroy();
            }
        }
    }
}
