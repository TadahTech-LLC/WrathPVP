package me.calebbfmv.nations.donorperks.perks;

import me.calebbfmv.nations.donorperks.DonorPerks;
import me.calebbfmv.nations.donorperks.Settings;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static me.calebbfmv.nations.donorperks.Settings.PREFIX;
/**
 * Created by Timothy Andis
 */
public abstract class AbstractSpecialItem {

    private Map<UUID, Long> cooldowns = new HashMap<>();
    private ItemStack itemStack;
    public static List<AbstractSpecialItem> ALL = new ArrayList<>();
    private static Map<ItemStack, AbstractSpecialItem> map = new HashMap<>();

    public AbstractSpecialItem(ItemStack item) {
        this.itemStack = item;
        this.cooldowns = new HashMap<>();
        if(ALL.contains(this)) {
            return;
        }
        ALL.add(this);
        map.putIfAbsent(item, this);
    }

    public void onInteract(PlayerInteractEvent playerInteractEvent) {
        Player player = playerInteractEvent.getPlayer();
        if(playerInteractEvent.getAction().name().contains("LEFT")) {
            return;
        }
        if(!canUse(player)) {
            player.sendMessage(PREFIX + "I'm still cooling down!");
            return;
        }
        this.onClick(player);
        ItemStack itemStack = player.getItemInHand();
        if(itemStack == null) {
            return;
        }
        short dur = itemStack.getDurability();
        if(dur == 0) {
            return;
        }
        itemStack.setDurability((short) 0);
        player.setItemInHand(itemStack);
    }

    public abstract void onClick(Player player);

    public boolean canUse(Player player) {
        long curr = System.currentTimeMillis();
        if(!cooldowns.containsKey(player.getUniqueId())) {
            return true;
        }
        long in = cooldowns.get(player.getUniqueId());
        long dif = (getCooldown()) - (curr - in) / 1000;
        if(dif <= 0) {
            cooldowns.remove(player.getUniqueId());
            return true;
        }
        return false;
    }

    protected abstract long getCooldown();

    public void use(Player player) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }

    public ItemStack getItem() {
        return itemStack;
    }

    public void register(Listener listener) {
        DonorPerks.getInstance().getServer().getPluginManager().registerEvents(listener, DonorPerks.getInstance());
    }

    public static AbstractSpecialItem get(ItemStack itemStack) {
        if(map.get(itemStack) != null) {
            return map.get(itemStack);
        }
        if(!itemStack.hasItemMeta()) {
            return null;
        }
        if(!itemStack.getItemMeta().hasDisplayName()) {
            return null;
        }
        String name = itemStack.getItemMeta().getDisplayName();
        String archer = Perk.ARCHER.getItem().getItemMeta().getDisplayName();
        String barbarian = Perk.BARBARIAN.getItem().getItemMeta().getDisplayName();
        String barbarian_king = Perk.BARBARIAN_KING.getItem().getItemMeta().getDisplayName();
        String dragon = Perk.DRAGON.getItem().getItemMeta().getDisplayName();
        String golem = Perk.GOLEM.getItem().getItemMeta().getDisplayName();
        String giant = Perk.GIANT.getItem().getItemMeta().getDisplayName();
        String pekka = Perk.PEKKA.getItem().getItemMeta().getDisplayName();
        String witch = Perk.WITCH.getItem().getItemMeta().getDisplayName();
        String wizard = Perk.WIZARD.getItem().getItemMeta().getDisplayName();
        if(name.equalsIgnoreCase(archer)) {
            return Perk.ARCHER;
        } else if(name.equalsIgnoreCase(barbarian)) {
            return Perk.BARBARIAN;
        } else if(name.equalsIgnoreCase(barbarian_king)) {
            return Perk.BARBARIAN_KING;
        } else if(name.equalsIgnoreCase(dragon)) {
            return Perk.DRAGON;
        } else if(name.equalsIgnoreCase(golem)) {
            return Perk.GOLEM;
        } else if(name.equalsIgnoreCase(giant)) {
            return Perk.GIANT;
        } else if(name.equalsIgnoreCase(pekka)) {
            return Perk.PEKKA;
        } else if(name.equalsIgnoreCase(witch)) {
            return Perk.WITCH;
        } else if(name.equalsIgnoreCase(wizard)) {
            return Perk.WIZARD;
        }
        return null;
    }

    public void give(Player player) {
        for(ItemStack item : player.getInventory().getContents()){
            if(item == null || item.getType() == Material.AIR) {
                continue;
            }
            if(item.isSimilar(getItem())) {
                player.sendMessage(Settings.PREFIX + "You already have this Item!");
                return;
            }
        }
        player.getInventory().addItem(getItem());
        player.sendMessage(Settings.PREFIX + "Redeemed: " + StringUtils.capitalize(getName().replace("king", " King")));

    }

    protected void noPermission(Player player) {
        player.sendMessage(Settings.PREFIX + "You don't have permission for this! Get it with /buy");
    }

    public String getName() {
        return getClass().getSimpleName().toLowerCase();
    }
}
