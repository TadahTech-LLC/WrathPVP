package com.tadahtech.wrathpvp.wall.utils;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @author Timothy Andis
 */
public class Utils {

    private Utils() {

    }

    public static Collection<Player> getOnlinePlayers() {
        try {
            Method method = Bukkit.class.getMethod("getOnlinePlayers");
            if(method.getReturnType() == Collection.class) {
                try {
                    return (Collection<Player>) method.invoke(null);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else if(method.getReturnType() == Player[].class) {
                return Lists.newArrayList((Player[]) method.invoke(null));
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return Lists.newArrayList();
    }

    public static String locToString(Location location) {
        if(location == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(location.getWorld().getName()).append(",").append(location.getBlockX()).append(",").append(location.getBlockY()).append(",").append(location.getBlockZ()).append(",").append(location.getYaw()).append(",").append(location.getPitch());
        return builder.toString();
    }

    public static Location locFromString(String s) {
        if (s == null) {
            return null;
        }
        String[] str = s.split(",");
        World world = Bukkit.getWorld(str[0]);
        int x = Integer.parseInt(str[1]);
        int y = Integer.parseInt(str[2]);
        int z = Integer.parseInt(str[3]);
        float yaw = Float.parseFloat(str[4]);
        float pitch = Float.parseFloat(str[5]);
        return new Location(world, x, y, z, yaw, pitch);
    }

    public static boolean isWeapon(ItemStack itemStack) {
        return itemStack.getType().name().toLowerCase().contains("sword") || itemStack.getType().name().toLowerCase().contains("bow");
    }

    public static Enchantment getFromString(String s){
        return Enchantment.getByName(s);
    }

    public static float percent(int amount, int total) {
        return (amount * 100.0f) / total;
    }
}
