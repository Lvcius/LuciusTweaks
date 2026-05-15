package io.github.Lvcius.lTW.util;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public final class PlayerUtil {

    private PlayerUtil() {}

    public static void resetToSpawn(Player player) {
        clearPlayer(player);
        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(new Location(player.getWorld(), -477.5, -58, 273.5));
    }

    public static void clearPlayer(Player player) {
        player.getInventory().clear();
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setSaturation(20f);
        player.clearActivePotionEffects();
        player.setFireTicks(0);
    }

    public static void refillStats(Player player) {
        player.setHealth(player.getMaxHealth());
        if (player.getFoodLevel() < 20) player.setFoodLevel(20);
        player.clearActivePotionEffects();
        player.setFireTicks(0);
    }

    public static void moveToBlank(Player player) {
        Scoreboard sb = player.getScoreboard();
        for (Team t : sb.getTeams()) {
            if (t.hasEntry(player.getName())) t.removeEntry(player.getName());
        }
        Team blank = sb.getTeam("blank");
        if (blank != null) blank.addEntry(player.getName());
    }
}
