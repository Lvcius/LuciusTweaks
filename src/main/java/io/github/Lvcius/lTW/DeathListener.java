package io.github.Lvcius.lTW;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class DeathListener implements Listener {
    @EventHandler
    public void onRespawn(PlayerPostRespawnEvent event) {
        final Player player = event.getPlayer();
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(new Location(player.getWorld(), player.getLastDeathLocation().getX(), player.getLastDeathLocation().getY(), player.getLastDeathLocation().getZ()));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeadlyDamage(EntityDamageEvent event) {
        final Player player = (Player) event.getEntity();
        if (player.getHealth() - event.getDamage() <= 0) {
            //set to spectator
            player.setGameMode(GameMode.SPECTATOR);
            Server server = Bukkit.getServer();

            if (event instanceof EntityDamageByEntityEvent) {
                final Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
                server.broadcastMessage(ChatColor.RED + player.getName() + ChatColor.WHITE + " was killed by " + ChatColor.RED + damager.getName() + ChatColor.WHITE + "!");
                event.setCancelled(true);
            }
            else {
                server.broadcastMessage(ChatColor.RED + player.getName() + ChatColor.WHITE + " was killed by " + ChatColor.RED + event.getCause() + ChatColor.WHITE + "!");
                event.setCancelled(true);
            }
        }
    }
}
