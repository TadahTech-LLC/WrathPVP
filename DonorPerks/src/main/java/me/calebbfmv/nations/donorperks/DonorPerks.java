package me.calebbfmv.nations.donorperks;

import me.calebbfmv.nations.donorperks.command.DPCommand;
import me.calebbfmv.nations.donorperks.menu.Button;
import me.calebbfmv.nations.donorperks.menu.Menu;
import me.calebbfmv.nations.donorperks.perks.AbstractSpecialItem;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Timothy Andis
 */
public class DonorPerks extends JavaPlugin implements Listener {

    private static DonorPerks instance;

    public static DonorPerks getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("dp").setExecutor(new DPCommand());
    }

    @EventHandler
    public void onTest(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        Material material = block.getType();
        if(material != Material.EMERALD_BLOCK) {
            return;
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        List<ItemStack> drops = new ArrayList<>(event.getDrops());
        for(ItemStack itemStack : drops) {
            AbstractSpecialItem item = AbstractSpecialItem.get(itemStack);
            if(item == null) {
                continue;
            }
            event.getDrops().remove(itemStack);
        }
    }

    @EventHandler
    public void onPlace(PlayerDropItemEvent event) {
        AbstractSpecialItem item = AbstractSpecialItem.get(event.getItemDrop().getItemStack());
        if(item == null) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getItemInHand();
        if(event.getAction().name().contains("LEFT")) {
            return;
        }
        AbstractSpecialItem item = AbstractSpecialItem.get(itemStack);
        if(item == null) {
            return;
        }
        if(player.getWorld().getName().equalsIgnoreCase("coc")) {
            player.sendMessage(ChatColor.RED + "You cannot do this in the capital!");
            return;
        }
        event.setUseInteractedBlock(Result.DENY);
        item.onInteract(event);

    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getItemInHand();
        AbstractSpecialItem item = AbstractSpecialItem.get(itemStack);
        if(item == null) {
            return;
        }
        itemStack.setDurability((short) 0);
        player.setItemInHand(itemStack);
    }


    @EventHandler
    public void onClick(InventoryClickEvent event) {
        String name = event.getInventory().getName();
        Player player = (Player) event.getWhoClicked();
        Menu gui = Menu.get(name);
        if (gui == null) {
            return;
        }
        Button button = gui.getButton(event.getRawSlot());
        if (button == null) {
            return;
        }
        if (button.shouldEmptyClick()) {
            button.onClick();
        } else {
            button.onClick(player);
        }
        event.setCancelled(true);
        event.setResult(Event.Result.DENY);
    }
}
