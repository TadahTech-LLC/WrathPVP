package com.tadahtech.pub.tcore;

import com.tadahtech.pub.tcore.additions.tab.TabMenu;
import com.tadahtech.pub.tcore.fixes.DepthStriderFix;
import com.tadahtech.pub.tcore.fixes.LibsDisguiseFix;
import com.tadahtech.pub.tcore.listeners.*;
import com.tadahtech.pub.tcore.settings.Settings;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Timothy Andis
 */
public class TCore extends JavaPlugin {

    private static TCore instance;
    private TabMenu tabMenu;
    private Settings settings;

    public static TCore getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.settings = new Settings();

        FileConfiguration config = getConfig();
        String header = config.getString("tab.header");
        String footer = config.getString("tab.footer");
        this.tabMenu = new TabMenu(header, footer);
        if(settings.isDoubleJump()) {
            getLogger().info("Enabled double jump");
            getServer().getPluginManager().registerEvents(new DoubleJumpListener(), this);
        }
        if(settings.isTabMenu()) {
            getLogger().info("Enabled TabMenu");
            getServer().getPluginManager().registerEvents(new PlayerJoinListener(tabMenu), this);
        }
        if(settings.isDepthStriderFix()) {
            getLogger().info("Enabled DepthStrider Fix");
            getServer().getPluginManager().registerEvents(new DepthStriderFix(), this);
        }
        if(settings.isBattle()) {
            getLogger().info("Enabled Battle");
            getServer().getPluginManager().registerEvents(new BattleListener(), this);
        }
        if (settings.isRespawn()) {
            getLogger().info("Enabled RespawnFix (Thanks Harry)");
            getServer().getPluginManager().registerEvents(new RespawnListener(), this);
        }
        if(settings.isDeathMessage()) {
            getLogger().info("Enabled DeathMessages (Thanks Harry)");
            boolean globally = getConfig().getBoolean("deathMessage.global");
            boolean pve = getConfig().getBoolean("deathMessage.pve");
            getServer().getPluginManager().registerEvents(new DeathMessageListener(globally, pve), this);
        }
        if(settings.isLibsDesguise()) {
            getLogger().info("Enabled Libs Disguise Fix (Thanks Harry)");
            getServer().getPluginManager().registerEvents(new LibsDisguiseFix(), this);
        }
    }

    public TabMenu getTabMenu() {
        return tabMenu;
    }
}
