package me.calebbfmv.nations.donorperks.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tim [calebbfmv]
 */
public class Utils {

    public static String friendlyName(String name) {
        StringBuilder builder = new StringBuilder();
        if (!name.contains("_")) {
            builder.append(name.substring(0, 1).toUpperCase()).append(name.substring(1).toLowerCase());
        } else {
            String[] str = name.split("_");
            for (int i = 0; i < str.length; i++) {
                String s = str[i];
                builder.append(s.substring(0, 1).toUpperCase()).append(s.substring(1).toLowerCase());
                if ((i + 1) != str.length) {
                    builder.append(" ");
                }
            }
        }
        return builder.toString();

    }

    public static String locToString(Location location) {
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
        int x = parse(str[1]);
        int y = parse(str[2]);
        int z = parse(str[3]);
        float yaw = Float.parseFloat(str[4]);
        float pitch = Float.parseFloat(str[5]);
        return new Location(world, x, y, z, yaw, pitch);
    }

    public static int parse(String s) {
        return Integer.parseInt(s);
    }

    public static List<Location> circle(Player player, Location loc, Integer r, Integer h, Boolean hollow, Boolean sphere, int plus_y) {
        List<Location> circleblocks = new ArrayList<>();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        for (int x = cx - r; x <= cx + r; x++)
            for (int z = cz - r; z <= cz + r; z++)
                for (int y = (sphere ? cy - r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        Location l = new Location(loc.getWorld(), x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }

        return circleblocks;
    }

}
