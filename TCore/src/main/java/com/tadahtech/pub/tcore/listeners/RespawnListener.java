package com.tadahtech.pub.tcore.listeners;

import com.tadahtech.pub.tcore.TCore;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Timothy Andis
 */
public class RespawnListener implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                Location location = player.getLocation();
                if(location == null) {
                    return;
                }
                player.teleport(location.clone().add(0.1, 0, 0.1));
            }
        }.runTaskLater(TCore.getInstance(), 20L * 3);
    }

}
