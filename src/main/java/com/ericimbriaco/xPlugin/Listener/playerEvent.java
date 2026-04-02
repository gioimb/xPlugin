package com.ericimbriaco.xPlugin.Listener;

import com.ericimbriaco.xPlugin.Main;
import com.ericimbriaco.xPlugin.Utils.ConfigManager;
import com.ericimbriaco.xPlugin.Utils.ErrorMessages;
import com.ericimbriaco.xPlugin.Utils.SimpleWsServer;
import com.ericimbriaco.xPlugin.Utils.WsSender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.ChatPaginator;

import java.util.HashMap;


public class playerEvent implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event){
        Player getPlayer = event.getPlayer();
        int usedSlots = Bukkit.getServer().getOnlinePlayers().size();

        if(ConfigManager.getBoolean("customJoinMessage.enable")){
            String serverJoinMessage = ConfigManager.getString("customJoinMessage.message");
            serverJoinMessage = serverJoinMessage.replace("%player%", getPlayer.getName());
            serverJoinMessage = ChatColor.translateAlternateColorCodes('&', serverJoinMessage);
            event.setJoinMessage(null); // Vanilla Join Message aus
            Bukkit.broadcastMessage(serverJoinMessage);

            //WelcomeMessage -> get from config
            String welcomeMessage = ConfigManager.getString("customJoinMessage.welcomeMessage");
            welcomeMessage = ChatColor.translateAlternateColorCodes('&', welcomeMessage);
            getPlayer.sendMessage(welcomeMessage);
        }

        String tabListFooterMessage = ConfigManager.getString("tabList.footer");
        tabListFooterMessage = tabListFooterMessage.replace("%servername%", ConfigManager.getString("serverName"));
        tabListFooterMessage = tabListFooterMessage.replace("%money%", String.valueOf(ConfigManager.get().getInt("Money." + getPlayer.getUniqueId())));
        tabListFooterMessage = ChatColor.translateAlternateColorCodes('&', tabListFooterMessage);
        event.getPlayer().setPlayerListFooter(tabListFooterMessage);


        SimpleWsServer.send(getPlayer.getName(), "online", "true");
        SimpleWsServer.send(getPlayer.getName(), "lastLogin", getPlayer.getLastPlayed());
        SimpleWsServer.send("server", "usedSlots", usedSlots);

        int level = getPlayer.getLevel();
        SimpleWsServer.send(getPlayer.getName(), "level", level);

        int healthRounded = (int) Math.round(getPlayer.getHealth());
        SimpleWsServer.send(getPlayer.getName(), "health", healthRounded);

        if (!ConfigManager.get().contains(getPlayer.getUniqueId() + ".money")) {
            ConfigManager.get().set(getPlayer.getUniqueId() + ".money", 1000);
            ConfigManager.saveDataFile();
        }

    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event){
        Player getPlayer = event.getPlayer();
        int usedSlots = Bukkit.getServer().getOnlinePlayers().size();

        if(ConfigManager.getBoolean("customQuitMessage.enable")){
            String serverQuitMessage = ConfigManager.getString("customQuitMessage.message");
            serverQuitMessage = serverQuitMessage.replace("%player%", getPlayer.getName());
            serverQuitMessage = ChatColor.translateAlternateColorCodes('&', serverQuitMessage);
            event.setQuitMessage(null); // Vanilla Join Message aus
            Bukkit.broadcastMessage(serverQuitMessage);
        }

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

    private HashMap<String, Integer> dirtStats = new HashMap<>();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.DIRT) {
            Player player = event.getPlayer();

            int amount = player.getStatistic(Statistic.MINE_BLOCK, Material.DIRT) + 1;

            SimpleWsServer.send(player.getName(), "mined_dirt", amount);
        }
    }

    @EventHandler
    public void onPlayerLevel(PlayerLevelChangeEvent event){
        Player getPlayer = event.getPlayer();

        int level = getPlayer.getLevel();
        SimpleWsServer.send(getPlayer.getName(), "level", level);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        Player player = (Player) e.getEntity();
        String spielername = player.getName();
        int healthRounded = (int) Math.round(player.getHealth());
        SimpleWsServer.send(spielername, "health", healthRounded);
    }
}
