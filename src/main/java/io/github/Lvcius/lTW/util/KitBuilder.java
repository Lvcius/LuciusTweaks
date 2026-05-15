package io.github.Lvcius.lTW.util;

import io.github.Lvcius.lTW.LTW;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public final class KitBuilder {

    private KitBuilder() {}

    /** Returns [helmet, chestplate, leggings, boots] */
    public static ItemStack[] buildDiamondArmor() {
        Material[] pieces = {
            Material.DIAMOND_HELMET,
            Material.DIAMOND_CHESTPLATE,
            Material.DIAMOND_LEGGINGS,
            Material.DIAMOND_BOOTS
        };
        ItemStack[] armor = new ItemStack[4];
        for (int i = 0; i < 4; i++) {
            armor[i] = new ItemStack(pieces[i]);
            armor[i].addEnchantment(Enchantment.UNBREAKING, 3);
            armor[i].addEnchantment(Enchantment.PROTECTION, 4);
        }
        return armor;
    }

    public static ItemStack buildRegSword() {
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addEnchantment(Enchantment.UNBREAKING, 3);
        sword.addEnchantment(Enchantment.SHARPNESS, 5);
        sword.addEnchantment(Enchantment.FIRE_ASPECT, 2);
        return sword;
    }

    public static ItemStack buildBonkStick() {
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName("Bonk Stick");
        sword.setItemMeta(meta);
        sword.addEnchantment(Enchantment.UNBREAKING, 3);
        sword.addEnchantment(Enchantment.SHARPNESS, 5);
        sword.addEnchantment(Enchantment.FIRE_ASPECT, 2);
        sword.addEnchantment(Enchantment.KNOCKBACK, 2);
        return sword;
    }

    public static ItemStack buildScythe(Material material, double damageBonus) {
        ItemStack scythe = new ItemStack(material);
        ItemMeta meta = scythe.getItemMeta();
        meta.setDisplayName("Scythe");
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
            new NamespacedKey(LTW.getInstance(), "scythe_attack_speed"),
            -2.4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HAND));
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
            new NamespacedKey(LTW.getInstance(), "scythe_attack_damage"),
            damageBonus, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HAND));
        scythe.setItemMeta(meta);
        scythe.addEnchantment(Enchantment.SHARPNESS, 5);
        scythe.addEnchantment(Enchantment.UNBREAKING, 3);
        return scythe;
    }

    public static ItemStack buildSpeedStrengthPotion() {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.setColor(Color.fromRGB(255, 0, 60));
        meta.setDisplayName(ChatColor.RESET + "Strength/Speed");
        meta.addCustomEffect(new PotionEffect(PotionEffectType.STRENGTH, 3600, 1), true);
        meta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 4500, 1), true);
        meta.addCustomEffect(new PotionEffect(PotionEffectType.SLOWNESS, 600, 1), true);
        potion.setItemMeta(meta);
        return potion;
    }

    public static ItemStack buildRegenPotion() {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.setBasePotionType(PotionType.REGENERATION);
        meta.addCustomEffect(new PotionEffect(PotionEffectType.REGENERATION, 4000, 0), true);
        meta.addCustomEffect(new PotionEffect(PotionEffectType.SLOWNESS, 400, 0), true);
        potion.setItemMeta(meta);
        return potion;
    }

    public static ItemStack buildFireResPotion() {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.setBasePotionType(PotionType.LONG_FIRE_RESISTANCE);
        potion.setItemMeta(meta);
        return potion;
    }

    public static ItemStack buildHealthSplash() {
        ItemStack potion = new ItemStack(Material.SPLASH_POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.setBasePotionType(PotionType.STRONG_HEALING);
        potion.setItemMeta(meta);
        return potion;
    }

    public static ItemStack buildTurtleMaster() {
        ItemStack potion = new ItemStack(Material.SPLASH_POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.setBasePotionType(PotionType.TURTLE_MASTER);
        potion.setItemMeta(meta);
        return potion;
    }

    public static ItemStack buildPickaxe() {
        ItemStack pick = new ItemStack(Material.DIAMOND_PICKAXE);
        pick.addEnchantment(Enchantment.UNBREAKING, 3);
        pick.addEnchantment(Enchantment.EFFICIENCY, 5);
        return pick;
    }

    public static ItemStack buildFishingRod() {
        ItemStack rod = new ItemStack(Material.FISHING_ROD);
        ItemMeta meta = rod.getItemMeta();
        meta.setDisplayName("fishy fishy");
        rod.setItemMeta(meta);
        rod.addEnchantment(Enchantment.UNBREAKING, 1);
        return rod;
    }

    public static ItemStack buildShield() {
        ItemStack shield = new ItemStack(Material.SHIELD);
        BlockStateMeta meta = (BlockStateMeta) shield.getItemMeta();
        Banner banner = (Banner) meta.getBlockState();
        banner.setBaseColor(DyeColor.RED);
        banner.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
        banner.addPattern(new Pattern(DyeColor.RED, PatternType.STRIPE_MIDDLE));
        banner.addPattern(new Pattern(DyeColor.ORANGE, PatternType.FLOWER));
        banner.addPattern(new Pattern(DyeColor.RED, PatternType.RHOMBUS));
        banner.addPattern(new Pattern(DyeColor.ORANGE, PatternType.CIRCLE));
        banner.update();
        meta.setBlockState(banner);
        shield.setItemMeta(meta);
        shield.addEnchantment(Enchantment.UNBREAKING, 1);
        return shield;
    }
}
