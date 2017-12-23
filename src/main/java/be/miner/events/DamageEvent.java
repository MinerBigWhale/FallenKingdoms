package be.miner.events;

import be.miner.Main;
import be.miner.data.Timer;
import be.miner.utils.Prefix;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageEvent implements Listener {
    private FileConfiguration config = org.bukkit.Bukkit.getPluginManager().getPlugin("FkPlugin").getConfig();

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (((e.getDamager() instanceof Player)) && ((e.getEntity() instanceof Player))) {
            Player killer = (Player) e.getDamager();
            Player victime = (Player) e.getEntity();
            if (Timer.getJours() < this.config.getInt("pvpDay")) {
                killer.sendMessage(Prefix.getPrefix() + ChatColor.RED + Main.getLangFile().getString("message.cannothurt"));
                e.setDamage(0.0D);
                e.setCancelled(true);
            } else {
                e.setCancelled(false);
            }
        }
    }
}