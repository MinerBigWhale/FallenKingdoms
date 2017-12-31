package be.miner.data;


import be.miner.Main;
import be.miner.gui.RegionHighlighter;
import be.miner.utils.Console;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;

public class Base {
    private String _color;
    private ChatColor _chatColor;
    private byte _colorByte;
    private final String _name;
    private final double _x;
    private final double _y;
    private final double _z;
    private final double _size;
    private final World _world;
    private Team _team;
    private final Scoreboard _scoreboard;
    private final RegionHighlighter _regionHighLighter;
    private final double _xPositive;
    private final double _zPositive;
    private final double _yPositive;
    private final double _xNegative;
    private final double _zNegative;
    private final double _yNegative;
    private final HashMap<String, Player> _players = new HashMap<String, Player>();

    public Base(String color) {
        this(color, color);
    }

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
        Console.log(ChatColor.GREEN + "Base " + _chatColor + _name + ChatColor.GREEN + " Created");
        Console.log(ChatColor.YELLOW + "Center location: ");
        Console.log(ChatColor.YELLOW + "x: " + ChatColor.WHITE + _x);
        Console.log(ChatColor.YELLOW + "y: " + ChatColor.WHITE + _y);
        Console.log(ChatColor.YELLOW + "z: " + ChatColor.WHITE + _z);
        Console.log(ChatColor.YELLOW + "Size: " + ChatColor.WHITE + _size);

        //Setup scoreboard team
        _scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        try {
            _team = _scoreboard.getTeam(_name);
            _team.setDisplayName(_chatColor + _name);
            _team.setAllowFriendlyFire(Main.getConfigFile().getBoolean("friendlyFire"));
            _team.setCanSeeFriendlyInvisibles(Main.getConfigFile().getBoolean("canSeeFriendlyInvisible"));
            _team.setPrefix(String.valueOf(_chatColor));
        } catch (Exception e) {
            _scoreboard.registerNewTeam(_name);
            _team = _scoreboard.getTeam(_name);
            _team.setDisplayName(_chatColor + _name);
            _team.setAllowFriendlyFire(Main.getConfigFile().getBoolean("friendlyFire"));
            _team.setCanSeeFriendlyInvisibles(Main.getConfigFile().getBoolean("canSeeFriendlyInvisible"));
            _team.setPrefix(String.valueOf(_chatColor));
        }

        //Setup Region HighLighter
        _regionHighLighter = new RegionHighlighter(new Location(_world, _x, _y, _z), _size / 2).setColor(_color);
    }

    public String getColor() {
        return _color;
    }

    public ChatColor getChatColor() {
        return _chatColor;
    }

    public Byte getColorByte() {
        return _colorByte;
    }

    public String getNameString() {
        return _chatColor + _name;
    }

    public String getName() {
        return _name;
    }

    public double getX() {
        return _x;
    }

    public double getY() {
        return _y;
    }

    public double getZ() {
        return _z;
    }

    public World getWorld() {
        return _world;
    }

    public double getPositiveX() {
        return _xPositive;
    }

    public double getPositiveZ() {
        return _zPositive;
    }

    public double getPositiveY() {
        return _yPositive;
    }

    public double getNegativeX() {
        return _xNegative;
    }

    public double getNegativeZ() {
        return _zNegative;
    }

    public double getNegativeY() {
        return _yNegative;
    }

    public ArrayList<Player> getPlayers() {
        return new ArrayList<Player>(_players.values());
    }

    public Boolean hasPlayer(Player player) {
        return _players.containsKey(player.getName());
    }

    public void addPlayer(Player player) {
        for (Base base : Game.getBases()) {
            if (base != this) {
                base.removePlayer(player);
            }
        }
        if (!hasPlayer(player)) {
            Console.broadcast( ChatColor.GOLD + player.getName() + ChatColor.GREEN + Main.getLangFile().getString("message.playerhasjointeam") + getNameString());
        }
        _players.put(player.getName(), player);
        _team.addPlayer(player);

    }

    public void removePlayer(Player player) {
        if (hasPlayer(player)) {
            _players.remove(player.getName());
            _team.removePlayer(player);
            Console.broadcast( ChatColor.GOLD + player.getName() + ChatColor.GREEN + Main.getLangFile().getString("message.playerhasleaveteam") + getNameString());
        }
    }

    public Double getDistance(Player player) {
        Location plocation = player.getLocation();
        return plocation.distance(new Location(_world, _x, plocation.getY(), _z));
    }

    private double calculAngle (Location block, Location player) {
        Vector vector = player.toVector().subtract(block.toVector());
        block = new Location(_world, _x, _y, _z).setDirection(vector);

        double pyaw = 0 - player.getYaw();
        while (pyaw < 0) {
            pyaw += 360;
        }
        while (pyaw > 360) {
            pyaw -= 360;
        }
        if (pyaw > 180) {
            pyaw -= 360;
        }

        double byaw = block.getYaw();
        while (byaw < 0) {
            byaw += 360;
        }
        while (byaw > 360) {
            byaw -= 360;
        }
        if (byaw > 180) {
            byaw -= 360;
        }

        double angle = 0 + (pyaw + byaw);
        while (angle < 0) {
            angle += 360;
        }
        while (angle > 360) {
            angle -= 360;
        }

        return angle;
    }

    public String getDirectionString(Player player) {

        if (getDistance(player) <= _size / 2) {
            return "❂";
            //return "⭘";
        }
        Location plocation = player.getLocation();
        Location blocation = new Location(_world, _x, _y, _z);

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
        } else {
            return "⤬";
            //return "❂";
            //return "⭘";
        }
    }

    public void border() {
        if (_regionHighLighter.isShown()) {
            _regionHighLighter.hideRegion();
        } else {
            _regionHighLighter.showRegion();
        }
    }

    public void showBorder() {
        if (!_regionHighLighter.isShown()) {
            _regionHighLighter.showRegion();
        }
    }

    public void hideBorder() {
        if (_regionHighLighter.isShown()) {
            _regionHighLighter.hideRegion();
        }
    }

}
