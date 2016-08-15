package be.miner.data;


import be.miner.Main;
import be.miner.utils.Prefix;
import com.google.common.collect.ForwardingMapEntry;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.awt.*;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.HashMap;

public class Base {
    private String _color;
    private ChatColor _chatColor;
    private byte _colorByte;
    private String _name;
    private double _x, _y, _z;
    private double _size;
    private World _world;
    private Team _team;
    private Scoreboard _scoreboard;
    private double _xPositive, _zPositive, _yPositive, _xNegative, _zNegative, _yNegative;
    private HashMap<String, Player> _players = new HashMap<String, Player>();

    public Base(String color){ this(color, color);}
    public Base(String color, String name) {
        _color = color;
        switch (_color) {
            case "Blue":
                _chatColor = ChatColor.BLUE;
                _colorByte = (byte) 9;
                break;
            case "Red":
                _chatColor = ChatColor.RED;
                _colorByte = (byte) 14;
                break;
            case "Green":
                _chatColor = ChatColor.GREEN;
                _colorByte = (byte) 5;
                break;
            case "Yellow":
                _chatColor = ChatColor.YELLOW;
                _colorByte = (byte) 4;
                break;
        }
        _name = name;
        _x = Main.getConfigFile().getDouble(_name + ".x");
        _y = Main.getConfigFile().getDouble(_name + ".y");
        _z = Main.getConfigFile().getDouble(_name + ".z");
        _world = Bukkit.getWorld(Main.getConfigFile().getString(_name + ".world"));

        // define positive and negative corner of bases
        _size = Main.getConfigFile().getDouble("BaseSize");
        _xPositive = _x + _size / 2.0D;
        _zPositive = _z + _size / 2.0D;
        _yPositive = _y + _size;

        _xNegative = _x - _size / 2.0D;
        _zNegative = _z - _size / 2.0D;
        _yNegative = _y - _size;

        // print Base informations
        Bukkit.getConsoleSender().sendMessage(Prefix.getPrefix() + ChatColor.GREEN + "Base " + _chatColor + _name + ChatColor.GREEN + " Created");
        Bukkit.getConsoleSender().sendMessage(Prefix.getPrefix() + ChatColor.YELLOW + "Center location: ");
        Bukkit.getConsoleSender().sendMessage(Prefix.getPrefix() + ChatColor.YELLOW + "x: " + ChatColor.WHITE + _x);
        Bukkit.getConsoleSender().sendMessage(Prefix.getPrefix() + ChatColor.YELLOW + "y: " + ChatColor.WHITE + _y);
        Bukkit.getConsoleSender().sendMessage(Prefix.getPrefix() + ChatColor.YELLOW + "z: " + ChatColor.WHITE + _z);
        Bukkit.getConsoleSender().sendMessage(Prefix.getPrefix() + ChatColor.YELLOW + "Size: " + ChatColor.WHITE + _size);

        //Setup scoreboard team
        _scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        try {
            _team = _scoreboard.getTeam(_name);
            _team.setDisplayName(_chatColor + _name);
            _team.setAllowFriendlyFire(Main.getConfigFile().getBoolean("friendlyFire"));
            _team.setCanSeeFriendlyInvisibles(Main.getConfigFile().getBoolean("canSeeFriendlyInvisible"));
            _team.setPrefix(_chatColor + "");
        } catch (Exception e) {
            _scoreboard.registerNewTeam(_name);
            _team = _scoreboard.getTeam(_name);
            _team.setDisplayName(_chatColor + _name);
            _team.setAllowFriendlyFire(Main.getConfigFile().getBoolean("friendlyFire"));
            _team.setCanSeeFriendlyInvisibles(Main.getConfigFile().getBoolean("canSeeFriendlyInvisible"));
            _team.setPrefix(_chatColor + "");
        }
    }

    public String getColor(){ return _color; }
    public ChatColor getChatColor(){ return _chatColor; }
    public Byte getColorByte(){ return _colorByte; }
    public String getNameString(){ return _chatColor + _name; }
    public String getName(){ return _name; }
    public double getX() { return _x; }
    public double getY() { return _y; }
    public double getZ() { return _z; }
    public World getWorld() { return _world; }
    public double getPositiveX() { return _xPositive; }
    public double getPositiveZ() { return _zPositive; }
    public double getPositiveY() { return _yPositive; }
    public double getNegativeX() { return _xNegative; }
    public double getNegativeZ() { return _zNegative; }
    public double getNegativeY() { return _yNegative; }

    //Players
    //------
    public ArrayList<Player> getPlayers() { return new ArrayList<Player>(_players.values()); }
    public Boolean hasPlayer(Player player){ return _players.containsKey(player.getName()); }

    public void addPlayer(Player player){
        for(Base base : Game.getBases()){
            if (base != this){
                base.removePlayer(player);
            }
        }
        if (!hasPlayer(player)) {
            Bukkit.broadcastMessage(Prefix.getPrefix() + ChatColor.GOLD + player.getName() + ChatColor.GREEN + " à rejoint l'équipe " + getNameString());
        }
        _players.put(player.getName(), player);
        _team.addPlayer(player);

    }
    public void removePlayer(Player player){
        if (hasPlayer(player)) {
            _players.remove(player.getName());
            _team.removePlayer(player);
            Bukkit.broadcastMessage(Prefix.getPrefix() + ChatColor.GOLD + player.getName() + ChatColor.GREEN + " à quitté l'equipe " + getNameString());
        }
    }

    public Double getDistance(Player player){
        Location plocation = player.getLocation();
        return plocation.distance(new Location(_world,_x,plocation.getY(),_z));
    }

    public double calculAngle(Location block, Location player){
        Vector vector = player.toVector().subtract(block.toVector());
        block = new Location(_world,_x,_y,_z).setDirection(vector);

        double pyaw = 0 - player.getYaw();
        while (pyaw < 0){ pyaw += 360; }
        while (pyaw > 360){ pyaw -= 360; }
        if (pyaw > 180) { pyaw -= 360; }

        double byaw = block.getYaw();
        while (byaw < 0){ byaw += 360; }
        while (byaw > 360){ byaw -= 360; }
        if (byaw > 180) { byaw -= 360; }

        double angle = 0 + ( pyaw + byaw );
        while (angle < 0){ angle += 360; }
        while (angle > 360){ angle -= 360; }

        return angle;
    }

    public String getDirectionString(Player player) {

        if (getDistance(player) <= _size / 2){ return "⭘";}
        Location plocation = player.getLocation();
        Location blocation = new Location(_world,_x,_y,_z);

        double angle = calculAngle(blocation, plocation);

        if (angle <= 22.5 || angle > 337.5) {
            return "⇩";
        } else if (angle > 22.5 && angle <= 67.5) {
            return "⬃";
        } else if (angle > 67.5 && angle <= 112.5) {
            return "⇦";
        } else if (angle > 112.5 && angle <= 157.5) {
            return "⬁";
        } else if (angle > 157.5 && angle <= 202.5) {
            return "⇧";
        } else if (angle > 202.5 && angle <= 247.5) {
            return "⬀";
        } else if (angle > 247.5 && angle <= 292.5) {
            return "⇨";
        } else if (angle > 292.5 && angle <= 337.5) {
            return "⬂";
        } else{
            return "⤬";//"❂";
        }
    }

}
