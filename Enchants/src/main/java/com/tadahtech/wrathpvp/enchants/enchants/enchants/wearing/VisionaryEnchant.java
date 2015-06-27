package com.tadahtech.wrathpvp.enchants.enchants.enchants.wearing;

import com.tadahtech.wrathpvp.enchants.enchants.EnchantableTarget;
import com.tadahtech.wrathpvp.enchants.enchants.WearingEnchant;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by Timothy Andis
 */
public class VisionaryEnchant extends WearingEnchant {

	public static VisionaryEnchant INSTANCE = new VisionaryEnchant();

	public VisionaryEnchant() {
		super(true);
	}

	@Override
	public EnchantableTarget getTarget() {
		return EnchantableTarget.HELMET;
	}

	@Override
	public PotionEffect getEffect() {
		return new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1);
	}
}
