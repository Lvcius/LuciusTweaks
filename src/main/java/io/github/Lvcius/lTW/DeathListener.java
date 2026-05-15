package io.github.Lvcius.lTW;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import io.github.Lvcius.lTW.util.PlayerUtil;
import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.land.LandWorld;
import me.angeschossen.lands.api.player.LandPlayer;
import me.angeschossen.lands.api.war.War;
import me.angeschossen.lands.api.war.WarStats;
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

    private final LandsIntegration api = LandsIntegration.of(LTW.getInstance());

    @EventHandler
    public void onRespawn(PlayerPostRespawnEvent event) {
        Player player = event.getPlayer();
        Location deathLoc = player.getLastDeathLocation();
        player.setGameMode(GameMode.SPECTATOR);
        if (deathLoc != null) {
            player.teleport(deathLoc);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeadlyDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.getGameMode() == GameMode.CREATIVE) return;

        double effectiveHealth = player.getHealth() + player.getAbsorptionAmount();
        if (effectiveHealth - event.getFinalDamage() > 0) return;

        player.getWorld().setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);

        Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getPlayerTeam(player);
        player.setGameMode(GameMode.SPECTATOR);

        String message = coloredName(player, team) + ChatColor.WHITE + " was killed by ";

        if (event instanceof EntityDamageByEntityEvent edbe) {
            Entity damager = edbe.getDamager();
            if (damager instanceof Player damagerPlayer) {
                trackLandsKill(player, damagerPlayer);
                Team damagerTeam = scoreboard.getPlayerTeam(damagerPlayer);
                message += coloredName(damagerPlayer, damagerTeam);
            } else {
                message += ChatColor.RED + damager.getName();
            }
        } else {
            message += ChatColor.WHITE + event.getCause().toString();
        }

        PlayerUtil.moveToBlank(player);
        Bukkit.broadcastMessage(message + ChatColor.WHITE + "!");

        if (player.getLocation().getY() < -100) {
            Location loc = player.getLocation();
            player.teleport(new Location(loc.getWorld(), loc.getX(), 0, loc.getZ()));
        }

        event.setCancelled(true);
    }

    private String coloredName(Player player, Team team) {
        if (team != null && team.getColor() != null) {
            return team.getColor().toString() + player.getName();
        }
        return ChatColor.WHITE + player.getName();
    }

    private void trackLandsKill(Player victim, Player killer) {
        LandWorld landworld = api.getWorld(victim.getWorld());
        if (landworld == null) return;
        LandPlayer landVictim = api.getLandPlayer(victim.getUniqueId());
        LandPlayer landKiller = api.getLandPlayer(killer.getUniqueId());
        if (landVictim == null || landKiller == null || !landVictim.isInWar()) return;
        for (War war : landVictim.getWars()) {
            if (landVictim.isInWar(war) && landKiller.isInWar(war)) {
                WarStats stats = war.getStats(war.getTeam(landKiller));
                stats.setKills(stats.getKills() + 1);
            }
        }
    }
}
