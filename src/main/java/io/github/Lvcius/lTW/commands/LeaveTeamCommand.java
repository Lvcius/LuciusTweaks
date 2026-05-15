package io.github.Lvcius.lTW.commands;

import io.github.Lvcius.lTW.util.PlayerUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP;

public class LeaveTeamCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
            return true;
        }

        Team current = player.getScoreboard().getPlayerTeam(player);
        String teamName = current != null ? current.getName() : "none";

        PlayerUtil.moveToBlank(player);

        player.sendMessage(ChatColor.WHITE + "You have left team " + teamName + "!");
        player.playSound(player, ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);

        return true;
    }
}
