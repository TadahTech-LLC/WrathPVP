package com.tadahtech.wrathpvp.enchants.enchants.enchants.damage;

import com.tadahtech.wrathpvp.enchants.enchants.DamageEnchant;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Created by Timothy Andis
 */
public class LifestealEnchant extends DamageEnchant {

	public static LifestealEnchant INSTANCE = new LifestealEnchant();

	@Override
	public void onDamage(EntityDamageByEntityEvent event, int level) {
//		Player player = (Player) event.getEntity();
		Player damager = (Player) event.getDamager();
		double chance = 5;
		double d = random.nextDouble() * 100;
		if(d > chance) {
			return;
		}
		double damage = event.getFinalDamage();
		double heal = damage * (15 / 100);
		damager.setHealth(damager.getHealth() + heal);
	}
}
