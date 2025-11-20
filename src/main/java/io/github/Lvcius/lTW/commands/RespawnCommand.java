package io.github.Lvcius.lTW.commands;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import static org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP;

public class RespawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        final Player player = (Player) sender;
        final PlayerInventory inventory = player.getInventory();

        //clear inventory
        inventory.clear();

        //clear teams
        Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getPlayerTeam(player);

        if (team != null) {
            team.removeEntry(player.getName());
        }

        //blank
        Team team2 = scoreboard.getTeam("blank");
        team2.addEntry(player.getName());

        //sets player health and saturation to max and clears potion effects
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(40);
        player.clearActivePotionEffects();
        player.setFireTicks(0);

        //set gamemode and tp to spawnpoint
        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(new Location(player.getWorld(), -477.5, -58, 273.5));

        player.playSound(player, ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);

        return true;
    }
}
