/*
 * Copyright (c) ReasonDev 2014.
 * All rights reserved.
 * No part of this project or any of its contents may be reproduced, copied, modified or adapted, without the prior written consent of SirReason.
 */

package co.reasondev.koth.hill;

import co.reasondev.koth.WrathKotH;
import co.reasondev.koth.util.Messaging;
import co.reasondev.koth.util.Settings;
import co.reasondev.koth.util.StringUtil;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HillManager {

    private final SimpleDateFormat FORMAT = new SimpleDateFormat("EEEE hh:mm a");
    public World KOTH_WORLD = Bukkit.getWorld(Settings.KOTH_WORLD.getString());
    public Location KOTH_SPAWN = StringUtil.parseLocation(KOTH_WORLD, Settings.KOTH_SPAWN.getString());
    private WrathKotH plugin;
    private File file;
    private YamlConfiguration config;
    private Map<String, Hill> loadedHills = new LinkedHashMap<>();
    private List<UUID> deathBans = new ArrayList<>();
    private List<HillTask> hillTasks = new ArrayList<>();

    public HillManager(WrathKotH plugin) {
        this.plugin = plugin;
    }

    public void loadHills() {
        //Load Hill Configuration File
        this.file = new File(plugin.getDataFolder(), "hills.yml");
        if (!file.exists()) {
            plugin.saveResource(file.getName(), false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);
        //Load Hills from Config
        for (String hillID : config.getKeys(false)) {
            loadedHills.put(hillID, Hill.deserialize(config.getConfigurationSection(hillID)));
        }
        plugin.getLogger().info("Loaded " + loadedHills.size() + " Hills from Hill Configuration");
    }

    public void saveHills() {
        //Deactivate any active Hills
        for (HillTask task : hillTasks) {
            task.cancelTask(false);
        }
        //Save Hills to Config
        for (String hillID : loadedHills.keySet()) {
            config.set(hillID, loadedHills.get(hillID).serialize());
        }
        //Save Hill Configuration File
        try {
            config.save(file);
            plugin.getLogger().info("Hill Configuration saved");
        } catch (IOException e) {
            plugin.getLogger().severe("Error saving Hill Configuration!");
            e.printStackTrace();
        }
    }

    public Collection<String> getHills() {
        return loadedHills.keySet();
    }

    public boolean hasHill(String hillID) {
        return loadedHills.containsKey(hillID);
    }

    public Hill getHill(String hillID) {
        return loadedHills.get(hillID);
    }

    public Hill getHillFromChest(Location chestLocation) {
        for (Hill hill : loadedHills.values()) {
            if (hill.getChestLocation().equals(chestLocation.toVector()) && isActive(hill)) {
                return hill;
            }
        }
        return null;
    }

    public void addHill(String hillID, Location chestLocation, Selection selection) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("EST"));
        loadedHills.put(hillID, new Hill(hillID, FORMAT.format(cal.getTime()), chestLocation.toVector(), selection));
    }

    public void removeHill(String hillID) {
        config.set(loadedHills.get(hillID).getHillID(), null);
        loadedHills.remove(hillID);
    }

    //Hill World Death Bans

    public boolean isDeathBanned(Player player) {
        return deathBans.contains(player.getUniqueId());
    }

    public void setDeathBanned(Player player) {
        deathBans.add(player.getUniqueId());
        Messaging.send(player, Settings.Messages.DEATH_BANNED);
    }

    //Check to see if any loaded Hills can be activated

    public boolean isActive(Hill hill) {
        for (HillTask task : hillTasks) {
            if (task.hasHill(hill)) return true;
        }
        return false;
    }

    public void spawnHillChest(Hill hill) {
        hill.getChestLocation().toLocation(KOTH_WORLD).getBlock().setType(Material.CHEST);
        Chest chest = (Chest) hill.getChestLocation().toLocation(KOTH_WORLD).getBlock().getState();
        plugin.getRewardManager().populateInventory(chest.getBlockInventory(), hill.getLootSize());
    }

    public void resetHill(Hill hill) {
        hill.setHillKing(null);
        hill.getChestLocation().toLocation(KOTH_WORLD).getBlock().setType(Material.AIR);
    }

    public void removeTask(HillTask task) {
        if (!hillTasks.contains(task)) {
            return;
        }
        hillTasks.remove(task);
    }

    public String getCurrentTime() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
        return FORMAT.format(cal.getTime());
    }

    public void runHillTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                String time = getCurrentTime();
                for (Hill hill : loadedHills.values()) {
                    if(hill.hasLoaded()) {
                        continue;
                    }
                    if (hill.getStartTime().equalsIgnoreCase(time)) {
                        deathBans.clear();
                        hillTasks.add(new HillTask(plugin, hill));
                        Messaging.broadcast(Settings.Broadcasts.HILL_ACTIVE, hill.getDisplayName());
                        hill.setLoaded(true);
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }
}
