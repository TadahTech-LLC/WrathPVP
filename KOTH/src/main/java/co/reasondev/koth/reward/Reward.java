/*
 * Copyright (c) ReasonDev 2014.
 * All rights reserved.
 * No part of this project or any of its contents may be reproduced, copied, modified or adapted, without the prior written consent of SirReason.
 */

package co.reasondev.koth.reward;

import co.reasondev.koth.util.StringUtil;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Reward {

    private int slot;
    private ItemStack rewardIcon;
    private List<ItemStack> rewardItems;

    public Reward(int slot, String displayName, Material iconMaterial, int iconData, List<String> iconLore, List<String> iconEnchants, List<ItemStack> rewardItems) {
        this.slot = slot;
        rewardIcon = new ItemStack(iconMaterial, 1, (short) iconData);
        ItemMeta meta = rewardIcon.getItemMeta();
        meta.setDisplayName(StringUtil.color(displayName));
        meta.setLore(StringUtil.colorAll(iconLore));
        for (String ench : iconEnchants) {
            String[] args = ench.split("/");
            meta.addEnchant(Enchantment.getByName(args[0]), Integer.valueOf(args[1]), true);
        }
        rewardIcon.setItemMeta(meta);
        this.rewardItems = rewardItems;
    }

    public static Reward deserialize(ConfigurationSection c) {
        return new Reward(Integer.valueOf(c.getName()), c.getString("displayName"), Material.getMaterial(c.getString("iconMaterial")),
                c.getInt("iconData"), c.getStringList("iconLore"), c.getStringList("iconEnchants"), StringUtil.parseItemList(c.getStringList("rewardItems")));
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getRewardIcon() {
        return rewardIcon;
    }

    public List<ItemStack> getRewardItems() {
        return rewardItems;
    }
}
