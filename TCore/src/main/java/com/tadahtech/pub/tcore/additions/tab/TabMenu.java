package com.tadahtech.pub.tcore.additions.tab;

import com.tadahtech.pub.tcore.utils.PacketUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Timothy Andis
 */
public class TabMenu {

    private String header, footer;

    public TabMenu(String header, String footer) {
        this.header = ChatColor.translateAlternateColorCodes('&', header);
        this.footer = ChatColor.translateAlternateColorCodes('&', footer);
    }

    public void sendTo(Player player) {
        PacketUtil.sendTabToPlayer(player, header, footer);
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }
}
