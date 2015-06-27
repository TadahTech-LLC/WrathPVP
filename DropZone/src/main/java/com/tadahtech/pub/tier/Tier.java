package com.tadahtech.pub.tier;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author Timothy Andis
 */
public class Tier {

    private int level, amount, min, max;
    private List<TierItem> drops;
    private Random random;
    private Map<Integer,TierItem> sets;
    private final Random itemRandom = new Random();
    private static Map<Integer, Tier> tiers = new HashMap<>();


    public Tier(int level, int amount, List<TierItem> drops) {
        this.level = level;
        this.amount = amount;
        this.drops = drops;
        this.sets = new HashMap<>();
        tiers.put(level, this);
    }

    public Tier(int level, int min, int max, List<TierItem> drops) {
        this.level = level;
        this.min = min;
        this.max = max;
        this.drops = drops;
        this.amount = -1;
        this.random = new Random();
        tiers.put(level, this);
    }

    public static Tier get(int i) {
        return tiers.get(i);
    }

    public void fill(Inventory inventory) {
        List<TierItem> items = new ArrayList<>(this.drops);
        for(int i = 0; i < drops.size(); i++) {
            TierItem comp = this.drops.get(i);
            if(i == 26) {
                break;
            }
            TierItem comp2 = items.get(i + 1);
            if(comp.equals(comp2)) {
                items.remove(i + 1);
            }
        }
        int slot = 0;
        for (int i = 0; i < amount; i++) {
            TierItem tierItem = items.get(itemRandom.nextInt(items.size() - 1));
            if(tierItem.size() > 1) {
                for(ItemStack itemStack : tierItem.getItems()) {
                    inventory.setItem(slot, itemStack);
                    slot++;
                }
                continue;
            }
            inventory.setItem(slot, tierItem.getItemStack());
            slot++;
        }
    }

    public TierItem getFromSlot(int slot) {
        return sets.get(slot);
    }

    public int getLevel() {
        return level;
    }

    public List<TierItem> getDrops() {
        return drops;
    }

    public void clearSet() {
        sets.clear();
    }

    public static void clear() {
        tiers.clear();
    }
}
