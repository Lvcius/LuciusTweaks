package io.github.Lvcius.lTW;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.land.Land;
import me.angeschossen.lands.api.land.LandWorld;
import me.angeschossen.lands.api.player.LandPlayer;
import me.angeschossen.lands.api.war.War;
import me.angeschossen.lands.api.war.WarStats;
import me.angeschossen.lands.api.war.enums.WarTeam;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;


public class DeathListener implements Listener {

    LandsIntegration api = LandsIntegration.of(LTW.getInstance());

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

        LandWorld landworld = api.getWorld(player.getWorld());

        World world = player.getWorld();
        world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, true);



        if (player.getGameMode() != GameMode.CREATIVE) {
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

                //blank
                Team team2 = scoreboard.getTeam("blank");
                team2.addEntry(player.getName());

                if (event instanceof EntityDamageByEntityEvent && !(event.getDamageSource() instanceof Player)) {
                    Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
                    message += ChatColor.RED + damager.getName();
                }

                else if (event.getDamageSource() instanceof Player) {

                    final Player damager = ((Player) event.getDamageSource()).getPlayer();

                    //LANDS POINTS
                    if (landworld != null) {
                        LandPlayer landplayer = api.getLandPlayer(player.getUniqueId());
                        LandPlayer landopp = api.getLandPlayer(damager.getUniqueId());
                        if (landplayer != null && landplayer.isInWar()) {
                            for (War war :landplayer.getWars()) {
                                if (landplayer.isInWar(war) && landopp.isInWar(war)) {
                                    WarStats warstats = war.getStats(war.getTeam(landopp));
                                    warstats.setKills(warstats.getKills() + 1);
                                }
                            }
                        }
                    }

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
