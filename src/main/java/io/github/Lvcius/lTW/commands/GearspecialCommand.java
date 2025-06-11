package io.github.Lvcius.lTW.commands;

import io.github.Lvcius.lTW.LTW;
import org.apache.maven.model.Plugin;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
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

public class GearspecialCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            //check for player
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
        }
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /gearspecial <player/team> <target>");
        }
        else {
            Server server = Bukkit.getServer();
            //assign arguments
            String targetchoice = args[0];
            String target = args[1];

            List<Player> playerList = new ArrayList<>();

            //select target player
            if (targetchoice.equals("player") || targetchoice.equals("Player")) {
                //initial
                final Player player = Bukkit.getPlayer(target);
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
                if (team == null) {
                    sender.sendMessage(ChatColor.RED + "That team does not exist!");
                }
                else if (team.getEntries() == null) {
                    sender.sendMessage(ChatColor.RED + "That team has no players!");
                }
                else {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (team.hasEntry(player.getName())) {
                            playerList.add(player);
                        }
                    }
                    //success message
                    sender.sendMessage(ChatColor.GREEN + "Gearing all members of team " + team.getName());
                }
            }

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
                ItemStack scythe = new ItemStack(Material.NETHERITE_AXE);
                ItemMeta scytheMeta = scythe.getItemMeta();

                //scythe custom dmg and atk
                AttributeModifier atkSpeedModifier = new AttributeModifier(Attribute.GENERIC_ATTACK_SPEED.getKey(), -2.4, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HAND);
                AttributeModifier atkDamageModifier = new AttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE.getKey(), 6.2, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HAND);
                scytheMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, atkSpeedModifier);
                scytheMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, atkDamageModifier);
                scythe.setItemMeta(scytheMeta);

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

                //give swords and food
                inventory.setItemInOffHand(new ItemStack(Material.COOKED_BEEF, 64));
                inventory.setItem(0, scythe);
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
                inventory.setItem(25, speedstrength);
                inventory.setItem(34, speedstrength);
                inventory.setItem(8, regen);
                inventory.setItem(26, regen);
                inventory.setItem(35, regen);
                inventory.setItem(6, fireres);
                inventory.setItem(33, fireres);

                //setup bow
                ItemStack bow = new ItemStack(Material.BOW);
                Map<Enchantment, Integer> bowenchantments = new HashMap<>();
                bowenchantments.put(Enchantment.UNBREAKING, 3);
                bowenchantments.put(Enchantment.POWER, 5);
                bowenchantments.put(Enchantment.FLAME, 1);
                bowenchantments.put(Enchantment.INFINITY, 1);
                bow.addEnchantments(bowenchantments);

                //give misc gear
                inventory.setItem(10, bow);
                inventory.setItem(9, new ItemStack(Material.ARROW));
                inventory.setItem(15, new ItemStack(Material.DIAMOND_PICKAXE));
                inventory.setItem(16, new ItemStack(Material.REDSTONE_BLOCK, 64));
                inventory.setItem(17, new ItemStack(Material.COBWEB, 16));
                inventory.setItem(18, new ItemStack(Material.ENDER_PEARL, 6));

                //fill with splash health
                ItemStack health = new ItemStack(Material.SPLASH_POTION);
                PotionMeta healthMeta = (PotionMeta) health.getItemMeta();
                healthMeta.setBasePotionType(PotionType.STRONG_HEALING);
                health.setItemMeta(healthMeta);
                for (int i = 0; i <= 20; i++) {
                    inventory.addItem(health);
                }

                //set gamemode
                player.setGameMode(GameMode.SURVIVAL);

                //annoy players
                player.playSound(player, ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                player.sendMessage(ChatColor.GREEN + "You have been geared! :)");
            }
            sender.sendMessage(ChatColor.GREEN + "Gearing complete!");

        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull Command command,
                                                @NotNull String label,
                                                @NotNull String[] args) {
        Server server = Bukkit.getServer();
        final List<String> completions = new ArrayList<>();
        final List<String> completions2 = new ArrayList<>();
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
        return List.of();
    }
}
