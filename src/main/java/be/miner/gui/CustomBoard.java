package be.miner.gui;


import be.miner.Main;
import be.miner.data.Base;
import be.miner.data.Game;
import be.miner.data.Timer;
//import be.miner.utils.Message;
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
    private final HashMap<String, String[]> _infoStrings = new HashMap<>();

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

        Boolean isSpectator = true;
        for (Base base : Game.getBases()) {
            if (base.hasPlayer(player)) {
                isSpectator = false;
                infoString[index] = ChatColor.YELLOW + "Equipe: " + base.getNameString();
                index++;
                infoString[index] = ChatColor.YELLOW + "Base: " + ChatColor.GOLD + base.getDirectionString(player);
                index++;
                infoString[index] = ChatColor.YELLOW + "      " + ChatColor.GOLD + Math.round(base.getDistance(player)) + "m";
            }
        }

        if (isSpectator) {
            infoString[index] = ChatColor.MAGIC + "1234567890ABCD";
            index++;
            infoString[index] = ChatColor.WHITE + "Spectator";
            index++;
            infoString[index] = ChatColor.MAGIC + "1234567890ABCD";
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

        for (String baseString : _baseStrings) {
            index++;
            strings[index] = baseString;
        }
        for (String timeString : _timeStrings) {
            index++;
            strings[index] = timeString;
        }
        for (String infoString: _infoStrings.get(player.getName())) {
            index++;
            strings[index] = infoString;
        }

        //creating and affect Scoreboard
        Scoreboard scoreboard = player.getScoreboard();
        if (scoreboard == null) scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        String objectivename = ("FK" + player.getName() + "00000000000000").substring(0, 16);
        Objective objective = scoreboard.getObjective(objectivename);
        if (objective == null) {
            objective = scoreboard.registerNewObjective(objectivename, "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        if (Game.isRunning()) {
            for (int scorepos = -1; scorepos >= 0 - index; scorepos--) {
                String teamname = ("TE" + scorepos + "AAAAAAAAAAAAAA").substring(0, 16);
                String entry = String.valueOf(ChatColor.values()[-scorepos % 16]);
                Team team = scoreboard.getTeam(teamname);
                if (team == null) {
                    team = scoreboard.registerNewTeam(teamname);
                    team.addEntry(entry);
                }
                int maxLength = (strings[-scorepos].length() < 16) ? strings[-scorepos].length() : 16;
                team.setPrefix(strings[-scorepos].substring(0, maxLength));
                //Message.log(entry + "|" + (team.getPrefix() + "                      ").substring(0, 16) + "|" + scorepos);
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
