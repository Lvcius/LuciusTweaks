package io.github.Lvcius.lTW.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP;

public class LeaveTeamCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final Player player = (Player) sender;
        //clear teams
        Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getPlayerTeam(player);

        //blank
        Team team2 = scoreboard.getTeam("blank");
        team2.addEntry(player.getName());

        player.sendMessage(ChatColor.WHITE + "You have left team " + team.getName() + ChatColor.WHITE + "!");
        player.playSound(player, ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
        team.removeEntry(player.getName());
        return true;
    }
}
