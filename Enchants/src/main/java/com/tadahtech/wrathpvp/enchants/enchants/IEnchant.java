package com.tadahtech.wrathpvp.enchants.enchants;

import com.tadahtech.wrathpvp.enchants.Enchants;
import net.minecraft.server.v1_7_R4.LocaleI18n;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Timothy Andis
 */
public interface IEnchant {

	public Enchants PLUGIN = Enchants.getInstance();

	public EnchantableTarget[] getEnchantableTarget();

	public void onBreak(BlockBreakEvent event, int level);

	public void onPlace(BlockPlaceEvent event, int level);

	public void onHit(EntityDamageEvent event, int level);

	public void onDamage(EntityDamageByEntityEvent event, int level);

	public void onFish(PlayerFishEvent event, int level);

	public String getName();

	default public int getCost() {
		return PLUGIN.getConfig().getInt("costs." + getName().toUpperCase());
	}

	public Enchantment getEnchantment();

	default public boolean canEnchant(ItemStack itemStack) {
		String name = itemStack.getType().name();
		if(itemStack.getType() == Material.BOW) {
			return hasTarget(EnchantableTarget.BOW);
		}
		if(itemStack.getType() == Material.FISHING_ROD) {
			return hasTarget(EnchantableTarget.FISHING_ROD);
		}
		name = name.split("_")[1];
		name = name.toLowerCase();
		switch (name) {
			case "spade":
				return hasTarget(EnchantableTarget.SHOVEL);
			case "sword":
				return hasTarget(EnchantableTarget.SWORD);
			case "pickaxe":
				return hasTarget(EnchantableTarget.PICKAXE);
			case "axe":
				return hasTarget(EnchantableTarget.AXE);
			case "helmet":
				return hasTarget(EnchantableTarget.HELMET);
			case "chestplate":
				return hasTarget(EnchantableTarget.CHESTPLATE);
			case "leggings":
				return hasTarget(EnchantableTarget.LEGGINGS);
			case "boots":
				return hasTarget(EnchantableTarget.BOOTS);
		}
		return false;
	}

	default boolean hasTarget(EnchantableTarget target) {
		for(EnchantableTarget enchantableTarget : getEnchantableTarget()) {
			if(target == enchantableTarget) {
				return true;
			}
		}
		return false;
	}

	default public void register(Enchantment ench) {
		try {
			Field f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null, true);
			Enchantment.registerEnchantment(ench);
			f.set(null, false);
		} catch (IllegalStateException | IllegalArgumentException e) {
			System.out.println("Error: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	default public void disable() {
		try {
			Field byIdField = Enchantment.class.getDeclaredField("byId");
			Field byNameField = Enchantment.class.getDeclaredField("byName");

			byIdField.setAccessible(true);
			byNameField.setAccessible(true);

			@SuppressWarnings("unchecked")
			HashMap<Integer, Enchantment> byId = (HashMap<Integer, Enchantment>) byIdField.get(null);
			@SuppressWarnings("unchecked")
			HashMap<String, Enchantment> byName = (HashMap<String, Enchantment>) byNameField.get(null);

			if (byId.containsKey(getEnchantment().getId()))
				byId.remove(getEnchantment().getId());

			if (byName.containsKey(getEnchantment().getName()))
				byName.remove(getEnchantment().getName());
		} catch (Exception ignored) {
		}
	}

	public default void enchant(ItemMeta meta, int level) {
		List<String> lore = meta.getLore() == null ? new ArrayList<>() : meta.getLore();
		lore.add(ChatColor.GRAY + getName() + " " + LocaleI18n.get("enchantment.level." + level));
		meta.setLore(lore);
	}


}
