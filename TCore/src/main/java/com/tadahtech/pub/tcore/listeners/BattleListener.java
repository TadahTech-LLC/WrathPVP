package com.tadahtech.pub.tcore.listeners;

import com.tadahtech.pub.tcore.TCore;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Timothy Andis
 */
public class BattleListener implements Listener {

    private Map<UUID, ItemStack[]> inventoryMap = new HashMap<>();

    private ItemStack[] armor = {
      new ItemStack(Material.IRON_BOOTS),
      new ItemStack(Material.IRON_LEGGINGS),
      new ItemStack(Material.IRON_CHESTPLATE),
      new ItemStack(Material.IRON_HELMET)
    };

    private ItemStack sword = new ItemStack(Material.IRON_SWORD);

    public BattleListener() {
        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "PvP Sword");
        sword.setItemMeta(meta);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        boolean pHas = player.hasMetadata("inBattle");
        if(!pHas) {
            return;
        }
        event.getDrops().clear();
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        ItemStack[] inventory = inventoryMap.get(player.getUniqueId());
        if(inventory != null) {
            player.getInventory().setContents(inventory);
        }
        player.updateInventory();
        player.sendMessage(ChatColor.GREEN + "PvP disabled.");
        player.removeMetadata("inBattle", TCore.getInstance());
        clearPending(player, false);
        inventoryMap.remove(player.getUniqueId());
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity dam = event.getDamager();
        if(!(entity instanceof Player)) {
            return;
        }
        if(!(dam instanceof Player)) {
            return;
        }
        Player player = (Player) entity;
        Player damager = (Player) dam;
        boolean pHas = player.hasMetadata("inBattle");
        boolean dHas = damager.hasMetadata("inBattle");
        if(pHas && dHas) {
            return;
        }
        event.setCancelled(true);
        String message = ChatColor.RED.toString() + ChatColor.BOLD + "You cannot hurt that player!";
        damager.sendMessage(message);
    }

    @EventHandler
    public void onHeld(PlayerItemHeldEvent event) {
        int to = event.getNewSlot();
        Player player = event.getPlayer();
        Inventory inventory = player.getInventory();
        ItemStack itemStack = inventory.getItem(to);
        if(player.hasMetadata("inBattle")) {
            if(player.hasMetadata("pending")) {
                if(itemStack != null && itemStack.getType() == Material.IRON_SWORD) {
                    clearPending(player, true);
                }
                return;
            }
            if (itemStack == null || itemStack.getType() != Material.IRON_SWORD) {
                BukkitTask task = new BukkitRunnable() {
                    private int total = 0;
                    @Override
                    public void run() {
                        if (total == 3) {
                            player.getInventory().clear();
                            player.getInventory().setArmorContents(null);
                            ItemStack[] inventory = inventoryMap.get(player.getUniqueId());
                            if(inventory != null) {
                                player.getInventory().setContents(inventory);
                            }
                            player.updateInventory();
                            player.sendMessage(ChatColor.GREEN + "PvP disabled.");
                            player.removeMetadata("inBattle", TCore.getInstance());
                            clearPending(player, false);
                            inventoryMap.remove(player.getUniqueId());
                            cancel();
                            return;
                        }
                        player.sendMessage(ChatColor.GOLD + "PvP disabled in " + (3 - total));
                        total++;
                    }
                }.runTaskTimer(TCore.getInstance(), 0L, 20L);
                player.setMetadata("pending", new FixedMetadataValue(TCore.getInstance(), task));
                return;
            }
            return;
        }
        if(itemStack == null || itemStack.getType() == Material.AIR) {
            clearPending(player, true);
            return;
        }
        if(itemStack.getType() != Material.DIAMOND_SWORD) {
            clearPending(player, true);
            return;
        }
        if(player.hasMetadata("pending")) {
            return;
        }
        BukkitTask task = new BukkitRunnable() {
            private int total = 0;
            @Override
            public void run() {
                if(total == 3) {
                    inventoryMap.put(player.getUniqueId(), player.getInventory().getContents());
                    player.getInventory().clear();
                    player.updateInventory();
                    player.getInventory().setArmorContents(armor);
                    player.getInventory().setItem(0, sword);
                    player.sendMessage(ChatColor.RED + "PvP enabled.");
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);
                    player.setMetadata("inBattle", new FixedMetadataValue(TCore.getInstance(), true));
                    clearPending(player, false);
                    player.updateInventory();
                    cancel();
                    return;
                }
                player.sendMessage(ChatColor.GOLD + "PvP enabled in " + (3 - total));
                total++;
            }
        }.runTaskTimer(TCore.getInstance(), 0L, 20L);
        player.setMetadata("pending", new FixedMetadataValue(TCore.getInstance(), task));
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        ItemStack itemStack = event.getItemDrop().getItemStack();
        if(itemStack.equals(sword)) {
            event.setCancelled(true);
        }
    }

    private void clearPending(Player player, boolean send) {
        if(player.hasMetadata("pending")) {
            BukkitTask task = (BukkitTask) player.getMetadata("pending").get(0).value();
            player.removeMetadata("pending", TCore.getInstance());
            if(task != null) {
                task.cancel();
            }
            if(!send) {
                return;
            }
            if(player.hasMetadata("inBattle")) {
                player.sendMessage(ChatColor.DARK_RED + "PvP will not be disabled.");
            } else {
                player.sendMessage(ChatColor.DARK_RED + "PvP will not be enabled.");
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(!player.hasMetadata("inBattle")) {
            return;
        }
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        ItemStack[] inventory = inventoryMap.get(player.getUniqueId());
        if(inventory != null) {
            player.getInventory().setContents(inventory);
        }
        player.updateInventory();
        player.removeMetadata("inBattle", TCore.getInstance());
        clearPending(player, false);
        inventoryMap.remove(player.getUniqueId());
    }


}
