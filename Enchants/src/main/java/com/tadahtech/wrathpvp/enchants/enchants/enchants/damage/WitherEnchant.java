package com.tadahtech.wrathpvp.enchants.enchants.enchants.damage;

import com.tadahtech.wrathpvp.enchants.enchants.DamageEnchant;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by Timothy Andis
 */
public class WitherEnchant extends DamageEnchant {

	public static WitherEnchant INSTANCE = new WitherEnchant();

	@Override
	public void onDamage(EntityDamageByEntityEvent event, int level) {
		Player player = (Player) event.getEntity();
//		Player damager = (Player) event.getDamager();
		double chance = 2 * level;
		double d = random.nextDouble() * 100;
		if(d > chance) {
			return;
		}
		PotionEffect effect = new PotionEffect(PotionEffectType.WITHER, 100, level);
		player.addPotionEffect(effect);
	}
}
