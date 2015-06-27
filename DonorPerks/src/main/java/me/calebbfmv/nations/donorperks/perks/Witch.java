package me.calebbfmv.nations.donorperks.perks;

import me.calebbfmv.nations.donorperks.DonorPerks;
import me.calebbfmv.nations.donorperks.nms.CSkeleton;
import me.calebbfmv.nations.donorperks.nms.CustomEntityType;
import me.calebbfmv.nations.donorperks.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Timothy Andis
 */
public class Witch extends AbstractSpecialItem implements Listener {

    public Witch(ItemStack item) {
        super(item);
        register(this);
    }

    @Override
    public void onClick(Player player) {
        if(!player.hasPermission("donorperks.witch")) {
            noPermission(player);
            return;
        }
        List<Location> circle = Utils.circle(player, player.getEyeLocation(), 4, 1, false, false, 0);
        List<PlayerSkeleton> skeletons = new ArrayList<>();
        for(Player player1 : Bukkit.getOnlinePlayers()) {
            player1.sendMessage(ChatColor.RED + "The pits of hell have opened...");
            player1.playSound(player1.getLocation(), Sound.ENDERDRAGON_GROWL, 2.0F, 1.0F);
        }
        player.playSound(player.getLocation(), Sound.AMBIENCE_THUNDER, 3.0F, 1.613F);
        for(int i = 0; i < 5; i++) {
            Location location = circle.get(new Random().nextInt(circle.size() - 1));
            skeletons.add(new PlayerSkeleton(player, location));
        }
        player.setMetadata("skeletons", new FixedMetadataValue(DonorPerks.getInstance(), skeletons));
        new BukkitRunnable() {
            @Override
            public void run() {
                player.removeMetadata("skeletons", DonorPerks.getInstance());
                PlayerSkeleton playerSkeleton = skeletons.get(0);
                CSkeleton skeleton = playerSkeleton.skeleton;
                Location location = skeleton.getBukkitEntity().getLocation();
                player.sendMessage(org.bukkit.ChatColor.DARK_GRAY + "Hades calls us...");
                player.sendMessage(org.bukkit.ChatColor.DARK_GRAY + "It has been an honor serving you master.");
                location.getWorld().playSound(location, Sound.SKELETON_DEATH, 1.0F, 1.0F);
                skeleton.salute(player);
                skeletons.stream().forEach(s -> s.skeleton.remove(player));
            }
        }.runTaskLater(DonorPerks.getInstance(), 20L * 60);
        use(player);
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity dam = event.getDamager();
        if(!(dam instanceof Player)) {
            return;
        }
        Player player = (Player) dam;
        if(!player.hasMetadata("skeletons")) {
            return;
        }
        //noinspection unchecked
        List<PlayerSkeleton> skeletons = (List<PlayerSkeleton>) player.getMetadata("skeletons").get(0).value();
        skeletons.stream().forEach(golem-> golem.attack(entity));
    }

    @EventHandler
    public void onSunBurn(EntityCombustEvent event) {
        if(event.getEntity().hasMetadata("noBurn")) {
            event.setCancelled(true);
        }
    }

    protected class PlayerSkeleton {

        private CSkeleton skeleton;

        private PlayerSkeleton(Player player, Location location) {
            this.skeleton = (CSkeleton) CustomEntityType.SKELETON.spawnEntity(location);
            System.out.println(skeleton);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(skeleton == null || !skeleton.isAlive()) {
                        cancel();
                        return;
                    }
                    if(skeleton.getGoalTarget() == null) {
                        return;
                    }
                    if(skeleton.getGoalTarget().isAlive()) {
                        return;
                    }
                    skeleton.setGoalTarget(null);
                }
            }.runTaskTimer(DonorPerks.getInstance(), 1L, 1L);
        }

        public void attack(Entity entity) {
            EntityLiving nmsEntity = ((CraftLivingEntity) entity).getHandle();
            skeleton.setGoalTarget(nmsEntity, TargetReason.CUSTOM, false);
        }
    }

    @Override
    protected long getCooldown() {
        return 60 * 5;
    }
}
