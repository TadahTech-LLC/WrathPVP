package me.calebbfmv.nations.donorperks.command;

import me.calebbfmv.nations.donorperks.Settings;
import me.calebbfmv.nations.donorperks.menu.Button;
import me.calebbfmv.nations.donorperks.menu.Menu;
import me.calebbfmv.nations.donorperks.perks.AbstractSpecialItem;
import me.calebbfmv.nations.donorperks.perks.Perk;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Timothy Andis
 */
public class DPCommand implements CommandExecutor {

    private final String archer = Perk.ARCHER.getName();
    private final String barbarian = Perk.BARBARIAN.getName();
    private final String barbarian_king = Perk.BARBARIAN_KING.getName();
    private final String dragon = Perk.DRAGON.getName();
    private final String golem = Perk.GOLEM.getName();
    private final String giant = Perk.GIANT.getName();
    private final String pekka = Perk.PEKKA.getName();
    private final String witch = Perk.WITCH.getName();
    private final String wizard = Perk.WIZARD.getName();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false;
        }
        String first = args[0];
        Player player = (Player) sender;
        switch (first.toLowerCase()) {
            case "redeemitem":
            case "redeem":
                open(player);
                break;
            case "giveitem":
            case "give":
                if (args.length < 3) {
                    player.sendMessage(Settings.PREFIX + "Please specify an item & player.");
                    break;
                }
                String type = args[1].toLowerCase();
                Player target = Bukkit.getPlayer(args[2]);
                switch (type.toLowerCase()) {
                    case "archer":
                        Perk.ARCHER.give(target);
                        break;
                    case "barbarian":
                        Perk.BARBARIAN.give(target);
                        break;
                    case "barbarianking":
                        Perk.BARBARIAN_KING.give(target);
                        break;
                    case "dragon":
                        Perk.DRAGON.give(target);
                        break;
                    case "giant":
                        Perk.GIANT.give(target);
                        break;
                    case "golem":
                        Perk.GOLEM.give(target);
                        break;
                    case "pekka":
                        Perk.PEKKA.give(target);
                        break;
                    case "witch":
                        Perk.WITCH.give(target);
                        break;
                    case "wizard":
                        Perk.WIZARD.give(target);
                        break;
                }
                break;
            default:
                sender.sendMessage(Settings.PREFIX + "Unknown command: " + first);
                return true;
        }
        return true;
    }

    private List<AbstractSpecialItem> getApplicablePerks(Player player) {
        List<AbstractSpecialItem> perks = new ArrayList<>();
        if (player.hasPermission("donorperks." + archer)) {
            perks.add(Perk.ARCHER);
        }
        if (player.hasPermission("donorperks." + barbarian)) {
            perks.add(Perk.BARBARIAN);
        }
        if (player.hasPermission("donorperks." + barbarian_king)) {
            perks.add(Perk.BARBARIAN_KING);
        }
        if (player.hasPermission("donorperks." + dragon)) {
            perks.add(Perk.DRAGON);
        }
        if (player.hasPermission("donorperks." + golem)) {
            perks.add(Perk.GOLEM);
        }
        if (player.hasPermission("donorperks." + giant)) {
            perks.add(Perk.GIANT);
        }
        if (player.hasPermission("donorperks." + pekka)) {
            perks.add(Perk.PEKKA);
        }
        if (player.hasPermission("donorperks." + witch)) {
            perks.add(Perk.WITCH);
        }
        if (player.hasPermission("donorperks." + wizard)) {
            perks.add(Perk.WIZARD);
        }
        return perks;
    }

    private void open(Player player) {
        new RedeemMenu(player).open(player);
    }

    protected class RedeemMenu extends Menu {

        private Player player;

        public RedeemMenu(Player player) {
            super("Perks");
            this.player = player;
        }

        @Override
        protected Button[] setUp() {
            Button[] buttons = new Button[18];
            List<AbstractSpecialItem> items = getApplicablePerks(player);
            int lastSlot = 0;
            for(int i = 0; i < items.size(); i++) {
                AbstractSpecialItem specialItem = items.get(i);
                ItemStack itemStack = specialItem.getItem().clone();
                buttons[i] = new Button(itemStack, player -> {
                    specialItem.give(player);
                    player.closeInventory();
                });
                lastSlot++;
            }
            List<AbstractSpecialItem> all = new ArrayList<>(AbstractSpecialItem.ALL);
            List<AbstractSpecialItem> left = new ArrayList<>(all);
            for(int i = 0; i < all.size(); i++) {
                AbstractSpecialItem specialItem = all.get(i);
                if(items.contains(specialItem)) {
                    left.remove(specialItem);
                }
            }
            for(AbstractSpecialItem specialItem : left) {
                ItemStack itemStack = specialItem.getItem().clone();
                List<String> lore = new ArrayList<>();
                ItemMeta meta = itemStack.getItemMeta();
                lore.add(ChatColor.RED + "You don't have permission for this!");
                meta.setLore(lore);
                itemStack.setItemMeta(meta);
                buttons[lastSlot + 9] = new Button(itemStack, player -> {
                    player.sendMessage(Settings.PREFIX + "You don't have permission for this! Get it with /buy");
                    player.closeInventory();
                });
                lastSlot ++;
            }
            return buttons;

        }
    }
}
