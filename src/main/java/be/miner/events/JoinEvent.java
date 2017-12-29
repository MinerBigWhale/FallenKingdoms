package be.miner.events;

import be.miner.Main;
import be.miner.data.Base;
import be.miner.data.Game;
import be.miner.data.Timer;
import be.miner.gui.MainMenu;
import be.miner.utils.Console;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements org.bukkit.event.Listener {
    int _taskId;

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent e) {
        Timer.getScoreBoard().updateBaseValue().update();
        Player player = e.getPlayer();
        for (Base base : Game.getBases()) {
            if (base.hasPlayer(player)) {
                base.addPlayer(player);
                Console.broadcast( ChatColor.YELLOW + player.getDisplayName() + Main.getLangFile().getString("message.isback1") + base.getNameString() + ChatColor.YELLOW + Main.getLangFile().getString("message.isback2"));
                e.setJoinMessage(null);
                return;
            }
        }
        if (Game.isRunning()) {
            player.setGameMode(GameMode.SPECTATOR);
            Console.broadcast( ChatColor.GRAY + player.getDisplayName() + Main.getLangFile().getString("message.spectator"));
            e.setJoinMessage(null);
        } else {
            if (Main.getConfigFile().getBoolean("autoStart")) {
                int playernum = Bukkit.getOnlinePlayers().size();
                if (Main.getConfigFile().getInt("maxPlayer") >= playernum) {
                    Game.start();
                }
                Console.broadcast( ChatColor.YELLOW + player.getDisplayName() + Main.getLangFile().getString("message.join") + " [" + playernum + "/" + Main.getConfigFile().getInt("maxPlayer") + "]");
                e.setJoinMessage(null);
            } else {
                Console.broadcast( ChatColor.YELLOW + player.getDisplayName() + Main.getLangFile().getString("message.join"));
                e.setJoinMessage(null);
            }
            _taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("FallenKingdoms"), () -> {
                MainMenu.openMenu(player);
                Bukkit.getScheduler().cancelTask(_taskId);
            }, 0L, 20L);
        }
        return;
    }
}