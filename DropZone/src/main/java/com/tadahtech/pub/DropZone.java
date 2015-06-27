package com.tadahtech.pub;

import com.tadahtech.pub.announcer.Announcement;
import com.tadahtech.pub.commands.DropZoneCommand;
import com.tadahtech.pub.drop.Drop;
import com.tadahtech.pub.listener.ChunkListener;
import com.tadahtech.pub.listener.MenuListener;
import com.tadahtech.pub.listener.MiscListener;
import com.tadahtech.pub.utils.GlowEnchant;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Timothy Andis
 */
public class DropZone extends JavaPlugin implements Listener {

    private static DropZone instance;
    private ConfigValues configValues;
    private int timeTillDrop = 5 * 60 * 60;
    private Drop serverDrop;

    public static DropZone getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new MiscListener(), this);
        getServer().getPluginManager().registerEvents(new ChunkListener(), this);
        GlowEnchant.register();
        this.configValues = new ConfigValues();
        this.getCommand("dz").setExecutor(new DropZoneCommand());
        run();
    }

    public void run() {
        new BukkitRunnable() {

            private int seconds = 1;
            private int minutes = 0;
            private int hours = 0;
            private Drop drop;
            private int totalSeconds = 5 * 60 * 60;

            @SuppressWarnings("all")
            @Override
            public void run() {
                seconds++;
                timeTillDrop--;
                totalSeconds--;
                if (seconds == 60) {
                    minutes++;
                    seconds = 0;
                }
                if (minutes == 60) {
                    hours++;
                    minutes = 0;
                }
                if (hours == 4 && minutes == 30 && seconds == 0) {
                    drop = new Drop(configValues.randomLocation(), configValues.getShieldStructure(), configValues.randomTier());
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            drop.reveal();
                            serverDrop = drop;
                        }
                    }.runTask(instance);
                }
                if (hours == 5 && minutes == 0 && seconds == 0) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            drop.drop();
                        }
                    }.runTask(instance);
                    timeTillDrop = 5 * 60 * 60;
                    serverDrop = null;
                    DropZone.getInstance().run();
                    try {
                        Announcement.getAnnouncement(0).announce(drop);
                    } catch (Exception e) {

                    }
                    cancel();
                }
                Announcement announcement = Announcement.getAnnouncement(totalSeconds);
                if (announcement == null) {
                    return;
                }
                synchronized (announcement) {
                    announcement.announce(drop);
                }
            }
        }.runTaskTimerAsynchronously(this, 20L, 20L);
    }

    @Override
    public void onDisable() {
        configValues.saveLocations();
        instance = null;
    }

    public ConfigValues getConfigValues() {
        return configValues;
    }

    public int getTimeTillDrop() {
        return timeTillDrop;
    }

    public Drop getServerDrop() {
        return serverDrop;
    }
}
