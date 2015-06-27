package com.tadahtech.wrathpvp.enchants.listener;

import com.tadahtech.wrathpvp.enchants.enchants.IEnchant;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by Timothy Andis
 */
public class BlockListener implements Listener {

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		ItemStack itemStack = player.getItemInHand();
		if(itemStack == null) {
			return;
		}
		if(itemStack.getType() == Material.AIR) {
			return;
		}
		ItemMeta meta = itemStack.getItemMeta();
		if(meta == null) {
			return;
		}
		Map<Enchantment, Integer> enchants = meta.getEnchants();
		if(enchants == null) {
			return;
		}
		if(enchants.isEmpty()) {
			return;
		}
		for(Entry<Enchantment, Integer> entry : enchants.entrySet()) {
			Enchantment enchantment = entry.getKey();
			if(enchantment instanceof IEnchant) {
				IEnchant blockEnchant = (IEnchant) enchantment;
				blockEnchant.onBreak(event, entry.getValue());
			}
		}
	}
}
