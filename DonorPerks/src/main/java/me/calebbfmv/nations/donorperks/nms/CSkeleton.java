package me.calebbfmv.nations.donorperks.nms;

import me.calebbfmv.nations.donorperks.DonorPerks;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * Created by Timothy Andis
 */
public class CSkeleton extends EntitySkeleton implements CustomEntity {

    public CSkeleton(World world) {
        super(world);
        NMS.clearGoals(goalSelector, targetSelector);
        this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this, 1.0D, true));
        this.goalSelector.a(0, new PathfinderGoalMoveTowardsTarget(this, 1.0D, 1F));
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(100.0D);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.25D);
        setCustomNameVisible(true);
        setCustomName(ChatColor.DARK_GRAY + "Undead Warrior");
        this.setEquipment(0, new ItemStack(Items.GOLDEN_SWORD));
        getBukkitEntity().setMetadata("noBurn", new FixedMetadataValue(DonorPerks.getInstance(), true));
        setSkeletonType(1);
    }

    @Override
    public int getSkeletonType() {
        return SkeletonType.WITHER.getId();
    }

    @Override
    public void g(double d, double d2, double d1){

    }

    @Override
    public void salute(Player player) {
        EntityHuman human = ((CraftPlayer) player).getHandle();
        Location location = this.getBukkitEntity().getLocation();
        location.getWorld().playSound(location, Sound.SKELETON_DEATH, 1.0F, 1.0F);
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                Location location = getBukkitEntity().getLocation();
                getControllerLook().a(human.locX, human.locY + (double) human.getHeadHeight(), human.locZ, 10.0F, (float) bQ());
                for(int i = 0; i < 5; i++) {
                    location.getWorld().spigot().playEffect(location, Effect.FLAME);
                }
            }
        }.runTaskTimer(DonorPerks.getInstance(), 0l, 1L);
        new BukkitRunnable() {
            @Override
            public void run() {
                getBukkitEntity().remove();
                for(int i = 0; i < 5; i++) {
                    location.getWorld().strikeLightningEffect(location);
                }
                player.playSound(location, Sound.AMBIENCE_THUNDER, 7.0F, 1.0F);
                task.cancel();
            }
        }.runTaskLater(DonorPerks.getInstance(), 20 * 3L);
    }

    public void remove(Player player) {
        EntityHuman human = ((CraftPlayer) player).getHandle();
        Location location = this.getBukkitEntity().getLocation();
        location.getWorld().playSound(location, Sound.SKELETON_DEATH, 1.0F, 1.0F);
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                Location location = getBukkitEntity().getLocation();
                getControllerLook().a(human.locX, human.locY + (double) human.getHeadHeight(), human.locZ, 10.0F, (float) bQ());
                for(int i = 0; i < 15; i++) {
                    location.getWorld().spigot().playEffect(location, Effect.MOBSPAWNER_FLAMES);
                }
            }
        }.runTaskTimer(DonorPerks.getInstance(), 0l, 1L);
        new BukkitRunnable() {
            @Override
            public void run() {
                getBukkitEntity().remove();
                for(int i = 0; i < 5; i++) {
                    location.getWorld().strikeLightningEffect(location);
                }
                player.playSound(location, Sound.AMBIENCE_THUNDER, 7.0F, 1.0F);
                task.cancel();
            }
        }.runTaskLater(DonorPerks.getInstance(), 20 * 3L);
    }
}
