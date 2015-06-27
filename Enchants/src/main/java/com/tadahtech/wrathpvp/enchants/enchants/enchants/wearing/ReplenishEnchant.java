package com.tadahtech.wrathpvp.enchants.enchants.enchants.wearing;

import com.tadahtech.wrathpvp.enchants.enchants.EnchantableTarget;
import com.tadahtech.wrathpvp.enchants.enchants.WearingEnchant;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.Iterator;

/**
 * Created by Timothy Andis
 */
public class ReplenishEnchant extends WearingEnchant {

	public static ReplenishEnchant INSTANCE = new ReplenishEnchant();

	public ReplenishEnchant() {
		super(false);
		PLUGIN.getServer().getScheduler().runTaskTimer(PLUGIN, this, 20 * 30L, 20 * 30L);
	}

	@Override
	public void run() {
		Iterator<Player> iterator = (Iterator<Player>) Bukkit.getOnlinePlayers().iterator();
		while(iterator.hasNext()) {
			Player player = iterator.next();
			ItemStack itemStack = null;
			if (getTarget() == EnchantableTarget.HELMET) {
				itemStack = player.getInventory().getHelmet();
			}
			if (getTarget() == EnchantableTarget.CHESTPLATE) {
				itemStack = player.getInventory().getChestplate();
			}
			if (getTarget() == EnchantableTarget.LEGGINGS) {
				itemStack = player.getInventory().getLeggings();
			}
			if (getTarget() == EnchantableTarget.BOOTS) {
				itemStack = player.getInventory().getBoots();
			}
			if (itemStack == null) {
				continue;
			}
			if (!itemStack.hasItemMeta()) {
				continue;
			}
			ItemMeta meta = itemStack.getItemMeta();
			if (!meta.hasEnchant(this)) {
				continue;
			}
			player.setFoodLevel(player.getFoodLevel() + Math.min(2, 20 - player.getFoodLevel()));
		}
	}

	@Override
	public EnchantableTarget getTarget() {
		return EnchantableTarget.HELMET;
	}

	@Override
	public PotionEffect getEffect() {
		return null;
	}
}
