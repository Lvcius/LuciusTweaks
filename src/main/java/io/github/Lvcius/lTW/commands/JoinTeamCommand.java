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

/**
 * /join <teamname> [player] — adds a player to one of the five coloured war teams.
 * The player's previous team is removed first to prevent them appearing on two rosters.
 * If no target is specified the sender joins themselves.
 */
public class JoinTeamCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /join <teamname> [player]");
            return true;
        }

        // resolve target: sender if no second argument, otherwise a named online player
        Player player;
        if (args.length < 2) {
            if (!(sender instanceof Player p)) {
                sender.sendMessage(ChatColor.RED + "You must be a player or specify a target.");
                return true;
            }
            player = p;
        } else {
            player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
        }

        Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getTeam(args[0]);
        if (team == null) {
            sender.sendMessage(ChatColor.RED + "That team does not exist!");
            return true;
        }

        // remove from current team so the player is never on two rosters at once
        Team current = scoreboard.getPlayerTeam(player);
        if (current != null) current.removeEntry(player.getName());
        team.addEntry(player.getName());

        player.sendMessage(ChatColor.WHITE + "You have joined team " + team.getName() + ChatColor.WHITE + "!");
        player.playSound(player, ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], List.of("Red", "Green", "Blue", "Yellow", "Purple"), completions);
            return completions;
        }
        if (args.length == 2) {
            List<String> players = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) players.add(p.getName());
            StringUtil.copyPartialMatches(args[1], players, completions);
            return completions;
        }
        return List.of();
    }
}
