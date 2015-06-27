package com.tadahtech.wrath.stattracker.block;

import com.google.common.collect.Lists;
import com.tadahtech.wrath.stattracker.ItemSetting;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Timothy Andis
 */
public class MiningSetting extends ItemSetting {

    public MiningSetting(ItemStack itemsStack, boolean enableCosmetics, int level, int xp, int nextXp, int total) {
        super(itemsStack, enableCosmetics, level, xp, nextXp, total);
    }

    @Override
    public ItemStack rebuild() {
        ItemStack itemStack = this.getItemsStack();
        ItemMeta meta = itemStack.getItemMeta();
        List<String> iLore = meta.getLore() == null ? Lists.newArrayList() : meta.getLore();
        int nextXp = getNextXp() == 0 ? (int) Math.round((Math.pow(getLevel(), 1.27)) * 1000) : getNextXp();
        boolean mcmmo = false;
        if(iLore.isEmpty()) {
            iLore.add(" ");
            iLore.addAll(this.format(getLevel(), getXp(), nextXp, getTotal(), false));
            meta.setLore(iLore);
            itemStack.setItemMeta(meta);
            this.setItemStack(itemStack);
            return itemStack;
        }
        if(iLore.contains("mcMMO Ability Tool")) {
            iLore.remove("mcMMO Ability Tool");
            mcmmo = true;
        }
        List<String> lore = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            try {
                String s = iLore.get(i);
                if(s == null || s.equalsIgnoreCase(" ")) {
                    continue;
                }
                if(s.contains(ChatColor.DARK_AQUA.toString())) {
                    continue;
                }
                lore.add(s);
            } catch (IndexOutOfBoundsException ignored) {

            }
        }
        iLore.clear();
        int level = getLevel(), xp = getXp();
        nextXp = getNextXp() == 0 ? (int) Math.round((Math.pow(getLevel(), 1.27)) * 1000) : getNextXp();
        if(xp >= nextXp) {
            this.setLevel(level + 1);
            level++;
            nextXp = (int) Math.round((Math.pow(level, 1.27)) * 1000);
            xp = 0;
        }
        lore.add(ChatColor.DARK_AQUA + "Level: " + ChatColor.GRAY + level);
        lore.add(ChatColor.DARK_AQUA + "Total Blocks Broken: " + ChatColor.GRAY + getTotal());
        lore.add(ChatColor.DARK_AQUA + "Blocks Till Next Level: " + ChatColor.GRAY + (nextXp - xp));
        if(mcmmo) {
            lore.add("mcMMO Ability Tool");
        }
        iLore.add(" ");
        iLore.addAll(lore);
        String cosmetics = (this.isEnableCosmetics() ?  "Enabled" : "Disabled");
//        lore.set(cosLine, ChatColor.DARK_AQUA + "Cosmetics " + cosmetics);
        meta.setLore(iLore);
        itemStack.setItemMeta(meta);
        this.setItemStack(itemStack);
        return itemStack;
    }
}
