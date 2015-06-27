package com.tadahtech.pub.tcore.listeners;

import com.tadahtech.pub.tcore.TCore;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Timothy Andis
 */
public class TestListener implements Listener {

    private int radius = 5;
    private List<Material> materials = new ArrayList<Material>(){{
        add(Material.STONE);
        add(Material.WOOD);
        add(Material.LOG);
        add(Material.DIAMOND_BLOCK);
        add(Material.EMERALD_BLOCK);
    }};

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Location location = block.getLocation();
        Material material = block.getType();
    }

/*    public void old() {
        Map<Integer, BlockPlacer> locations = new HashMap<>();
        int index = 0;
        for(int i = 5; i > 0; i--) {
            List<Location> list = outline(location, index, i);
            Material material = materials.get((int) (Math.random() * materials.size() - 1));
            BlockPlacer placer = new BlockPlacer(list, material, player);
            locations.put(i, placer);
            index++;
        }
        player.sendMessage(ChatColor.GREEN + "Building....");
        new BukkitRunnable() {

            private int index = locations.size();

            @Override
            public void run() {
                BlockPlacer placer = locations.get(index);
                if(!placer.running) {
                    placer.run();
                }
                if(placer.isDone()) {
                    index--;
                }
                if(index <= 0) {
                    cancel();
                }
            }
        }.runTaskTimer(TCore.getInstance(), 20L, 1L);

    }*/

    public List<Location> outline(Location location, int area, int plusY) {
        int blockX = location.getBlockX();
        int blockZ = location.getBlockZ();
        int y = location.getBlockY() + plusY;
        List<Location> locations = new ArrayList<>();
        for(int x = blockX - area; x <= blockX + area; x++) {
            for(int z = blockZ - area; z <= blockZ + area; z++) {
                if(x == blockX - area || x == blockX + area) {
                    locations.add(new Location(location.getWorld(), x,y, z));
                } else {
                    if(z == blockZ - area || z == blockZ + area) {
                        locations.add(new Location(location.getWorld(), x,y, z));
                    }
                }
            }
        }
        return locations;
    }

    private class BlockPlacer {

        private List<Location> blocks;
        private Material material;
        private boolean done;
        private boolean running;
        private Player player;

        public BlockPlacer(List<Location> blocks, Material material, Player player) {
            this.blocks = blocks;
            this.material = material;
            this.done = false;
            this.running = false;
            this.player = player;
        }

        public void run() {
            this.running = true;
            new BukkitRunnable() {

                private int index = 0;

                @Override
                public void run() {
                    Location location = blocks.get(index);
                    if(location == null) {
                        return;
                    }
//                    location.getBlock().setType(material);
                    player.playEffect(location, Effect.HAPPY_VILLAGER, 0);
                    index++;
                    if(index == blocks.size()) {
                        cancel();
                        done = true;
                    }
                }
            }.runTaskTimer(TCore.getInstance(), 0L, 1L);
        }

        public boolean isDone() {
            return done;
        }

        public boolean isRunning() {
            return running;
        }
    }

}
