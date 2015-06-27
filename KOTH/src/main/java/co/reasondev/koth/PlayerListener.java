/*
 * Copyright (c) ReasonDev 2014.
 * All rights reserved.
 * No part of this project or any of its contents may be reproduced, copied, modified or adapted, without the prior written consent of SirReason.
 */

package co.reasondev.koth;

import co.reasondev.koth.hill.Hill;
import co.reasondev.koth.hill.HillManager;
import co.reasondev.koth.util.Messaging;
import co.reasondev.koth.util.Settings;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerListener implements Listener {

    private WrathKotH plugin;
    private HillManager hillManager;

    private Map<UUID, Long> pvpDelay = new HashMap<>();

    public PlayerListener(WrathKotH plugin) {
        this.plugin = plugin;
        this.hillManager = plugin.getHillManager();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (e.getEntity().isOp() || e.getEntity().hasPermission("koth.admin") || e.getEntity().getWorld() != hillManager.KOTH_WORLD) {
            return;
        }
        if (Settings.ENABLE_DEATH_BAN.getBoolean()) hillManager.setDeathBanned(e.getEntity());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getPlayer().isOp() || e.getPlayer().hasPermission("koth.admin") || e.getPlayer().getWorld() != hillManager.KOTH_WORLD) {
            return;
        }
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getClickedBlock().getType() != Material.CHEST) {
            return;
        }
        Hill hill = hillManager.getHillFromChest(e.getClickedBlock().getLocation());
        if (hill != null) {
            if (hill.getHillKing() == null || !hill.getHillKing().equals(e.getPlayer().getUniqueId())) {
                e.setCancelled(true);
                Messaging.send(e.getPlayer(), Settings.Messages.NOT_HILL_KING);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        if (e.getTo().getWorld() != hillManager.KOTH_WORLD || e.getFrom().getWorld() == hillManager.KOTH_WORLD) {
            return;
        }
        if (!e.getPlayer().isOp() && !e.getPlayer().hasPermission("koth.admin") && hillManager.isDeathBanned(e.getPlayer())) {
            e.setTo(e.getFrom());
            Messaging.send(e.getPlayer(), Settings.Messages.DEATH_BANNED);
        } else {
            pvpDelay.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
            Messaging.send(e.getPlayer(), Settings.Messages.KOTH_JOINED, Settings.PVP_DELAY.getInt());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPvP(EntityDamageByEntityEvent e) {
        if (e.getEntity().getWorld() != hillManager.KOTH_WORLD) {
            return;
        }
        if (!(e.getEntity() instanceof Player) || (!(e.getDamager() instanceof Player) && !(e.getDamager() instanceof Projectile))) {
            return;
        }
        if (e.getDamager() instanceof Projectile && !(((Projectile) e.getDamager()).getShooter() instanceof Player)) {
            return;
        }
        Player player = (Player) e.getEntity();
        Player damager = (Player) (e.getDamager() instanceof Player ? e.getDamager() : ((Projectile) e.getDamager()).getShooter());
        if (pvpDelay.containsKey(player.getUniqueId())) {
            if (System.currentTimeMillis() - pvpDelay.get(player.getUniqueId()) < Settings.PVP_DELAY.getInt() * 1000) {
                e.setCancelled(true);
                Messaging.send(damager, "&cThis player is temporarily protected from PvP!");
            } else {
                pvpDelay.remove(player.getUniqueId());
            }
        } else if (pvpDelay.containsKey(damager.getUniqueId())) {
            if (System.currentTimeMillis() - pvpDelay.get(damager.getUniqueId()) < Settings.PVP_DELAY.getInt() * 1000) {
                e.setCancelled(true);
                Messaging.send(damager, "&cYour PvP is temporarily disabled!");
            } else {
                pvpDelay.remove(damager.getUniqueId());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent e) {
        if (plugin.getRewardManager().isRewardMenu(e.getInventory())) {
            e.setCancelled(true);
        }
    }
}
