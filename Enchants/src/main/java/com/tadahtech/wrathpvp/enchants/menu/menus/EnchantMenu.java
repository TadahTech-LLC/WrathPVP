package com.tadahtech.wrathpvp.enchants.menu.menus;

import com.tadahtech.wrathpvp.enchants.Enchants;
import com.tadahtech.wrathpvp.enchants.enchants.IEnchant;
import com.tadahtech.wrathpvp.enchants.menu.Button;
import com.tadahtech.wrathpvp.enchants.menu.Menu;
import com.tadahtech.wrathpvp.enchants.utils.GlowEnchant;
import com.tadahtech.wrathpvp.enchants.utils.ItemBuilder;
import com.tadahtech.wrathpvp.enchants.utils.WrappedEnchantment;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Timothy Andis
 */
public class EnchantMenu extends Menu {

	private List<Enchantment> ENCHANTS = new ArrayList<>();
	private ItemStack itemStack;
	private boolean doClose = true;

	public EnchantMenu(ItemStack itemStack) {
		super("Pick an Enchantment");
		this.itemStack = itemStack;
		for (Enchantment enchantment : Enchantment.values()) {
			if (enchantment.getName().equalsIgnoreCase("glow")) {
				continue;
			}
			if (enchantment instanceof IEnchant) {
				IEnchant iEnchant = (IEnchant) enchantment;
				if (iEnchant.canEnchant(itemStack)) {
					ENCHANTS.add(enchantment);
				}
				continue;
			}
			if (enchantment.canEnchantItem(itemStack)) {
				ENCHANTS.add(enchantment);
			}
		}
	}

	@Override
	protected Button[] setUp() {
		Button[] buttons = new Button[45];
		int c = 5;
		int o = 0;
		for (int i = 4; i < (45); i += 9) {
			ItemStack itemStack = ItemBuilder.wrap(new ItemStack(Material.STAINED_GLASS_PANE))
			  .amount(1)
			  .data(DyeColor.BLACK.getWoolData())
			  .name(" ")
			  .build();
			buttons[i] = create(itemStack);
		}
		buttons[22] = create(this.itemStack.clone());
		for (Enchantment enchantment : ENCHANTS) {
			int cost, slot;
			if (enchantment instanceof IEnchant) {
				slot = c;
				c++;
				if (c >= 9) {
					c = 14;
				}
				if (c >= 18) {
					c = 23;
				}
				if (c >= 27) {
					c = 32;
				}
				if (c >= 36) {
					c = 41;
				}
				cost = ((IEnchant) enchantment).getCost();
			} else {
				slot = o;
				o++;
				if (o >= 4) {
					o = 9;
				}
				if (o >= 13) {
					o = 18;
				}
				if (o >= 23) {
					o = 27;
				}
				if (o >= 32) {
					o = 36;
				}
				cost = Enchants.getInstance().getCost(enchantment);
			}
			ItemBuilder book = ItemBuilder.wrap(new ItemStack(Material.BOOK))
			  .name(ChatColor.YELLOW + "Enchanted Book")
			  .enchant(new WrappedEnchantment(GlowEnchant.getGlowEnchant()))
			  .lore(ChatColor.GRAY + friendlyName(enchantment), " ",
				ChatColor.RED + "Cost: " + ChatColor.GRAY + cost);
			buttons[slot] = new Button(book.build(), player -> {
				doClose = false;
				LevelMenu levelMenu = new LevelMenu(itemStack, enchantment, cost);
				levelMenu.open(player);
//				new LevelMenu(itemStack, enchantment, cost).open(player);
			});
		}
		return buttons;
	}

	@Override
	public void onClose(Player player) {
		if (doClose) {
			if (itemStack == null) {
				return;
			}
			if (player.getInventory().firstEmpty() < 0) {
				player.getWorld().dropItem(player.getEyeLocation(), itemStack);
			} else {
				player.getInventory().addItem(itemStack);
			}
			guis.remove(player.getUniqueId());
		}
	}

	public String friendlyName(Enchantment enchantment) {
		if (enchantment instanceof IEnchant) {
			return enchantment.getName();
		}
		switch (enchantment.getId()) {
			default:
				return "Unknown";

			case 0:
				return "Protection";
			case 1:
				return "Fire Protection";
			case 2:
				return "Feather Falling";
			case 3:
				return "Blast Protection";
			case 4:
				return "Projectile Protection";
			case 5:
				return "Respiration";
			case 6:
				return "Aqua Affinity";
			case 7:
				return "Thorns";

			case 16:
				return "Sharpness";
			case 17:
				return "Smite";
			case 18:
				return "Bane of Arthropods";
			case 19:
				return "Knockback";
			case 20:
				return "Fire Aspect";
			case 21:
				return "Looting";

			case 32:
				return "Efficiency";
			case 33:
				return "Silk Touch";
			case 34:
				return "Unbreaking";
			case 35:
				return "Fortune";

			case 48:
				return "Power";
			case 49:
				return "Punch";
			case 50:
				return "Flame";
			case 51:
				return "Infinity";

			case 61:
				return "Luck of the Sea";
			case 62:
				return "Lure";
		}
	}

	public ItemStack getItem() {
		return itemStack;
	}
}
