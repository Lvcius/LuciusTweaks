package io.github.Lvcius.lTW;

import io.github.Lvcius.lTW.commands.*;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public final class LTW extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("LTW Enabled");
        getServer().getPluginManager().registerEvents(new JoinLeaveListener(), this);
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getCommand("gear").setExecutor(new GearCommand());
        getCommand("gearspecial").setExecutor(new GearspecialCommand());
        getCommand("respawn").setExecutor(new RespawnCommand());
        getCommand("join").setExecutor(new JoinTeamCommand());
        getCommand("leave").setExecutor(new LeaveTeamCommand());
        getCommand("tpteam").setExecutor(new TeamTeleportCommand());
        getCommand("chud").setExecutor(new ChudCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
