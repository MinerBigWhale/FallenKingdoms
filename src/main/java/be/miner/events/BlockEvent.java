package be.miner.events;

import be.miner.Main;
import be.miner.data.Base;
import be.miner.data.Game;
import be.miner.utils.Message;
import be.miner.utils.Prefix;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockEvent implements org.bukkit.event.Listener {
    @org.bukkit.event.EventHandler
    public void onBreakEvent(BlockBreakEvent breakEvent) {
        Player player = breakEvent.getPlayer();
        Block block = breakEvent.getBlock();
        Location loc = block.getLocation();

        double x = loc.getX();
        double z = loc.getZ();
        if (Game.isPaused()) {
            breakEvent.setCancelled(true);
            Message.send(player, ChatColor.RED + Main.getLangFile().getString("restriction.breakblockpause"));
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
                                    breakEvent.setCancelled(true);
                                    Message.send(player, ChatColor.RED + Main.getLangFile().getString("restriction.breakblockbase"));
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
        breakEvent.setCancelled(false);
    }

    @org.bukkit.event.EventHandler
    public void onPlaceEvent(BlockPlaceEvent placeEvent) {
        Player player = placeEvent.getPlayer();
        Block block = placeEvent.getBlock();
        Location loc = block.getLocation();

        double x = loc.getX();
        double z = loc.getZ();
        double y = loc.getY();
        if (Game.isPaused()) {
            placeEvent.setCancelled(true);
            Message.send(player, ChatColor.RED + Main.getLangFile().getString("restriction.placeblockpause"));
            return;
        }
        if (Game.isRunning()) { //is the game is running
            if (block.getType().equals(Material.SIGN)) {
                for (Base base : Game.getBases()) {
                    if (base.hasPlayer(player)) {
                        if (!(x > base.getNegativeX() && x < base.getPositiveX() && z > base.getNegativeZ() && z < base.getPositiveZ() && y > base.getNegativeY() && y < base.getPositiveY())) {
                            /*
                             * TODO: Secret Chest Room Balise
                             * ------------------------------
                             * Create room in base object if sign with Secret Room on first line
                             */
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
                                    placeEvent.setCancelled(true);
                                    Message.send(player, ChatColor.RED + Main.getLangFile().getString("restriction.placeblockbase"));
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
        placeEvent.setCancelled(false);
    }
}