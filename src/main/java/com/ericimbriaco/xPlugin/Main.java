package com.ericimbriaco.xPlugin;

import com.ericimbriaco.xPlugin.Listener.playerEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getConsoleSender().sendMessage("xPlugin gestartet");
        Bukkit.getPluginManager().registerEvents(new playerEvent(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
