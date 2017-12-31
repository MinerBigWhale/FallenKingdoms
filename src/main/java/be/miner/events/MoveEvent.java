package be.miner.events;

import be.miner.Main;
import be.miner.data.Base;
import be.miner.data.Game;
import be.miner.data.Timer;
import be.miner.utils.Prefix;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveEvent implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent moveEvent) {
        Player player = moveEvent.getPlayer();

        Boolean isSpectator = true;
        for (Base base : Game.getBases()){
            if (base.hasPlayer(player)){
                isSpectator = false;
            }
        }
        if (isSpectator) return;

        if (Game.isPaused()) {
            moveEvent.setCancelled(true);
            player.sendMessage(Prefix.getPrefix() + ChatColor.RED + Main.getLangFile().getString("restriction.movepause"));
            return;
        }
        Timer.getScoreBoard().updateInfoValue(player).update(player);
    }
}
