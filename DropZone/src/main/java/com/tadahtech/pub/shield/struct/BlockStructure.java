package com.tadahtech.pub.shield.struct;

import com.tadahtech.pub.DropZone;
import com.tadahtech.pub.drop.Drop;
import com.tadahtech.pub.shield.ShieldStructure;
import com.tadahtech.pub.shield.ShieldType;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Timothy Andis
 */
public class BlockStructure implements ShieldStructure {

    private List<Block> blocks = new ArrayList<>();

    @Override
    public ShieldType getShieldType() {
        return ShieldType.BLOCK;
    }

    @Override
    public String name() {
        return "BlockShield";
    }

    @Override
    public void run(Location loc, Drop drop) {
        Map<Integer, List<Block>> map = new HashMap<>();
        loc = loc.clone().subtract(0, 1, 0);
        int lx = loc.getBlockX();
        int ly = loc.getBlockY();
        int lz = loc.getBlockZ();
        int fy = ly;
        for (int y = ly - 1; y <= ly + 1; y++) {
            fy--;
            for (int x = lx - 1; x <= lx + 1; x++) {
                for (int z = lz - 1; z <= lz + 1; z++) {
                    Location location = new Location(loc.getWorld(), x, y, z);
                    display(Material.STAINED_GLASS, location, DyeColor.BLACK.getWoolData());
                    this.blocks.add(location.getBlock());
                }
            }
            map.put(y, new ArrayList<>(this.blocks));
            this.blocks.clear();
        }
        long delay = (long) 92.31;
        display(Material.BEACON, loc, (byte) 0);

        blocks.add(loc.getBlock());
        final int finalFy = fy;
        new BukkitRunnable() {

            private int Y = ly;
            private List<Block> blocks = map.get(Y);
            private int i = 0;

            @Override
            public void run() {
                if(Y == finalFy) {
                    cancel();
                    drop.release();
                    return;
                }
                if(i == blocks.size()) {
                    Y--;
                    blocks = map.get(Y);
                    i = 0;
                }
                Block block = blocks.get(i);
                block.setType(Material.AIR);
            }
        }.runTaskTimer(DropZone.getInstance(), delay, (long) 92.31);
    }

    public void display(Material effect, Location location, byte woolData) {
        location.getBlock().setType(effect);
        location.getBlock().setData(woolData);
    }
}
