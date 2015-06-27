package com.tadahtech.wrathpvp.enchants.enchants.enchants.damage;

import com.tadahtech.wrathpvp.enchants.Enchants;
import com.tadahtech.wrathpvp.enchants.enchants.DamageEnchant;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Timothy Andis
 */
public class ParalyzeEnchant extends DamageEnchant {

	public static ParalyzeEnchant INSTANCE = new ParalyzeEnchant();

	@Override
	public void onDamage(EntityDamageByEntityEvent event, int level) {
		Player player = (Player) event.getEntity();
//		Player damager = (Player) event.getDamager();
		double chance = 2.5 * level;
		double d = random.nextDouble() * 100;
		if(d > chance) {
			return;
		}
		player.setWalkSpeed(0);
		new BukkitRunnable() {
			@Override
			public void run() {
				player.setWalkSpeed(0.2F);
			}
		}.runTaskLater(Enchants.getInstance(), 20 * level);
	}
}
