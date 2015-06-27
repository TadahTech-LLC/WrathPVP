package com.tadahtech.wrathpvp.enchants.enchants.enchants.block;

import com.tadahtech.wrathpvp.enchants.Enchants;
import com.tadahtech.wrathpvp.enchants.enchants.EnchantableTarget;
import com.tadahtech.wrathpvp.enchants.enchants.IEnchant;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.inventory.ItemStack;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Timothy Andis
 */
public class LocksmithEnchant extends Enchantment implements IEnchant {

	public static LocksmithEnchant INSTANCE = new LocksmithEnchant();

	private static Map<Double, Integer> items = new HashMap<>();

	static {
		FileConfiguration config = PLUGIN.getConfig();
		List<String> list = config.getStringList("keys");
		for(String s : list) {
			String[] str = s.split(":");
			int d = Integer.parseInt(str[0]);
			double w = Double.parseDouble(str[1]);
			items.put(w, d);
		}
	}

	private String COMMAND = "giveCrateKey <level> <player>";

	private SecureRandom random = new SecureRandom();

	public LocksmithEnchant() {
		super(Enchants.LAST_ID++);
		register(this);
	}

	@Override
	public EnchantableTarget[] getEnchantableTarget() {
		return new EnchantableTarget[]{
		  EnchantableTarget.PICKAXE, EnchantableTarget.AXE, EnchantableTarget.SHOVEL, EnchantableTarget.FISHING_ROD
		};
	}

	@Override
	public void onBreak(BlockBreakEvent event, int level) {
		double chance = 0.003;
		if (level == 2) {
			chance = 0.005;
		}
		double d = this.random.nextDouble() * 100.0D;
		if (d > chance) {
			return;
		}
		reward(event.getPlayer());
	}


	@Override
	public void onFish(PlayerFishEvent event, int level) {
		if(event.getState() != State.CAUGHT_FISH) {
			return;
		}
		double chance = 0.003;
		if (level == 2) {
			chance = 0.005;
		}
		double d = this.random.nextDouble() * 100.0D;
		if (d > chance) {
			return;
		}
		reward(event.getPlayer());
	}

	public void reward(Player player) {
		int key = randomKey();
		String command = COMMAND;
		command = command.replace("<level>", String.valueOf(key));
		command = command.replace("<player>", player.getName());
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
	}

	public int randomKey() {
		// Compute the total weight of all items together
		List<Double> weights = new ArrayList<>();
		weights.addAll(items.keySet());
		double totalWeight = 0.0d;
		for (Double i : weights) {
			totalWeight += i;
		}
		double randomIndex = 0;
		List<Integer> keys = new ArrayList<>();
		keys.addAll(items.values());
		double random = Math.random() * totalWeight;
		for (int i = 0; i < keys.size(); ++i) {
			random -= weights.get(i);
			if (random <= 0.0d) {
				randomIndex = i;
				break;
			}
		}
		return keys.get((int) randomIndex);
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
	public String getName() {
		return "Locksmith";
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
