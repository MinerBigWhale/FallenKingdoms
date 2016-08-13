package be.miner.events;

import be.miner.data.Base;
import be.miner.data.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements org.bukkit.event.Listener {
    @org.bukkit.event.EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        String msg = e.getMessage();
        e.setCancelled(true);
        if (Game.isRunning()) {
            if (msg.startsWith("!")) {
                Bukkit.broadcastMessage("<" + ChatColor.GOLD + player.getDisplayName() + ChatColor.WHITE + ">" + msg.replaceFirst("!", " "));
            } else {
                for (Base base : Game.getBases()) {
                    if (base.hasPlayer(player)) {
                        for (Player players : base.getPlayers()) {
                            players.sendMessage(ChatColor.ITALIC  + "[" + base.getChatColor() + player.getDisplayName() + "] " + ChatColor.WHITE + msg);
                        }
                        continue;
                    }
                }
            }
        } else {
            Bukkit.broadcastMessage(player.getName() + " ==> " + msg);
        }
    }
}