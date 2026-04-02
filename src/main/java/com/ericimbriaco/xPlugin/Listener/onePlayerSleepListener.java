package com.ericimbriaco.xPlugin.Listener;

import com.ericimbriaco.xPlugin.Utils.ConfigManager;
import com.ericimbriaco.xPlugin.Utils.ErrorMessages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class onePlayerSleepListener implements Listener {

    private final JavaPlugin plugin;

    public onePlayerSleepListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerBedEnterEvent(PlayerBedEnterEvent event) {
        if(!ConfigManager.getBoolean("onePlayerSleep.enable")){return;}

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if(event.getPlayer().isSleeping()){
                event.getPlayer().getWorld().setTime(0L);
                event.getPlayer().getWorld().setStorm(false);
                event.getPlayer().getWorld().setThundering(false);
                Bukkit.getConsoleSender().sendMessage(ErrorMessages.LOGO + "Ein Spieler hat sich schlafen gelegt");
            }
        }, 60L);
    }
}
