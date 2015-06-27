package com.tadahtech.wrathpvp.enchants.enchants;

import com.tadahtech.wrathpvp.enchants.Enchants;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.Iterator;

/**
 * Created by Timothy Andis
 */
public abstract class WearingEnchant extends Enchantment implements IEnchant, Runnable {

	public WearingEnchant(boolean run) {
		super(Enchants.LAST_ID++);
		register(this);
		if(run) {
			PLUGIN.getServer().getScheduler().runTaskTimer(PLUGIN, this, 20L, 20L);
		}
	}

	@Override
	public void run() {
		Iterator<Player> iterator = (Iterator<Player>) Bukkit.getOnlinePlayers().iterator();
		while(iterator.hasNext()) {
			Player player = iterator.next();
			ItemStack itemStack = null;
			if(getTarget() == EnchantableTarget.HELMET) {
				itemStack = player.getInventory().getHelmet();
			}
			if(getTarget() == EnchantableTarget.CHESTPLATE) {
				itemStack = player.getInventory().getChestplate();
			}
			if(getTarget() == EnchantableTarget.LEGGINGS) {
				itemStack = player.getInventory().getLeggings();
			}
			if(getTarget() == EnchantableTarget.BOOTS) {
				itemStack = player.getInventory().getBoots();
			}
			if(itemStack == null) {
				remove(player);
				continue;
			}
			if(!itemStack.hasItemMeta()) {
				remove(player);
				continue;
			}
			ItemMeta meta = itemStack.getItemMeta();
			if(!meta.hasEnchant(this)) {
				remove(player);
				continue;
			}
			if(getEffect() == null) {
				return;
			}
			if(!player.hasPotionEffect(getEffect().getType())) {
				player.addPotionEffect(getEffect());
			}
		}
	}


	public void remove(Player player) {
		if(getEffect() == null) {
			return;
		}
		if(player.hasPotionEffect(getEffect().getType())) {
			player.removePotionEffect(getEffect().getType());
		}
	}

	@Override
	public EnchantableTarget[] getEnchantableTarget() {
		return new EnchantableTarget[] { getTarget() };
	}

	@Override
	public void onBreak(BlockBreakEvent event, int level) {

	}

	@Override
	public void onPlace(BlockPlaceEvent event, int level) {

	}

	@Override
	public void onHit(EntityDamageEvent event, int level) {

	}

	@Override
	public void onDamage(EntityDamageByEntityEvent event, int level) {

	}

	@Override
	public void onFish(PlayerFishEvent event, int level) {

	}

	@Override
	public String getName() {
		return getClass().getSimpleName().replace("Enchant", "");
	}

	@Override
	public Enchantment getEnchantment() {
		return this;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	@Override
	public int getStartLevel() {
		return 1;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return null;
	}

	@Override
	public boolean conflictsWith(Enchantment enchantment) {
		return false;
	}

	@Override
	public boolean canEnchantItem(ItemStack itemStack) {
		return false;
	}

	public abstract EnchantableTarget getTarget();
	public abstract PotionEffect getEffect();

}
