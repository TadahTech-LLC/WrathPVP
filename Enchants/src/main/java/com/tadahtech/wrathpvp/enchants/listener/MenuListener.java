package com.tadahtech.wrathpvp.enchants.listener;

import com.tadahtech.wrathpvp.enchants.menu.Button;
import com.tadahtech.wrathpvp.enchants.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * @author Timothy Andis
 */
public class MenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        String name = event.getInventory().getName();
        Player player = (Player) event.getWhoClicked();
        Menu gui = Menu.get(player.getUniqueId());
        if (gui == null) {
            return;
        }
        if(!gui.getName().equalsIgnoreCase(name)) {
            return;
        }
        Button button = gui.getButton(event.getRawSlot());
        if (button == null) {
            return;
        }
        if (button.shouldEmptyClick()) {
            button.onClick();
        } else {
            button.onClick(player);
        }
        event.setCancelled(true);
        event.setResult(Event.Result.DENY);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        String name = event.getInventory().getName();
        Player player = (Player) event.getPlayer();
        Menu gui = Menu.get(player.getUniqueId());
        if (gui == null) {
            return;
        }
        if(!gui.getName().equalsIgnoreCase(name)) {
            return;
        }
        gui.onClose(player);
    }

}
