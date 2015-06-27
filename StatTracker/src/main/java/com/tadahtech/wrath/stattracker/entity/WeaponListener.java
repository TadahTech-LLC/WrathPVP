package com.tadahtech.wrath.stattracker.entity;

import com.tadahtech.wrath.stattracker.ItemSetting;
import com.tadahtech.wrath.stattracker.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Timothy Andis
 */
public class WeaponListener implements Listener {

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if(!(entity instanceof Player)) {
            return;
        }
        Player killer = entity.getKiller();
        if(killer == null) {
            return;
        }
        ItemStack itemStack = killer.getItemInHand();
        if(itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }
        if(!Utils.isWeapon(itemStack)) {
            return;
        }
        WeaponSetting setting = (WeaponSetting) ItemSetting.get(itemStack);
        setting.incrementXp();
        setting.addLastKilled((Player) entity);
        itemStack = setting.rebuild();
        killer.setItemInHand(itemStack);
    }
}
