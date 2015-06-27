package me.calebbfmv.nations.donorperks.perks;

import me.calebbfmv.nations.donorperks.DonorPerks;
import me.calebbfmv.nations.donorperks.Settings;
import me.calebbfmv.nations.donorperks.utils.ParticleEffect;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.EntityArrow;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArrow;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Field;


/**
 * Created by Timothy Andis
 */
public class Archer extends AbstractSpecialItem implements Listener {

    private static PotionEffect EFFECT = new PotionEffect(PotionEffectType.BLINDNESS, 20 * 5, 2);
    private static ParticleEffect effect = ParticleEffect.CRIT_MAGIC;

    public Archer(ItemStack item) {
        super(item);
        register(this);
    }

    @Override
    public void onClick(Player player) {
        if(!player.hasPermission("donorperks.archer")) {
            noPermission(player);
            return;
        }
        player.setMetadata("archer", new FixedMetadataValue(DonorPerks.getInstance(), this));
        player.sendMessage(Settings.PREFIX + "You bow is ready master! Your next arrow will shoot fiery death!");
        player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0F, 1.0F);
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        Entity entity = event.getEntity();
        if(!(entity instanceof Player)) {
            return;
        }
        Player player = (Player) entity;
        ItemStack itemStack = player.getItemInHand();
        //for some odd reason, I am doing this check...
        if(itemStack == null || itemStack.getType() != Material.BOW) {
            return;
        }
        if(!itemStack.equals(this.getItem())) {
            return;
        }
        if(!this.canUse(player)) {
            return;
        }
        if(!player.hasMetadata("archer")) {
            return;
        }
        Entity projectile = event.getProjectile();
        if(projectile.getType() != EntityType.ARROW) {
            return;
        }
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                Arrow arrow = (Arrow) projectile;
                arrow.spigot().setDamage(10);
                Location location = arrow.getLocation();
                effect.display(null, location, Color.BLUE, 32, 0, 0, 0, 0, 1);
            }
        }.runTaskTimer(DonorPerks.getInstance(), 0L, 1L);
        projectile.setMetadata("archerArrow", new FixedMetadataValue(DonorPerks.getInstance(), task));
        use(player);
        player.getItemInHand().setDurability((short) 0);
    }

    @EventHandler
    public void onLand(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if(!(projectile instanceof Arrow)) {
            return;
        }
        Arrow arrow = (Arrow) projectile;
        if(!arrow.hasMetadata("archerArrow")) {
            return;
        }
        EntityArrow entityArrow = ((CraftArrow)arrow).getHandle();
        BukkitTask task = (BukkitTask) arrow.getMetadata("archerArrow").get(0).value();
        task.cancel();
        new BukkitRunnable() {
            @Override
            public void run() {

                try {
                    Field fieldX = net.minecraft.server.v1_8_R3.EntityArrow.class
                      .getDeclaredField("d");
                    Field fieldY = net.minecraft.server.v1_8_R3.EntityArrow.class
                      .getDeclaredField("e");
                    Field fieldZ = net.minecraft.server.v1_8_R3.EntityArrow.class
                      .getDeclaredField("f");

                    fieldX.setAccessible(true);
                    fieldY.setAccessible(true);
                    fieldZ.setAccessible(true);

                    int x = fieldX.getInt(entityArrow);
                    int y = fieldY.getInt(entityArrow);
                    int z = fieldZ.getInt(entityArrow);
                    if (y == -1) {
                        return;
                    }

                    TNTPrimed tntPrimed = arrow.getWorld().spawn(new Location(arrow.getWorld(), x, y, z), TNTPrimed.class);
                    tntPrimed.setFuseTicks(40);
                    tntPrimed.setMetadata("explosion", new FixedMetadataValue(DonorPerks.getInstance(), true));
                    arrow.removeMetadata("archerArrow", DonorPerks.getInstance());
                    arrow.remove();
                } catch (Exception ignored) {
                    arrow.remove();
                }
            }
        }.runTaskLater(DonorPerks.getInstance(), 0L);

    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity dam = event.getDamager();
        if(!(entity instanceof Player)) {
            return;
        }
        if(!(dam instanceof Arrow)) {
            return;
        }
        Arrow arrow = (Arrow) dam;
        Player player = (Player) entity;
        if(!arrow.hasMetadata("archerArrow")) {
            return;
        }
        BukkitTask task = (BukkitTask) arrow.getMetadata("archerArrow").get(0).value();
        task.cancel();
        arrow.removeMetadata("archerArrow", DonorPerks.getInstance());
        arrow.remove();
        player.addPotionEffect(EFFECT);
        player.setFireTicks(60);
        player.sendMessage(ChatColor.RED + "Sniped!");
        Player shooter = (Player) arrow.getShooter();
        shooter.sendMessage(Settings.PREFIX + "Hit! " + player.getName() + " has tasted your fiery wrath.");
    }

    @EventHandler
    public void onExplod(EntityExplodeEvent event) {
        Entity entity = event.getEntity();
        if(entity.hasMetadata("explosion")) {
            event.blockList().clear();
        }
    }

    @Override
    protected long getCooldown() {
        return 15;
    }
}
