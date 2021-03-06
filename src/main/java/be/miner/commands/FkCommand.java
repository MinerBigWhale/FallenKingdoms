package be.miner.commands;

import be.miner.Main;
import be.miner.data.Base;
import be.miner.data.Game;
import be.miner.data.Timer;
import be.miner.gui.MainMenu;
import be.miner.utils.Message;
import be.miner.utils.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class FkCommand implements CommandExecutor {
    private static final Plugin _pl = Bukkit.getPluginManager().getPlugin("FallenKingdoms");

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("start")) {
                if (args.length == 1) {
                    if (p.hasPermission("fk.start")) {
                        if (!Game.isRunning()) {
                            Timer.startFk(_pl);
                        } else {
                            Message.broadcast( ChatColor.RED + Main.getLangFile().getString("error.alreadystarted"));
                        }
                    } else {
                        Message.send(p, ChatColor.RED + Main.getLangFile().getString("error.nopermission"));
                    }
                } else {
                    Message.send(p, ChatColor.RED + "Usage: /fk start !");
                }
            } else if (args[0].equalsIgnoreCase("stop")) {
                if (args.length == 1) {
                    if (p.hasPermission("fk.stop")) {
                        Timer.stopFk(_pl);
                    } else {
                        Message.send(p, ChatColor.RED + Main.getLangFile().getString("error.nopermission"));
                    }
                } else {
                    Message.send(p, ChatColor.RED + "Usage: /fk stop !");
                }
            } else if (args[0].equalsIgnoreCase("limit")) {
                if (args.length == 1) {
                    for (Base base : Game.getBases()) {
                        base.border();
                    }
                } else {
                    Message.send(p, ChatColor.RED + "Usage: /fk limit !");
                }
            } else if (args[0].equalsIgnoreCase("open")) {
                if (args.length == 1) {
                    MainMenu.openMenu(p);
                    Message.send(p, ChatColor.GREEN + Main.getLangFile().getString("commandfeedback.menuopening"));
                } else {
                    Message.send(p, ChatColor.RED + "Usage: /fk open !");
                }
            } else if (args[0].equalsIgnoreCase("pause")) {
                if (args.length == 1) {
                    if (p.hasPermission("fk.pause")) {
                        Game.pause();
                        if (Game.isPaused()) {
                            Message.broadcast( ChatColor.GREEN + Main.getLangFile().getString("commandfeedback.pauseon"));
                        } else {
                            Message.broadcast( ChatColor.YELLOW + Main.getLangFile().getString("commandfeedback.pauseoff"));
                        }
                    } else {
                        Message.send(p, ChatColor.RED + Main.getLangFile().getString("error.nopermission"));
                    }
                } else {
                    Message.send(p, ChatColor.RED + "Usage: /fk pause !");
                }
            } else if (args[0].equalsIgnoreCase("block")) {
                if (args.length == 3) {
                    if (p.hasPermission("fk.block")) {
                        if (args[2].contains("minecraft:")) {
                            args[2] = args[2].replace("minecraft:", "");
                        }
                        if (args[1].equalsIgnoreCase("add")) {
                            Game.addBlock(args[2]);
                            if (Game.hasBlock(args[2])) {
                                Message.send(p, ChatColor.GREEN + Main.getLangFile().getString("commandfeedback.blockadded"));
                            } else {
                                Message.send(p, ChatColor.RED + Main.getLangFile().getString("error.cannotadd") + args[2]);
                            }
                        } else if (args[1].equalsIgnoreCase("remove")) {
                            Game.removeBlock(args[2]);
                            if (!Game.hasBlock(args[2])) {
                                Message.send(p, ChatColor.GREEN + Main.getLangFile().getString("commandfeedback.blockremoved"));
                            } else {
                                Message.send(p, ChatColor.RED + Main.getLangFile().getString("error.cannotremove") + args[2]);
                            }
                        } else {
                            Message.send(p, ChatColor.RED + "Usage: /fk block <add/remove> <Block> !");
                        }
                    } else {
                        Message.send(p, ChatColor.RED + Main.getLangFile().getString("error.nopermission"));
                    }
                } else if (args.length == 2) {
                    if (p.hasPermission("fk.block")) {
                        if (args[1].equalsIgnoreCase("list")) {
                            ArrayList<String> blocks = new ArrayList<String>();
                            for (Material material : Game.getBlocks()) {
                                blocks.add(material.toString());
                            }
                            Message.send(p,ChatColor.GREEN + "Blocks in the " + ChatColor.GRAY + "blacklist :");
                            Message.send(p,ChatColor.YELLOW + "  " + String.join(", ",blocks));
                        } else {
                            Message.send(p,ChatColor.RED + "Usage: /fk block list !");
                        }
                    } else {
                        Message.send(p, ChatColor.RED + Main.getLangFile().getString("error.nopermission"));
                    }
                } else {
                    Message.send(p, ChatColor.RED + "Usage: /fk block <add/remove> <Block> !");
                }
            } else if (args[0].equalsIgnoreCase("player")) {
                if (p.hasPermission("fk.player")) {
                    if (args.length == 4) {
                        Player player = Bukkit.getPlayer(args[2]);
                        if (args[1].equalsIgnoreCase("add")) {
                            if (player == null) {
                                Message.send(p, ChatColor.RED + Main.getLangFile().getString("error.playernotconnected"));
                            } else if (args[3].equalsIgnoreCase("Blue") || args[3].equalsIgnoreCase("Red") || args[3].equalsIgnoreCase("Green") || args[3].equalsIgnoreCase("Yellow")) {
                                if (Game.hasBase(args[3])) {
                                    if (!Game.getBase(args[3]).hasPlayer(player)) {
                                        Game.getBase(args[3]).addPlayer(player);
                                    } else {
                                        Message.send(p, ChatColor.RED + Main.getLangFile().getString("error.playerisinthisteam"));
                                    }
                                } else {
                                    Message.send(p, ChatColor.RED + Main.getLangFile().getString("error.thisisnotabase"));
                                }
                            } else {
                                Message.send(p, ChatColor.RED + "Usage: /fk player add <Player> <Blue, Red, Green, Yellow> !");
                            }
                        } else if (args[1].equalsIgnoreCase("remove")) {
                            if (player == null) {
                                Message.send(p, ChatColor.RED + Main.getLangFile().getString("error.playernotconnected"));
                            } else if (args[3].equalsIgnoreCase("Blue") || args[3].equalsIgnoreCase("Red") || args[3].equalsIgnoreCase("Green") || args[3].equalsIgnoreCase("Yellow")) {
                                if (Game.hasBase(args[3])) {
                                    if (Game.getBase(args[3]).hasPlayer(player)) {
                                        Game.getBase(args[3]).removePlayer(player);
                                    } else {
                                        Message.send(p, ChatColor.RED + Main.getLangFile().getString("error.playernotinthisteam"));
                                    }
                                } else {
                                    Message.send(p, ChatColor.RED + Main.getLangFile().getString("error.thisisnotabase"));
                                }
                            } else {
                                Message.send(p, ChatColor.RED + "Usage: /fk player remove <Player> <Blue, Red, Green, Yellow> !");
                            }
                        } else {
                            Message.send(p, ChatColor.RED + "Usage: /fk player <add/remove> <Player> <Blue, Red, Green, Yellow> !");
                        }
                    } else {
                        Message.send(p, ChatColor.RED + "Usage: /fk player <add/remove> <Player> <Blue, Red, Green, Yellow> !");
                    }
                } else {
                    Message.send(p, ChatColor.RED + Main.getLangFile().getString("error.nopermission"));
                }
            }
        } else {
            p.sendMessage(ChatColor.GOLD + "---------- FkPlugin Help ----------");
            p.sendMessage(ChatColor.YELLOW + "/fk start");
            p.sendMessage(ChatColor.WHITE + Main.getLangFile().getString("commanddescription.start"));
            p.sendMessage(ChatColor.GREEN + "    permission: fk.start");
            p.sendMessage(ChatColor.YELLOW + "/fk stop");
            p.sendMessage(ChatColor.WHITE + Main.getLangFile().getString("commanddescription.stop"));
            p.sendMessage(ChatColor.GREEN + "    permission: fk.stop");
            p.sendMessage(ChatColor.YELLOW + "/fk open");
            p.sendMessage(ChatColor.WHITE + Main.getLangFile().getString("commanddescription.open"));
            p.sendMessage(ChatColor.YELLOW + "/fk pause");
            p.sendMessage(ChatColor.WHITE + Main.getLangFile().getString("commanddescription.pause1"));
            p.sendMessage(ChatColor.WHITE + Main.getLangFile().getString("commanddescription.pause2"));
            p.sendMessage(ChatColor.WHITE + Main.getLangFile().getString("commanddescription.pause3"));
            p.sendMessage(ChatColor.WHITE + Main.getLangFile().getString("commanddescription.pause4"));
            p.sendMessage(ChatColor.GREEN + "    permission: fk.pause");
            p.sendMessage(ChatColor.YELLOW + "/fk player <add/remove> <Player> <Blue, Red, Green, Yellow>");
            p.sendMessage(ChatColor.WHITE + Main.getLangFile().getString("commanddescription.player"));
            p.sendMessage(ChatColor.GREEN + "    permission: fk.player");
            p.sendMessage(ChatColor.YELLOW + "/fk block <add/remove> <Block>");
            p.sendMessage(ChatColor.WHITE + Main.getLangFile().getString("commanddescription.block"));
            p.sendMessage(ChatColor.GREEN + "    permission: fk.block");
            p.sendMessage(ChatColor.YELLOW + "/fk block list");
            p.sendMessage(ChatColor.WHITE + Main.getLangFile().getString("commanddescription.blocklist"));
            p.sendMessage(ChatColor.GREEN + "    permission: fk.block");
            p.sendMessage(ChatColor.GOLD + "--------------------------------");
        }
        return false;
    }
}