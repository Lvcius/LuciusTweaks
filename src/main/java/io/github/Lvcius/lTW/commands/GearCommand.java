package io.github.Lvcius.lTW.commands;

import io.github.Lvcius.lTW.util.KitBuilder;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP;

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

        ItemStack[] armor = KitBuilder.buildDiamondArmor();
        inventory.setHelmet(armor[0]);
        inventory.setChestplate(armor[1]);
        inventory.setLeggings(armor[2]);
        inventory.setBoots(armor[3]);

        inventory.setItem(0, KitBuilder.buildRegSword());
        inventory.setItem(1, KitBuilder.buildBonkStick());
        inventory.setItemInOffHand(new ItemStack(Material.COOKED_BEEF, 64));

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

        // fill remaining slots with splash health (slots 2-5, 9-15, 18-24, 27-32)
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
