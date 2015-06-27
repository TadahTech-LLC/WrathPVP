/*
 * Copyright (c) ReasonDev 2014.
 * All rights reserved.
 * No part of this project or any of its contents may be reproduced, copied, modified or adapted, without the prior written consent of SirReason.
 */

package co.reasondev.koth;

import co.reasondev.koth.hill.Hill;
import co.reasondev.koth.hill.HillManager;
import co.reasondev.koth.util.Messaging;
import co.reasondev.koth.util.Settings;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class KotHCommand implements CommandExecutor {

    private WrathKotH plugin;
    private HillManager hillManager;

    public KotHCommand(WrathKotH plugin) {
        this.plugin = plugin;
        this.hillManager = plugin.getHillManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //Invalid Argument / Help Command Message
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            Messaging.send(sender, "&eUse &6/koth &e+ one of the following: " +
                    "\n&6coords - &eview all Hills and their coordinates in the KotH World" +
                    "\n&6join - &ejoin the KotH World and join the fight" +
                    "\n&6loot - &eview all possible rewards for claiming a Hill" +
                    "\n&6status - &eview all Hills and their start times" +
                    (!sender.isOp() && !sender.hasPermission("koth.admin") ? "" :
                            "\n&ccreate <ID> - &ecreate a new Hill with the given ID" +
                                    "\n&cremove <ID> - &edelete an existing Hill" +
                                    "\n&csetchest <ID> - &eset the chest spawn of a Hill to your location" +
                                    "\n&csetregion <ID> - &eupdate the WorldEdit region of a Hill" +
                                    "\n&csetspawn - &eset the spawn point of the KotH World to your location" +
                                    "\n&creload - &ereload the config.yml file"));
            //Configuration Reload Command
        } else if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            Settings.registerConfig(plugin.getConfig());
            Messaging.send(sender, "&aSuccessfully reloaded configuration!");

            //KotH World Spawn Setting Command
        } else if (args[0].equalsIgnoreCase("setspawn")) {
            if (!(sender instanceof Player)) {
                Messaging.send(sender, "This command cannot be run from the Console!");
            } else if (!sender.isOp() && !sender.hasPermission("koth.admin")) {
                Messaging.send(sender, "&cYou do not have permission to use this command!");
            } else if (((Player) sender).getWorld() != hillManager.KOTH_WORLD) {
                Messaging.send(sender, "&cError! You must be in the KotH World  to use this command!");
            } else {
                //Location l = ((Player) sender).getLocation();
                //hillManager.KOTH_WORLD.setSpawnLocation(l.getBlockX(), l.getBlockY(), l.getBlockZ());
                Messaging.send(sender, "&cError! This value must now be set in the config manually!");
            }

            //Hill Creation Command
        } else if (args[0].equalsIgnoreCase("create")) {
            if (!(sender instanceof Player)) {
                Messaging.send(sender, "This command cannot be run from the Console!");
            } else if (!sender.isOp() && !sender.hasPermission("koth.admin")) {
                Messaging.send(sender, "&cYou do not have permission to use this command!");
            } else if (args.length != 2) {
                Messaging.send(sender, "&cInvalid arguments! Usage: &6/koth create <ID>");
            } else if (hillManager.hasHill(args[1])) {
                Messaging.send(sender, "&cError! There is already a Hill with that ID!");
            } else {
                Player player = (Player) sender;
                Selection sel = plugin.getWorldEdit().getSelection(player);
                if (sel == null) {
                    Messaging.send(sender, "&cError! You must make a WorldEdit selection first!");
                } else if (sel.getWorld() != hillManager.KOTH_WORLD) {
                    Messaging.send(sender, "&cError! Your WorldEdit selection must be in the KotH World!");
                } else if (!sel.contains(player.getLocation())) {
                    Messaging.send(sender, "&cError! You must be inside of your WorldEdit selection to set the chest location");
                } else {
                    hillManager.addHill(args[1], player.getLocation(), sel);
                    Messaging.send(sender, "&aSuccessfully created a new Hill with an ID of &b" + args[1]);
                }
            }

            //Hill Removal Command
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (!(sender instanceof Player)) {
                Messaging.send(sender, "This command cannot be run from the Console!");
            } else if (!sender.isOp() && !sender.hasPermission("koth.admin")) {
                Messaging.send(sender, "&cYou do not have permission to use this command!");
            } else if (args.length != 2) {
                Messaging.send(sender, "&cInvalid arguments! Usage: &6/koth remove <ID>");
            } else if (!hillManager.hasHill(args[1])) {
                Messaging.send(sender, "&cError! There is no Hill with that ID!");
            } else {
                hillManager.removeHill(args[1]);
                Messaging.send(sender, "&aSuccessfully removed the Hill with an ID of &b" + args[1]);
            }

            //Hill Region Setting Command
        } else if (args[0].equalsIgnoreCase("setregion")) {
            if (!(sender instanceof Player)) {
                Messaging.send(sender, "This command cannot be run from the Console!");
            } else if (!sender.isOp() && !sender.hasPermission("koth.admin")) {
                Messaging.send(sender, "&cYou do not have permission to use this command!");
            } else if (args.length != 2) {
                Messaging.send(sender, "&cInvalid arguments! Usage: &6/koth setregion <ID>");
            } else if (!hillManager.hasHill(args[1])) {
                Messaging.send(sender, "&cError! There is no Hill with that ID!");
            } else {
                Player player = (Player) sender;
                Selection sel = plugin.getWorldEdit().getSelection(player);
                if (sel == null) {
                    Messaging.send(sender, "&cError! You must make a WorldEdit selection first!");
                } else if (sel.getWorld() != hillManager.KOTH_WORLD) {
                    Messaging.send(sender, "&cError! Your WorldEdit selection must be in the KotH World!");
                } else {
                    hillManager.getHill(args[1]).updateRegion(sel);
                    Messaging.send(sender, "&aSuccessfully updated region for the Hill with an ID of &b" + args[1]);
                }
            }

            //Hill Chest Location Setting Command
        } else if (args[0].equalsIgnoreCase("setchest")) {
            if (!(sender instanceof Player)) {
                Messaging.send(sender, "This command cannot be run from the Console!");
            } else if (!sender.isOp() && !sender.hasPermission("koth.admin")) {
                Messaging.send(sender, "&cYou do not have permission to use this command!");
            } else if (args.length != 2) {
                Messaging.send(sender, "&cInvalid arguments! Usage: &6/koth setchest <ID>");
            } else if (!hillManager.hasHill(args[1])) {
                Messaging.send(sender, "&cError! There is no Hill with that ID!");
            } else {
                Player player = (Player) sender;
                Hill hill = hillManager.getHill(args[1]);
                if (!hill.isInRegion(player)) {
                    Messaging.send(sender, "&cError! Chest Location must be inside the Hill region!");
                } else {
                    hill.setChestLocation(player.getLocation().toVector());
                    Messaging.send(sender, "&aSuccessfully set Chest Location to your coordinates");
                }
            }

            //Hill Time Listing Command
        } else if (args[0].equalsIgnoreCase("status")) {
            StringBuilder sb = new StringBuilder("&6Fetching times for all Hills...");
            sb.append("\n&8==============================");
            sb.append("\n&cRED = INACTIVE &7 &aGREEN = ACTIVE");
            for (String hillID : hillManager.getHills()) {
                Hill hill = hillManager.getHill(hillID);
                String status = hillManager.isActive(hill) ? "&a" : "&c";
                sb.append("\n&7" + hill.getStartTime() + " EST - " + status + ChatColor.stripColor(hill.getDisplayName()));
            }
            sb.append(" \n&6It is currently &e" + hillManager.getCurrentTime() + " EST");
            sb.append("\n&8==============================");
            Messaging.send(sender, sb.toString());

            //Hill Coordinate Listing Command
        } else if (args[0].equalsIgnoreCase("coords")) {
            List<String> usedCoords = new ArrayList<>();
            StringBuilder sb = new StringBuilder("&6Fetching coordinates for all Hills...");
            sb.append("\n&8==============================");
            sb.append("\n&cRED = INACTIVE &7 &aGREEN = ACTIVE");
            for (String hillID : hillManager.getHills()) {
                Hill hill = hillManager.getHill(hillID);
                //Prevent duplicate coordinates for repeat Hills
                if (!usedCoords.contains(hill.getChestLocString())) {
                    String status = hillManager.isActive(hill) ? "&a" : "&c";
                    sb.append("\n&7" + hill.getChestLocString() + " - " + status + ChatColor.stripColor(hill.getDisplayName()));
                    usedCoords.add(hill.getChestLocString());
                }
            }
            sb.append("\n&8==============================");
            Messaging.send(sender, sb.toString());

            //KotH World Joining Command
        } else if (args[0].equalsIgnoreCase("join")) {
            if (!(sender instanceof Player)) {
                Messaging.send(sender, "This command cannot be run from the Console!");
            } else if (hillManager.isDeathBanned((Player) sender)) {
                Messaging.send(sender, Settings.Messages.DEATH_BANNED);
            } else {
                ((Player) sender).teleport(hillManager.KOTH_SPAWN);
            }

            //KotH Loot Viewing Command
        } else if (args[0].equalsIgnoreCase("loot")) {
            if (!(sender instanceof Player)) {
                Messaging.send(sender, "This command cannot be run from the Console!");
            } else {
                plugin.getRewardManager().openRewardMenu((Player) sender);
            }

        } else {
            Messaging.send(sender, "&cError! Invalid command arguments! Type &6/koth help &cfor help");
        }
        return true;
    }
}
