package com.tadahtech.wrath.stattracker;

import com.tadahtech.wrath.stattracker.block.PickaxeListener;
import com.tadahtech.wrath.stattracker.entity.WeaponListener;
import com.tadahtech.wrath.stattracker.utils.GlowEnchant;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Timothy Andis
 */
public class StatTracker extends JavaPlugin {

    private static StatTracker instance;

    public static StatTracker getInstance() {
        return instance;
    }

    public static String PREFIX = ChatColor.GRAY + "[" + ChatColor.AQUA + "StatTracker" + ChatColor.GRAY + "] ";

    @Override
    public void onEnable() {
        instance = this;
        GlowEnchant.register();
        getServer().getPluginManager().registerEvents(new PickaxeListener(), this);
        getServer().getPluginManager().registerEvents(new WeaponListener(), this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        player.sendMessage(PREFIX + ChatColor.RED + "This feature is still being worked on");
        player.sendMessage(PREFIX + ChatColor.GREEN + "Make sure to check " + ChatColor.UNDERLINE + "http://wrathpvp.com/threads/cosmetics-for-stattracker.3034/" + ChatColor.YELLOW + " to vote.");
//        ItemStack itemStack = player.getItemInHand();
//        if(itemStack == null || itemStack.getType() == Material.AIR) {
//            player.sendMessage(ChatColor.RED + "Please have an item in your hand!");
//            return true;
//        }
//        ItemSetting setting = ItemSetting.get(itemStack);
//        setting.setEnableCosmetics(!setting.isEnableCosmetics());
//        itemStack = setting.rebuild();
//        player.setItemInHand(itemStack);
        return true;
    }

}
