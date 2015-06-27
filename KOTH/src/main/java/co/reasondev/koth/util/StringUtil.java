/*
 * Copyright (c) ReasonDev 2014.
 * All rights reserved.
 * No part of this project or any of its contents may be reproduced, copied, modified or adapted, without the prior written consent of SirReason.
 */

package co.reasondev.koth.util;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {

    public static String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static List<String> colorAll(List<String> input) {
        List<String> output = new ArrayList<>();
        for (String i : input) {
            output.add(color(i));
        }
        return output;
    }

    public static Location parseLocation(World world, String input) {
        String[] args = input.split(",");
        return new Location(world, Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]),
                Float.parseFloat(args[3]), Float.parseFloat(args[4]));
    }

    public static Vector parseVector(String input) {
        String[] args = input.split(",");
        return new Vector(Integer.valueOf(args[0]), Integer.valueOf(args[1]), Integer.valueOf(args[2]));
    }

    public static String getVectorString(Vector vector) {
        return vector.getBlockX() + "," + vector.getBlockY() + "," + vector.getBlockZ();
    }

    public static ItemStack parseItem(String input) {
        String[] args = input.split(" ");
        ItemStack output = new ItemStack(Material.valueOf(args[0].split(":")[0]), args.length < 2 ? 1 : Integer.parseInt(args[1]));
        if (args[0].contains(":")) output.setDurability(Short.parseShort(args[0].split(":")[1]));
        if (args.length < 3) return output;
        ItemMeta meta = output.getItemMeta();
        if (!args[2].equalsIgnoreCase("NONE")) meta.setDisplayName(color(args[2].replaceAll("_", " ")));
        if (args.length > 3 && !args[3].equalsIgnoreCase("NONE")) {
            String[] args2 = args[3].split(",");
            List<String> lore = new ArrayList<>();
            for (int i = 0; i < args2.length; i++) {
                lore.add(color(args2[i]).replaceAll("_", " "));
            }
            meta.setLore(lore);
        }
        if (args.length > 4) {
            String[] args2 = args[4].split(",");
            for (int i = 0; i < args2.length; i++) {
                String[] ench = args2[i].split("/");
                meta.addEnchant(Enchantment.getByName(ench[0]), ench.length < 2 ? 1 : Integer.parseInt(ench[1]), true);
            }
        }
        output.setItemMeta(meta);
        return output;
    }

    public static List<ItemStack> parseItemList(List<String> input) {
        List<ItemStack> output = new ArrayList<>();
        for (String i : input) {
            output.add(parseItem(i));
        }
        return output;
    }

    public static String parseTime(int input) {
        if (input <= 60) {
            return String.valueOf(input);
        }
        return String.valueOf(input / 60) + ":" + (String.valueOf(input % 60).length() == 1 ? "0" + String.valueOf(input % 60) : String.valueOf(input % 60));
    }
}
