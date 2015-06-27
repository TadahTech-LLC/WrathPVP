package com.tadahtech.wrathpvp.enchants.enchants.enchants.block;

import com.tadahtech.wrathpvp.enchants.enchants.BlockEnchant;
import com.tadahtech.wrathpvp.enchants.utils.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Created by Timothy Andis
 */
public class InfusionEnchant extends BlockEnchant {

	public static InfusionEnchant INSTANCE = new InfusionEnchant();

	@Override
	public void onBreak(BlockBreakEvent event, int level) {
		if(event instanceof InfusionBlockBreakEvent) {
			return;
		}
		Block block = event.getBlock();
		Location location = block.getLocation().clone();
		Player player = event.getPlayer();
		for(int x = -level; x <= level; ++x) {
			player.playSound(player.getLocation(), Sound.EXPLODE, 1.0F, 1.0F);
			for(int y = -level; y <= level; ++y) {
				for(int z = -level; z <= level; ++z) {
					Location blockLoc = location.clone().add(x, y, z);
					Block blockAt = blockLoc.getBlock();
					if(blockAt == null) {
						continue;
					}
					Material type = blockAt.getType();
					if(type == Material.AIR) {
						continue;
					}
					if(type == Material.MOB_SPAWNER) {
						continue;
					}
					if(type == Material.BEDROCK) {
						continue;
					}
					InfusionBlockBreakEvent breakEvent = new InfusionBlockBreakEvent(blockAt, player);
					PLUGIN.getServer().getPluginManager().callEvent(breakEvent);
					if(!breakEvent.isCancelled()) {
						if(block.hasMetadata("InfusionIgnoreDrops")) {
							block.removeMetadata("InfusionIgnoreDrops", PLUGIN);
						} else {
							vanillaBreakMethods(event.getPlayer(), blockLoc, true, true, true, -1);
							blockAt.setTypeIdAndData(0, (byte) 0, false);
							Bukkit.getScheduler().runTaskAsynchronously(PLUGIN, () -> {
								try {
									ParticleEffect.sendToLocation(ParticleEffect.LARGE_EXPLODE, blockLoc.add(0.0D, 0.5D, 0.0D), 0.0F, 0.0F, 0.0F, 0.025F, 1);
								} catch (Exception e) {
//									e.printStackTrace();
								}
							});
						}
					}
				}
			}
		}
	}

	public static class InfusionBlockBreakEvent extends BlockBreakEvent {

		public InfusionBlockBreakEvent(Block theBlock, Player player) {
			super(theBlock, player);
		}
	}
}
