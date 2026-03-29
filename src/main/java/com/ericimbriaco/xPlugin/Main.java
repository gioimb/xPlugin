package com.ericimbriaco.xPlugin;

import com.ericimbriaco.xPlugin.Listener.playerEvent;
import com.ericimbriaco.xPlugin.Utils.ErrorMessages;
import com.ericimbriaco.xPlugin.Utils.SimpleWsServer;
import com.ericimbriaco.xPlugin.Utils.WebServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static String global_server_name = "iboprofaxe.it";

    private SimpleWsServer wsServer;
    private WebServer webServer;

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ErrorMessages.LOGO + "gestartet.");
        Bukkit.getPluginManager().registerEvents(new playerEvent(), this);

        try {
           wsServer = new SimpleWsServer(8082, this);
            getServer().getConsoleSender().sendMessage(ErrorMessages.LOGO + "WebSocket Server wird gestartet.");
            wsServer.start();
        } catch (Exception e) {
            getServer().getConsoleSender().sendMessage(ErrorMessages.LOGO + "WebSocker Server konnte nicht gestartet werden: " + e.getMessage());
            e.printStackTrace();
        }

        webServer = new WebServer(this);

        try {
            webServer.start();
        } catch (Exception e) {
            getServer().getConsoleSender().sendMessage(ErrorMessages.LOGO_Error + "Webserver konnte nicht gestartet werden.");
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        if (wsServer != null) {
            try {
                wsServer.stop();
            } catch (Exception e) {
                getServer().getConsoleSender().sendMessage(ErrorMessages.LOGO_Error + "WebSocket konnte nicht gestoppt werden.");
            }
        }

        if (webServer != null) {
            webServer.stop();
        }

        getServer().getConsoleSender().sendMessage(ErrorMessages.LOGO + "gestoppt.");
    }

}
