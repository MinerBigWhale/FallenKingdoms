package be.miner.commands;

import be.miner.data.Timer;
import be.miner.gui.MainMenu;
import be.miner.Main;
import be.miner.data.Base;
import be.miner.data.Game;
import be.miner.utils.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class FkCommand implements CommandExecutor {
    private static Plugin pl = Bukkit.getPluginManager().getPlugin("FkPlugin");

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("start")) {
                if (args.length == 1) {
                    if (p.hasPermission("fk.start")) {
                        if (!Game.isRunning()) {
                            Timer.startFk(pl);
                        } else {
                            Bukkit.broadcastMessage(Prefix.getPrefix() + ChatColor.RED + Main.getConfigFile().getString("text.alreadystarted"));
                        }
                    } else {
                        p.sendMessage(Prefix.getPrefix() + ChatColor.RED + Main.getConfigFile().getString("text.nopermission"));
                    }
                } else {
                    p.sendMessage(Prefix.getPrefix() + ChatColor.RED + "Usage: /fk start !");
                }
            } else if (args[0].equalsIgnoreCase("stop")) {
                if (args.length == 1) {
                    if (p.hasPermission("fk.stop")) {
                        Timer.stopFk(pl);
                    } else {
                        p.sendMessage(Prefix.getPrefix() + ChatColor.RED + Main.getConfigFile().getString("text.nopermission"));
                    }
                } else {
                    p.sendMessage(Prefix.getPrefix() + ChatColor.RED + "Usage: /fk stop !");
                }
            } else if (args[0].equalsIgnoreCase("open")) {
                if (args.length == 1) {
                    MainMenu.openMenu(p);
                    p.sendMessage(Prefix.getPrefix() + ChatColor.GREEN + Main.getConfigFile().getString("text.menuopening"));
                } else {
                    p.sendMessage(Prefix.getPrefix() + ChatColor.RED + "Usage: /fk open !");
                }
            } else if (args[0].equalsIgnoreCase("pause")) {
                if (args.length == 1) {
                    if (p.hasPermission("fk.pause")) {
                        Game.pause();
                        if (Game.isPaused()) {
                            Bukkit.broadcastMessage(Prefix.getPrefix() + ChatColor.GREEN + Main.getConfigFile().getString("text.pauseon"));
                        } else {
                            Bukkit.broadcastMessage(Prefix.getPrefix() + ChatColor.YELLOW + Main.getConfigFile().getString("text.pauseoff"));
                        }
                    } else {
                        p.sendMessage(Prefix.getPrefix() + ChatColor.RED + Main.getConfigFile().getString("text.nopermission"));
                    }
                } else {
                    p.sendMessage(Prefix.getPrefix() + ChatColor.RED + "Usage: /fk pause !");
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
                                p.sendMessage(Prefix.getPrefix() + ChatColor.GREEN + Main.getConfigFile().getString("text.blockadded"));
                            } else {
                                p.sendMessage(Prefix.getPrefix() + ChatColor.RED + "Erreur : Impossible d'ajouter " + args[2]);
                            }
                        } else if (args[1].equalsIgnoreCase("remove")) {
                            Game.removeBlock(args[2]);
                            if (!Game.hasBlock(args[2])) {
                                p.sendMessage(Prefix.getPrefix() + ChatColor.GREEN + Main.getConfigFile().getString("text.blockremoved"));
                            } else {
                                p.sendMessage(Prefix.getPrefix() + ChatColor.RED + "Erreur : Impossible d'enlever " + args[2]);
                            }
                        } else {
                            p.sendMessage(Prefix.getPrefix() + ChatColor.RED + "Usage: /fk block <add/remove> <Block> !");
                        }
                    } else {
                        p.sendMessage(Prefix.getPrefix() + ChatColor.RED + Main.getConfigFile().getString("text.nopermission"));
                    }
                } else if (args.length == 2) {
                    if (p.hasPermission("fk.block")) {
                        if (args[1].equalsIgnoreCase("list")) {
                            for (Material material : Game.getBlocks()){
                                p.sendMessage(Prefix.getPrefix() + ChatColor.GREEN + "- " + material.name());
                            }
                        } else {
                            p.sendMessage(Prefix.getPrefix() + ChatColor.RED + "Usage: /fk block list !");
                        }
                    } else {
                        p.sendMessage(Prefix.getPrefix() + ChatColor.RED + Main.getConfigFile().getString("text.nopermission"));
                    }
                } else {
                    p.sendMessage(Prefix.getPrefix() + ChatColor.RED + "Usage: /fk block <add/remove> <Block> !");
                }
            } else if (args[0].equalsIgnoreCase("player")) {
                if (p.hasPermission("fk.player")) {
                    if (args.length == 4) {
                        Player player = Bukkit.getPlayer(args[2]);
                        if (args[1].equalsIgnoreCase("add")) {
                            if (player == null) {
                                p.sendMessage(Prefix.getPrefix() + ChatColor.RED + Main.getConfigFile().getString("text.playernotconnected"));
                            } else if (args[3].equalsIgnoreCase("Blue") || args[3].equalsIgnoreCase("Red") || args[3].equalsIgnoreCase("Green") || args[3].equalsIgnoreCase("Yellow")) {
                                if (Game.hasBase(args[3])) {
                                    if (!Game.getBase(args[3]).hasPlayer(player)) {
                                        Game.getBase(args[3]).addPlayer(player);
                                    } else {
                                        p.sendMessage(Prefix.getPrefix() + ChatColor.RED + "Erreur: le joueur est déja dans cette équipe !");
                                    }
                                }else {
                                    p.sendMessage(Prefix.getPrefix() + ChatColor.RED + "Erreur: Cette équipe ne correspond à aucune base activée !");
                                }
                            } else {
                                p.sendMessage(Prefix.getPrefix() + ChatColor.RED + "Usage: /fk player add <Player> <Blue, Red, Green, Yellow> !");
                            }
                        } else if (args[1].equalsIgnoreCase("remove")) {
                            if (player == null) {
                                p.sendMessage(Prefix.getPrefix() + ChatColor.RED + Main.getConfigFile().getString("text.playernotconnected"));
                            } else if (args[3].equalsIgnoreCase("Blue") || args[3].equalsIgnoreCase("Red") || args[3].equalsIgnoreCase("Green") || args[3].equalsIgnoreCase("Yellow")) {
                                if (Game.hasBase(args[3])) {
                                    if (Game.getBase(args[3]).hasPlayer(player)) {
                                        Game.getBase(args[3]).removePlayer(player);
                                    } else {
                                        p.sendMessage(Prefix.getPrefix() + ChatColor.RED + "Erreur: le joueur n'est pas dans cette équipe !");
                                    }
                                } else {
                                    p.sendMessage(Prefix.getPrefix() + ChatColor.RED + "Erreur: Cette équipe ne correspond à aucune base activée !");
                                }
                            } else {
                                p.sendMessage(Prefix.getPrefix() + ChatColor.RED + "Usage: /fk player remove <Player> <Blue, Red, Green, Yellow> !");
                            }
                        } else {
                            p.sendMessage(Prefix.getPrefix() + ChatColor.RED + "Usage: /fk player <add/remove> <Player> <Blue, Red, Green, Yellow> !");
                        }
                    } else {
                        p.sendMessage(Prefix.getPrefix() + ChatColor.RED + "Usage: /fk player <add/remove> <Player> <Blue, Red, Green, Yellow> !");
                    }
                } else {
                    p.sendMessage(Prefix.getPrefix() + ChatColor.RED + Main.getConfigFile().getString("text.nopermission"));
                }
            }
        } else {
            p.sendMessage(ChatColor.GOLD + "---------- FkPlugin Help ----------");
            p.sendMessage(ChatColor.YELLOW + "/fk start");
            p.sendMessage(ChatColor.WHITE + Main.getConfigFile().getString("text.commanddescription.start"));
            p.sendMessage(ChatColor.GREEN + "    permission: fk.start");
            p.sendMessage(ChatColor.YELLOW + "/fk stop");
            p.sendMessage(ChatColor.WHITE + Main.getConfigFile().getString("text.commanddescription.stop"));
            p.sendMessage(ChatColor.GREEN + "    permission: fk.stop");
            p.sendMessage(ChatColor.YELLOW + "/fk open");
            p.sendMessage(ChatColor.WHITE + Main.getConfigFile().getString("text.commanddescription.open"));
            p.sendMessage(ChatColor.YELLOW + "/fk pause");
            p.sendMessage(ChatColor.WHITE + Main.getConfigFile().getString("text.commanddescription.pause1"));
            p.sendMessage(ChatColor.WHITE + Main.getConfigFile().getString("text.commanddescription.pause2"));
            p.sendMessage(ChatColor.WHITE + Main.getConfigFile().getString("text.commanddescription.pause3"));
            p.sendMessage(ChatColor.WHITE + Main.getConfigFile().getString("text.commanddescription.pause4"));
            p.sendMessage(ChatColor.GREEN + "    permission: fk.pause");
            p.sendMessage(ChatColor.YELLOW + "/fk player <add/remove> <Player> <Blue, Red, Green, Yellow>");
            p.sendMessage(ChatColor.WHITE + Main.getConfigFile().getString("text.commanddescription.player"));
            p.sendMessage(ChatColor.GREEN + "    permission: fk.player");
            p.sendMessage(ChatColor.YELLOW + "/fk block <add/remove> <Block>");
            p.sendMessage(ChatColor.WHITE + Main.getConfigFile().getString("text.commanddescription.block"));
            p.sendMessage(ChatColor.GREEN + "    permission: fk.block");
            p.sendMessage(ChatColor.YELLOW + "/fk block list");
            p.sendMessage(ChatColor.WHITE + Main.getConfigFile().getString("text.commanddescription.blocklist"));
            p.sendMessage(ChatColor.GREEN + "    permission: fk.block");
            p.sendMessage(ChatColor.GOLD + "--------------------------------");
        }
        return false;
    }
}