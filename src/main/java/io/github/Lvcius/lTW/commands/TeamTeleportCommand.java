package io.github.Lvcius.lTW.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
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

public class TeamTeleportCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        final Player player = (Player) sender;
        Server server = Bukkit.getServer();
        Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getTeam(args[0]);
        team.getPlayers().stream().filter(p -> p.isOnline()).forEach(p -> p.getPlayer().teleport(player.getLocation()));
        server.broadcastMessage("Teleported all members of team " + team.getName() + " to " + ChatColor.RED + sender.getName() + ChatColor.WHITE + "!");
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
        return List.of();
    }
}
