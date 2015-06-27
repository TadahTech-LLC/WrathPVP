package com.tadahtech.wrathpvp.enchants.enchants;

import com.tadahtech.wrathpvp.enchants.Enchants;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.security.SecureRandom;

/**
 * Created by Timothy Andis
 */
public abstract class DamageEnchant extends Enchantment implements IEnchant {

	protected static SecureRandom random = new SecureRandom();

	public DamageEnchant() {
		super(Enchants.LAST_ID++);
		register(this
		);
	}

	@Override
	public EnchantableTarget[] getEnchantableTarget() {
		return new EnchantableTarget[] {
		  EnchantableTarget.AXE, EnchantableTarget.SWORD
		};
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
		return 2;
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
}
