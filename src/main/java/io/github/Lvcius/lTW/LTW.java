package io.github.Lvcius.lTW;

import io.github.Lvcius.lTW.commands.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class LTW extends JavaPlugin {

    private static LTW instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("LTW Enabled");

        // register listeners
        getServer().getPluginManager().registerEvents(new JoinLeaveListener(), this);
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);

        // register commands
        getCommand("gear").setExecutor(new GearCommand());
        getCommand("gearspecial").setExecutor(new GearspecialCommand());
        getCommand("gearwar").setExecutor(new GearwarCommand());
        getCommand("respawn").setExecutor(new RespawnCommand());
        getCommand("join").setExecutor(new JoinTeamCommand());
        getCommand("leave").setExecutor(new LeaveTeamCommand());
        getCommand("tpteam").setExecutor(new TeamTeleportCommand());
        getCommand("chud").setExecutor(new ChudCommand());
    }

    @Override
    public void onDisable() {}

    public static LTW getInstance() {
        return instance;
    }
}
