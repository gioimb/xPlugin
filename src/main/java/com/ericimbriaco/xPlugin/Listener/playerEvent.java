package com.ericimbriaco.xPlugin.Listener;

import com.ericimbriaco.xPlugin.Main;
import com.ericimbriaco.xPlugin.Utils.SimpleWsServer;
import com.ericimbriaco.xPlugin.Utils.WsSender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class playerEvent implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event){
        Player getPlayer = event.getPlayer();
        int usedSlots = Bukkit.getServer().getOnlinePlayers().size();
        event.setJoinMessage(ChatColor.GREEN + "→ " + ChatColor.GRAY + getPlayer.getName());
        //WsSender.sendSlots();
        SimpleWsServer.send(getPlayer.getName(), "online", "true");
        SimpleWsServer.send(getPlayer.getName(), "lastLogin", getPlayer.getLastPlayed());
        SimpleWsServer.send("server", "usedSlots", usedSlots);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event){
        Player getPlayer = event.getPlayer();
        int usedSlots = Bukkit.getServer().getOnlinePlayers().size();
        event.setQuitMessage(ChatColor.RED + "← " + ChatColor.GRAY + getPlayer.getName());
        SimpleWsServer.send(getPlayer.getName(), "online", "false");
        SimpleWsServer.send("server", "usedSlots", usedSlots - 1);
        //WsSender.sendSlots();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player getPlayer = event.getPlayer();
        int deaths = getPlayer.getStatistic(Statistic.DEATHS) + 1; // add 1 for this death
        SimpleWsServer.send(getPlayer.getName(), "deaths", String.valueOf(deaths));
    }

}
