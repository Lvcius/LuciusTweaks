package io.github.Lvcius.lTW;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scoreboard.*;

public class JoinLeaveListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        //initial
        final Player player = event.getPlayer();
        final PlayerInventory inventory = player.getInventory();
        //clear teams
        Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getPlayerTeam(player);

        if (team != null) {
            team.removeEntry(player.getName());
        }

        //blank
        Team team2 = scoreboard.getTeam("blank");
        team2.addEntry(player.getName());

        //clear inventory
        inventory.clear();
        //sets player health and saturation to max and clears potion effects
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(40);
        player.clearActivePotionEffects();
        //set gamemode and tp to spawnpoint
        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(new Location(player.getWorld(), -477.5, -58, 273.5));
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        //gets event player
        final Player player = event.getPlayer();
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam(player.getName());
        team.removePlayer(player);
    }
}
