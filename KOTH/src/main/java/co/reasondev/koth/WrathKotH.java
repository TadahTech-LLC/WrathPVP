/*
 * Copyright (c) ReasonDev 2014.
 * All rights reserved.
 * No part of this project or any of its contents may be reproduced, copied, modified or adapted, without the prior written consent of SirReason.
 */

package co.reasondev.koth;

import co.reasondev.koth.hill.HillManager;
import co.reasondev.koth.reward.RewardManager;
import co.reasondev.koth.util.Settings;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.plugin.java.JavaPlugin;

public class WrathKotH extends JavaPlugin {

    private HillManager hillManager;
    private RewardManager rewardManager;
    private WorldEditPlugin worldEdit;

    public void onEnable() {
        if (getWorldEdit() == null) {
            getLogger().severe("WorldEdit dependency not found! Plugin cannot enable!");
            getServer().getPluginManager().disablePlugin(this);
        }
        saveDefaultConfig();
        Settings.registerConfig(getConfig());
        getHillManager().loadHills();
        getHillManager().runHillTask();
        getRewardManager().loadRewards();
        getCommand("koth").setExecutor(new KotHCommand(this));
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getLogger().info("has been enabled");
    }

    public void onDisable() {
        getHillManager().saveHills();
        getLogger().info("has been disabled");
    }

    public HillManager getHillManager() {
        if (hillManager == null) {
            hillManager = new HillManager(this);
        }
        return hillManager;
    }

    public RewardManager getRewardManager() {
        if (rewardManager == null) {
            rewardManager = new RewardManager(this);
        }
        return rewardManager;
    }

    public WorldEditPlugin getWorldEdit() {
        if (worldEdit == null) {
            worldEdit = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");
        }
        return worldEdit;
    }

}
