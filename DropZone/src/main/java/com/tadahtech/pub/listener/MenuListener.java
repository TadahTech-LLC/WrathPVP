package com.tadahtech.pub.listener;

import com.tadahtech.pub.menu.Button;
import com.tadahtech.pub.menu.Menu;
import com.tadahtech.pub.tier.Tier;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author Timothy Andis
 */
public class MenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        String name = event.getInventory().getName();
        Player player = (Player) event.getWhoClicked();
        Menu gui = Menu.get(name);
        if (gui == null) {
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
        Inventory inventory = event.getInventory();
        Player player = (Player) event.getPlayer();
        Location location = player.getLocation();
        World world = location.getWorld();
        if (inventory.getName().contains(ChatColor.GOLD.toString() + ChatColor.BOLD + "Loot")) {
            String name = inventory.getName();
            name = name.split(": ")[1];
            Tier tier = Tier.get(Integer.parseInt(name));
            for (int i = 0; i < inventory.getContents().length; i++) {
                ItemStack itemStack = inventory.getItem(i);
                if (itemStack == null || itemStack.getType() == Material.AIR) {
                    continue;
                }
                Location drop = location.clone().add(Math.random() * 5, 0, Math.random() * 5);
                world.dropItemNaturally(drop, itemStack);
            }
            tier.clearSet();
        }
    }

}
