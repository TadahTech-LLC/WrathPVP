package com.tadahtech.pub.tier;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Timothy Andis
 */
public class TierItem {

    private List<ItemStack> items;

    public TierItem(ItemStack... items) {
        this.items = new ArrayList<>();
        Collections.addAll(this.items, items);
    }

    public void give(Player player) {
        player.getInventory().addItem(items.toArray(new ItemStack[items.size()]));
    }

    public ItemStack getItemStack() {
        if(items.size() == 1) {
            return items.get(0);
        }
        return items.get(1);
    }

    public int size() {
        return items.size();
    }

    public void drop(Location drop) {
        for(ItemStack itemStack : this.items) {
            drop.getWorld().dropItemNaturally(drop, itemStack);
        }
    }

    public List<ItemStack> getItems() {
        return items;
    }
}
