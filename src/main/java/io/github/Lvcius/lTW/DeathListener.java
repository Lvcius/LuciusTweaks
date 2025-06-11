package io.github.Lvcius.lTW;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;


public class DeathListener implements Listener {
    @EventHandler
    public void onRespawn(PlayerPostRespawnEvent event) {
        final Player player = event.getPlayer();
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(new Location(player.getWorld(), player.getLastDeathLocation().getX(), player.getLastDeathLocation().getY(), player.getLastDeathLocation().getZ(), player.getLastDeathLocation().getYaw(), player.getLastDeathLocation().getPitch()));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeadlyDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return; // Added safety check

        final Player player = (Player) event.getEntity();
        final double finalDamage = event.getFinalDamage(); // Use built-in final damage calculation
        final double effectiveHealth = player.getHealth() + player.getAbsorptionAmount();

        World world = player.getWorld();
        world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, true);

        if (player.getGameMode() != GameMode.SURVIVAL) {
            world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
            if (effectiveHealth - finalDamage <= 0) {
                Scoreboard scoreboard = player.getScoreboard();
                Team team = scoreboard.getPlayerTeam(player);
                player.setGameMode(GameMode.SPECTATOR);
                Server server = Bukkit.getServer();

                String message = "";

                if (team != null && team.getColor() != null) {
                    message = team.getColor().toString() + player.getName() + ChatColor.WHITE + " was killed by ";
                } else {
                    message = ChatColor.WHITE + player.getName().toString() + ChatColor.WHITE + " was killed by ";
                }

                if (event instanceof EntityDamageByEntityEvent && !(event.getDamageSource() instanceof Player)) {
                    Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
                    message += ChatColor.RED + damager.getName();
                }

                else if (event.getDamageSource() instanceof Player) {
                    final Player damager = ((Player) event.getDamageSource()).getPlayer();
                    Team damagerTeam = scoreboard.getPlayerTeam(damager);
                    if (damagerTeam != null && damagerTeam.getColor() != null) {
                        message += damagerTeam.getColor().toString() + damager.displayName().toString();
                    } else {
                        message += ChatColor.WHITE + damager.displayName().toString();
                    }

                }

                else {
                    message += ChatColor.WHITE + event.getCause().toString();
                }

                server.broadcastMessage(message + ChatColor.WHITE + "!");

                if (team != null) {
                    team.removeEntry(player.getName());
                }

                if (player.getLocation().getY() < -100) {
                    player.teleport(new Location(player.getWorld(), player.getLocation().getX(), 0, player.getLocation().getZ()));
                }

                event.setCancelled(true); // Prevent normal death handling
            }
        }

    }
}
