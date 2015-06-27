package com.tadahtech.pub.commands;

import com.tadahtech.pub.ConfigValues;
import com.tadahtech.pub.DropZone;
import com.tadahtech.pub.announcer.Announcement;
import com.tadahtech.pub.drop.Drop;
import com.tadahtech.pub.listener.MiscListener;
import com.tadahtech.pub.menu.menus.MainMenu;
import com.tadahtech.pub.tier.Tier;
import com.tadahtech.pub.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Timothy Andis
 */
public class DropZoneCommand implements CommandExecutor {

    private Queue<Drop> dropQueue = new ConcurrentLinkedQueue<>();
    private Drop drop;
    private Map<Drop, String> drops = new HashMap<>();
    public static String MESSAGE;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(args.length == 0 || args[0].equalsIgnoreCase("help")) {
            help(sender);
            return true;
        }
        String arg = args[0];
        if(sender instanceof ConsoleCommandSender) {
            if(arg.equalsIgnoreCase("force")) {
                if(args.length == 1) {
                    sender.sendMessage(ChatColor.RED + "Specify a player.");
                    return true;
                }
                Player player = Bukkit.getPlayerExact(args[1]);
                if(player == null) {
                    sender.sendMessage(ChatColor.RED + "Invalid player: " + args[1]);
                    return true;
                }
                int tier = 3;
                try {
                    tier = Integer.parseInt(args[2]);
                } catch (IndexOutOfBoundsException e) {
                    sender.sendMessage(ChatColor.RED + "No Tier Specified, defaulting to 3.");
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Malformed number: " + args[2] + ". Defaulting to 3");
                }
                newDrop(player, tier);
                return true;
            }
            return true;
        }
        Player player = (Player) sender;
        if (arg.equalsIgnoreCase("loot")) {
            new MainMenu().open(player);
            return true;
        }
        if(arg.equalsIgnoreCase("reload") && player.hasPermission("dz.admin")) {
            player.sendMessage(ConfigValues.PREFIX + ChatColor.GREEN.toString() + ChatColor.BOLD + "Config options reloaded.");
            return DropZone.getInstance().getConfigValues().reload();
        }
        if(arg.equalsIgnoreCase("force") && player.hasPermission("dz.admin")) {
            if(args.length == 1) {
                sender.sendMessage(ChatColor.RED + "Specify a player.");
                return true;
            }
            Player target = Bukkit.getPlayerExact(args[1]);
            if(target == null) {
                sender.sendMessage(ChatColor.RED + "Invalid player: " + args[1]);
                return true;
            }
            Integer tier = 3;
            try {
                tier = Integer.parseInt(args[2]);
            } catch (IndexOutOfBoundsException e) {
                sender.sendMessage(ChatColor.RED + "No Tier Specified, defaulting to 3.");
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Malformed number: " + args[2] + ". Defaulting to 3");
            }
            newDrop(player, tier);
            return true;
        }
        if(arg.equalsIgnoreCase("time")) {
            int time = DropZone.getInstance().getTimeTillDrop();
            String t = time(time);
            StringBuilder builder = new StringBuilder();
            Drop serverDrop = DropZone.getInstance().getServerDrop();
            if(serverDrop != null) {
                builder.append(ConfigValues.PREFIX).append(ChatColor.RED).append("Tier ").append(serverDrop.getTier().getLevel())
                  .append(ChatColor.GOLD).append(" drop arrives in ").append(ChatColor.RED)
                  .append(t).append(ChatColor.GOLD).append(" at ").append(ChatColor.RED).append(Utils.locToFriendlyString(serverDrop.getLocation()));
            } else {
                builder.append(ConfigValues.PREFIX).append(ChatColor.GOLD).append("Drop in ").append(ChatColor.RED).append(t);
            }
            player.sendMessage(builder.toString());
            return true;
        }
        if(arg.equalsIgnoreCase("claim") && player.hasPermission("dz.admin")) {
            if(player.getInventory().contains(MiscListener.LOCATION_CLAIMER)) {
                player.getInventory().remove(MiscListener.LOCATION_CLAIMER);
                return true;
            }
            player.getInventory().addItem(MiscListener.LOCATION_CLAIMER);
            return true;
        }
        return false;
    }

    private String time(int totalSeconds) {
        final int MINUTES_IN_AN_HOUR = 60;
        final int SECONDS_IN_A_MINUTE = 60;
        int seconds = totalSeconds % SECONDS_IN_A_MINUTE;
        int totalMinutes = totalSeconds / SECONDS_IN_A_MINUTE;
        int minutes = totalMinutes % MINUTES_IN_AN_HOUR;
        int hours = totalMinutes / MINUTES_IN_AN_HOUR;
        StringBuilder time = new StringBuilder();
        if(hours > 0) {
            time.append(hours).append(" hour");
            if(hours > 1) {
                time.append("s");
            }
        }
        if(minutes > 0) {
            if(hours > 0) {
                time.append(" ");
            }
            time.append(minutes).append(" minute");
            if(minutes > 1) {
                time.append("s");
            }
        }
        if(seconds > 0) {
            if(minutes > 0) {
                time.append(" ");
            }
            time.append(seconds).append(" ").append("second");
            if(seconds > 1) {
                time.append("s");
            }
        }
        return time.toString();
    }

    private void help(CommandSender player) {
        String line = ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + ChatColor.STRIKETHROUGH + "=";
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < 15; i++) {
            builder.append(line);
        }
        line = builder.toString();
        builder = new StringBuilder();
        builder.append(ChatColor.GOLD).append(ChatColor.BOLD).append(ChatColor.STRIKETHROUGH).append("[");
        builder.append(ChatColor.GRAY).append(ChatColor.BOLD).append("  DropZone  ");
        builder.append(ChatColor.GOLD).append(ChatColor.BOLD).append(ChatColor.STRIKETHROUGH).append("]");
        String center = line + builder.toString() + line;
        String dz_time = "/dz time - Shows the time until the next drop. If a drop is currently revealed, it will display the tier and location of it.";
        String dz_loot = "/dz loot - Open a GUI which shows all the possible loot / tiers";
        String dz_force = "/dz force <player> [tier] - Force a drop for that player. Acts as a donation drop. If no tier is specified, it defaults to Tier 3";
        String dz_claim = "/dz claim - Initiate / Stop the claiming process.";
        String dz_reload = "/dz reload - Reload's the config.yml";
        String[] commands = {
          dz_time, dz_loot, dz_force, dz_claim, dz_reload
        };
        String[] shown = new String[(player.hasPermission("dz.admin") ? 5 : 2)];
        for(int i = 0; i < commands.length; i++) {
            String base = commands[i];
            String[] desc = base.split(" - ");
            String command = desc[0];
            String description = desc[1];
            command = ChatColor.DARK_AQUA + command + ChatColor.GRAY + " - ";
            description = ChatColor.GRAY + description;
            //Admin commands
            if(i > 1) {
                if(player.hasPermission("dz.admin")) {
                    shown[i] = command + description;
                }
            } else {
                shown[i] = command + description;
            }
        }
        player.sendMessage(center);
        player.sendMessage(" ");
        player.sendMessage(shown);
        player.sendMessage(" ");
        player.sendMessage(center);
    }
    
    private void newDrop(Player player, int tier) {
        ConfigValues configValues = DropZone.getInstance().getConfigValues();
        Drop drop = new Drop(configValues.randomLocation(), configValues.getShieldStructure(), Tier.get(tier));
        drops.put(drop, player.getName());
        if(this.drop != null) {
            this.dropQueue.add(drop);
            player.sendMessage(ConfigValues.PREFIX + ChatColor.RED + "Your drop is now queued. (" + dropQueue.size() + ")");
        } else {
            runDrop(drop);
        }
    }

    public void runDrop(Drop newDrop) {
        this.drop = newDrop;
        this.drop.reveal();
        String name = drops.remove(drop);
        if(name != null) {
            String base = ChatColor.translateAlternateColorCodes('&', MESSAGE);
            base = base.replace("$player$", name);
            base = base.replace("$tier$", String.valueOf(this.drop.getTier().getLevel()));
            for (Player p : Utils.getOnlinePlayers()) {
                p.sendMessage(base);
            }
        }
        new BukkitRunnable() {

            private int seconds = 60 * 5;

            @Override
            public void run() {
                seconds--;
                if(seconds == 0) {
                    drop.drop();
                    cancel();
                    delayDrop();
                    return;
                }
                Announcement announcement = Announcement.getAnnouncement(seconds);
                if(announcement == null) {
                    return;
                }
                announcement.announce(drop);
            }
        }.runTaskTimer(DropZone.getInstance(), 20L, 20L);
    }

    private void delayDrop() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(dropQueue.peek() != null) {
                    runDrop(dropQueue.poll());
                } else {
                    drop = null;
                }
            }
        }.runTaskLater(DropZone.getInstance(), 20 * 60 * 3);
    }
}
