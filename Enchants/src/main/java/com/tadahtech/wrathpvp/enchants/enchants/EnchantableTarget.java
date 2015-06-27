package com.tadahtech.wrathpvp.enchants.enchants;

/**
 * Created by Timothy Andis
 */
public enum EnchantableTarget {

	BOW,
	SWORD,
	AXE,
	PICKAXE,
	SHOVEL,
	HELMET,
	CHESTPLATE,
	LEGGINGS,
	BOOTS,
	FISHING_ROD
	;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		String name = name().replace("_", " ");
		builder.append(name.substring(0, 1).toUpperCase());
		builder.append(name.substring(1).toLowerCase());
		return builder.toString();
	}

}
