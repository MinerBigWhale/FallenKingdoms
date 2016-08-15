package be.miner.events;

import be.miner.utils.PluginFile;
import be.miner.Main;
import be.miner.data.Base;
import be.miner.data.Game;
import be.miner.utils.Prefix;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockEvent implements org.bukkit.event.Listener {
    @org.bukkit.event.EventHandler
    public void onBreakEvent(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        Location loc = block.getLocation();

        double x = loc.getX();
        double z = loc.getZ();
        if (Game.isPaused()){
            e.setCancelled(true);
            player.sendMessage(Prefix.getPrefix() + ChatColor.RED + "Vous ne pouvez pas casser de blocks pendant la pause !");
            return;
        }
        if (Game.isRunning()) { //is the game is running
            if (player.getWorld().equals(Bukkit.getWorld(Main.getConfigFile().getString("world")))) { //is the player in over world
                if (!player.getGameMode().equals(GameMode.CREATIVE)) { //is not the player in creative
                    //is not player in his base
                    if (!Game.hasBlock(block.getType())) { //is the block in authorized block
                        for (Base base : Game.getBases()) {
                            if (!base.hasPlayer(player)) {
                                if ((x > base.getNegativeX() && x < base.getPositiveX() && z > base.getNegativeZ() && z < base.getPositiveZ())) {
                                    //block if all of this true
                                    e.setCancelled(true);
                                    player.sendMessage(Prefix.getPrefix() + ChatColor.RED + "Vous ne pouvez pas casser de blocks dans les autres bases !");
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
        e.setCancelled(false);
        return;
    }
    @org.bukkit.event.EventHandler
    public void onPlaceEvent(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        Location loc = block.getLocation();

        double x = loc.getX();
        double z = loc.getZ();
        double y = loc.getY();
        if (Game.isPaused()){
            e.setCancelled(true);
            player.sendMessage(Prefix.getPrefix() + ChatColor.RED + "Vous ne pouvez pas poser de blocks pendant la pause !");
            return;
        }
        if (Game.isRunning()) { //is the game is running
            if (block.getType().equals(Material.SIGN)) {
                for (Base base : Game.getBases()) {
                    if (base.hasPlayer(player)) {
                        if (!(x > base.getNegativeX() && x < base.getPositiveX() && z > base.getNegativeZ() && z < base.getPositiveZ() && y > base.getNegativeY() && y < base.getPositiveY())) {

                        }
                    }
                }
            }
            if (player.getWorld().equals(Bukkit.getWorld(Main.getConfigFile().getString("world")))) { //is the player in over world
                if (!player.getGameMode().equals(GameMode.CREATIVE)) { //is not the player in creative
                    if (!Game.hasBlock(block.getType())) { //is the block in authorized block
                        //is player in his base
                        for (Base base : Game.getBases()) {
                            if (base.hasPlayer(player)) {
                                if (!(x > base.getNegativeX() && x < base.getPositiveX() && z > base.getNegativeZ() && z < base.getPositiveZ())) {
                                    //block if all of this true
                                    e.setCancelled(true);
                                    player.sendMessage(Prefix.getPrefix() + ChatColor.RED + "Vous ne pouvez pas poser de blocks hors de votre base !");
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
        e.setCancelled(false);
        return;
    }
}