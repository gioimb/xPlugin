package com.ericimbriaco.xPlugin.Listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class playerEvent implements Listener {
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event){
        Player getPlayer = event.getPlayer();
        event.setJoinMessage(ChatColor.GREEN + "> " + ChatColor.GRAY + getPlayer.getName());
    }

}
