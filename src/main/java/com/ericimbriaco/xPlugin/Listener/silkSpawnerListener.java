package com.ericimbriaco.xPlugin.Listener;

import com.ericimbriaco.xPlugin.Utils.ConfigManager;
import com.ericimbriaco.xPlugin.Utils.ErrorMessages;
import com.ericimbriaco.xPlugin.Utils.SimpleWsServer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class silkSpawnerListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Block getBlock = event.getBlock();

        if(getBlock.getType() != Material.SPAWNER){
            return;
        }

        if(!ConfigManager.getBoolean("silkSpawner.enable")){
            if(!event.getPlayer().hasPermission("xplugin.break.spawner")){
                return;
            }
        }

        ItemStack tool = event.getPlayer().getInventory().getItemInMainHand();

        if (tool == null || tool.getType().isAir()) {
            return;
        }

        List<String> allowed_materials = ConfigManager.getStringList("silkSpawner.material");

        boolean IsPlayerAllowedToBreakSpawner = false;
        for (String matName : allowed_materials) {
            try {
                Material mat = Material.valueOf(matName.toUpperCase());
                if (mat == tool.getType()) {
                    IsPlayerAllowedToBreakSpawner = true;
                    break;
                }
            } catch (IllegalArgumentException ignored) {
            }
        }

        if(!IsPlayerAllowedToBreakSpawner ){
            return;
        }

        if(!tool.containsEnchantment(Enchantment.SILK_TOUCH)){
            return;
        }

        event.setDropItems(false);
        event.setExpToDrop(0);

        getBlock.getWorld().dropItemNaturally(
                getBlock.getLocation(),
                new ItemStack(Material.SPAWNER)
        );

        int amount = event.getPlayer().getStatistic(Statistic.MINE_BLOCK, Material.DIRT) + 1;

        SimpleWsServer.send(event.getPlayer().getName(), "mined_SPAWNER", amount);
    }
}
