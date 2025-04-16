package io.github.Lvcius.lTW.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP;

public class JoinTeamCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /join <teamname> <player>");
        }
        // check for <player> argument
        else {
            if (args.length < 2) {
                final Player player = Bukkit.getPlayer(sender.getName());
                //add to team
                Scoreboard scoreboard = player.getScoreboard();
                Team team = scoreboard.getTeam(args[0]);
                team.addEntry(player.getName());
                //confirm message
                player.playSound(player, ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                player.sendMessage(ChatColor.WHITE + "You have joined team " + team.getName() + ChatColor.WHITE + "!");
            }
            else {
                final Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    final Player playersender = Bukkit.getPlayer(sender.getName());
                    playersender.sendMessage(ChatColor.RED + "That player does not exist!");
                }
                else {
                    //add to team
                    Scoreboard scoreboard = player.getScoreboard();
                    Team team = scoreboard.getTeam(args[0]);
                    team.addEntry(player.getName());
                    //confirm message
                    player.playSound(player, ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                    player.sendMessage(ChatColor.WHITE + "You have joined team " + team.getName() + ChatColor.WHITE + "!");
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
        final List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], List.of("Red", "Green", "Blue", "Yellow", "Purple"), completions);
            return completions;
        }

        final List<String> completions2 = new ArrayList<>();
        if (args.length == 2) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                completions2.add(p.getName());
            }
            StringUtil.copyPartialMatches(args[1], List.of(), completions2);
            return completions2;
        }
        return List.of();
    }
}
