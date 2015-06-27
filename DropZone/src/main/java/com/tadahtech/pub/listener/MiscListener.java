package com.tadahtech.pub.listener;

import com.tadahtech.pub.ConfigValues;
import com.tadahtech.pub.DropZone;
import com.tadahtech.pub.drop.Drop;
import com.tadahtech.pub.tier.Tier;
import com.tadahtech.pub.utils.ItemBuilder;
import com.tadahtech.pub.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Timothy Andis
 */
public class MiscListener implements Listener {

    public static ItemStack LOCATION_CLAIMER = ItemBuilder.wrap(new ItemStack(Material.EMERALD_BLOCK))
      .name(ChatColor.YELLOW + "Locationer")
      .lore(" ", ChatColor.GRAY + "Place this block down to add a new Drop Location")
      .build();

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getItemInHand();
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }
        if (!itemStack.equals(LOCATION_CLAIMER)) {
            return;
        }
        event.setCancelled(true);
        DropZone.getInstance().getConfigValues().addLocation(event.getBlockPlaced());
        player.sendMessage(ConfigValues.PREFIX + ChatColor.YELLOW + "Location claimed: " + Utils.locToFriendlyString(event.getBlockPlaced().getLocation()));
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Block block = event.getClickedBlock();
        if (!block.hasMetadata("drop")) {
            return;
        }
        event.setCancelled(true);
        Drop drop = (Drop) block.getMetadata("drop").get(0).value();
        if (!drop.canOpen) {
            return;
        }
        Tier tier = drop.getTier();
        Inventory inventory = Bukkit.createInventory(player, 27, ChatColor.GOLD.toString() + ChatColor.BOLD + "Loot: " + tier.getLevel());
        tier.fill(inventory);
        player.openInventory(inventory);
        block.removeMetadata("drop", DropZone.getInstance());
        block.setType(Material.AIR);
        String message = DropZone.getInstance().getConfigValues().getClaimMessage();
        message = ChatColor.translateAlternateColorCodes('&', message);
        message = message.replace("$player$", player.getName()).replace("$prefix$", ConfigValues.PREFIX);
        player.getWorld().playSound(player.getLocation(), Sound.ZOMBIE_WOODBREAK, 1.0f, 1.0f);
        for (Player play : Utils.getOnlinePlayers()) {
            play.sendMessage(message);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.getOpenInventory().getTopInventory().equals(inventory)) {
                    player.closeInventory();
                }
            }
        }.runTaskLater(DropZone.getInstance(), 20L * 10);
    }

    @EventHandler
    public void onChange(EntityChangeBlockEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof FallingBlock)) {
            return;
        }
        FallingBlock fallingBlock = (FallingBlock) entity;
        if (!fallingBlock.hasMetadata("drop")) {
            return;
        }
        Drop drop = (Drop) fallingBlock.getMetadata("drop").get(0).value();
        event.getBlock().setMetadata("drop", new FixedMetadataValue(DropZone.getInstance(), drop));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.dispatchCommand(player, "dz time");
            }
        }.runTaskLater(DropZone.getInstance(), 5 * 20L);
    }
}
