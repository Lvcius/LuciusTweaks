package io.github.Lvcius.lTW.commands;

import org.bukkit.*;
import org.bukkit.Color;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP;

public class GearCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) {
            //check for player
            commandSender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
        }

        //initial
        final Player player = (Player) commandSender;
        final PlayerInventory inventory = player.getInventory();

        //set health, saturation, and clear potions
        player.setHealth(player.getMaxHealth());
        if (player.getFoodLevel() < 20) {
            player.setFoodLevel(22);
        }
        player.clearActivePotionEffects();

        //reset inventory
        inventory.clear();

        player.setFireTicks(0);

        //setup armor
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS);

        Map<Enchantment, Integer> enchantments = new HashMap<>();
        enchantments.put(Enchantment.UNBREAKING, 3);
        enchantments.put(Enchantment.PROTECTION, 4);
        helmet.addEnchantments(enchantments);
        chestplate.addEnchantments(enchantments);
        leggings.addEnchantments(enchantments);
        boots.addEnchantments(enchantments);

        //give armor
        inventory.setHelmet(helmet);
        inventory.setChestplate(chestplate);
        inventory.setLeggings(leggings);
        inventory.setBoots(boots);

        //setup swords
        ItemStack regsword = new ItemStack(Material.DIAMOND_SWORD);
        ItemStack kbsword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta kbMeta = kbsword.getItemMeta();
        kbMeta.setDisplayName("Bonk Stick");
        kbsword.setItemMeta(kbMeta);

        Map<Enchantment, Integer> swordenchantments = new HashMap<>();
        swordenchantments.put(Enchantment.UNBREAKING, 3);
        swordenchantments.put(Enchantment.SHARPNESS, 5);
        swordenchantments.put(Enchantment.FIRE_ASPECT, 2);
        regsword.addEnchantments(swordenchantments);
        kbsword.addEnchantments(swordenchantments);
        kbsword.addEnchantment(Enchantment.KNOCKBACK, 2);

        //give swords and food
        inventory.setItemInOffHand(new ItemStack(Material.COOKED_BEEF, 64));
        inventory.setItem(0, regsword);
        inventory.setItem(1, kbsword);

        //setup buffs

        ItemStack speedstrength = new ItemStack(Material.POTION);
        PotionMeta speedstrengthMeta = (PotionMeta) speedstrength.getItemMeta();
        speedstrengthMeta.setColor(Color.fromRGB(255, 0, 60));
        speedstrengthMeta.setDisplayName(ChatColor.RESET + "Strength/Speed");
        speedstrengthMeta.addCustomEffect(new PotionEffect(PotionEffectType.STRENGTH, 3600, 1), true);
        speedstrengthMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 4500, 1), true);
        speedstrengthMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOWNESS, 600, 1), true);
        speedstrength.setItemMeta(speedstrengthMeta);

        ItemStack regen = new ItemStack(Material.POTION);
        PotionMeta regenMeta = (PotionMeta) regen.getItemMeta();
        regenMeta.setBasePotionType(PotionType.REGENERATION);
        regenMeta.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 4000, 0), true);
        regenMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOWNESS, 400, 0), true);
        regen.setItemMeta(regenMeta);

        ItemStack fireres = new ItemStack(Material.POTION);
        PotionMeta fireresMeta = (PotionMeta) fireres.getItemMeta();
        fireresMeta.setBasePotionType(PotionType.LONG_FIRE_RESISTANCE);
        fireres.setItemMeta(fireresMeta);

        //give buffs
        inventory.setItem(7, speedstrength);
        inventory.setItem(16, speedstrength);
        inventory.setItem(25, speedstrength);
        inventory.setItem(34, speedstrength);
        inventory.setItem(8, regen);
        inventory.setItem(17, regen);
        inventory.setItem(26, regen);
        inventory.setItem(35, regen);
        inventory.setItem(6, fireres);
        inventory.setItem(33, fireres);


        //fill with splash health
        ItemStack health = new ItemStack(Material.SPLASH_POTION);
        PotionMeta healthMeta = (PotionMeta) health.getItemMeta();
        healthMeta.setBasePotionType(PotionType.STRONG_HEALING);
        health.setItemMeta(healthMeta);
        for (int i = 0; i <= 24; i++) {
            inventory.addItem(health);
        }

        //successful gear message
        player.sendMessage(ChatColor.GREEN + player.getName() + " has been geared! :)");
        player.playSound(player, ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);

        //set gamemode
        player.setGameMode(GameMode.ADVENTURE);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull Command command,
                                                @NotNull String label,
                                                @NotNull String[] args) {
        final List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                completions.add(p.getName());
            }
            StringUtil.copyPartialMatches(args[0], List.of(), completions);
            return completions;
        }
        return List.of();
    }
}
