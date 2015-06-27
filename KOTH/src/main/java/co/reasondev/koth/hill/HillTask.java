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
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class HillTask {

    private WrathKotH plugin;
    private HillManager hillManager;
    private Hill hill;
    private BukkitTask hillTask;
    private State hillState;
    private int countdown = 0;
    private UUID claimingPlayer = null;

    public HillTask(WrathKotH plugin, Hill hill) {
        this.plugin = plugin;
        this.hillManager = plugin.getHillManager();
        this.hill = hill;
        setState(State.INACTIVE);
        this.hillTask = run();
    }

    public boolean hasHill(Hill hill) {
        return this.hill != null && this.hill.getHillID().equals(hill.getHillID());
    }

    private void setState(State hillState) {
        this.hillState = hillState;
        this.countdown = hillState.getCountdownValue(hill);
    }

    private boolean canSendBroadcast() {
        return countdown < 5 || (countdown < 60 && countdown % 10 == 0) || (countdown < 180 && countdown % 30 == 0) || (countdown > 180 && countdown % 300 == 0);
    }

    private boolean canIncrementClaim() {
        return claimingPlayer != null && Bukkit.getPlayer(claimingPlayer) != null && hill.isInRegion(Bukkit.getPlayer(claimingPlayer));
    }

    public BukkitTask run() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (hillState == State.INACTIVE) {
                    //Check to see if there's anyone claiming the Hill and change the state if so
                    for (Player player : hillManager.KOTH_WORLD.getPlayers()) {
                        if (hill.isInRegion(player)) {
                            setState(State.CLAIMING);
                            claimingPlayer = player.getUniqueId();
                            Messaging.send(player, Settings.Messages.CLAIMING_HILL, hill.getDisplayName(), countdown);
                            return;
                        }
                    }
                    //If no Player is found claiming the Hill, increment inactivity timer
                    if (countdown-- <= 0) {
                        Messaging.broadcast(Settings.Broadcasts.HILL_INACTIVE, hill.getDisplayName());
                        cancelTask(true);
                    } else if (canSendBroadcast()) {
                        Messaging.broadcast(Settings.Broadcasts.UNCONTROLLED_HILL, hill.getDisplayName(), StringUtil.parseTime(countdown));
                    }

                } else if (hillState == State.CLAIMING) {
                    //Check to see if the current claiming Player is still controlling the Hill
                    if (canIncrementClaim()) {
                        Player player = Bukkit.getPlayer(claimingPlayer);
                        if (countdown-- <= 0) {
                            hill.setHillKing(player);
                            hillManager.spawnHillChest(hill);
                            setState(State.RESETTING);
                        } else if (canSendBroadcast()) {
                            Messaging.broadcast(Settings.Broadcasts.CONTROLLED_HILL, player.getName(), hill.getDisplayName(), StringUtil.parseTime(countdown));
                        }
                        //If the claiming Player is no longer valid
                    } else {
                        if (Bukkit.getPlayer(claimingPlayer) != null) {
                            Messaging.send(Bukkit.getPlayer(claimingPlayer), Settings.Messages.NOT_CLAIMING_HILL, hill.getDisplayName());
                        }
                        claimingPlayer = null;
                        setState(State.INACTIVE);
                    }

                } else if (hillState == State.RESETTING) {
                    //Is it time to reset the Hill?
                    if (countdown-- <= 0) {
                        cancelTask(true);
                    } else if (countdown % 3 == 0) {
                        Firework firework = hillManager.KOTH_WORLD.spawn(hill.getChestLocation().toLocation(hillManager.KOTH_WORLD), Firework.class);
                        FireworkMeta meta = firework.getFireworkMeta();
                        meta.addEffect(FireworkEffect.builder().flicker(true).trail(true).withColor(Color.BLUE, Color.RED, Color.GREEN).with(FireworkEffect.Type.BALL_LARGE).build());
                        firework.setFireworkMeta(meta);
                    }
                }

            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public void cancelTask(boolean remove) {
        if (hillTask != null) {
            hillTask.cancel();
        }
        if (hill != null) {
            hillManager.resetHill(hill);
            hill.setLoaded(false);
        }
        hill = null;
        hillTask = null;
        if (remove) {
            plugin.getHillManager().removeTask(this);
        }
    }

    public enum State {

        INACTIVE, CLAIMING, RESETTING;

        public int getCountdownValue(Hill hill) {
            switch (this) {
                case INACTIVE:
                    return hill.getInactive();
                case CLAIMING:
                    return hill.getClaiming();
                case RESETTING:
                    return hill.getResetting();
                default:
                    return 0;
            }
        }
    }
}
