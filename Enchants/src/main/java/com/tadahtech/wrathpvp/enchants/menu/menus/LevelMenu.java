package com.tadahtech.wrathpvp.enchants.menu.menus;

import com.tadahtech.wrathpvp.enchants.Enchants;
import com.tadahtech.wrathpvp.enchants.enchants.IEnchant;
import com.tadahtech.wrathpvp.enchants.menu.Button;
import com.tadahtech.wrathpvp.enchants.menu.Menu;
import com.tadahtech.wrathpvp.enchants.utils.ItemBuilder;
import com.tadahtech.wrathpvp.enchants.utils.WrappedEnchantment;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_7_R4.LocaleI18n;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Timothy Andis
 */
public class LevelMenu extends Menu {

	private ItemStack itemStack;
	private Enchantment enchantment;
	private int cost;
	private boolean close = true;

	public LevelMenu(ItemStack itemStack, Enchantment enchantment, int cost) {
		super("Pick a Level");
		this.itemStack = itemStack;
		this.enchantment = enchantment;
		this.cost = cost;
	}

	@Override
	public Button[] setUp() {
		Button[] buttons = new Button[18];
		int credit = this.cost;
		for(Enchantment enchantment : this.itemStack.getItemMeta().getEnchants().keySet()) {
			int cost = Enchants.getInstance().getCost(enchantment);
			if(enchantment instanceof IEnchant) {
				cost = ((IEnchant) enchantment).getCost();
			}
			credit -= cost;
		}
		boolean charge = credit > 0;
		int cost = credit <= 0 ? 0 : credit;
		int max = enchantment.getMaxLevel();
		for(int i = 0; i < max; i++) {
			int level = 1 + i;
			ItemBuilder builder = ItemBuilder.wrap(new ItemStack(Material.WOOL));
			String name = ChatColor.YELLOW + "Level " + level;
			builder.data(DyeColor.LIME.getWoolData());
			builder.name(name);
			builder.lore(ChatColor.RED + "Cost: " + ChatColor.GRAY + cost);
			buttons[i] = new Button(builder.build(), player -> {
				Economy economy = Enchants.getInstance().getEconomy();
				if(charge && economy != null) {
					if(!economy.has(player, cost)) {
						player.sendMessage(ChatColor.RED + "You cannot Afford this level!");
						return;
					}
					economy.withdrawPlayer(player, cost);
				}
				ItemBuilder itemBuilder = ItemBuilder.wrap(this.itemStack);
				List<WrappedEnchantment> enchantments = this.itemStack.getItemMeta().getEnchants().entrySet().stream().map(entry -> new WrappedEnchantment(entry.getKey(), entry.getValue())).collect(Collectors.toList());
				enchantments.add(new WrappedEnchantment(this.enchantment, level));
				itemBuilder.enchant(enchantments.toArray(new WrappedEnchantment[enchantments.size()]));
				List<String> lore = this.itemStack.getItemMeta().getLore();
				if(lore == null) {
					lore = new ArrayList<>();
				}
				if(this.enchantment instanceof IEnchant) {
					lore.add(ChatColor.GRAY + enchantment.getName() + " " + LocaleI18n.get("enchantment.level." + level));
				}
				itemBuilder.lore(lore.toArray(new String[lore.size()]));
				ItemStack stack = itemBuilder.build();
				stack.setDurability(this.itemStack.getDurability());
				player.getInventory().addItem(stack);
				close = false;
				player.closeInventory();
				player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
				player.sendMessage(ChatColor.GREEN + "Successfully added " + friendlyName(LevelMenu.this.enchantment) + " Level " + level + " to " + friendlyName(itemStack.getType().name()));
			});
		}
		return buttons;
	}

	@Override
	public void onClose(Player player) {
		guis.remove(player.getUniqueId());
		if(close) {
			if(player.getInventory().firstEmpty() < 0) {
				player.getWorld().dropItem(player.getEyeLocation(), itemStack);
			} else {
				player.getInventory().addItem(itemStack);
			}
		}
	}

	public static String friendlyName(String name) {
		StringBuilder builder = new StringBuilder();
		if (!name.contains("_")) {
			builder.append(name.substring(0, 1).toUpperCase()).append(name.substring(1).toLowerCase());
		} else {
			String[] str = name.split("_");
			for (int i = 0; i < str.length; i++) {
				String s = str[i];
				builder.append(s.substring(0, 1).toUpperCase()).append(s.substring(1).toLowerCase());
				if ((i + 1) != str.length) {
					builder.append(" ");
				}
			}
		}
		return builder.toString();

	}

	public String friendlyName(Enchantment enchantment) {
		if(enchantment instanceof IEnchant) {
			return enchantment.getName();
		}
		switch (enchantment.getId()) {
			default: return "Unknown";

			case 0: return "Protection";
			case 1: return "Fire Protection";
			case 2: return "Feather Falling";
			case 3: return "Blast Protection";
			case 4: return "Projectile Protection";
			case 5: return "Respiration";
			case 6: return "Aqua Affinity";
			case 7: return "Thorns";

			case 16: return "Sharpness";
			case 17: return "Smite";
			case 18: return "Bane of Arthropods";
			case 19: return "Knockback";
			case 20: return "Fire Aspect";
			case 21: return "Looting";

			case 32: return "Efficiency";
			case 33: return "Silk Touch";
			case 34: return "Unbreaking";
			case 35: return "Fortune";

			case 48: return "Power";
			case 49: return "Punch";
			case 50: return "Flame";
			case 51: return "Infinity";

			case 61: return "Luck of the Sea";
			case 62: return "Lure";
		}
	}
}
