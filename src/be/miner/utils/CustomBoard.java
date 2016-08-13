package be.miner.utils;


import be.miner.Main;
import be.miner.data.Base;
import be.miner.data.Timer;
import be.miner.data.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.lang.*;
import java.lang.Math;

public class CustomBoard {


    public CustomBoard() {
        update();
    }

    public void update(){ for (Player player : Bukkit.getOnlinePlayers()) update(player); }
    public void update(Player player){

        //prepare building Scoreboard
        String[] strings = new String[15];
        strings[0] = ChatColor.GOLD + "====" + Main.getConfigFile().getString("ScoreBoardName") + "  " + ChatColor.GRAY + Timer.getTotalTime() + ChatColor.GOLD + "====";
        int index = 0;

        index++; strings[index] = ChatColor.GRAY + "--- Bases --- ";
        index++; strings[index] = ChatColor.YELLOW + "Equipes: " + ChatColor.AQUA + Game.getBases().size();
        for (Base base : Game.getBases()){
            index++; strings[index] = ChatColor.GRAY + "- " + base.getNameString() + ": "+ ChatColor.AQUA + base.getPlayers().size();
        }
        index++; strings[index] = ChatColor.GRAY + "--- Timer --- ";
        index++; strings[index] = ChatColor.YELLOW + "PvP: " + ChatColor.AQUA + Timer.getToPvpTime();
        index++; strings[index] = ChatColor.YELLOW + "Assault: " + ChatColor.AQUA + Timer.getToAssaultTime();
        index++; strings[index] = ChatColor.GRAY + "--- Infos --- ";
        for (Base base : Game.getBases()){
            if (base.hasPlayer(player)){
                index++; strings[index] = ChatColor.YELLOW + "Equipe: " + base.getNameString();
                index++; strings[index] = ChatColor.YELLOW + "Base: " + ChatColor.GOLD + base.getDirectionString(player) +" "+ ChatColor.YELLOW + Math.round(base.getDistance(player)) + "m";
            }
        }

        //creating and affect Scoreboard
        Scoreboard scoreboard = player.getScoreboard();
        if (scoreboard == null){
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        }
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        try {
            objective.setDisplaySlot(null);
            objective.unregister();
        } catch (Exception e) {}

        String objectivename = ("FK" + player.getName() + "00000000000000").substring(0, 16);
        objective = scoreboard.registerNewObjective(objectivename, "dummy");
        objective = scoreboard.getObjective(objectivename);

        if (Game.isRunning()) {
            for (int scorepos = -1; scorepos >= 0 - index; scorepos--) {
                objective.getScore(strings[-scorepos]).setScore(scorepos);
            }
        } else {
            for (Base base : Game.getBases()){
                if (base.hasPlayer(player)) {
                    objective.getScore(ChatColor.YELLOW + "Equipe: " + base.getNameString()).setScore(0);
                }
            }
        }

        objective.setDisplayName(strings[0]);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void close(){ for (Player player : Bukkit.getOnlinePlayers()) close(player); }
    public void close(Player player){
        Scoreboard scoreboard = player.getScoreboard();
        if (scoreboard != null) {
            Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
            if (objective != null) {
                objective.unregister();
            }
            player.setScoreboard(scoreboard);
        }
    }
    public void getTeam(String team){

    }
}
