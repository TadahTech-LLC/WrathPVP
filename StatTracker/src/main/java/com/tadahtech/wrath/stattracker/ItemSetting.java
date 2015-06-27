package com.tadahtech.wrath.stattracker;

import com.tadahtech.wrath.stattracker.block.MiningSetting;
import com.tadahtech.wrath.stattracker.entity.WeaponSetting;
import com.tadahtech.wrath.stattracker.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Timothy Andis
 */
@SuppressWarnings("unchecked")
public abstract class ItemSetting {

    private boolean enableCosmetics;
    private int level, xp, nextXp, total;
    protected ItemStack itemStack;

    private static Map<ItemStack, ItemSetting> settings = new HashMap<>();

    public ItemSetting(ItemStack itemStack, boolean enableCosmetics, int level, int xp) {
        this.itemStack = itemStack;
        this.enableCosmetics = enableCosmetics;
        this.level = level;
        this.xp = xp;
//        settings.put(itemStack, this);
    }

    public ItemSetting(ItemStack itemStack, boolean enableCosmetics, int level, int xp, int nextXP, int total) {
        this(itemStack, enableCosmetics, level, xp);
        this.nextXp = nextXP;
        this.total = total;
    }

    public ItemStack getItemsStack() {
        return itemStack;
    }

    public boolean isEnableCosmetics() {
        return enableCosmetics;
    }

    public void setEnableCosmetics(boolean enableCosmetics) {
        this.enableCosmetics = enableCosmetics;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void incrementXp(int amt) {
        this.xp += amt;
        this.total += amt;
    }

    public void incrementXp() {
        this.incrementXp(1);
    }

    public abstract ItemStack rebuild();

    protected List<String> format(int level, int xp, int nextXp, int total, boolean kills) {
        List<String> lore = new ArrayList<>(10);

        String xp_s = kills ? "Kills" : "Blocks Broken";
        lore.add(ChatColor.DARK_AQUA + "Level: " + ChatColor.GRAY + level);
        lore.add(ChatColor.DARK_AQUA + "Total " + xp_s + ": " + ChatColor.GRAY + total);
        lore.add(ChatColor.DARK_AQUA + xp_s.replace(" Broken", "") + " Till Next Level: " + ChatColor.GRAY + (nextXp - xp));
        if(kills) {
            lore.add(ChatColor.DARK_AQUA + "Last Killed: ");
        }
        String cosmetics = (this.isEnableCosmetics() ?  "Enabled" : "Disabled");
//        list.add(ChatColor.DARK_AQUA + "Cosmetics " + cosmetics);
        return lore;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static ItemSetting get(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if(meta == null) {
            return returnSetting(itemStack);
        }
        List<String> lore = meta.getLore();
        if(lore == null) {
            meta.setLore(new ArrayList<>());
            lore = new ArrayList<>();
        }
        boolean weapon = Utils.isWeapon(itemStack);
        boolean enableCosmetics = true;
        String[] lastKilled = new String[3];
        int level = 1, xp = 0, nextXp = 0, total = 0;
        for (int i = 0; i < lore.size(); i++) {
            String s = lore.get(i);
            s = ChatColor.stripColor(s);
            s = s.toLowerCase();
            String[] str = s.split(": ");
            s = str[0];
            if(s.equalsIgnoreCase(" ")) {
                continue;
            }
            String xp_s = weapon ? "kills" : "blocks broken";
            if (s.equalsIgnoreCase("Level")) {
                level = Integer.parseInt(str[1]);
            }
            if (s.equalsIgnoreCase("total " + xp_s)) {
                total = Integer.parseInt(str[1]);
            }
            if (s.equalsIgnoreCase(xp_s.replace(" broken", "") + " till next level")) {
                nextXp = Integer.parseInt(str[1]);
            }
            if(s.equalsIgnoreCase("Last Killed" )) {
                try {
                    lastKilled[0] = ChatColor.stripColor(lore.get(i + 1).replace("- ", ""));
                    lastKilled[1] = ChatColor.stripColor(lore.get(i + 2).replace("- ", ""));
                    lastKilled[2] = ChatColor.stripColor(lore.get(i + 3).replace("- ", ""));
                } catch (Exception ignored) {

                }
            }
            if(s.equalsIgnoreCase("view cosmetics")) {
                enableCosmetics = Boolean.valueOf(str[1]);
            }
        }
        if(weapon) {
            WeaponSetting weaponSetting = new WeaponSetting(itemStack, enableCosmetics, level, xp, nextXp, total, lastKilled);
            weaponSetting.rebuild();
            return weaponSetting;
        } else {
            MiningSetting miningSetting = new MiningSetting(itemStack, enableCosmetics, level, xp, nextXp, total);
            miningSetting.rebuild();
            return miningSetting;
        }
    }

    private static ItemSetting returnSetting(ItemStack itemStack) {
        boolean weapon = Utils.isWeapon(itemStack);
        if(weapon) {
            return new WeaponSetting(itemStack, true, 1, 0, 0, 0);
        } else {
            return new MiningSetting(itemStack, true, 1, 0, 0, 0);
        }
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getNextXp() {
        return nextXp;
    }

    public void setNextXp(int nextXp) {
        this.nextXp = nextXp;
    }
}
