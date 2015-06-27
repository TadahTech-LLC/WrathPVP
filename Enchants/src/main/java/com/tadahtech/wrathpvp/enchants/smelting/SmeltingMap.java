package com.tadahtech.wrathpvp.enchants.smelting;

import org.bukkit.Material;

import java.util.HashMap;

/**
 * Created by Timothy Andis
 */
public class SmeltingMap {

	private static HashMap<Material, SmeltableInfo> smelting_map = new HashMap();

	public SmeltingMap() {
	}

	public static SmeltableInfo getSmeletable(Material material) {
		return smelting_map.get(material);
	}

	static {
		smelting_map.put(Material.COBBLESTONE, new SmeltableInfo(Material.STONE, 0));
		smelting_map.put(Material.STONE, new SmeltableInfo(Material.STONE, 0));
		smelting_map.put(Material.IRON_ORE, new SmeltableInfo(Material.IRON_INGOT, 4));
		smelting_map.put(Material.GOLD_ORE, new SmeltableInfo(Material.GOLD_INGOT, 7));
		smelting_map.put(Material.NETHERRACK, new SmeltableInfo(Material.NETHER_BRICK_ITEM, 0));
	}
}
