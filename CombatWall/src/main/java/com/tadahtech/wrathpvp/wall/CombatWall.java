package com.tadahtech.wrathpvp.wall;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.tadahtech.wrathpvp.wall.utils.Utils;
import me.sirreason.combat.TagHandler;
import me.sirreason.combat.WrathCombat;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.*;

import java.util.*;

/**
 * Created by Timothy Andis
 */
public class CombatWall extends JavaPlugin implements Listener {

	private List<UUID> shown = new ArrayList<>();
	private Map<UUID, List<Location>> locations = new HashMap<>();
	private Map<UUID, List<Location>> lookingAt = new HashMap<>();

	@Override
	public void onEnable() {
		WrathCombat wrathCombat = WrathCombat.getPlugin(WrathCombat.class);
		TagHandler handler = wrathCombat.getTagHandler();
		getServer().getPluginManager().registerEvents(this, this);
		new BukkitRunnable() {
			@Override
			public void run() {
				Utils.getOnlinePlayers().stream().filter(player -> !handler.isTagged(player)).filter(player -> shown.contains(player.getUniqueId())).forEach(player -> {
					shown.remove(player.getUniqueId());
					if (locations.get(player.getUniqueId()) != null) {
						for (Location location : locations.remove(player.getUniqueId())) {
							player.sendBlockChange(location, Material.AIR, (byte) 0);
						}
					}
				});
			}
		}.runTaskTimer(this, 20L, 20L);
	}

	public void showRegions(Player player) {
		WorldGuardPlugin plugin = WorldGuardPlugin.inst();
		RegionManager regionManager = plugin.getRegionManager(player.getWorld());
		for (ProtectedRegion region : regionManager.getRegions().values()) {
			ApplicableRegionSet set = regionManager.getApplicableRegions(region);
			State state = set.queryState(null, DefaultFlag.PVP);
			if (state == State.ALLOW) {
				continue;
			}
			BlockVector max = region.getMaximumPoint();
			BlockVector min = region.getMinimumPoint();
			int maxX = max.getBlockX();
			int maxZ = max.getBlockZ();
			int minX = min.getBlockX();
			int minZ = min.getBlockZ();
			int maxY = min.getBlockY() + 6;
			List<Location> locationList = new ArrayList<>();
			for (int x = minX; x <= maxX; x++) {
				for (int y = min.getBlockY(); y < maxY; y++) {
					for (int z = minZ; z <= maxZ; z++) {
						Location location = null;
						if (x == minX || x == maxX) {
							location = new Location(player.getWorld(), x, y, z);
						} else {
							if (z == minZ || z == maxZ) {
								location = new Location(player.getWorld(), x, y, z);
							}
						}
						if (location == null) {
							continue;
						}
						double distance = Math.sqrt(NumberConversions.square(location.getX() - player.getLocation().getX()) + (NumberConversions.square(location.getZ() - player.getLocation().getZ())));
						if (distance > 5) {
							continue;
						}
						if (location.getBlock().getType() != Material.AIR) {
							continue;
						}
						locationList.add(location);
						player.sendBlockChange(location, Material.STAINED_GLASS, DyeColor.RED.getWoolData());
					}
				}
			}
			this.locations.put(player.getUniqueId(), locationList);
			this.lookingAt.put(player.getUniqueId(), locationList);
			this.shown.add(player.getUniqueId());
			new BukkitRunnable() {
				@Override
				public void run() {
					for (Location location : locationList) {
						player.sendBlockChange(location, Material.AIR, (byte) 0);
					}
				}
			}.runTaskLater(this, 60L);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onDamage(EntityDamageByEntityEvent event) {
		Entity entity = event.getEntity();
		if (!(entity instanceof Player)) {
			return;
		}
		Entity dam = event.getDamager();
		if (!(dam instanceof Player)) {
			return;
		}
		Player player = (Player) entity;
		Player damager = (Player) dam;
		new BukkitRunnable() {
			@Override
			public void run() {
				WrathCombat wrathCombat = WrathCombat.getPlugin(WrathCombat.class);
				TagHandler handler = wrathCombat.getTagHandler();
				if (handler.isTagged(player)) {
					showRegions(player);
				}
				if (handler.isTagged(damager)) {
					showRegions(damager);
				}
			}
		}.runTaskLater(this, 2L);

	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		WrathCombat wrathCombat = WrathCombat.getPlugin(WrathCombat.class);
		TagHandler handler = wrathCombat.getTagHandler();
		if (!handler.isTagged(player)) {
			return;
		}
		showRegions(player);
		int x = player.getLocation().getBlockX();
		int y = (int) player.getLocation().getY();
		int z = (int) player.getLocation().getZ();
		boolean has = false;
		Location location = player.getLocation().clone();
		for(int i = 5; i > 0; i--) {
			Block block = location.getBlock();
			if(block.getRelative(BlockFace.DOWN).getType() == Material.BEDROCK) {
				has = true;
			}
			location.setY(y - i);
		}
		if(has) {
			org.bukkit.util.Vector vector = player.getEyeLocation().getDirection().multiply(-1).multiply(1.6);
			vector.setY(0);
			player.setVelocity(vector);
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		WrathCombat wrathCombat = WrathCombat.getPlugin(WrathCombat.class);
		TagHandler handler = wrathCombat.getTagHandler();
		if (handler.isTagged(player)) {
			if (locations.get(player.getUniqueId()) != null) {
				event.setUseInteractedBlock(Result.DENY);
				event.setCancelled(true);
			}
			showRegions(player);
		}
	}

}
