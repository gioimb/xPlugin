package com.ericimbriaco.xPlugin.Utils;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.Executors;

import static org.bukkit.Bukkit.getServer;

public class WebServer {

    private final JavaPlugin plugin;
    private HttpServer server;

    public WebServer(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress("0.0.0.0", ConfigManager.getInt("WebServer.port")), 0);
        server.createContext("/", this::handleRequest);
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();

        getServer().getConsoleSender().sendMessage(ErrorMessages.LOGO + "Webserver gestartet: " + ChatColor.GOLD + "http://localhost:8080");

    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    private void handleRequest(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        if (path.equals("/")) {
            path = "/index.html";
        }

        if (path.contains("..")) {
            sendText(exchange, 404, "404 Not Found", "text/plain; charset=UTF-8");
            return;
        }

        String resourcePath = "docs" + path;

        try (InputStream inputStream = plugin.getResource(resourcePath)) {
            if (inputStream == null) {
                sendText(exchange, 404, "404 Not Found", "text/plain; charset=UTF-8");
                return;
            }

            byte[] data = inputStream.readAllBytes();
            String contentType = getContentType(path);

            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.sendResponseHeaders(200, data.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(data);
            }
        }
    }

    private void sendText(HttpExchange exchange, int status, String body, String contentType) throws IOException {
        byte[] data = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(status, data.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(data);
        }
    }

    private String getContentType(String path) {
        String lower = path.toLowerCase();

        if (lower.endsWith(".html")) return "text/html; charset=UTF-8";
        if (lower.endsWith(".css")) return "text/css; charset=UTF-8";
        if (lower.endsWith(".js")) return "application/javascript; charset=UTF-8";
        if (lower.endsWith(".json")) return "application/json; charset=UTF-8";
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".gif")) return "image/gif";
        if (lower.endsWith(".svg")) return "image/svg+xml";
        if (lower.endsWith(".ico")) return "image/x-icon";
        if (lower.endsWith(".txt")) return "text/plain; charset=UTF-8";

        return "application/octet-stream";
    }
}
