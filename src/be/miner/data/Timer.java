package be.miner.data;

import be.miner.Main;
import be.miner.utils.Prefix;
import be.miner.utils.CustomBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;


public class Timer {
    private static Plugin pl = Bukkit.getPluginManager().getPlugin("FkPlugin");

    private static int _startTimer;
    private static int _stopTimer;

    public static int taskId;
    private static int _taskIdStart;
    private static int _taskIdStop;
    private static int _jours, _minutes, _secondes;
    private static int _totalSecond;
    private static int _toPvp, _toAssault;

    private static CustomBoard _scoreboard;

    public static int getJours() { return _jours; }
    public static String getTotalTime() {
        int seconds=_totalSecond%60;
        int minutes=_totalSecond/60;
        String sseconds = "" + seconds ;
        String sminutes = "" + minutes ;
        if (seconds < 10) sseconds = "0" + seconds;
        if (minutes < 10) sminutes = "0" + minutes;
        return sminutes+":"+sseconds;
    }
    public static String getToPvpTime() {
        if (_toPvp <= 0){
            return ChatColor.GREEN + " Activé";
        }
        int seconds=_toPvp%60;
        int minutes=_toPvp/60;
        String sseconds = "" + seconds ;
        String sminutes = "" + minutes ;
        if (seconds < 10) sseconds = "0" + seconds;
        if (minutes < 10) sminutes = "0" + minutes;
        return sminutes+":"+sseconds;
    }
    public static String getToAssaultTime() {
        if (_toAssault <= 0){
            return ChatColor.GREEN + " Activé";
        }
        int seconds=_toAssault%60;
        int minutes=_toAssault/60;
        String sseconds = "" + seconds ;
        String sminutes = "" + minutes ;
        if (seconds < 10) sseconds = "0" + seconds;
        if (minutes < 10) sminutes = "0" + minutes;
        return sminutes+":"+sseconds;
    }

    public static void startFk(Plugin pl) {
        stopTimer();
        checkPlayerDistribution();
        _taskIdStart = Bukkit.getScheduler().scheduleSyncRepeatingTask(pl, () -> {
            _startTimer -= 1;
            Bukkit.broadcastMessage(Prefix.getPrefix() + ChatColor.YELLOW + "La partie commence dans " + ChatColor.WHITE + (_startTimer + 1) + ChatColor.YELLOW + " seconde(s) !");
            if (_startTimer == 0) {
                Bukkit.getWorld(Main.getConfigFile().getString("world")).setTime(0L);
                Game.pause(false);
                Bukkit.broadcastMessage(Prefix.getPrefix() + ChatColor.YELLOW + ChatColor.BOLD + "La partie commence !");
                updateTimer();
                Game.start();
                Bukkit.getWorld(Main.getConfigFile().getString("world")).setTime(0);

                for (Player players: Bukkit.getOnlinePlayers()) {
                    for (Base base : Game.getBases()){
                        if (base.hasPlayer(players)) {
                            players.setGameMode(GameMode.SURVIVAL);
                            Location loc = new org.bukkit.Location(base.getWorld(), base.getX(), base.getY(), base.getZ());
                            players.teleport(loc);
                        }
                    }
                }
                Bukkit.broadcastMessage(Prefix.getPrefix() + ChatColor.GREEN + "Téléportation des joueurs .");
                Bukkit.getScheduler().cancelTask(_taskIdStart);
            }
        }, 0L, 20L);
    }

    public static void updateTimer() {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(pl, () -> {
            if (!Game.isPaused()) {
                _secondes++;
                _totalSecond++;
                _toPvp--;
                _toAssault--;
                if (_secondes == 60) {
                    _secondes = 0;
                    _minutes++;
                    if (_minutes == 20) {
                        Bukkit.getWorld(Main.getConfigFile().getString("world")).setTime(0L);
                        Bukkit.broadcastMessage(Prefix.getPrefix() + ChatColor.GREEN + "Fin du jour " + ChatColor.AQUA + _jours);
                        if (_jours == Main.getConfigFile().getInt("pvpDay")) {
                            Bukkit.broadcastMessage(Prefix.getPrefix() + ChatColor.YELLOW + "Que le sang coule à flots !");
                        }
                        if (_jours == Main.getConfigFile().getInt("AssaultDay")) {
                            Bukkit.broadcastMessage(Prefix.getPrefix() + ChatColor.YELLOW + "À l'abordage !");
                        }
                        _minutes = 0;
                        _jours++;
                    }
                }
            }
            //Update scoreboard with new info
            getScoreBoard().updateTimeValue().update();
        }, 0L, 20L);
    }

    public static void stopFk(Plugin pl) {
        stopTimer();
        Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "===  Fin de la partie  ===");
        Game.stop();
        if (Main.getConfigFile().getBoolean("stopServer")) {
            _taskIdStop = Bukkit.getScheduler().scheduleSyncRepeatingTask(pl, () -> {
                _stopTimer -= 1;
                if ((_stopTimer == 15) || (_stopTimer == 10) || ((_stopTimer > 1) && (_stopTimer <= 5))) {
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "Fin du serveur dans: " + ChatColor.WHITE + _stopTimer + ChatColor.YELLOW + " secondes !");
                } else if (_stopTimer == 1) {
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "Fin du serveur dans: " + ChatColor.WHITE + _stopTimer + ChatColor.YELLOW + " seconde !");
                } else if (_stopTimer == 0) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
                    Bukkit.getScheduler().cancelTask(_taskIdStop);
                }
            }, 0L, 20L);
        }
    }

    public static CustomBoard getScoreBoard(){
        if (_scoreboard == null ) {
            _scoreboard = new CustomBoard();
        }
        return _scoreboard;
    }

    protected static void stopTimer() {
        Bukkit.getScheduler().cancelTask(taskId);
        //reset variables
        _startTimer = Main.getConfigFile().getInt("startTimer");
        _stopTimer = Main.getConfigFile().getInt("stopTimer");
        _jours = 1;
        _minutes = 0;
        _secondes = 0;
        _totalSecond = 0;
        _toPvp = Main.getConfigFile().getInt("pvpDay")*60*20;
        _toAssault=Main.getConfigFile().getInt("AssaultDay")*60*20;

        getScoreBoard().updateTimeValue();
        getScoreBoard().updateInfoValue();
        getScoreBoard().updateBaseValue();
        getScoreBoard().update();
    }

    public static void checkPlayerDistribution() {
        Bukkit.broadcastMessage(Prefix.getPrefix() + ChatColor.GREEN + "Répartition des joueurs sans équipe");
        //Distribute undecided players

        try {
            HashMap<String, Player> players = new HashMap<String, Player>();
            for (Player player: Bukkit.getOnlinePlayers()) {
                players.put(player.getName(), player);
                Bukkit.getConsoleSender().sendMessage(Prefix.getPrefix() + ChatColor.BLUE + player.getName() + "is online");
            }

            players.forEach((name, player) -> {
                for (Base base : Game.getBases()) {
                    if (base.hasPlayer(player)) {
                        players.remove(name);
                        Bukkit.getConsoleSender().sendMessage(Prefix.getPrefix() + ChatColor.BLUE + name + "has already a team");
                    }
                }
            });

            players.forEach((name, player) -> {
                Base poorbase = null;
                for (Base base : Game.getBases()) {
                    if (poorbase == null || base.getPlayers().size() < poorbase.getPlayers().size()) {
                        poorbase = base;
                    }
                }
                poorbase.addPlayer(player);
            });
        } catch (Exception e) {Bukkit.getConsoleSender().sendMessage(Prefix.getPrefix() + ChatColor.BLUE + "No player without team");}

            Bukkit.broadcastMessage(Prefix.getPrefix() + ChatColor.GREEN + "Equilibrage des équipes");
            //equilibrate base load
            HashMap<String, Player> players = new HashMap<String, Player>();
            for (Player player: Bukkit.getOnlinePlayers()) {
                players.put(player.getName(), player);
                Bukkit.getConsoleSender().sendMessage(Prefix.getPrefix() + ChatColor.BLUE + player.getName() + "is online");
            }

            players.forEach((name, player) -> {
                Base poorbase = null;
                Base richbase = null;
                for (Base base : Game.getBases()) {
                    if (poorbase == null || base.getPlayers().size() < poorbase.getPlayers().size()) {
                        poorbase = base;
                    }
                    if (richbase == null || base.getPlayers().size() > richbase.getPlayers().size()) {
                        richbase = base;
                    }
                }
                if (richbase.getPlayers().size() - poorbase.getPlayers().size() > 1) {
                    poorbase.addPlayer(player);
                }
            });
        try {
        } catch (Exception e) {Bukkit.getConsoleSender().sendMessage(Prefix.getPrefix() + ChatColor.BLUE + "No needs to balance teams");}
    }
}