package com.ericimbriaco.xPlugin.Utils;

import com.ericimbriaco.xPlugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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

            int playersMoney = ConfigManager.get().getInt(player.getUniqueId() + ".money");


            SimpleWsServer.send(escapeJson(player.getName()), "name", escapeJson(player.getName()));
            SimpleWsServer.send(escapeJson(player.getName()), "deaths", player.getStatistic(Statistic.DEATHS));
            SimpleWsServer.send(escapeJson(player.getName()), "playTime", playtimeMinutes);
            SimpleWsServer.send(escapeJson(player.getName()), "lastLogin", player.getLastPlayed());
            SimpleWsServer.send(escapeJson(player.getName()), "online", player.isOnline());
            SimpleWsServer.send(escapeJson(player.getName()), "mined_dirt", player.getStatistic(Statistic.MINE_BLOCK, Material.DIRT));
            SimpleWsServer.send(escapeJson(player.getName()), "mined_SPAWNER", player.getStatistic(Statistic.MINE_BLOCK, Material.SPAWNER));
            SimpleWsServer.send(escapeJson(player.getName()), "money", playersMoney);

            SimpleWsServer.send("server", "usedSlots", usedSlots);
            SimpleWsServer.send("server", "maxSlots", maxSlots);

            if(ConfigManager.getBoolean("customMotd.enable")){
                SimpleWsServer.send("server", "server_modtTop", ConfigManager.getString("customMotd.top"));
                SimpleWsServer.send("server", "server_modtBottom", ConfigManager.getString("customMotd.bottom"));
            }else{
                SimpleWsServer.send("server", "server_modtTop", Bukkit.getServer().getMotd());
            }

            SimpleWsServer.send("server", "server_ip", ConfigManager.getString("serverName"));
            SimpleWsServer.send("server", "server_version", Bukkit.getServer().getVersion());
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
