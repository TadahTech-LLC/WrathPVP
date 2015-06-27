package me.calebbfmv.nations.donorperks.perks.giant;

import me.calebbfmv.nations.donorperks.DonorPerks;
import me.calebbfmv.nations.donorperks.perks.AbstractSpecialItem;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Timothy Andis
 */
public class Giant extends AbstractSpecialItem implements Listener {

    public Giant(ItemStack item) {
        super(item);
        DonorPerks perks = DonorPerks.getInstance();
        perks.getServer().getPluginManager().registerEvents(this, perks);
    }

    @Override
    public void onClick(Player player) {
        if(!player.hasPermission("donoritems.giant")) {
            noPermission(player);
            return;
        }
        List<PlayerGolem> golems = new ArrayList<>();
        for(int i = 0; i < 2; i++) {
            golems.add(new PlayerGolem(player));
        }
        player.setMetadata("giant", new FixedMetadataValue(DonorPerks.getInstance(), golems));
        player.playSound(player.getLocation(), Sound.IRONGOLEM_DEATH, 1.0F, 1.613F);
        use(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.removeMetadata("giant", DonorPerks.getInstance());
            }
        }.runTaskLater(DonorPerks.getInstance(), 20L * 60);
    }

    @Override
    protected long getCooldown() {
        return 60 * 5;
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity dam = event.getDamager();
        if(!(dam instanceof Player)) {
            return;
        }
        Player player = (Player) dam;
        if(!player.hasMetadata("giant")) {
            return;
        }
        //noinspection unchecked
        List<PlayerGolem> golems = (List<PlayerGolem>) player.getMetadata("giant").get(0).value();
        golems.stream().forEach(golem-> golem.attack(entity));
    }
}
