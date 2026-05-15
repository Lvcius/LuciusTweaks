package io.github.Lvcius.lTW.commands;

import io.github.Lvcius.lTW.util.KitBuilder;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP;

public class GearspecialCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player senderPlayer)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /gearspecial <player/team> <target>");
            return true;
        }

        String targetchoice = args[0];
        String target = args[1];
        List<Player> playerList = new ArrayList<>();

        if (targetchoice.equalsIgnoreCase("player")) {
            Player player = Bukkit.getPlayer(target);
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            playerList.add(player);
            sender.sendMessage(ChatColor.GREEN + "Gearing " + player.getName() + "!");
        } else if (targetchoice.equalsIgnoreCase("team")) {
            Scoreboard scoreboard = senderPlayer.getScoreboard();
            Team team = scoreboard.getTeam(target);
            if (team == null) {
                sender.sendMessage(ChatColor.RED + "That team does not exist!");
                return true;
            }
            if (team.getEntries().isEmpty()) {
                sender.sendMessage(ChatColor.RED + "That team has no players!");
                return true;
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (team.hasEntry(player.getName())) playerList.add(player);
            }
            sender.sendMessage(ChatColor.GREEN + "Gearing all members of team " + team.getName());
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: /gearspecial <player/team> <target>");
            return true;
        }

        ItemStack speedstrength = KitBuilder.buildSpeedStrengthPotion();
        ItemStack regen = KitBuilder.buildRegenPotion();
        ItemStack fireres = KitBuilder.buildFireResPotion();
        ItemStack health = KitBuilder.buildHealthSplash();

        for (Player player : playerList) {
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

            ItemStack bow = new ItemStack(Material.BOW);
            bow.addEnchantment(org.bukkit.enchantments.Enchantment.UNBREAKING, 3);
            bow.addEnchantment(org.bukkit.enchantments.Enchantment.POWER, 5);
            bow.addEnchantment(org.bukkit.enchantments.Enchantment.FLAME, 1);
            bow.addEnchantment(org.bukkit.enchantments.Enchantment.INFINITY, 1);

            inventory.setItem(0, KitBuilder.buildScythe(Material.NETHERITE_AXE, 6.2));
            inventory.setItem(1, KitBuilder.buildBonkStick());
            inventory.setItemInOffHand(new ItemStack(Material.COOKED_BEEF, 64));
            inventory.setItem(6, fireres);
            inventory.setItem(7, speedstrength);
            inventory.setItem(8, regen);
            inventory.setItem(9, new ItemStack(Material.ARROW));
            inventory.setItem(10, bow);
            inventory.setItem(15, KitBuilder.buildPickaxe());
            inventory.setItem(16, new ItemStack(Material.REDSTONE_BLOCK, 64));
            inventory.setItem(17, new ItemStack(Material.COBWEB, 16));
            inventory.setItem(18, new ItemStack(Material.ENDER_PEARL, 6));
            inventory.setItem(25, speedstrength);
            inventory.setItem(26, regen);
            inventory.setItem(33, fireres);
            inventory.setItem(34, speedstrength);
            inventory.setItem(35, regen);

            // fill remaining slots with splash health (slots 2-5, 11-14, 19-24, 27-32)
            for (int i = 2; i <= 5; i++) inventory.setItem(i, health);
            for (int i = 11; i <= 14; i++) inventory.setItem(i, health);
            for (int i = 19; i <= 24; i++) inventory.setItem(i, health);
            for (int i = 27; i <= 32; i++) inventory.setItem(i, health);

            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(ChatColor.GREEN + "You have been geared! :)");
            player.playSound(player, ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
        }

        sender.sendMessage(ChatColor.GREEN + "Gearing complete!");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], List.of("player", "team"), completions);
            return completions;
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("player")) {
                List<String> players = new ArrayList<>();
                for (Player p : Bukkit.getOnlinePlayers()) players.add(p.getName());
                StringUtil.copyPartialMatches(args[1], players, completions);
            } else if (args[0].equalsIgnoreCase("team")) {
                StringUtil.copyPartialMatches(args[1], List.of("Red", "Green", "Blue", "Yellow", "Purple"), completions);
            }
            return completions;
        }
        return List.of();
    }
}
