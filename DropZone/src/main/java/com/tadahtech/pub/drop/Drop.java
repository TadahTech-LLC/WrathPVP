package com.tadahtech.pub.drop;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.tadahtech.pub.ConfigValues;
import com.tadahtech.pub.DropZone;
import com.tadahtech.pub.listener.ChunkListener;
import com.tadahtech.pub.shield.ShieldEffect;
import com.tadahtech.pub.shield.ShieldStructure;
import com.tadahtech.pub.tier.Tier;
import com.tadahtech.pub.utils.Utils;
import org.bukkit.*;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

/**
 * @author Timothy Andis
 */
public class Drop {

    private Location center, current;
    private Tier tier;
    private ShieldEffect effect;
    public boolean canOpen;
    private BukkitTask fireworks;

    public Drop(Location center, ShieldStructure structure, Tier tier) {
        this.center = center;
        this.tier = tier;
        this.effect = new ShieldEffect(this, structure);
        if(center.getWorld() == null) {
            center.setWorld(new WorldCreator("wrathworld").createWorld());
        }
    }

    public Location getLocation() {
        return center;
    }

    public void drop() {
        new BukkitRunnable() {
            @Override
            public void run() {
                dropIt();
            }
        }.runTask(DropZone.getInstance());
    }

    private void dropIt() {
        this.fireworks.cancel();
        if (!center.getChunk().isLoaded()) {
            center.getChunk().load();
            ChunkListener.CHUNKS.add(center.getChunk());
            new BukkitRunnable() {
                @Override
                public void run() {
                    dropIt();
                }
            }.runTaskLater(DropZone.getInstance(), 40L);
            return;
        }
        center.getWorld().playSound(center, Sound.ENDERDRAGON_GROWL, 1.0f, 1.0f);
        int y = center.getBlockY() + 5;
        FallingBlock block = center.getWorld().spawnFallingBlock(center.clone().add(0, y, 0), Material.CHEST, (byte) 0);
        block.setVelocity(new Vector(0, -0.2, 0));
        int highestY = center.getWorld().getHighestBlockYAt(center) + 6 ;
        block.setMetadata("drop", new FixedMetadataValue(DropZone.getInstance(), this));
        Location highest = center.getWorld().getHighestBlockAt(center).getLocation();
        new BukkitRunnable() {
            @Override
            public void run() {
                int y = block.getLocation().getBlockY();
                if (y <= highestY) {
                    cancel();
                    Location location = block.getLocation();
                    block.remove();
                    effect.run(location);
                    current = location;
                    hologram(highest.clone().add(0, 1.9, 0));
                    return;
                }
            }
        }.runTaskTimer(DropZone.getInstance(), 0L, 1L);
    }

    public void hologram(Location location) {
        Hologram hologram = HologramsAPI.createHologram(DropZone.getInstance(), location);
        String green = ChatColor.GREEN + "|";
        String red = ChatColor.RED + "|";
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < 10; i++) {
            builder.append(green);
        }
        builder.append(ChatColor.GOLD).append(" (").append(ChatColor.GOLD).append(Utils.percent(10, 10)).append("%)");
        hologram.insertTextLine(0, builder.toString());
        new BukkitRunnable() {

            private int total = 10;
            private int left = 10;

            @Override
            public void run() {
                StringBuilder builder = new StringBuilder();
                total--;
                if(total == 0) {
                    hologram.delete();
                    cancel();
                    return;
                }
                for(int i = 0; i < total; i++) {
                    builder.append(green);
                    left--;
                }
                for(int i = 0; i < left; i++) {
                    builder.append(red);
                }
                left = 10;
                hologram.clearLines();
                builder.append(ChatColor.GOLD).append(" (").append(ChatColor.GOLD).append(Utils.percent(total, 10)).append("%)");
                hologram.appendTextLine(builder.toString());
            }
        }.runTaskTimer(DropZone.getInstance(), 20 * 12L, 20 * 12L);

    }

    public ShieldEffect getEffect() {
        return effect;
    }

    public void release() {
        FallingBlock fallingBlock = current.getWorld().spawnFallingBlock(current, Material.ENDER_CHEST, (byte) 0);
        fallingBlock.setVelocity(new Vector(0, -0.2, 0));
        fallingBlock.setDropItem(false);
        fallingBlock.setMetadata("drop", new FixedMetadataValue(DropZone.getInstance(), this));
        canOpen = true;
        ChunkListener.CHUNKS.remove(this.center.getChunk());
    }

    public Tier getTier() {
        return tier;
    }

    public void reveal() {
        for(Player player : Utils.getOnlinePlayers()) {
            String base = DropZone.getInstance().getConfigValues().getRevealMessage();
            base = base.replace("$location$", Utils.locToFriendlyString(this.getLocation()))
              .replace("$prefix$", ConfigValues.PREFIX);
            base = base.replace("$tier$", String.valueOf(this.getTier().getLevel()));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', base));
        }
        Location location = center.clone();

        if(!location.getChunk().isLoaded()) {
            location.getChunk().load();
            ChunkListener.CHUNKS.add(location.getChunk());
            new BukkitRunnable() {
                @Override
                public void run() {
                    reveal();
                }
            }.runTaskLater(DropZone.getInstance(), 40L);
            return;
        }
        FireworkEffect effect = FireworkEffect.builder()
          .withColor(Color.BLUE, Color.GREEN, Color.RED)
          .with(Type.BALL)
          .withTrail()
          .withFlicker().build();
        this.fireworks = new BukkitRunnable() {
            @Override
            public void run() {
                Firework firework = location.getWorld().spawn(location, Firework.class);
                FireworkMeta meta = firework.getFireworkMeta();
                meta.setPower(1);
                meta.addEffect(effect);
                firework.setFireworkMeta(meta);
            }
        }.runTaskTimer(DropZone.getInstance(), 60L, 60L);
    }
}
