/*
 * Copyright (c) ReasonDev 2014.
 * All rights reserved.
 * No part of this project or any of its contents may be reproduced, copied, modified or adapted, without the prior written consent of SirReason.
 */

package co.reasondev.koth.hill;

import co.reasondev.koth.util.Messaging;
import co.reasondev.koth.util.Settings;
import co.reasondev.koth.util.StringUtil;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class Hill {

    private String hillID;
    private String startTime;
    private String displayName;
    private int lootSize;
    private Vector chestLocation;
    private Vector regionMin;
    private Vector regionMax;
    private boolean hasLoaded;
    private UUID hillKing;
    private int claiming, resetting, inactive;

    public Hill(String hillID, String startTime, Vector chestLocation, Selection selection) {
        this.hillID = hillID;
        this.startTime = startTime;
        this.displayName = hillID;
        this.lootSize = 1;
        this.chestLocation = chestLocation;
        this.regionMin = selection.getMinimumPoint().toVector();
        this.regionMax = selection.getMaximumPoint().toVector();
        this.hasLoaded = false;
    }

    public Hill(String hillID, String startTime, String displayName, int lootSize, Vector chestLocation, Vector regionMin, Vector regionMax) {
        this.hillID = hillID;
        this.startTime = startTime;
        this.displayName = displayName;
        this.lootSize = lootSize;
        this.chestLocation = chestLocation;
        this.regionMin = regionMin;
        this.regionMax = regionMax;
        this.hasLoaded = false;
    }

    public static Hill deserialize(ConfigurationSection c) {
        Hill hIll = new Hill(c.getName(), c.getString("startTime"), StringUtil.color(c.getString("displayName")), c.getInt("lootSize"),
                StringUtil.parseVector(c.getString("chestLocation")), StringUtil.parseVector(c.getString("regionMin")), StringUtil.parseVector(c.getString("regionMax")));
        hIll.setClaiming(c.getInt("claim"));
        hIll.setResetting(c.getInt("reset"));
        hIll.setInactive(c.getInt("inactive"));
        return hIll;
    }

    public int getInactive() {
        return inactive;
    }

    public void setInactive(int inactive) {
        this.inactive = inactive;
    }

    public int getClaiming() {
        return claiming;
    }

    public void setClaiming(int claiming) {
        this.claiming = claiming;
    }

    public int getResetting() {
        return resetting;
    }

    public void setResetting(int resetting) {
        this.resetting = resetting;
    }

    public String getHillID() {
        return hillID;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getLootSize() {
        return lootSize;
    }

    public Vector getChestLocation() {
        return chestLocation;
    }

    public void setChestLocation(Vector chestLocation) {
        this.chestLocation = chestLocation;
    }

    public boolean hasLoaded() {
        return hasLoaded;
    }

    public void setLoaded(boolean loaded) {
        this.hasLoaded = loaded;
    }

    public String getChestLocString() {
        return "X:" + chestLocation.getBlockX() + " Y:" + chestLocation.getBlockY() + " Z:" + chestLocation.getBlockZ();
    }

    public boolean isInRegion(Player player) {
        Vector v = player.getLocation().toVector();
        return v.getBlockX() >= regionMin.getX() && v.getBlockY() >= regionMin.getY() && v.getBlockZ() >= regionMin.getZ()
                && v.getBlockX() <= regionMax.getX() && v.getBlockY() <= regionMax.getY() && v.getBlockZ() <= regionMax.getZ();
    }

    public void updateRegion(Selection selection) {
        this.regionMin = selection.getMinimumPoint().toVector();
        this.regionMax = selection.getMaximumPoint().toVector();
    }

    public UUID getHillKing() {
        return hillKing;
    }

    public void setHillKing(Player player) {
        if (player == null) {
            this.hillKing = null;
            return;
        }
        this.hillKing = player.getUniqueId();
        Messaging.send(player, Settings.Messages.HILL_CLAIMED, displayName);
        Messaging.broadcast(Settings.Broadcasts.HILL_CLAIMED, player.getName(), displayName);
    }

    public Map<String, Object> serialize() {
        return new LinkedHashMap<String, Object>() {{
            put("startTime", startTime);
            put("displayName", displayName);
            put("lootSize", lootSize);
            put("chestLocation", StringUtil.getVectorString(chestLocation));
            put("regionMin", StringUtil.getVectorString(regionMin));
            put("regionMax", StringUtil.getVectorString(regionMax));
            put("claim", claiming);
            put("reset", resetting);
            put("inactive", inactive);
        }};
    }
}
