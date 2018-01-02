package be.miner.events;

import be.miner.Main;
import be.miner.data.Timer;
import be.miner.utils.Message;
import be.miner.utils.Prefix;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageEvent implements Listener {
    private final FileConfiguration config = org.bukkit.Bukkit.getPluginManager().getPlugin("FallenKingdoms").getConfig();

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent damageEvent) {
        if (((damageEvent.getDamager() instanceof Player)) && ((damageEvent.getEntity() instanceof Player))) {
            Player killer = (Player) damageEvent.getDamager();
            Player victime = (Player) damageEvent.getEntity();
            if (Timer.getJours() < config.getInt("pvpDay")) {
                Message.send(killer, ChatColor.RED + Main.getLangFile().getString("message.cannothurt"));
                damageEvent.setDamage(0.0D);
                damageEvent.setCancelled(true);
            } else {
                damageEvent.setCancelled(false);
            }
        }
    }
}