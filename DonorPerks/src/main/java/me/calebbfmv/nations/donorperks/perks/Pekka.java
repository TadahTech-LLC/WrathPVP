package me.calebbfmv.nations.donorperks.perks;

import me.calebbfmv.nations.donorperks.DonorPerks;
import me.calebbfmv.nations.donorperks.Settings;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

/**
 * Created by Timothy Andis
 */
public class Pekka extends AbstractSpecialItem implements Listener {

    private static Random random = new Random();

    public Pekka(ItemStack item) {
        super(item);
        register(this);
    }

    @Override
    public void onClick(Player player) {
        player.sendMessage(Settings.PREFIX + "Pekka's Hyde Activated....");
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
        Player damager = (Player) dam;
        Player player = (Player) entity;
        if(event.isCancelled()) {
            ItemStack itemStack = damager.getItemInHand();
            if(itemStack != null) {
                itemStack.setDurability((short) 0);
                damager.setItemInHand(itemStack);
            }
            return;
        }
        if(player.isBlocking()) {
            if(player.getItemInHand() != null && player.getItemInHand().isSimilar(this.getItem())) {
                if(!player.hasPermission("donorperks.pekka")) {
//                    noPermission(player);
                    return;
                }
                event.setCancelled(true);
                ItemStack itemStack = damager.getItemInHand();
                if(itemStack != null) {
                    itemStack.setDurability((short) 0);
                    damager.setItemInHand(itemStack);
                }
                return;
            }
        }
        ItemStack itemStack = damager.getItemInHand();
        if(itemStack == null || !itemStack.isSimilar(this.getItem())) {
            return;
        }
        itemStack.setDurability((short) 0);
        damager.setItemInHand(itemStack);
        if(!damager.hasPermission("donorperks.pekka")) {
            noPermission(damager);
            return;
        }
        int next = random.nextInt(4);
        ItemStack[] armor = player.getInventory().getArmorContents();
        for(int i = 0; i < next; i++) {
            ItemStack armorItem = armor[i];
            if(armorItem == null || armorItem.getType() == Material.AIR) {
                continue;
            }
            armor[i] = null;
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.0F);
            damager.playSound(player.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.0F);
            damager.sendMessage(Settings.PREFIX + "Found a chink in " + player.getName() + "'s armor!");
        }
        event.setDamage(event.getFinalDamage() * 10);
        player.getInventory().setArmorContents(armor);
        new BukkitRunnable() {
            @Override
            public void run() {
                itemStack.setDurability((short) 0);
                damager.setItemInHand(itemStack);
            }
        }.runTaskLater(DonorPerks.getInstance(), 3L);
    }

    @Override
    protected long getCooldown() {
        return 15;
    }
}
