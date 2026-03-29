package com.ericimbriaco.xPlugin.Utils;

import com.ericimbriaco.xPlugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.Collection;

import static org.bukkit.Bukkit.getServer;

public class SimpleWsServer extends WebSocketServer {

    private final Main plugin;

    // Statische Referenz auf die aktuelle Instanz
    private static SimpleWsServer instance;

    public SimpleWsServer(int port, Main plugin) {
        super(new InetSocketAddress(port));
        this.plugin = plugin;
        instance = this; // aktuelle Instanz speichern
    }

    @Override
    public void onStart() {
        getServer().getConsoleSender().sendMessage(ErrorMessages.LOGO + "WebSocket Server gestartet auf Port: " + ChatColor.GOLD + getPort());

    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        getServer().getConsoleSender().sendMessage(ErrorMessages.LOGO + "WebSocket Client verbunden: " + ChatColor.GOLD + conn.getRemoteSocketAddress());
        send("server", "ws", "connected");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        getServer().getConsoleSender().sendMessage(ErrorMessages.LOGO + "WebSocket Client getrennt: " + ChatColor.GOLD + conn.getRemoteSocketAddress());


    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        getServer().getConsoleSender().sendMessage(ErrorMessages.LOGO + "WebSocket hat folgende Nachricht empfangen: " + ChatColor.GOLD + message);
        //plugin.getLogger().info("Empfangen von WS Client: " + message);
        //conn.send("Minecraft hat empfangen: " + message);

        if (message.contains("\"action\":\"send_first_data\"")) { //{"action":"send_player_stats"}
            WsSender.sendPlayerStats();
            return;
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        getServer().getConsoleSender().sendMessage(ErrorMessages.LOGO_Error + "WebSocket Fehler: " + ex.getMessage());
    }

    public void broadcastMessage(String message) {
        Collection<WebSocket> conns = getConnections();
        for (WebSocket conn : conns) {
            if (conn != null && conn.isOpen()) {
                conn.send(message);
            }
        }
    }

    public void wsSender(String key, String value) {
        String json = "{\"" + key + "\":\"" + value + "\"}";
        broadcastMessage(json);
    }

    public static void send(String key, String type, Object value) {
        if (instance != null) {
            String valuePart;

            if (value instanceof Number || value instanceof Boolean) {
                valuePart = value.toString();
            } else {
                valuePart = "\"" + value.toString() + "\"";
            }

            String json = String.format(
                    "{\"%s\":{\"type\":\"%s\",\"value\":%s}}",
                    key, type, valuePart
            );

            instance.broadcastMessage(json);
        }
    }
    public static SimpleWsServer getInstance() {
        return instance;
    }
}