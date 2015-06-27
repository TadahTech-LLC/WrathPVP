package com.tadahtech.wrath.stattracker.entity;

import com.google.common.collect.Lists;
import com.tadahtech.wrath.stattracker.ItemSetting;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Timothy Andis
 */
public class WeaponSetting extends ItemSetting {

    private String[] lastKilled;

    public WeaponSetting(ItemStack itemsStack, boolean enableCosmetics, int level, int xp, int nextXp, int total) {
        super(itemsStack, enableCosmetics, level, xp, nextXp,total);
        this.lastKilled = new String[3];
    }

    public WeaponSetting(ItemStack itemsStack, boolean enableCosmetics, int level, int xp, int nextXp, int total, String[] lastKilled) {
        super(itemsStack, enableCosmetics, level, xp, nextXp, total);
        this.lastKilled = lastKilled;
    }

    @Override
    public ItemStack rebuild() {
        ItemStack itemStack = this.getItemsStack().clone();
        ItemMeta meta = itemStack.getItemMeta();
        List<String> iLore = meta.getLore() == null ? Lists.newArrayList() : meta.getLore();
        int nextXp = getNextXp() == 0 ? (int) (getLevel() * 3.27D) + 10 : getNextXp();
        if (iLore.isEmpty()) {
            iLore.add(" ");
            iLore.addAll(this.format(getLevel(), getXp(), nextXp, getTotal(), true));
            meta.setLore(iLore);
            itemStack.setItemMeta(meta);
            this.setItemStack(itemStack);
            return itemStack;
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
        lore.add(" ");
        int level = getLevel(), xp = getXp();
        nextXp = getNextXp() == 0 ? (int) (level * 3.27D) + 10 : getNextXp();
        if (xp >= nextXp) {
            this.setLevel(level + 1);
            level++;
            nextXp = (int) (level * 3.27D) + 10;
            xp = 0;
        }
        lore.add(ChatColor.DARK_AQUA + "Item Level: " + ChatColor.GRAY + level);
        lore.add(ChatColor.DARK_AQUA + "Total Kills: " + ChatColor.GRAY + getTotal());
        lore.add(ChatColor.DARK_AQUA + "Kills till level up: " + ChatColor.GRAY + (nextXp - xp));
        lore.add(ChatColor.DARK_AQUA + "Last Killed: ");
        List<String> kills = new ArrayList<>(Arrays.asList(lastKilled));
        Collections.reverse(kills);
        for (String s : kills) {
            if(s == null) {
                continue;
            }
            lore.add(ChatColor.GRAY + "- " + s);
        }
        String cosmetics = (this.isEnableCosmetics() ? "Enabled" : "Disabled");
//        lore.set(cosLine, ChatColor.DARK_AQUA + "Cosmetics " + cosmetics);
        iLore.addAll(lore);
        meta.setLore(iLore);
        itemStack.setItemMeta(meta);
        this.setItemStack(itemStack);
        return itemStack;
    }

    public void addLastKilled(Player player) {
        for (int i = 0; i < 3; i++) {
            String s = lastKilled[i];
            if (s == null) {
                lastKilled[i] = player.getName();
                return;
            } else {
                if(s.equalsIgnoreCase(player.getName())) {
                    return;
                }
            }
        }
        //All are taken.
        String first = lastKilled[0];
        String second = lastKilled[1];
        lastKilled[0] = player.getName();
        lastKilled[1] = first;
        lastKilled[2] = second;
    }
}
