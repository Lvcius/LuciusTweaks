package io.github.Lvcius.lTW.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TeamTeleportCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /tpteam <teamname>");
            return true;
        }

        Team team = player.getScoreboard().getTeam(args[0]);
        if (team == null) {
            sender.sendMessage(ChatColor.RED + "That team does not exist!");
            return true;
        }

        for (Player member : Bukkit.getOnlinePlayers()) {
            if (team.hasEntry(member.getName())) {
                member.teleport(player.getLocation());
            }
        }

        Bukkit.broadcastMessage("Teleported all members of team " + team.getName() + " to " + ChatColor.RED + sender.getName() + ChatColor.WHITE + "!");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], List.of("Red", "Green", "Blue", "Yellow", "Purple"), completions);
            return completions;
        }
        return List.of();
    }
}
