package me.calebbfmv.nations.donorperks.perks.giant;

import me.calebbfmv.nations.donorperks.DonorPerks;
import me.calebbfmv.nations.donorperks.nms.CustomEntityType;
import me.calebbfmv.nations.donorperks.nms.DissipateTask;
import me.calebbfmv.nations.donorperks.nms.SIronGolem;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Timothy Andis
 */
public class PlayerGolem {

    private SIronGolem ironGolem;

    public PlayerGolem(Player player) {
        this.ironGolem = (SIronGolem) CustomEntityType.IRON_GOLEM.spawnEntity(player.getLocation());
        new DissipateTask(ironGolem, player).runTaskLater(DonorPerks.getInstance(), 60 * 20);
        new BukkitRunnable() {
            @Override
            public void run() {
                if(ironGolem == null || !ironGolem.isAlive()) {
                    cancel();
                    return;
                }
                if(ironGolem.getGoalTarget() == null) {
                    return;
                }
                if(ironGolem.getGoalTarget().isAlive()) {
                    return;
                }
                ironGolem.setGoalTarget(null);
            }
        }.runTaskTimer(DonorPerks.getInstance(), 1L, 1L);
    }

    public void attack(Entity entity) {
        EntityLiving nmsEntity = ((CraftLivingEntity) entity).getHandle();
        if(ironGolem.getGoalTarget() != null && ironGolem.getGoalTarget().equals(entity)) {
            return;
        }
        ironGolem.setGoalTarget(nmsEntity, EntityTargetEvent.TargetReason.CUSTOM, false);
    }
}
