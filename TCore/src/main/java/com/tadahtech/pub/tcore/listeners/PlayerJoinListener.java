package com.tadahtech.pub.tcore.listeners;

import com.tadahtech.pub.tcore.additions.tab.TabMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author Timothy Andis
 */
public class PlayerJoinListener implements Listener {

    private TabMenu tabMenu;

    public PlayerJoinListener(TabMenu tabMenu) {
        this.tabMenu = tabMenu;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        tabMenu.sendTo(player);
    }
}
