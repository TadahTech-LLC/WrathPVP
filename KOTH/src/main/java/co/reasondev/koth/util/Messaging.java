/*
 * Copyright (c) ReasonDev 2014.
 * All rights reserved.
 * No part of this project or any of its contents may be reproduced, copied, modified or adapted, without the prior written consent of SirReason.
 */

package co.reasondev.koth.util;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class Messaging {

    private static String PREFIX = Settings.Messages.PREFIX.getMessage();
    private static String BROADCAST_PREFIX = Settings.Broadcasts.PREFIX.getMessage();

    //Send Private (Player) Messages
    public static void send(CommandSender sender, String message) {
        sender.sendMessage(StringUtil.color(PREFIX + message));
    }

    public static void send(CommandSender sender, Settings.Messages message) {
        send(sender, message.getMessage());
    }

    public static void send(CommandSender sender, Settings.Messages message, Object... args) {
        send(sender, String.format(message.getMessage(), args));
    }

    //Send Public (Server) Messages
    public static void broadcast(String message) {
        Bukkit.broadcastMessage(StringUtil.color(BROADCAST_PREFIX + message));
    }

    public static void broadcast(Settings.Broadcasts message) {
        broadcast(message.getMessage());
    }

    public static void broadcast(Settings.Broadcasts message, Object... args) {
        broadcast(String.format(message.getMessage(), args));
    }
}
