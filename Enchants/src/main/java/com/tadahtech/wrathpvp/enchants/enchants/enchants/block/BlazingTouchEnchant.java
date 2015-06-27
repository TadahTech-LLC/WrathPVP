package com.tadahtech.wrathpvp.enchants.enchants.enchants.block;

import com.tadahtech.wrathpvp.enchants.enchants.BlockEnchant;
import com.tadahtech.wrathpvp.enchants.enchants.enchants.block.InfusionEnchant.InfusionBlockBreakEvent;
import com.tadahtech.wrathpvp.enchants.smelting.SmeltableInfo;
import com.tadahtech.wrathpvp.enchants.smelting.SmeltingMap;
import com.tadahtech.wrathpvp.enchants.utils.ParticleEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * Created by Timothy Andis
 */
public class BlazingTouchEnchant extends BlockEnchant {

	public static BlazingTouchEnchant INSTANCE = new BlazingTouchEnchant();

	@Override
	public void onBreak(BlockBreakEvent event, int level) {
		SmeltableInfo smelting_result = SmeltingMap.getSmeletable(event.getBlock().getType());
		event.getBlock().setType(Material.AIR);
		if(!event.getBlock().hasMetadata("duplicate_block")) {
			event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(smelting_result.getMaterial(), 1));
			vanillaBreakMethods(event.getPlayer(), event.getBlock().getLocation(), false, true, true, smelting_result.getExp());
		} else {
			event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(smelting_result.getMaterial(), 2));
			vanillaBreakMethods(event.getPlayer(), event.getBlock().getLocation(), false, true, true, smelting_result.getExp() * 2);
			event.getBlock().removeMetadata("duplicate_block", PLUGIN);
		}

		if(event instanceof InfusionBlockBreakEvent) {
			event.getBlock().setMetadata("InfusionIgnoreDrops", new FixedMetadataValue(PLUGIN, "InfusionIgnoreDrops"));
		}

		try {
			ParticleEffect.sendToLocation(ParticleEffect.LARGE_SMOKE, event.getBlock().getLocation().add(0.0D, 0.5D, 0.0D), 1.0F, 0.5F, 0.5F, 0.0F, 10);
		} catch (Exception var6) {
			var6.printStackTrace();
		}

		event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.FIRE, 1.0F, 1.0F);
	}

	@Override
	public String getName() {
		return "Blazing Touch";
	}
}
