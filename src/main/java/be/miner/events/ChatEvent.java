package be.miner.events;

import be.miner.data.Base;
import be.miner.data.Game;
import be.miner.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements org.bukkit.event.Listener {
    @org.bukkit.event.EventHandler
    public void onChat(AsyncPlayerChatEvent chatEvent) {
        Player player = chatEvent.getPlayer();
        String msg = chatEvent.getMessage();
        chatEvent.setCancelled(true);
        boolean printed = false;
        if (Game.isRunning()) {
            if (msg.startsWith("!")) {
                for (Base base : Game.getBases()) {
                    if (base.hasPlayer(player)) {
                        printed = true;
                        Bukkit.broadcastMessage("<" + base.getChatColor() + player.getDisplayName() + ChatColor.WHITE + "> " + msg.replaceFirst("!", " "));
                        continue;
                    }
                }
            } else {
                for (Base base : Game.getBases()) {
                    if (base.hasPlayer(player)) {
                        printed = true;
                        for (Player players : base.getPlayers()) {
                            Message.send(players, String.valueOf(ChatColor.ITALIC) + ChatColor.GRAY + "[" + player.getDisplayName() + "] " + ChatColor.WHITE + msg);
                        }
                        continue;
                    }
                }
            }
        } else {
            for (Base base : Game.getBases()) {
                if (base.hasPlayer(player)) {
                    printed = true;
                    Bukkit.broadcastMessage("<" + base.getChatColor() + player.getDisplayName() + ChatColor.WHITE + "> " + msg);
                    continue;
                }
            }
        }

        if (!printed) {
            Bukkit.broadcastMessage("<" + ChatColor.GRAY + player.getDisplayName() + ChatColor.WHITE + "> " + msg);
        }
    }
}