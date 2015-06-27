package com.tadahtech.wrathpvp.enchants.menu.menus;

import com.tadahtech.wrathpvp.enchants.menu.Button;
import com.tadahtech.wrathpvp.enchants.menu.Menu;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Timothy Andis
 */
public class InputMenu extends Menu {

    private ItemStack confirm = new ItemStack(Material.WOOL, 1, DyeColor.LIME.getWoolData());
    private boolean close;

    public InputMenu() {
        super("Please insert your desired Item.");
        ItemMeta meta = confirm.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Click to proceed.");
        confirm.setItemMeta(meta);
        this.close = true;
    }

    @Override
    protected Button[] setUp() {
        Button[] buttons = new Button[9];
        buttons[8] = new Button(confirm, (player) ->{
            Inventory inventory = player.getOpenInventory().getTopInventory();
            ItemStack itemStack = null;
            for(ItemStack stack : inventory.getContents()) {
                if(stack == null || stack.getType() == Material.AIR) {
                    continue;
                }
                if(stack.getType() == Material.WOOL) {
                    continue;
                }
                itemStack = stack;
                break;
            }
            if(itemStack == null || itemStack.getType() == Material.AIR) {
                player.sendMessage(ChatColor.RED + "Please insert an item.");
                return;
            }
            this.close = false;
            new EnchantMenu(itemStack).open(player);
        });
        return buttons;
    }

    @Override
    public void onClose(Player player) {
        if(close) {
            Inventory inventory = player.getOpenInventory().getTopInventory();
            ItemStack itemStack = null;
            for(ItemStack stack : inventory.getContents()) {
                if(stack == null || stack.getType() == Material.AIR) {
                    continue;
                }
                if(stack.getType() == Material.WOOL) {
                    continue;
                }
                itemStack = stack;
                break;
            }
            if(itemStack != null) {
                if(player.getInventory().firstEmpty() < 0) {
                    player.getWorld().dropItem(player.getEyeLocation(), itemStack);
                } else {
                    player.getInventory().addItem(itemStack);
                }
            }
            guis.remove(player.getUniqueId());
            player.updateInventory();
        }
    }

    public boolean doClose() {
        return close;
    }
}
