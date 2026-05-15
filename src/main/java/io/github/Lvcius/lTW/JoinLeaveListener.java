package io.github.Lvcius.lTW;

import io.github.Lvcius.lTW.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class JoinLeaveListener implements Listener {

    /**
     * On join: strip all team memberships, put the player in the "blank" lobby team,
     * then wipe their inventory/stats and teleport them to spawn in adventure mode.
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerUtil.moveToBlank(player);
        PlayerUtil.resetToSpawn(player);
    }

    /**
     * On leave: remove the player from whichever team they're currently on so the
     * team roster stays accurate for commands like /tpteam.
     */
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // use main scoreboard explicitly — the player object may be partially invalid at quit time
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getPlayerTeam(player);
        if (team != null) team.removeEntry(player.getName());
    }
}
