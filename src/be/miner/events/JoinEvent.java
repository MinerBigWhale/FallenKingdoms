package be.miner.events;

import be.miner.data.Timer;
import be.miner.gui.MainMenu;
import be.miner.Main;
import be.miner.data.Base;
import be.miner.data.Game;
import be.miner.utils.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Time;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class JoinEvent implements org.bukkit.event.Listener {
    int _taskId;

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent e) {
        Timer.getScoreBoard().updateBaseValue().update();
        Player player = e.getPlayer();
        for(Base base : Game.getBases()) {
            if (base.hasPlayer(player)){
                base.addPlayer(player);
                Bukkit.broadcastMessage(Prefix.getPrefix() + ChatColor.YELLOW + player.getDisplayName() +" from "+ base.getNameString() +ChatColor.YELLOW +" team is back in the game");
                e.setJoinMessage(null);
                return;
            }
        }
        if (Game.isRunning()){
            player.setGameMode(GameMode.SPECTATOR);
            Bukkit.broadcastMessage(Prefix.getPrefix() + ChatColor.GRAY+ player.getDisplayName() +" has join as spectator");
            e.setJoinMessage(null);
        } else {
            if (Main.getConfigFile().getBoolean("autoStart")) {
                int playernum = Bukkit.getOnlinePlayers().size();
                if (Main.getConfigFile().getInt("maxPlayer") >= playernum){
                    Game.start();
                }
                Bukkit.broadcastMessage(Prefix.getPrefix() + ChatColor.YELLOW + player.getDisplayName() +" has join de game [" + playernum + "/" + Main.getConfigFile().getInt("maxPlayer") + "]" );
                e.setJoinMessage(null);
            } else {
                Bukkit.broadcastMessage(Prefix.getPrefix() + ChatColor.YELLOW + player.getDisplayName() +" has join de game");
                e.setJoinMessage(null);
            }
            _taskId= Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("FkPlugin"), () -> {
                MainMenu.openMenu(player);
                Bukkit.getScheduler().cancelTask(_taskId);
            }, 0L, 20L);
        }
        return;
    }
}