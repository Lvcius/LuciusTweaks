package io.github.Lvcius.lTW.commands;

import io.github.Lvcius.lTW.LTW;
import io.github.Lvcius.lTW.util.KitBuilder;
import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.land.LandWorld;
import me.angeschossen.lands.api.player.LandPlayer;
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

import static me.angeschossen.lands.api.items.ItemType.CAPTURE_FLAG;
import static org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP;

/**
 * /gearwar <attacker|defender|capper> [player|team] [target]
 *
 * Gives role-specific war kits. Without a target argument the sender gears
 * themselves; targeting others requires the "gearspecial" permission.
 *
 * Universal inventory layout (slots 0-35, offhand):
 *   0  scythe          1  ender pearl x16  2  pickaxe
 *   3  redstone x64    4  TNT x10          5  beef x32
 *   6  cobweb x16      7  str/speed        8  regen
 *   9-10 [kit-specific]  11-15  health x5
 *   16-17 fire res     18-19 [kit-specific]
 *   20-24 health x5    25-26 regen
 *   27-28 [kit-specific]  29-33 health x5
 *   34-35 str/speed
 *   offhand: bonk stick
 *
 * Kit-specific slots:
 *   defender:  9-11 turtle master, 18-19 health, 27 shield, 28 sand x64,  +TNT x11 (overflow)
 *   attacker:  9 turtle master,    10 blackstone slab x64, 18 rod, 19 health, 27 shield, 28 anvil x10
 *   capper:    9 end stone x64,    10 blackstone slab x64, 18 rod, 19 turtle, 27 cap block/shield, 28 anvil x10
 */
public class GearwarCommand implements TabExecutor {

    private final LandsIntegration api = LandsIntegration.of(LTW.getInstance());

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player senderPlayer)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
            return true;
        }

        String kitType;
        String targetchoice;
        String target;

        // single-arg form: gear yourself
        if (args.length == 1) {
            kitType = args[0];
            targetchoice = "player";
            target = sender.getName();
        } else if (!sender.hasPermission("gearspecial")) {
            sender.sendMessage(ChatColor.RED + "YOU DO NOT HAVE PERMISSION TO GEAR OTHER PLAYERS");
            return true;
        } else if (args.length != 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /gearwar <attacker/defender/capper> <player/team> <target>");
            return true;
        } else {
            kitType = args[0];
            targetchoice = args[1];
            target = args[2];
        }

        if (!kitType.equalsIgnoreCase("attacker") && !kitType.equalsIgnoreCase("defender") && !kitType.equalsIgnoreCase("capper")) {
            sender.sendMessage(ChatColor.RED + "Please type a valid kit: attacker, defender, or capper");
            return true;
        }

        // resolve target to a list of players
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
            sender.sendMessage(ChatColor.RED + "Usage: /gearwar <attacker/defender/capper> <player/team> <target>");
            return true;
        }

        // build shared items once outside the loop — Bukkit clones on setItem
        ItemStack health = KitBuilder.buildHealthSplash();
        ItemStack speedstrength = KitBuilder.buildSpeedStrengthPotion();
        ItemStack regen = KitBuilder.buildRegenPotion();
        ItemStack fireres = KitBuilder.buildFireResPotion();
        ItemStack scythe = KitBuilder.buildScythe(Material.DIAMOND_AXE, 6.0);
        ItemStack[] armor = KitBuilder.buildDiamondArmor();

        // --- universal kit (all roles) ---
        for (Player player : playerList) {
            PlayerInventory inventory = player.getInventory();

            inventory.clear();
            player.setHealth(player.getMaxHealth());
            if (player.getFoodLevel() < 20) player.setFoodLevel(20);
            player.clearActivePotionEffects();
            player.setFireTicks(0);
            player.setGameMode(GameMode.SURVIVAL);

            // armor
            inventory.setHelmet(armor[0]);
            inventory.setChestplate(armor[1]);
            inventory.setLeggings(armor[2]);
            inventory.setBoots(armor[3]);

            // hotbar weapons and utility
            inventory.setItem(0, scythe);
            inventory.setItem(1, new ItemStack(Material.ENDER_PEARL, 16));
            inventory.setItem(2, KitBuilder.buildPickaxe());
            inventory.setItem(3, new ItemStack(Material.REDSTONE_BLOCK, 64));
            inventory.setItem(4, new ItemStack(Material.TNT, 10));
            inventory.setItem(5, new ItemStack(Material.COOKED_BEEF, 32));
            inventory.setItem(6, new ItemStack(Material.COBWEB, 16));
            inventory.setItem(7, speedstrength);
            inventory.setItem(8, regen);
            inventory.setItemInOffHand(KitBuilder.buildBonkStick());

            // potions at fixed slots (slots 9/10 and 18/19 reserved for kit-specific items)
            inventory.setItem(16, fireres);
            inventory.setItem(17, fireres);
            inventory.setItem(25, regen);
            inventory.setItem(26, regen);
            inventory.setItem(34, speedstrength);
            inventory.setItem(35, speedstrength);

            // health splash fills rows 2-3 between the reserved pot slots (15 total)
            for (int i = 11; i <= 15; i++) inventory.setItem(i, health);
            for (int i = 20; i <= 24; i++) inventory.setItem(i, health);
            for (int i = 29; i <= 33; i++) inventory.setItem(i, health);

            player.sendMessage(ChatColor.GREEN + "You have been geared! :)");
            player.playSound(player, ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
        }

        // --- kit-specific items ---

        if (kitType.equalsIgnoreCase("defender")) {
            // three turtle masters for holding a position; extra TNT added via addItem
            // to wherever it fits (the universal kit already used slot 4 for TNT x10)
            ItemStack turtle = KitBuilder.buildTurtleMaster();
            ItemStack shield = KitBuilder.buildShield();
            for (Player player : playerList) {
                PlayerInventory inventory = player.getInventory();
                inventory.setItem(9, turtle);
                inventory.setItem(10, turtle);
                inventory.setItem(11, turtle); // overwrites one health pot at slot 11
                inventory.setItem(18, health);
                inventory.setItem(19, health);
                inventory.setItem(27, shield);
                inventory.setItem(28, new ItemStack(Material.SAND, 64));
                inventory.addItem(new ItemStack(Material.TNT, 11));
            }
        } else if (kitType.equalsIgnoreCase("attacker")) {
            ItemStack turtle = KitBuilder.buildTurtleMaster();
            ItemStack shield = KitBuilder.buildShield();
            ItemStack rod = KitBuilder.buildFishingRod();
            for (Player player : playerList) {
                PlayerInventory inventory = player.getInventory();
                inventory.setItem(9, turtle);
                inventory.setItem(10, new ItemStack(Material.BLACKSTONE_SLAB, 64));
                inventory.setItem(18, rod);
                inventory.setItem(19, health);
                inventory.setItem(27, shield);
                inventory.setItem(28, new ItemStack(Material.ANVIL, 10));
            }
        } else if (kitType.equalsIgnoreCase("capper")) {
            // capper gets capture-flag blocks if Lands is active on this world,
            // otherwise falls back to a shield as a placeholder
            LandWorld landworld = api.getWorld(senderPlayer.getWorld());
            ItemStack rod = KitBuilder.buildFishingRod();
            ItemStack turtle = KitBuilder.buildTurtleMaster();
            ItemStack shield = KitBuilder.buildShield();
            for (Player player : playerList) {
                PlayerInventory inventory = player.getInventory();
                inventory.setItem(9, new ItemStack(Material.END_STONE, 64));
                inventory.setItem(10, new ItemStack(Material.BLACKSTONE_SLAB, 64));
                inventory.setItem(18, rod);
                inventory.setItem(19, turtle);
                inventory.setItem(28, new ItemStack(Material.ANVIL, 10));
                if (landworld != null) {
                    LandPlayer landPlayer = api.getLandPlayer(player.getUniqueId());
                    if (landPlayer != null) {
                        ItemStack capblock = CAPTURE_FLAG.build(landPlayer);
                        capblock.setAmount(16);
                        inventory.setItem(27, capblock);
                    } else {
                        inventory.setItem(27, shield);
                    }
                } else {
                    inventory.setItem(27, shield);
                }
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], List.of("defender", "attacker", "capper"), completions);
            return completions;
        }
        if (args.length == 2) {
            StringUtil.copyPartialMatches(args[1], List.of("player", "team"), completions);
            return completions;
        }
        if (args.length == 3) {
            if (args[1].equalsIgnoreCase("player")) {
                List<String> players = new ArrayList<>();
                for (Player p : Bukkit.getOnlinePlayers()) players.add(p.getName());
                StringUtil.copyPartialMatches(args[2], players, completions);
            } else if (args[1].equalsIgnoreCase("team")) {
                StringUtil.copyPartialMatches(args[2], List.of("Red", "Green", "Blue", "Yellow", "Purple"), completions);
            }
            return completions;
        }
        return List.of();
    }
}
