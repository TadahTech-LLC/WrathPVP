package com.tadahtech.wrathpvp.enchants.enchants.enchants.block;

import com.tadahtech.wrathpvp.enchants.enchants.BlockEnchant;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by Timothy Andis
 */
public class HasteEnchant extends BlockEnchant implements Listener {

	public static final HasteEnchant INSTANCE = new HasteEnchant();
	private final PotionEffect EFFECT_1 = new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1);
	private final PotionEffect EFFECT_2 = new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 2);

	public HasteEnchant() {
		PLUGIN.getServer().getPluginManager().registerEvents(this, PLUGIN);
	}

	@Override
	public void onBreak(BlockBreakEvent event, int level) {
	}

	@EventHandler
	public void held(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		Inventory inventory = player.getInventory();
		ItemStack itemStack = inventory.getItem(event.getNewSlot());
		if(itemStack == null || itemStack.getType() == Material.AIR) {
			remove(player);
			return;
		}
		ItemMeta meta = itemStack.getItemMeta();
		if(meta == null) {
			remove(player);
			return;
		}
		if(!meta.hasEnchant(getEnchantment())) {
			remove(player);
			return;
		}
		if(player.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
			return;
		}
		switch (meta.getEnchantLevel(this)) {
			case 1:
				player.addPotionEffect(EFFECT_1);
				return;
			case 2:
				player.addPotionEffect(EFFECT_2);
				return;
			default:
				break;
		}
	}

	private void remove(Player player) {
		if(!player.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
			return;
		}
		player.removePotionEffect(PotionEffectType.FAST_DIGGING);
	}

}
