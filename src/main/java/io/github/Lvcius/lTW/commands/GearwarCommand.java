package io.github.Lvcius.lTW.commands;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP;



public class GearwarCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            //check for player
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
            return true;
        }
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /gearwar <player/team> <target> <Combatant/Ganker>");
            return true;
        }

        String targetchoice = args[0];
        String target = args[1];
        String kitType = args[2];

        List<Player> playerList = new ArrayList<>();

        //select target player
        if (targetchoice.equals("player") || targetchoice.equals("Player")) {
            final Player player = Bukkit.getPlayer(target);
            //check if target is online
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "That player does not exist!");
            }
            else {
                playerList.add(player);
                sender.sendMessage(ChatColor.GREEN + "Gearing " + player.getName() + "!");
            }
        }

        //select target team
        else if (targetchoice.equals("team") || targetchoice.equals("Team")) {
            final Player playersender = Bukkit.getPlayer(sender.getName());
            Scoreboard scoreboard = playersender.getScoreboard();
            Team team = scoreboard.getTeam(args[1]);
            //check if team exists
            if (team == null) {
                sender.sendMessage(ChatColor.RED + "That team does not exist!");
            }
            //check if team has members
            else if (team.getEntries() == null) {
                sender.sendMessage(ChatColor.RED + "That team has no players!");
            }
            else {
                //add every player in team to playerList
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (team.hasEntry(player.getName())) {
                        playerList.add(player);
                    }
                }
                //success message
                sender.sendMessage(ChatColor.GREEN + "Gearing all members of team " + team.getName());
            }
        }

        if (kitType.equals("Combatant") || kitType.equals("combatant")) {
            for (Player player : playerList) {
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

                //give gear
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

                ItemStack turtle = new ItemStack(Material.SPLASH_POTION);
                PotionMeta turtleMeta = (PotionMeta) turtle.getItemMeta();
                turtleMeta.setBasePotionType(PotionType.TURTLE_MASTER);
                turtle.setItemMeta(turtleMeta);

                //setup shield
                ItemStack shield = new ItemStack(Material.SHIELD);
                ItemMeta shieldMeta = shield.getItemMeta();
                BlockStateMeta shieldMetaState = (BlockStateMeta) shieldMeta;

                Banner legionBanner = (Banner) shieldMetaState.getBlockState();
                legionBanner.setBaseColor(DyeColor.RED);
                legionBanner.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
                legionBanner.addPattern(new Pattern(DyeColor.RED, PatternType.STRIPE_MIDDLE));
                legionBanner.addPattern(new Pattern(DyeColor.ORANGE, PatternType.FLOWER));
                legionBanner.addPattern(new Pattern(DyeColor.RED, PatternType.RHOMBUS));
                legionBanner.addPattern(new Pattern(DyeColor.ORANGE, PatternType.CIRCLE));

                legionBanner.update();

                shieldMetaState.setBlockState(legionBanner);

                shield.setItemMeta(shieldMeta);
                shield.setItemMeta(shieldMetaState);

                shield.addEnchantment(Enchantment.UNBREAKING, 1);

                //setup rod
                ItemStack rod = new ItemStack(Material.FISHING_ROD);
                ItemMeta rodMeta = rod.getItemMeta();
                rodMeta.setDisplayName("fishy fishy");
                rod.setItemMeta(rodMeta);

                rod.addEnchantment(Enchantment.UNBREAKING, 1);

                //give swords and food
                inventory.setItem(7, new ItemStack(Material.COOKED_BEEF, 32));
                inventory.setItem(0, regsword);
                inventory.setItemInOffHand(kbsword);

                //give buffs
                inventory.setItem(4, speedstrength);
                inventory.setItem(25, speedstrength);
                inventory.setItem(34, speedstrength);
                inventory.setItem(5, regen);
                inventory.setItem(26, regen);
                inventory.setItem(35, regen);
                inventory.setItem(16, fireres);
                inventory.setItem(17, fireres);
                inventory.setItem(9, turtle);

                //give misc gear
                inventory.setItem(8, new ItemStack(Material.ENDER_PEARL, 3));
                inventory.setItem(6, new ItemStack(Material.COBWEB, 8));
                inventory.setItem(1, new ItemStack(Material.DIAMOND_PICKAXE));
                inventory.setItem(2, new ItemStack(Material.REDSTONE_BLOCK, 64));
                inventory.setItem(3, new ItemStack(Material.BLACKSTONE_SLAB, 64));
                inventory.setItem(27, shield);
                inventory.setItem(28, rod);

                inventory.setItem(10, new ItemStack(Material.GOLD_INGOT, 64));

                //fill with splash health
                ItemStack health = new ItemStack(Material.SPLASH_POTION);
                PotionMeta healthMeta = (PotionMeta) health.getItemMeta();
                healthMeta.setBasePotionType(PotionType.STRONG_HEALING);
                health.setItemMeta(healthMeta);
                for (int i = 0; i <= 16; i++) {
                    inventory.addItem(health);
                }

                inventory.clear(10);

                //set gamemode
                player.setGameMode(GameMode.SURVIVAL);

                //annoy players
                player.playSound(player, ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                player.sendMessage(ChatColor.GREEN + "You have been geared! :)");
            }
        }
        else if (kitType.equals("Ganker") || kitType.equals("ganker")) {
            for (Player player : playerList) {
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

                //give gear
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
                ItemStack scythe = new ItemStack(Material.DIAMOND_AXE);
                ItemMeta scytheMeta = scythe.getItemMeta();

                //scythe custom dmg and atk
                AttributeModifier atkSpeedModifier = new AttributeModifier(Attribute.GENERIC_ATTACK_SPEED.getKey(), -2.4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HAND);
                AttributeModifier atkDamageModifier = new AttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE.getKey(), 6.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HAND);
                scytheMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, atkSpeedModifier);
                scytheMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, atkDamageModifier);
                scythe.setItemMeta(scytheMeta);
                scytheMeta.setDisplayName("Scythe");

                ItemStack kbsword = new ItemStack(Material.DIAMOND_SWORD);
                ItemMeta kbMeta = kbsword.getItemMeta();
                kbMeta.setDisplayName("Bonk Stick");
                kbsword.setItemMeta(kbMeta);

                Map<Enchantment, Integer> swordenchantments = new HashMap<>();
                swordenchantments.put(Enchantment.UNBREAKING, 3);
                swordenchantments.put(Enchantment.SHARPNESS, 5);
                scythe.addEnchantments(swordenchantments);
                kbsword.addEnchantments(swordenchantments);
                kbsword.addEnchantment(Enchantment.KNOCKBACK, 2);
                kbsword.addEnchantment(Enchantment.FIRE_ASPECT, 2);

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

                ItemStack turtle = new ItemStack(Material.SPLASH_POTION);
                PotionMeta turtleMeta = (PotionMeta) turtle.getItemMeta();
                turtleMeta.setBasePotionType(PotionType.TURTLE_MASTER);
                turtle.setItemMeta(turtleMeta);

                //setup shield
                ItemStack shield = new ItemStack(Material.SHIELD);
                ItemMeta shieldMeta = shield.getItemMeta();
                BlockStateMeta shieldMetaState = (BlockStateMeta) shieldMeta;

                Banner legionBanner = (Banner) shieldMetaState.getBlockState();
                legionBanner.setBaseColor(DyeColor.RED);
                legionBanner.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
                legionBanner.addPattern(new Pattern(DyeColor.RED, PatternType.STRIPE_MIDDLE));
                legionBanner.addPattern(new Pattern(DyeColor.ORANGE, PatternType.FLOWER));
                legionBanner.addPattern(new Pattern(DyeColor.RED, PatternType.RHOMBUS));
                legionBanner.addPattern(new Pattern(DyeColor.ORANGE, PatternType.CIRCLE));

                legionBanner.update();

                shieldMetaState.setBlockState(legionBanner);

                shield.setItemMeta(shieldMeta);
                shield.setItemMeta(shieldMetaState);

                shield.addEnchantment(Enchantment.UNBREAKING, 1);

                //setup rod
                ItemStack rod = new ItemStack(Material.FISHING_ROD);
                ItemMeta rodMeta = rod.getItemMeta();
                rodMeta.setDisplayName("fishy fishy");
                rod.setItemMeta(rodMeta);

                rod.addEnchantment(Enchantment.UNBREAKING, 1);

                //give swords and food
                inventory.setItem(7, new ItemStack(Material.COOKED_BEEF, 64));
                inventory.setItem(0, scythe);
                inventory.setItemInOffHand(kbsword);

                //give buffs
                inventory.setItem(4, speedstrength);
                inventory.setItem(25, speedstrength);
                inventory.setItem(34, speedstrength);
                inventory.setItem(5, regen);
                inventory.setItem(26, regen);
                inventory.setItem(35, regen);
                inventory.setItem(16, fireres);
                inventory.setItem(17, fireres);
                inventory.setItem(9, turtle);
                inventory.setItem(10, turtle);

                //give misc gear
                inventory.setItem(8, new ItemStack(Material.ENDER_PEARL, 6));
                inventory.setItem(6, new ItemStack(Material.COBWEB, 16));
                inventory.setItem(1, new ItemStack(Material.DIAMOND_PICKAXE));
                inventory.setItem(2, new ItemStack(Material.REDSTONE_BLOCK, 64));
                inventory.setItem(3, new ItemStack(Material.BLACKSTONE_SLAB, 64));
                inventory.setItem(27, shield);
                inventory.setItem(28, rod);

                inventory.setItem(11, new ItemStack(Material.GOLD_INGOT, 64));

                //fill with splash health
                ItemStack health = new ItemStack(Material.SPLASH_POTION);
                PotionMeta healthMeta = (PotionMeta) health.getItemMeta();
                healthMeta.setBasePotionType(PotionType.STRONG_HEALING);
                health.setItemMeta(healthMeta);
                for (int i = 0; i <= 16; i++) {
                    inventory.addItem(health);
                }

                inventory.clear(11);

                //set gamemode
                player.setGameMode(GameMode.SURVIVAL);

                //annoy players
                player.playSound(player, ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                player.sendMessage(ChatColor.GREEN + "You have been geared! :)");
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final List<String> completions = new ArrayList<>();
        final List<String> completions2 = new ArrayList<>();
        final List<String> completions3 = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], List.of("player", "team"), completions);
            return completions;
        }
        if (args[0].equals("player")) {
            if (args.length == 2) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    completions2.add(p.getName());
                }
                StringUtil.copyPartialMatches(args[1], List.of(), completions2);
                return completions2;
            }
        }
        else if (args[0].equals("team")) {
            if (args.length == 2) {
                StringUtil.copyPartialMatches(args[1], List.of("Red", "Green", "Blue", "Yellow", "Purple"), completions2);
                return completions2;
            }
        }
        if (args.length == 3) {
            StringUtil.copyPartialMatches(args[2], List.of("Combatant", "Ganker"), completions3);
            return completions3;
        }
        return List.of();
    }
}
