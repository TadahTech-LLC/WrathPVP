package me.calebbfmv.nations.donorperks.perks;

import me.calebbfmv.nations.donorperks.DonorPerks;
import org.bukkit.Sound;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * Created by Timothy Andis
 */
public class Wizard extends AbstractSpecialItem {

    public Wizard(ItemStack item) {
        super(item);
    }

    @Override
    public void onClick(Player player) {
        if(!player.hasPermission("donorperks.wizard")) {
            noPermission(player);
            return;
        }
        player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0F, 1.0F);
        new BukkitRunnable() {
            private int total = 28;
            @Override
            public void run() {
                if(total <= 0) {
                    cancel();
                    return;
                }
                Vector direction = player.getEyeLocation().getDirection().multiply(2);
                Fireball fireball = player.getWorld().spawn(player.getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()), Fireball.class);
                fireball.setShooter(player);
                fireball.setDirection(direction);
                total--;
            }
        }.runTaskTimer(DonorPerks.getInstance(), 0L, 5L);
        use(player);
    }

    @Override
    protected long getCooldown() {
        return 15;
    }

}
