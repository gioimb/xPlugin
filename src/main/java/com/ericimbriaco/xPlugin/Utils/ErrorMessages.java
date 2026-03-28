package com.ericimbriaco.xPlugin.Utils;

import org.bukkit.ChatColor;

public class ErrorMessages {
    public static final String LOGO = ChatColor.DARK_PURPLE + "xPlugin: " + ChatColor.GRAY;
    public static final String LOGO_Error = ChatColor.DARK_PURPLE + "xPlugin Error: " + ChatColor.RED;

    public static final String NotaPlayer = ChatColor.DARK_PURPLE + "xPlugin Error: " + ChatColor.RED + "Du musst ein Spieler sein um diesen Befehl nutzen zu können!";
    public static final String NotOp = ChatColor.DARK_PURPLE + "xPlugin Error: " +  ChatColor.RED + "Du Spast hast keine Rechte um diesem Befehl zu benutzen!";
    public static final String PlayerNotFound = ChatColor.DARK_PURPLE + "xPlugin Error: " + ChatColor.RED + "Diesen Spieler gibt es nicht oder ist nicht online. tfuu";
    public static final String SomethingWentWrong = ChatColor.DARK_PURPLE + "xPlugin Error: " + ChatColor.RED + "Irgendetwas ist falsch gelaufen";
    public static final String UsageError = ChatColor.DARK_PURPLE + "xPlugin Error: " + ChatColor.RED;

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
