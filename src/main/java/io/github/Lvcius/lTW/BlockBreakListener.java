package io.github.Lvcius.lTW;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Intended to restrict which blocks survival-mode players can break during wars,
 * limiting them to the kit blocks they placed (redstone blocks, cobwebs,
 * blackstone slabs). Currently disabled — uncomment the body to activate.
 */
public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        // Material blockType = event.getBlock().getType();
        // Player player = event.getPlayer();
        // if (player.getGameMode() == GameMode.SURVIVAL) {
        //     if (blockType != Material.REDSTONE_BLOCK
        //             && blockType != Material.COBWEB
        //             && blockType != Material.BLACKSTONE_SLAB) {
        //         event.setCancelled(true);
        //     }
        // }
    }
}
