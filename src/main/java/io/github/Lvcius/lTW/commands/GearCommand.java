package io.github.Lvcius.lTW.commands;

import io.github.Lvcius.lTW.util.KitBuilder;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP;

/**
 * /gear — gives the command sender a standard PvP loadout in adventure mode.
 *
 * Inventory layout (slots 0-35, offhand):
 *   0  reg sword      1  bonk stick     2-5   health splash x4
 *   6  fire res       7  str/speed       8    regen
 *   9-15 health x7   16  str/speed      17   regen
 *  18-24 health x7   25  str/speed      26   regen
 *  27-32 health x6   33  fire res       34   str/speed   35 regen
 *  offhand: cooked beef x64
 */
public class GearCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
            return true;
        }

        PlayerInventory inventory = player.getInventory();
        inventory.clear();
        player.setHealth(player.getMaxHealth());
        if (player.getFoodLevel() < 20) player.setFoodLevel(20);
        player.clearActivePotionEffects();
        player.setFireTicks(0);

        // armor
        ItemStack[] armor = KitBuilder.buildDiamondArmor();
        inventory.setHelmet(armor[0]);
        inventory.setChestplate(armor[1]);
        inventory.setLeggings(armor[2]);
        inventory.setBoots(armor[3]);

        // weapons and food
        inventory.setItem(0, KitBuilder.buildRegSword());
        inventory.setItem(1, KitBuilder.buildBonkStick());
        inventory.setItemInOffHand(new ItemStack(Material.COOKED_BEEF, 64));

        // potions — fixed slots so players always find them in the same place
        ItemStack speedstrength = KitBuilder.buildSpeedStrengthPotion();
        ItemStack regen = KitBuilder.buildRegenPotion();
        ItemStack fireres = KitBuilder.buildFireResPotion();
        ItemStack health = KitBuilder.buildHealthSplash();

        inventory.setItem(6, fireres);
        inventory.setItem(7, speedstrength);
        inventory.setItem(8, regen);
        inventory.setItem(16, speedstrength);
        inventory.setItem(17, regen);
        inventory.setItem(25, speedstrength);
        inventory.setItem(26, regen);
        inventory.setItem(33, fireres);
        inventory.setItem(34, speedstrength);
        inventory.setItem(35, regen);

        // fill every remaining slot with splash health (24 total)
        for (int i = 2; i <= 5; i++) inventory.setItem(i, health);
        for (int i = 9; i <= 15; i++) inventory.setItem(i, health);
        for (int i = 18; i <= 24; i++) inventory.setItem(i, health);
        for (int i = 27; i <= 32; i++) inventory.setItem(i, health);

        player.setGameMode(GameMode.ADVENTURE);
        player.sendMessage(ChatColor.GREEN + player.getName() + " has been geared! :)");
        player.playSound(player, ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
