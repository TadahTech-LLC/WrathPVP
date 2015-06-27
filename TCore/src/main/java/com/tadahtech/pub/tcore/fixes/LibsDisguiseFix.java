package com.tadahtech.pub.tcore.fixes;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

/**
 * Created by Timothy Andis
 */
public class LibsDisguiseFix implements Listener {

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        Entity entity = event.getEntity();
        Disguise disguise = DisguiseAPI.getDisguise(entity);
        if(disguise == null) {
            return;
        }
        disguise.removeDisguise();
    }
}
