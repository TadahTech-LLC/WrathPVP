/*
 * Copyright (c) ReasonDev 2014.
 * All rights reserved.
 * No part of this project or any of its contents may be reproduced, copied, modified or adapted, without the prior written consent of SirReason.
 */

package co.reasondev.koth.util;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public enum Settings {

    KOTH_WORLD, KOTH_SPAWN, ENABLE_DEATH_BAN,

    PVP_DELAY, CLAIM_DELAY, INACTIVE_DELAY, RESET_DELAY,

    REWARD_MENU_SIZE, REWARD_MENU_TITLE;

    //Plugin Configuration
    private static FileConfiguration c;

    public static void registerConfig(FileConfiguration c) {
        Settings.c = c;
    }

    //Config variables getters
    public boolean getBoolean() {
        return c.getBoolean(name());
    }

    public int getInt() {
        return c.getInt(name());
    }

    public String getString() {
        return c.getString(name());
    }

    public List<String> getList() {
        return c.getStringList(name());
    }

    //Configured private (Player) messages
    public static enum Messages {

        PREFIX,

        KOTH_JOINED,
        DEATH_BANNED,
        NOT_HILL_KING,

        CLAIMING_HILL,
        NOT_CLAIMING_HILL,
        HILL_CLAIMED;

        public String getMessage() {
            return c.getString("messages." + name());
        }

    }

    //Configured Public (Server) Messages
    public static enum Broadcasts {

        PREFIX,

        HILL_ACTIVE,

        UNCONTROLLED_HILL,
        HILL_INACTIVE,

        CONTROLLED_HILL,
        HILL_CLAIMED;

        public String getMessage() {
            return c.getString("broadcasts." + name());
        }

    }
}
