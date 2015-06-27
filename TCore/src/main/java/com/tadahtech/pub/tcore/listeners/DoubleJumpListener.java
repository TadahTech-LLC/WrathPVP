package com.tadahtech.pub.tcore.listeners;

import com.tadahtech.pub.tcore.TCore;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Timothy Andis
 */
public class DoubleJumpListener implements Listener {

    private static List<UUID> candoublejump = new ArrayList<>();

    @EventHandler
    public void onFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        if (!candoublejump.contains(player.getUniqueId())) {
            return;
        }
        event.setCancelled(true);
        player.setAllowFlight(false);
        player.setFlying(false);
        Vector vector = player.getEyeLocation().getDirection();
        vector.multiply(1.1).setY(0.9);
        player.setVelocity(vector);
        candoublejump.remove(player.getUniqueId());
        player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 1.0F, 1.0F);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (candoublejump.contains(player.getUniqueId())) {
                    cancel();
                    return;
                }
                dj(player);
            }
        }.runTaskTimer(TCore.getInstance(), 0L, 1L);
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().setAllowFlight(true);
        candoublejump.add(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if(candoublejump.contains(event.getPlayer().getUniqueId())) {
            candoublejump.remove(event.getPlayer().getUniqueId());
        }
    }

    public static void dj(Player p) {
        if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isSolid() || p.getLocation().subtract(-0.3D, 0.0D, 0.0D).getBlock().getRelative(BlockFace.DOWN).getType().isSolid() || p.getLocation().subtract(0.3D, 0.0D, 0.0D).getBlock().getRelative(BlockFace.DOWN).getType().isSolid() || p.getLocation().subtract(0.0D, 0.0D, 0.3D).getBlock().getRelative(BlockFace.DOWN).getType().isSolid() || p.getLocation().subtract(0.0D, 0.0D, -0.3D).getBlock().getRelative(BlockFace.DOWN).getType().isSolid() || p.getLocation().subtract(0.3D, 0.0D, 0.3D).getBlock().getRelative(BlockFace.DOWN).getType().isSolid() || p.getLocation().subtract(0.3D, 0.0D, -0.3D).getBlock().getRelative(BlockFace.DOWN).getType().isSolid() || p.getLocation().subtract(-0.3D, 0.0D, 0.3D).getBlock().getRelative(BlockFace.DOWN).getType().isSolid() || p.getLocation().subtract(-0.3D, 0.0D, -0.3D).getBlock().getRelative(BlockFace.DOWN).getType().isSolid() &&p.getGameMode() != GameMode.CREATIVE && !candoublejump.contains(p.getUniqueId()) && !p.getAllowFlight()){
            p.setAllowFlight(true);
            candoublejump.add(p.getUniqueId());
        }
    }
}
