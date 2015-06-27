package com.tadahtech.pub.tcore.fixes;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Timothy Andis
 */
public class DepthStriderFix implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Block block = event.getClickedBlock();
        if(block.getType() != Material.ANVIL) {
            return;
        }
        clean(player);
    }

    private void clean(Player player) {
        Map<Integer, ItemStack> items = new HashMap<>();
        Map<Integer, ItemStack> armor = new HashMap<>();
        for(int i = 0; i < player.getInventory().getContents().length; i++) {
            ItemStack itemStack = player.getInventory().getItem(i);
            if(!contains(itemStack)) {
                continue;
            }
            ItemMeta meta = itemStack.getItemMeta();
            Enchantment boo = Enchantment.getById(8);
            meta.removeEnchant(boo);
            itemStack.setItemMeta(meta);
            items.put(i, itemStack);
        }
        for(int i = 0; i < player.getInventory().getArmorContents().length; i++) {
            ItemStack itemStack = player.getInventory().getArmorContents()[i];
            if(!contains(itemStack)) {
                continue;
            }
            ItemMeta meta = itemStack.getItemMeta();
            Enchantment boo = Enchantment.getById(8);
            meta.removeEnchant(boo);
            itemStack.setItemMeta(meta);
            armor.put(i, itemStack);
        }
        for(Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
            player.getInventory().setItem(entry.getKey(), entry.getValue());
        }
        for(Map.Entry<Integer, ItemStack> entry : armor.entrySet()) {
            player.getInventory().getArmorContents()[entry.getKey()] = entry.getValue();
        }

    }

    private boolean contains(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        if (itemStack.getItemMeta() == null) {
            return false;
        }
        ItemMeta meta = itemStack.getItemMeta();
        for (Map.Entry<Enchantment, Integer> entry : meta.getEnchants().entrySet()) {
            if(entry.getKey().getId() == 8) {
                return true;
            }
        }
        return false;
    }
}
