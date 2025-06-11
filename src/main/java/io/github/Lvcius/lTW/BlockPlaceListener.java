package io.github.Lvcius.lTW;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Material blockType = event.getBlock().getType();
        final Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.SURVIVAL) {
            if (blockType != Material.REDSTONE_BLOCK && blockType != Material.COBWEB) {
                event.setCancelled(true);
            }
        }
    }
}
