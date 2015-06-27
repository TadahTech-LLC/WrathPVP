package com.tadahtech.wrathpvp.enchants.enchants.enchants.wearing;

import com.tadahtech.wrathpvp.enchants.enchants.EnchantableTarget;
import com.tadahtech.wrathpvp.enchants.enchants.WearingEnchant;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by Timothy Andis
 */
public class NetherSkinEnchant extends WearingEnchant {

	public static NetherSkinEnchant INSTANCE = new NetherSkinEnchant();

	public NetherSkinEnchant() {
		super(true);
	}

	@Override
	public EnchantableTarget getTarget() {
		return EnchantableTarget.CHESTPLATE;
	}

	@Override
	public PotionEffect getEffect() {
		return new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1);
	}

	@Override
	public String getName() {
		return "Nether Skin";
	}
}
