package io.github.Lvcius.lTW;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Intended to restrict which blocks survival-mode players can place during wars,
 * limiting them to the kit blocks they carry (redstone blocks, cobwebs).
 * Currently disabled — uncomment the body to activate.
 * NOTE: this listener is not yet registered in LTW.java; add
 *   getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
 * to onEnable() when ready to enable it.
 */
public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        // Material blockType = event.getBlock().getType();
        // Player player = event.getPlayer();
        // if (player.getGameMode() == GameMode.SURVIVAL) {
        //     if (blockType != Material.REDSTONE_BLOCK
        //             && blockType != Material.COBWEB) {
        //         event.setCancelled(true);
        //     }
        // }
    }
}
