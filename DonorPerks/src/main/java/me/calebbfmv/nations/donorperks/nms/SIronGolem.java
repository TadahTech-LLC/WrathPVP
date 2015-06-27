package me.calebbfmv.nations.donorperks.nms;

import me.calebbfmv.nations.donorperks.DonorPerks;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * Created by Timothy Andis
 */
public class SIronGolem extends EntityIronGolem implements CustomEntity {

    public SIronGolem(World world) {
        super(world);
        NMS.clearGoals(goalSelector, targetSelector);
        this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this, 1.0D, true));
        this.goalSelector.a(0, new PathfinderGoalMoveTowardsTarget(this, 1.0D, 1F));
        this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(100.0D);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.25D);
        setCustomNameVisible(true);
        setCustomName(ChatColor.GOLD + "Golem Warrior");

    }

    @Override
    public void g(double d, double d2, double d1){

    }

    @Override
    public void salute(Player player) {
        EntityHuman human = ((CraftPlayer) player).getHandle();
        Location location = this.getBukkitEntity().getLocation();
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                getControllerLook().a(human.locX, human.locY + (double) human.getHeadHeight(), human.locZ, 10.0F, (float) bQ());
                for(int i = 0; i < 50; i++) {
                    location.getWorld().spigot().playEffect(location, Effect.EXPLOSION);
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
                player.sendMessage(ChatColor.DARK_GRAY + "It has been an honor serving you master.");
                location.getWorld().playSound(location, Sound.IRONGOLEM_DEATH, 1.0F, 1.0F);
                task.cancel();
            }
        }.runTaskLater(DonorPerks.getInstance(), 20 * 3L);
    }
}
