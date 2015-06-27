package com.tadahtech.pub.shield;

import com.tadahtech.pub.drop.Drop;
import com.tadahtech.pub.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Timothy Andis
 */
public class ShieldEffect {

    private Drop drop;
    private ShieldStructure type;
    public static String MESSAGE;

    public ShieldEffect(Drop drop, ShieldStructure type) {
        this.drop = drop;
        this.type = type;
    }

    public void run(Location loc) {
        type.run(loc, drop);
        MESSAGE = ChatColor.translateAlternateColorCodes('&', MESSAGE);
        for(Player player : Utils.getOnlinePlayers()) {
            player.sendMessage(MESSAGE);
        }
    }

    public Drop getDrop() {
        return drop;
    }

    public ShieldStructure getType() {
        return type;
    }
}
