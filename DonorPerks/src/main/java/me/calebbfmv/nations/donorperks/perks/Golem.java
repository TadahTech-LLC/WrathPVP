package me.calebbfmv.nations.donorperks.perks;

import me.calebbfmv.nations.donorperks.DonorPerks;
import me.calebbfmv.nations.donorperks.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Timothy Andis
 */
public class Golem extends AbstractSpecialItem {

    public Golem(ItemStack item) {
        super(item);
    }

    @Override
    public void onClick(Player player) {
        if(!player.hasPermission("donorperks.golem")) {
            noPermission(player);
            return;
        }
        List<Player> players = player.getNearbyEntities(15, 15, 15).stream()
          .filter(e -> e instanceof Player)
          .map(e -> (Player) e)
          .filter(play -> !play.equals(player))
          .collect(Collectors.toList());
        List<Location> circle = Utils.circle(player, player.getLocation(), 3, 1, false, false, 0);
        int size = players.size();
        int cSize = circle.size() - 1;
        int c = 0;
        for(int i = 0; i < size; i++) {
            Player player1;
            try {
                player1 = players.remove(i);
            } catch (IndexOutOfBoundsException e) {
                return;
            }
            Location location = circle.get(c);
            player1.teleport(location);
            player.playSound(player1.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            player1.setWalkSpeed(0);
            new BukkitRunnable() {
                @Override
                public void run() {
                    Vector vector = player.getEyeLocation().getDirection();
                    vector = vector.multiply(-1).multiply(2.1);
                    vector = vector.setY(2.7);
                    player1.setVelocity(vector);
                    player1.damage(10);
                    player1.setWalkSpeed(0.2F);
//                    player.setVelocity(vector);
                }
            }.runTaskLater(DonorPerks.getInstance(), 1L);
            c++;
            if(c >= cSize) {
                c = 0;
            }
        }
        use(player);
    }

    @Override
    protected long getCooldown() {
        return 15;
    }

    /**
     * The the worth of an Item from it's lore.
     * @param itemStack The item stack to check
     * @return The worth of the item, or -1 if it doesn't contain it.
     *//*
    public double getWorth(ItemStack itemStack) {
        if(itemStack == null || itemStack.getType() == Material.AIR) {
            //applies if you dont check the item first, this will make it so no nasty, code-breaking exception is thrown.
            System.out.println("Null itemStack check with getWorth(" + (itemStack == null ? "null" : "AIR"));
            return -1;
        }
        if(!itemStack.hasItemMeta()) {
            //Default Minecraft item.. This can happen.
            System.out.println("Null ItemMeta with getWorth(" + itemStack.getType().name());
            return -1;
        }
        ItemMeta meta = itemStack.getItemMeta();
        if(!meta.hasLore()) {
            //No lore stored in the meta.
            System.out.println("Null Lore with getWorth(" + itemStack.getType().name());
            return -1;
        }
        List<String> lore = meta.getLore();
        //The lore cannot be empty, so don't worry about that
        String worthRaw;
        //Use this method if you don't know where the "Worth: $" tag is stored
        for(int i = 0; i < lore.size(); i++) {
            String s = lore.get(i);
            //Strip the color chars from the item
            //If you don't do this, you will get either an NPE, or NumberFormatException because of the color.
            s = ChatColor.stripColor(s);
            //begin optional:
            //s = s.toLowerCase(); - make the string all lowercase like this
            //s = s.replace(" ", ""); - This will remove all spaces from the string.
            String[] str = s.split(":");
            //Check to see if it exists.
            if(str.length == 0) {
                //It doesn't
                //We don't care about this string, so lets go to the next iteration in the loop
                continue;
            }
            //See if it's in the format of "Key:Value
            if(str.length != 1) {
                //Contains more than 1 value
                //We don't care about this string, so lets go to the next iteration in the loop
                continue;
            }
            //Get the "Key"
            s = str[0];
            //Get the value
            String value = str[1];
            //Is it oor value string?
            if(!s.equalsIgnoreCase("worth")) {
                //No :(
                //We don't care about this string, so lets go to the next iteration in the loop
                continue;
            }
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException nfe) {
                System.out.println("Error with reading the value: " + "\"" + value + "\". Is it a valid double?");
                return -1;
            }
        }
        //Use this method if you do know.
        worthRaw = lore.get(1);
        worthRaw = ChatColor.stripColor(worthRaw);
        //begin optional:
        //worthRaw = worthRaw.toLowerCase(); - make the string all lowercase like this
        worthRaw = worthRaw.replace(" ", ""); *//*This will remove all spaces from the string.*//*
        String[] str = worthRaw.split(":");
        //Check to see if it exists.
        if(str.length == 0) {
            //It doesn't
            //Failed attempt;
            System.out.println("No char \":\" found");
            return -1;
        }
        //See if it's in the format of "Key:Value
        if(str.length != 1) {
            //Contains more than 1 value
            System.out.println("The String[] " + Arrays.toString(str) + " contains more than 2 values, voiding a Key:Value checl");
            return -1;
        }
        //Get the "Key"
        worthRaw = str[0];
        //Get the value
        String value = str[1];
        //Is it oor value string?
        if(!worthRaw.equalsIgnoreCase("worth")) {
            //No :(
            //Looks like you didn't know.
            System.out.println("The lore line \"" + worthRaw + " \" does not equal \"worth\"");
            return -1;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException nfe) {
            System.out.println("Error with reading the value: " + "\"" + value + "\". Is it a valid double?");
            return -1;
        }
    }*/
}
