package me.calebbfmv.nations.donorperks.perks;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Timothy Andis
 */
public class Barbarian extends AbstractSpecialItem {

    public Barbarian(ItemStack item) {
        super(item);
    }

    @Override
    public void onClick(Player player) {
        if(!player.hasPermission("donorperks.barbarian")) {
            noPermission(player);
            return;
        }
        List<Player> players = player.getNearbyEntities(10, 10, 10).stream()
          .filter(e -> e instanceof Player)
          .map(e -> (Player) e)
          .collect(Collectors.toList());
        players.stream().forEach(player1 -> {
            Vector dir = player1.getEyeLocation().getDirection();
            dir.multiply(-1).multiply(2.7).setY(dir.getY() * 1.5);
            player1.setVelocity(dir);
            player1.damage(6);
        });
        this.use(player);
    }

    @Override
    protected long getCooldown() {
        return 15;
    }
}
