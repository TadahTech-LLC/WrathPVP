package com.tadahtech.pub.tcore.listeners;

import com.tadahtech.pub.tcore.TCore;
import com.tadahtech.pub.tcore.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.shininet.bukkit.playerheads.events.PlayerDropHeadEvent;

/**
 * Created by Timothy Andis
 */
public class DeathMessageListener implements Listener {

    private boolean globally, pve;

    public DeathMessageListener(boolean globally, boolean pve) {
        this.globally = globally;
        this.pve = pve;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String message = event.getDeathMessage();
        Player player = event.getEntity();
        Player killer = player.getKiller();
        if(player.hasMetadata("beheaded")) {
            event.setDeathMessage(null);
            player.removeMetadata("beheaded", TCore.getInstance());
            return;
        }
        message = ChatColor.YELLOW + message;
        message = message.replace(player.getName(), ChatColor.RED + player.getName() + ChatColor.YELLOW);
        for(EntityType type : EntityType.values()) {
            if(!type.isAlive()) {
                continue;
            }
            if(!type.isSpawnable()) {
                continue;
            }
            message = message.replace(Utils.friendlyName(type.name()), ChatColor.RED + Utils.friendlyName(type.name()) + ChatColor.YELLOW);
        }
        event.setDeathMessage(null);
        if(killer == null) {
            if(!pve) {
                return;
            } else {
                if(globally) {
                    event.setDeathMessage(message);
                } else {
                    player.sendMessage(message.replace(player.getName(), "You"));
                }
            }
            return;
        }
        message = message.replace(killer.getName(), ChatColor.RED + killer.getName() + ChatColor.YELLOW);
        if(!globally) {
            ItemStack inhand = killer.getItemInHand();
            String name;
            if(inhand != null && inhand.hasItemMeta() && inhand.getItemMeta().hasDisplayName()) {
                name = inhand.getItemMeta().getDisplayName();
            } else {
                name = Utils.friendlyName(killer.getItemInHand().getType().name());
            }
            name = ChatColor.RED + name + ChatColor.YELLOW;
            player.sendMessage(ChatColor.RED + "You" +
              ChatColor.YELLOW + " were slain by " +
              ChatColor.RED + killer.getName() +
              ChatColor.YELLOW +  " using " +  name);
            killer.sendMessage(ChatColor.RED + "You " + ChatColor.YELLOW + "killed " + ChatColor.RED + player.getName() +
              ChatColor.YELLOW + " with " +name);
            return;
        }
        ItemStack inhand = killer.getItemInHand();
        String name;
        if(inhand != null && inhand.hasItemMeta() && inhand.getItemMeta().hasDisplayName()) {
            name = inhand.getItemMeta().getDisplayName();
        } else {
            name = Utils.friendlyName(killer.getItemInHand().getType().name());
        }
        if(message.contains("using")) {
            try {
                String stripped = message;
                stripped = stripped.substring(0, stripped.indexOf(" using"));
                message = stripped + ChatColor.YELLOW + " using " + ChatColor.RED + name + ChatColor.YELLOW;
            } catch (StringIndexOutOfBoundsException e) {
                //
            }
        }
        event.setDeathMessage(message);
    }

    @EventHandler
    public void onBehead(PlayerDropHeadEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();
        ItemStack inhand = killer.getItemInHand();
        String name;
        if(inhand != null && inhand.hasItemMeta() && inhand.getItemMeta().hasDisplayName()) {
            name = inhand.getItemMeta().getDisplayName();
        } else {
            name = Utils.friendlyName(killer.getItemInHand().getType().name());
        }
        name = ChatColor.RED + name + ChatColor.YELLOW;
        if(!globally) {
            player.sendMessage(ChatColor.RED + killer.getName() + ChatColor.YELLOW + " beheaded you with " + ChatColor.RED + name);
            killer.sendMessage(ChatColor.RED + "You " + ChatColor.YELLOW + "beheaded " + ChatColor.RED + player.getName() +
              ChatColor.YELLOW + " with " +name);
        } else {
            Bukkit.broadcastMessage(ChatColor.RED + killer.getName() + ChatColor.YELLOW + " beheaded " +
              ChatColor.RED + player.getName() + ChatColor.YELLOW + " with " + ChatColor.RED + name);
        }
        player.setMetadata("beheaded", new FixedMetadataValue(TCore.getInstance(), this));
    }
}
