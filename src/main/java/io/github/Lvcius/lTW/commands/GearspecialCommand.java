package io.github.Lvcius.lTW.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP;

public class GearspecialCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            //check for player
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
        }
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /gearspecial <player/group> <target>");
        }
        else {
            Server server = Bukkit.getServer();
            //assign arguments
            String targetchoice = args[0];
            String target = args[1];

            //select target player
            if (targetchoice.equals("player")) {
                //initial
                final Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(ChatColor.RED + "That player does not exist!");
                }
                else {
                    final PlayerInventory inventory = player.getInventory();

                    //annoy players
                    player.playSound(player, ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);

                    //set health, saturation, and clear potions
                    player.setHealth(player.getMaxHealth());
                    if (player.getFoodLevel() < 20) {
                        player.setFoodLevel(22);
                    }
                    player.clearActivePotionEffects();

                    //reset inventory
                    inventory.clear();
                    player.setFireTicks(0);

                    //give gear

                    //success message
                    sender.sendMessage(ChatColor.GREEN + "Geared " + args[1]);
                }
            }
            //select target group
            else if (targetchoice.equals("group")) {
                final Player playersender = Bukkit.getPlayer(sender.getName());
                Scoreboard scoreboard = playersender.getScoreboard();
                Team team = scoreboard.getTeam(args[1]);
                if (team == null) {
                    sender.sendMessage(ChatColor.RED + "That team does not exist!");
                }
                else if (team.getEntries() == null) {
                    sender.sendMessage(ChatColor.RED + "That group has no players!");
                }
                else {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (team.hasEntry(player.getName())) {
                            final PlayerInventory inventory = player.getInventory();

                            //annoy players
                            player.playSound(player, ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);

                            //set health, saturation, and clear potions
                            player.setHealth(player.getMaxHealth());
                            if (player.getFoodLevel() < 20) {
                                player.setFoodLevel(22);
                            }
                            player.clearActivePotionEffects();

                            //reset inventory
                            inventory.clear();
                            player.setFireTicks(0);

                            //give gear
                        }
                    }
                    //success message
                    sender.sendMessage(ChatColor.GREEN + "Geared all members of team " + team.getName());
                }
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull Command command,
                                                @NotNull String label,
                                                @NotNull String[] args) {
        Server server = Bukkit.getServer();
        final List<String> completions = new ArrayList<>();
        final List<String> completions2 = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], List.of("player", "group"), completions);
            return completions;
        }
        if (args[0].equals("player")) {
            if (args.length == 2) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    completions2.add(p.getName());
                }
                StringUtil.copyPartialMatches(args[1], List.of(), completions2);
                return completions2;
            }
        }
        else if (args[0].equals("group")) {
            if (args.length == 2) {
                StringUtil.copyPartialMatches(args[1], List.of("Red", "Green", "Blue", "Yellow", "Purple"), completions2);
                return completions2;
            }
        }
        return List.of();
    }
}
