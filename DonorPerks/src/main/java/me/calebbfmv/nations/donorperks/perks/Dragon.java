package me.calebbfmv.nations.donorperks.perks;

import me.calebbfmv.nations.donorperks.DonorPerks;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * Created by Timothy Andis
 */
public class Dragon extends AbstractSpecialItem implements Listener {

    public Dragon(ItemStack item) {
        super(item);
        register(this);
    }

    @Override
    public void onClick(Player player) {
        if(!player.hasPermission("donorperks.dragon")) {
            noPermission(player);
            return;
        }
        player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 1.0F, 1.0F);
        player.setAllowFlight(true);
        player.setVelocity(new Vector(0, 1.6, 1));
        player.setFlying(true);
        player.setMetadata("dragon", new FixedMetadataValue(DonorPerks.getInstance(), 0));
        new BukkitRunnable() {
            private int total = 28;
            @Override
            public void run() {
                if(total <= 0) {
                    cancel();
                    if(player.isFlying()) {
                        player.setFlying(false);
                    }
                    player.setAllowFlight(false);
                    player.removeMetadata("dragon", DonorPerks.getInstance());
                    return;
                }
                Vector direction = player.getEyeLocation().getDirection().multiply(2);
                Fireball fireball = player.getWorld().spawn(player.getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()), Fireball.class);
                fireball.setShooter(player);
                fireball.setDirection(direction);
                fireball.setMetadata("dragon", new FixedMetadataValue(DonorPerks.getInstance(), 3.0D));
                total--;
            }
        }.runTaskTimer(DonorPerks.getInstance(), 0L, 5L);
        use(player);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity dam = event.getDamager();
        if(!(dam instanceof Fireball)) {
            return;
        }
        Fireball fireball = (Fireball) dam;
        if(!fireball.hasMetadata("fireball")) {
            return;
        }
        double mult = fireball.getMetadata("fireball").get(0).asDouble();
        fireball.removeMetadata("fireball", DonorPerks.getInstance());
        fireball.remove();
        entity.setFireTicks(60);
        event.setDamage(event.getFinalDamage() * mult);
    }

    @EventHandler
    public void onAttadck(EntityDamageByEntityEvent event) {
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
        if(player.hasMetadata("dragon")) {
            event.setCancelled(true);
            return;
        }
        if(!damager.hasMetadata("dragon")) {
            return;
        }
        event.setDamage(event.getFinalDamage() * 3);
    }
    @Override
    protected long getCooldown() {
        return 15;
    }

}
