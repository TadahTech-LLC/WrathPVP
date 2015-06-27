package com.tadahtech.pub.announcer;

import com.tadahtech.pub.ConfigValues;
import com.tadahtech.pub.DropZone;
import com.tadahtech.pub.drop.Drop;
import com.tadahtech.pub.utils.Time;
import com.tadahtech.pub.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Timothy Andis
 */
public class Announcement {

    private String message;
    private Time time;
    private boolean reveal;

    private static Map<Integer, Announcement> map = new HashMap<>();

    public Announcement(Time time, String message) {
        this.time = time;
        this.message = message;
        this.reveal = false;
        map.put(time.getTotal(), this);
    }

    public Announcement(Time time, String message, boolean reveal) {
        this(time, message);
        this.reveal = (time.hours == 0 && time.minutes <= 30) || reveal;
    }

    public static Announcement getAnnouncement(int time) {
        return map.get(time);
    }

    public void announce(Drop drop) {
        String base = ChatColor.translateAlternateColorCodes('&', (reveal ? message : DropZone.getInstance().getConfigValues().getBaseMessage()));
        String seconds = String.valueOf((time.seconds < 10 ? "" + time.seconds : time.seconds));
        String minutes = String.valueOf((time.minutes < 10 ? "" + time.minutes : time.minutes));
        String hours = String.valueOf((time.hours < 10 ? "" + time.hours : time.hours));
        StringBuilder time = new StringBuilder();
        if(this.time.hours > 0) {
            time.append(hours).append(" hour");
            if(this.time.hours > 1) {
                time.append("s");
            }
        }
        if(this.time.minutes > 0) {
            if(this.time.hours > 0) {
                time.append(" ");
            }
            time.append(minutes).append(" minute");
            if(this.time.minutes > 1) {
                time.append("s");
            }
        }
        if(this.time.seconds > 0) {
            if(this.time.minutes > 0) {
                time.append(" ");
            }
            time.append(seconds).append(" ").append("second");
            if(this.time.seconds > 1) {
                time.append("s");
            }
        }
        base = base.replace("$time$", time.toString());
        if(reveal && drop != null) {
            base = base.replace("$location$", Utils.locToFriendlyString(drop.getLocation()));
            base = base.replace("$tier$", String.valueOf(drop.getTier().getLevel()));
        } else {
            base = base.replace("$location$", "");
            base = base.replace("$tier$", "");
        }
        for(Player player : Utils.getOnlinePlayers()) {
            player.sendMessage(ConfigValues.PREFIX + base);
        }
    }
}
