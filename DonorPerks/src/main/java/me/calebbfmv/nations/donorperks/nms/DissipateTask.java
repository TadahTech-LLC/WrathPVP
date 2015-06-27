package me.calebbfmv.nations.donorperks.nms;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Timothy Andis
 */
public class DissipateTask extends BukkitRunnable {

    private CustomEntity customEntity;
    private Player player;

    public DissipateTask(CustomEntity customEntity, Player player) {
        this.customEntity = customEntity;
        this.player = player;
    }

    @Override
    public void run() {
        customEntity.salute(player);
    }
}
