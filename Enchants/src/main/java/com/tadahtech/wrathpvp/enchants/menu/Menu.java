package com.tadahtech.wrathpvp.enchants.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Tim [calebbfmv]
 *         Created by Tim [calebbfmv] on 10/19/2014.
 */
public abstract class Menu {

    protected static final Button[] EMPTY = new Button[45];
    protected static Map<UUID, Menu> guis = new HashMap<>();
    private String name;
    private Button[] buttons;
    protected int size;

    public Menu(String name) {
        this.name = name;
        name = getName();
        this.buttons = EMPTY;
    }

    public static Menu get(UUID name) {
        return guis.get(name);
    }


    protected Button create(ItemStack item) {
        return new Button(item, () -> {

        });
    }

    public String getName() {
        return ChatColor.translateAlternateColorCodes('&', name);
    }

    public void open(Player player) {
        this.setButtons(setUp());
        guis.put(player.getUniqueId(), this);
        int size = (this.buttons.length + 8) / 9 * 9;
        Inventory inventory = Bukkit.createInventory(player, size, getName());
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == null) {
                continue;
            }
            ItemStack item = buttons[i].getItemStack();
            inventory.setItem(i, item);
        }
        player.openInventory(inventory);
    }

    protected abstract Button[] setUp();

    public Button[] getButtons() {
        return buttons;
    }

    public void setButtons(Button[] buttons) {
        this.buttons = buttons;
    }

    public Button getButton(int slot) {
        try {
            return buttons[slot];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public void setButton(int slot, Button button, Player player) {
        try {
            buttons[slot] = button;
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        update(player);
    }

    public void update(Player player) {
        InventoryView view = player.getOpenInventory();
        if (view == null) {
            return;
        }
        if (!view.getTitle().equalsIgnoreCase(this.getName())) {
            return;
        }
        Inventory inventory = view.getTopInventory();
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == null) {
                continue;
            }
            ItemStack item = buttons[i].getItemStack();
            inventory.setItem(i, item);
        }
    }

    public void setTitle(String title) {
        this.name = title;
    }

    public abstract void onClose(Player player);

}
