package com.ericimbriaco.xPlugin.Utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WsSender {

    public static void sendPlayerStats() {
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {

            long playtimeTicks = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
            long playtimeMinutes = playtimeTicks / 1200;
            int usedSlots = Bukkit.getServer().getOnlinePlayers().size();
            int maxSlots = Bukkit.getMaxPlayers();
            String server_motd = Bukkit.getServer().getMotd();

            SimpleWsServer.send(escapeJson(player.getName()), "name", escapeJson(player.getName()));
            SimpleWsServer.send(escapeJson(player.getName()), "deaths", player.getStatistic(Statistic.DEATHS));
            SimpleWsServer.send(escapeJson(player.getName()), "playTime", playtimeMinutes);
            SimpleWsServer.send(escapeJson(player.getName()), "lastLogin", player.getLastPlayed());
            SimpleWsServer.send(escapeJson(player.getName()), "online", player.isOnline());

            SimpleWsServer.send("server", "usedSlots", usedSlots);
            SimpleWsServer.send("server", "maxSlots", maxSlots);
            SimpleWsServer.send("server", "server_motd", server_motd);
        }
    }
    private static String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

}
