/*
 * Copyright (c) ReasonDev 2014.
 * All rights reserved.
 * No part of this project or any of its contents may be reproduced, copied, modified or adapted, without the prior written consent of SirReason.
 */

package co.reasondev.koth.reward;

import co.reasondev.koth.WrathKotH;
import co.reasondev.koth.util.Settings;
import co.reasondev.koth.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class RewardManager {

    private WrathKotH plugin;

    private File file;
    private YamlConfiguration config;
    private List<Integer> rewardSlots = new ArrayList<>();
    private Map<Integer, Reward> loadedRewards = new TreeMap<>();

    private Inventory rewardMenu;

    public RewardManager(WrathKotH plugin) {
        this.plugin = plugin;
    }

    public void loadRewards() {
        //Load Rewards Configuration File
        this.file = new File(plugin.getDataFolder(), "rewards.yml");
        if (!file.exists()) {
            plugin.saveResource(file.getName(), false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);
        //Load Rewards from Config
        for (String slot : config.getKeys(false)) {
            try {
                rewardSlots.add(Integer.valueOf(slot));
                loadedRewards.put(Integer.valueOf(slot), Reward.deserialize(config.getConfigurationSection(slot)));
            } catch (NumberFormatException e) {
                plugin.getLogger().severe("Error parsing slot '" + slot + "'. Not a number!");
                e.printStackTrace();
            }
        }
        plugin.getLogger().info("Loaded " + loadedRewards.size() + " Rewards from Reward Configuration");
    }

    public boolean isRewardMenu(Inventory inventory) {
        if (inventory == null) {
            return false;
        }
        if (rewardMenu == null) {
            rewardMenu = Bukkit.createInventory(null, Settings.REWARD_MENU_SIZE.getInt(), StringUtil.color(Settings.REWARD_MENU_TITLE.getString()));
            for (Integer i : loadedRewards.keySet()) {
                rewardMenu.setItem(i, loadedRewards.get(i).getRewardIcon());
            }
        }
        return rewardMenu.getTitle().equals(inventory.getTitle()) && rewardMenu.getSize() == inventory.getSize();
    }

    public void openRewardMenu(Player player) {
        if (rewardMenu == null) {
            rewardMenu = Bukkit.createInventory(null, Settings.REWARD_MENU_SIZE.getInt(), StringUtil.color(Settings.REWARD_MENU_TITLE.getString()));
            for (Integer i : loadedRewards.keySet()) {
                rewardMenu.setItem(i, loadedRewards.get(i).getRewardIcon());
            }
        }
        player.openInventory(rewardMenu);
    }

    public void populateInventory(Inventory inventory, int lootSize) {
        if (inventory == null || lootSize <= 0) {
            return;
        }
        Random random = new Random();
        for (int i = 0; i < lootSize; i++) {
            Reward reward = loadedRewards.get(rewardSlots.get(random.nextInt(rewardSlots.size())));
            for (ItemStack item : reward.getRewardItems()) {
                inventory.addItem(item);
            }
        }
    }
}
