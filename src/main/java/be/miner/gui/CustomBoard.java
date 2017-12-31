package be.miner.gui;


import be.miner.Main;
import be.miner.data.Base;
import be.miner.data.Game;
import be.miner.data.Timer;
import be.miner.utils.Console;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;

public class CustomBoard {

    private final String[] strings = new String[15];
    private String[] _baseStrings;
    private String[] _timeStrings;
    private final HashMap<String, String[]> _infoStrings = new HashMap<String, String[]>();

    public CustomBoard() {
        close();
        updateBaseValue();
        updateTimeValue();
        updateInfoValue();
        update();
    }

    public CustomBoard updateBaseValue() {
        _baseStrings = new String[2 + Game.getBases().size()];
        int index = 0;

        _baseStrings[index] = ChatColor.GRAY + "--- Bases --- ";
        index++;
        _baseStrings[index] = ChatColor.YELLOW + "Equipes: " + ChatColor.AQUA + Game.getBases().size();
        index++;
        for (Base base : Game.getBases()) {
            _baseStrings[index] = ChatColor.GRAY + "- " + base.getNameString() + ": " + ChatColor.AQUA + base.getPlayers().size();
            index++;
        }
        return this;
    }

    public CustomBoard updateTimeValue() {
        _timeStrings = new String[3];
        int index = 0;

        strings[0] = ChatColor.GOLD + "====" + Main.getConfigFile().getString("ScoreBoardName") + "  " + ChatColor.GRAY + Timer.getTotalTime() + ChatColor.GOLD + "====";

        _timeStrings[index] = ChatColor.GRAY + "--- Timer --- ";
        index++;
        _timeStrings[index] = ChatColor.YELLOW + "PvP: " + ChatColor.AQUA + Timer.getToPvpTime();
        index++;
        _timeStrings[index] = ChatColor.YELLOW + "Clash: " + ChatColor.AQUA + Timer.getToAssaultTime();
        index++;
        return this;
    }

    public CustomBoard updateInfoValue() {
        for (Player player : Bukkit.getOnlinePlayers()) updateInfoValue(player);
        return this;
    }

    public CustomBoard updateInfoValue(Player player) {
        String[] infoString = new String[4];
        int index = 0;

        infoString[index] = ChatColor.GRAY + "--- Infos --- ";
        index++;
        for (Base base : Game.getBases()) {
            if (base.hasPlayer(player)) {
                infoString[index] = ChatColor.YELLOW + "Equipe: " + base.getNameString();
                index++;
                infoString[index] = ChatColor.YELLOW + "Base: " + ChatColor.GOLD + base.getDirectionString(player);
                index++;
                infoString[index] = ChatColor.YELLOW + "      " + ChatColor.GOLD + Math.round(base.getDistance(player)) + "m";
                index++;
            }
        }
        _infoStrings.put(player.getName(), infoString);
        return this;
    }

    public void update() {
        for (Player player : Bukkit.getOnlinePlayers()) update(player);
    }

    public void update(Player player) {

        //prepare building Scoreboard
        int index = 0;

        for (int xedni = 0; xedni < _baseStrings.length; xedni++) {
            index++;
            strings[index] = _baseStrings[xedni];
        }
        for (int xedni = 0; xedni < _timeStrings.length; xedni++) {
            index++;
            strings[index] = _timeStrings[xedni];
        }
        for (int xedni = 0; xedni < _infoStrings.get(player.getName()).length; xedni++) {
            index++;
            strings[index] = _infoStrings.get(player.getName())[xedni];
        }

        //creating and affect Scoreboard
        Scoreboard scoreboard = player.getScoreboard();
        if (scoreboard == null) {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        }
        String objectivename = ("FK" + player.getName() + "00000000000000").substring(0, 16);
        Objective objective = scoreboard.getObjective(objectivename);
        if (objective == null) {
            objective = scoreboard.registerNewObjective(objectivename, "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        if (Game.isRunning()) {
            for (int scorepos = -1; scorepos >= 0 - index; scorepos--) {
                String teamname = ("TE" + scorepos + "AAAAAAAAAAAAAA").substring(0, 16);
                String entry = ChatColor.values()[-scorepos % 16] + "";
                Team team = scoreboard.getTeam(teamname);
                if (team == null) {
                    team = scoreboard.registerNewTeam(teamname);
                    team.addEntry(entry);
                }
                int maxLength = (strings[-scorepos].length() < 16) ? strings[-scorepos].length() : 16;
                team.setPrefix(strings[-scorepos].substring(0, maxLength));
                //Console.log(entry + "|" + (team.getPrefix() + "                      ").substring(0, 16) + "|" + scorepos);
                objective.getScore(entry).setScore(scorepos);
            }
        } else {
            for (Base base : Game.getBases()) {
                if (base.hasPlayer(player)) {
                    objective.getScore(ChatColor.YELLOW + "Equipe: " + base.getNameString()).setScore(-1);
                }
            }
        }

        objective.setDisplayName(strings[0]);
    }

    public void close() {
        for (Player player : Bukkit.getOnlinePlayers()) close(player);
    }

    public void close(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        if (scoreboard == null) return;
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        if (objective != null) {
            for (int scorepos = -1; scorepos >= 0 - 15; scorepos--) {
                String teamname = ("TE" + scorepos + "00000000000000").substring(0, 16);
                Team team = scoreboard.getTeam(teamname);
                if (team != null) team.unregister();
            }
            objective.unregister();
        }
        player.setScoreboard(scoreboard);
    }
}
