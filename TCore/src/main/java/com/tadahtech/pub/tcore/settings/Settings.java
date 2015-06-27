package com.tadahtech.pub.tcore.settings;

import com.tadahtech.pub.tcore.TCore;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by Timothy Andis
 */
public class Settings {

    private boolean depthStriderFix, doubleJump, tabMenu, battle, respawn, deathMessage, libsDesguise;
    private FileConfiguration config;

    public Settings() {
        this.config = TCore.getInstance().getConfig();
        load();
    }

    public void load() {
        ConfigurationSection section = config.getConfigurationSection("enabled");
        this.depthStriderFix = section.getBoolean("depthStrider");
        this.doubleJump = section.getBoolean("doubleJump");
        this.battle = section.getBoolean("battle");
        this.tabMenu = section.getBoolean("tabMenu");
        this.respawn = section.getBoolean("respawn");
        this.deathMessage = section.getBoolean("deathMessage");
        this.libsDesguise = section.getBoolean("libsDisguise");
    }

    public boolean isDepthStriderFix() {
        return depthStriderFix;
    }

    public boolean isDoubleJump() {
        return doubleJump;
    }

    public boolean isTabMenu() {
        return tabMenu;
    }

    public boolean isBattle() {
        return battle;
    }

    public boolean isRespawn() {
        return respawn;
    }

    public boolean isDeathMessage() {
        return deathMessage;
    }

    public boolean isLibsDesguise() {
        return libsDesguise;
    }
}
