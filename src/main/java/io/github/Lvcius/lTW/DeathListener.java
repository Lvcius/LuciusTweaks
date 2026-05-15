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

    /**
     * After the vanilla respawn screen clears, place the player in spectator mode
     * at their death location so they can watch the fight continue.
     */
    @EventHandler
    public void onRespawn(PlayerPostRespawnEvent event) {
        Player player = event.getPlayer();
        Location deathLoc = player.getLastDeathLocation();
        player.setGameMode(GameMode.SPECTATOR);
        // getLastDeathLocation is null on first-ever respawn with no prior death
        if (deathLoc != null) {
            player.teleport(deathLoc);
        }
    }

    /**
     * Intercepts lethal damage to non-creative players before Minecraft processes
     * the death normally. This lets us:
     *   - suppress the vanilla death message and replace it with a coloured one
     *   - switch the player to spectator instead of triggering the respawn screen
     *   - record the kill in Lands war stats
     *   - move the dead player to the "blank" team
     *
     * HIGHEST priority so we see the final damage value after all other plugins
     * have modified it, then cancel to prevent the vanilla death sequence.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeadlyDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.getGameMode() == GameMode.CREATIVE) return;

        // absorption counts as extra health buffer before the player actually dies
        double effectiveHealth = player.getHealth() + player.getAbsorptionAmount();
        if (effectiveHealth - event.getFinalDamage() > 0) return;

        // suppress vanilla death message; we broadcast our own coloured version below
        player.getWorld().setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);

        Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getPlayerTeam(player);
        player.setGameMode(GameMode.SPECTATOR);

        String message = coloredName(player, team) + ChatColor.WHITE + " was killed by ";

        if (event instanceof EntityDamageByEntityEvent edbe) {
            Entity damager = edbe.getDamager();
            if (damager instanceof Player damagerPlayer) {
                // player-vs-player kill: record in Lands and show damager's team colour
                trackLandsKill(player, damagerPlayer);
                Team damagerTeam = scoreboard.getPlayerTeam(damagerPlayer);
                message += coloredName(damagerPlayer, damagerTeam);
            } else {
                // killed by a mob or projectile
                message += ChatColor.RED + damager.getName();
            }
        } else {
            // environmental kill (lava, fall, etc.)
            message += ChatColor.WHITE + event.getCause().toString();
        }

        // move to blank before broadcast so the team colour in chat is already updated
        PlayerUtil.moveToBlank(player);
        Bukkit.broadcastMessage(message + ChatColor.WHITE + "!");

        // safety net: players who die in the void would be stuck at y < -100 in spectator
        if (player.getLocation().getY() < -100) {
            Location loc = player.getLocation();
            player.teleport(new Location(loc.getWorld(), loc.getX(), 0, loc.getZ()));
        }

        // cancel so Bukkit doesn't trigger the normal death/respawn flow
        event.setCancelled(true);
    }

    /** Returns the player's name prefixed with their team colour, or white if teamless. */
    private String coloredName(Player player, Team team) {
        if (team != null && team.getColor() != null) {
            return team.getColor().toString() + player.getName();
        }
        return ChatColor.WHITE + player.getName();
    }

    /** Increments the killer's war kill count if both players are in the same active war. */
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
