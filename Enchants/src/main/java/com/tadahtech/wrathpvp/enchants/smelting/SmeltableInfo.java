package com.tadahtech.wrathpvp.enchants.smelting;

import org.bukkit.Material;

import java.beans.ConstructorProperties;

/**
 * Created by Timothy Andis
 */
public class SmeltableInfo {
	private final Material material;
	private final int exp;

	@ConstructorProperties({"material", "exp"})
	public SmeltableInfo(Material material, int exp) {
		this.material = material;
		this.exp = exp;
	}

	public Material getMaterial() {
		return this.material;
	}

	public int getExp() {
		return this.exp;
	}
}
