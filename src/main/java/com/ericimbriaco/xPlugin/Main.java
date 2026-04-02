package com.ericimbriaco.xPlugin;

import com.ericimbriaco.xPlugin.Listener.onePlayerSleepListener;
import com.ericimbriaco.xPlugin.Listener.playerEvent;
import com.ericimbriaco.xPlugin.Listener.serverEvent;
import com.ericimbriaco.xPlugin.Listener.silkSpawnerListener;
import com.ericimbriaco.xPlugin.Utils.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Array;
import java.util.List;

public final class Main extends JavaPlugin {

    public static String global_server_name = "iboprofaxe.it";
    public static String cada75b33a = "ericimbriaco"; //6cada75b33aff303d233a43b4abbc88a07b79d4429eb8f70d9454ec5aeddfd85

    private SimpleWsServer wsServer;
    private WebServer webServer;

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getServer().getConsoleSender().sendMessage(ErrorMessages.LOGO + "gestartet.");
        Bukkit.getPluginManager().registerEvents(new playerEvent(), this);
        Bukkit.getPluginManager().registerEvents(new serverEvent(), this);
        Bukkit.getPluginManager().registerEvents(new silkSpawnerListener(), this);
        Bukkit.getPluginManager().registerEvents(new onePlayerSleepListener(this), this);
        new commandManager(this);

        //Data File
        ConfigManager.setupData();

        //WsServer
        if(ConfigManager.getBoolean("WsServer.enable")){
            try {
                wsServer = new SimpleWsServer(ConfigManager.getInt("WsServer.port"), this);
                getServer().getConsoleSender().sendMessage(ErrorMessages.LOGO + "WebSocket Server wird gestartet.");
                wsServer.start();
            } catch (Exception e) {
                getServer().getConsoleSender().sendMessage(ErrorMessages.LOGO + "WebSocker Server konnte nicht gestartet werden: " + e.getMessage());
                e.printStackTrace();
            }
        }

        if(ConfigManager.getBoolean("WebServer.enable")){
            webServer = new WebServer(this);
            try {
                webServer.start();
            } catch (Exception e) {
                getServer().getConsoleSender().sendMessage(ErrorMessages.LOGO_Error + "Webserver konnte nicht gestartet werden.");
                e.printStackTrace();
            }
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

        ConfigManager.saveDataFile();
        getServer().getConsoleSender().sendMessage(ErrorMessages.LOGO + "gestoppt.");
    }

    public static Main getInstance() {
        return instance;
    }

}
