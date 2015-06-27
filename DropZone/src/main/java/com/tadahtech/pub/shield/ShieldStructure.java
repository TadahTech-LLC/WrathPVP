package com.tadahtech.pub.shield;

import com.tadahtech.pub.drop.Drop;
import org.bukkit.Location;

/**
 * @author Timothy Andis
 */
public interface ShieldStructure {

    ShieldType getShieldType();
    String name();
    void run(Location loc, Drop drop);

}
