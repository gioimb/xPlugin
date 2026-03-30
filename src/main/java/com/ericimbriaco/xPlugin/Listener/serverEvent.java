package com.ericimbriaco.xPlugin.Listener;

import com.ericimbriaco.xPlugin.Utils.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class serverEvent implements Listener {

    @EventHandler
    public void onServerPing(ServerListPingEvent event){
        if (!ConfigManager.getBoolean("customMotd.enable")) return;

        String lineTop1 = ConfigManager.getString("customMotd.top");
        String lineBottom2 = ConfigManager.getString("customMotd.bottom");

        lineTop1 = lineTop1.replace("%servername%", ConfigManager.getString("serverName"));
        lineBottom2 = lineBottom2.replace("%servername%", ConfigManager.getString("serverName"));

        lineTop1 = ChatColor.translateAlternateColorCodes('&', lineTop1);
        lineBottom2 = ChatColor.translateAlternateColorCodes('&', lineBottom2);

        event.setMotd(lineTop1 + "\n" + lineBottom2);

    }
}
