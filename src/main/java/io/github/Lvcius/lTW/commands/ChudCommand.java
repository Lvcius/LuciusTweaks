package io.github.Lvcius.lTW.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/** /chud — broadcasts a server-wide message about Lispy. */
public class ChudCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Bukkit.broadcastMessage(ChatColor.RED + "Lispy is a chud!");
        return true;
    }
}
