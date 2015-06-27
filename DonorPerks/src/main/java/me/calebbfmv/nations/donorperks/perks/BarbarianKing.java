package me.calebbfmv.nations.donorperks.perks;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Timothy Andis
 */
public class BarbarianKing extends AbstractSpecialItem implements Listener {

    public BarbarianKing(ItemStack item) {
        super(item);
        register(this);
    }

    @Override
    public void onClick(Player player) {

    }

    @Override
    protected long getCooldown() {
        return 15;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity dam = event.getDamager();
        if(!(entity instanceof Player)) {
            return;
        }
        if(!(dam instanceof Player)) {
            return;
        }
        Player player = (Player) entity;
        Player damager = (Player) dam;
        if(!damager.hasPermission("donorperks.barbarianking")) {
            noPermission(player);
            return;
        }
        if(damager.getItemInHand() == null || !damager.getItemInHand().isSimilar(getItem())) {
            return;
        }
        player.damage(16);
        player.setFireTicks(120);
        ItemStack[] armor = player.getInventory().getArmorContents();
        if(armor == null) {
            return;
        }
        player.getInventory().setArmorContents(null);
        damager.getInventory().addItem(armor);
        player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.0F);
        damager.playSound(damager.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
        damager.getItemInHand().setDurability((short) 0);
        damager.getItemInHand().setDurability((short) 0);
    }
}
