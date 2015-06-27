package com.tadahtech.wrath.stattracker.block;

import com.tadahtech.wrath.stattracker.ItemSetting;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Timothy Andis
 */
public class PickaxeListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getItemInHand();
        if(itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }
        String type = itemStack.getType().name();
        if(!type.toLowerCase().contains("axe") && !type.toLowerCase().contains("spade")) {
            return;
        }
        MiningSetting setting = (MiningSetting) ItemSetting.get(itemStack);
        setting.incrementXp();
        itemStack = setting.rebuild();
        player.setItemInHand(itemStack);
    }
}
