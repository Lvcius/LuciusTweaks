package io.github.Lvcius.lTW;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Material blockType = event.getBlock().getType();
        final Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.SURVIVAL) {
            if (blockType != Material.REDSTONE_BLOCK && blockType != Material.COBWEB) {
                event.setCancelled(true);
            }
        }
    }
}
