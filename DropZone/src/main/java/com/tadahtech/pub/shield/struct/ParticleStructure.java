package com.tadahtech.pub.shield.struct;

import com.tadahtech.pub.DropZone;
import com.tadahtech.pub.drop.Drop;
import com.tadahtech.pub.shield.ShieldStructure;
import com.tadahtech.pub.shield.ShieldType;
import com.tadahtech.pub.utils.ParticleEffect;
import com.tadahtech.pub.utils.RandomUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

/**
 * @author Timothy Andis
 */
public class ParticleStructure implements ShieldStructure {

    /**
     * ParticleType of spawned particle
     */
    private ParticleEffect particle = ParticleEffect.CRIT_MAGIC;

    /**
     * Radius of the shield
     */
    private int radius = 3;

    /**
     * Particles to display
     */
    private int particles = 180;

    /**
     * Set to false for a half-sphere and true for a complete sphere
     */
    private boolean sphere = true;

    private BukkitTask task;

    @Override
    public ShieldType getShieldType() {
        return ShieldType.PARTICLE;
    }

    @Override
    public String name() {
        return "Particle";
    }

    @Override
    public void run(Location loc, Drop drop) {
        Location finalLoc = loc.clone();
        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < particles; i++) {
                    Vector vector = RandomUtils.getRandomVector().multiply(radius);
                    if (!sphere) {
                        vector.setY(Math.abs(vector.getY()));
                    }
                    finalLoc.add(vector);
                    display(particle, finalLoc);
                    finalLoc.subtract(vector);
                }
            }
        }.runTaskTimer(DropZone.getInstance(), 0L, 1L);
        display(Material.ENDER_CHEST, loc, (byte) 0);
        loc.getBlock().setMetadata("drop", new FixedMetadataValue(DropZone.getInstance(), drop));
        new BukkitRunnable() {
            @Override
            public void run() {
                task.cancel();
                display(Material.AIR, loc, (byte) 0);
                drop.release();
                cancel();
            }
        }.runTaskLater(DropZone.getInstance(), 2400L);
    }

    protected void display(ParticleEffect particle, Location location) {
        particle.display(null, location, Color.BLUE, 32, 0, 0, 0, 0, 1);
    }

    public void display(Material effect, Location location, byte woolData) {
        location.getBlock().setType(effect);
        location.getBlock().setData(woolData);
    }
}
