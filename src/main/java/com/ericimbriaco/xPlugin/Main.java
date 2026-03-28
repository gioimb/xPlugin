package com.ericimbriaco.xPlugin;

import com.ericimbriaco.xPlugin.Listener.playerEvent;
import com.ericimbriaco.xPlugin.Utils.SimpleWsServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URI;

public final class Main extends JavaPlugin {

    private SimpleWsServer wsServer;

    @Override
    public void onEnable() {
        getLogger().info("xPlugin gestartet");
        // Plugin startup logic
        getServer().getConsoleSender().sendMessage("xPlugin gestartet");
        Bukkit.getPluginManager().registerEvents(new playerEvent(), this);

        try {
            wsServer = new SimpleWsServer(8080, this);
            wsServer.start();
            getLogger().info("WebSocket-Server wird gestartet...");
        } catch (Exception e) {
            getLogger().severe("Konnte WebSocket-Server nicht starten: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
