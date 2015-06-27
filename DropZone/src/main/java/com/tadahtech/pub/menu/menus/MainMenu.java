package com.tadahtech.pub.menu.menus;

import com.tadahtech.pub.menu.Button;
import com.tadahtech.pub.menu.Menu;
import com.tadahtech.pub.tier.Tier;
import com.tadahtech.pub.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author Timothy Andis
 */
public class MainMenu extends Menu {

    public MainMenu() {
        super("DropZone Rewards");
    }

    @Override
    protected Button[] setUp() {
        Button[] buttons = new Button[27];
        ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLUE.getWoolData());
        ItemMeta meta = pane.getItemMeta();
        meta.setDisplayName(" ");
        pane.setItemMeta(meta);
        for (int i = 0; i < 9; i++) {
            buttons[i] = create(pane);
            if (i == 0 || i == 8) {
                buttons[i + 9] = create(pane);
            }
            buttons[i + 18] = create(pane);
        }
        Tier tier1 = Tier.get(1);
        Tier tier2 = Tier.get(2);
        Tier tier3 = Tier.get(3);
        ItemBuilder builder = ItemBuilder.wrap(new ItemStack(Material.CHEST));
        builder.name(ChatColor.YELLOW + "Tier: 1");
        buttons[11] = new Button(builder.build(), player -> {
            new TierMenu(tier1).open(player);
        });
        builder = ItemBuilder.wrap(new ItemStack(Material.CHEST));
        builder.name(ChatColor.YELLOW + "Tier: 2");
        buttons[13] = new Button(builder.build(), player -> {
            new TierMenu(tier2).open(player);
        });
        builder = ItemBuilder.wrap(new ItemStack(Material.CHEST));
        builder.name(ChatColor.YELLOW + "Tier: 3");
        buttons[15] = new Button(builder.build(), player -> {
            new TierMenu(tier3).open(player);
        });
        return buttons;
    }
}
