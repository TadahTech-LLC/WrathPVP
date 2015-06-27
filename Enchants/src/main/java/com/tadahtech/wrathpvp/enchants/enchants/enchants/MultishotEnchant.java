package com.tadahtech.wrathpvp.enchants.enchants.enchants;

import com.tadahtech.wrathpvp.enchants.Enchants;
import com.tadahtech.wrathpvp.enchants.enchants.EnchantableTarget;
import com.tadahtech.wrathpvp.enchants.enchants.IEnchant;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Timothy Andis
 */
public class MultishotEnchant extends Enchantment implements Listener, IEnchant {

	public static MultishotEnchant INSTANCE = new MultishotEnchant();

	public MultishotEnchant() {
		super(Enchants.LAST_ID++);
		PLUGIN.getServer().getPluginManager().registerEvents(this, PLUGIN);
		register(this);
	}

	@EventHandler
	public void onShoot(EntityShootBowEvent event) {
		if(!(event.getEntity() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntity();
		ItemStack itemStack = player.getItemInHand();
		if(itemStack == null || itemStack.getType() != Material.BOW) {
			return;
		}
		ItemMeta meta = itemStack.getItemMeta();
		if(meta == null) {
			return;
		}
		if(!meta.hasEnchant(this)) {
			return;
		}
		int level = meta.getEnchantLevel(this);
		Projectile projectile = (Projectile) event.getProjectile();
		World world = projectile.getWorld();
		Location location = projectile.getLocation();
		new BukkitRunnable() {

			private int ran = 0;

			@Override
			public void run() {
				if(ran >= level) {
					cancel();
					return;
				}
				Arrow arrow = world.spawn(location, Arrow.class);
				arrow.setShooter(player);
				arrow.setVelocity(projectile.getVelocity());
				ran++;
			}
		}.runTaskTimer(Enchants.getInstance(), 5L, 5L);
	}

	@Override
	public EnchantableTarget[] getEnchantableTarget() {
		return new EnchantableTarget[] {
		  EnchantableTarget.BOW
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
	public void onDamage(EntityDamageByEntityEvent event, int level) {

	}

	@Override
	public void onFish(PlayerFishEvent event, int level) {

	}

	@Override
	public String getName() {
		return "Multishot";
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
