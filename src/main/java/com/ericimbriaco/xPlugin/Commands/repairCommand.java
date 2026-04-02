package com.ericimbriaco.xPlugin.Commands;

import com.ericimbriaco.xPlugin.Utils.ErrorMessages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class repairCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(args.length == 0){
            if (!(sender instanceof Player)) {sender.sendMessage(ErrorMessages.NotaPlayer);return true;} // Check if Player is a Player
            if (!sender.hasPermission("xplugin.command.repair")) {sender.sendMessage(ErrorMessages.NoPermission);return true;} //has Permission
            Player getplayer = (Player) sender;
            repairItem(getplayer);
            return true;
        }

        if(args.length == 1){
            if (!sender.hasPermission("xplugin.command.repair.other")) {sender.sendMessage(ErrorMessages.NoPermission);return true;} //has Permission
            Player target = Bukkit.getPlayerExact(args[0]);
            if(target == null){sender.sendMessage(ErrorMessages.PlayerNotFound); return true;} // Check if Player exis
            if(!target.isOnline()){sender.sendMessage(ErrorMessages.PlayerNotOnline); return true;} //Check if target Player online
            repairItem(target);
            sender.sendMessage(ErrorMessages.LOGO + "Du hast das Item von " + ChatColor.GOLD + target.getName() + ChatColor.GRAY + " repariert.");
            return true;
        }

        if(args.length > 1){sender.sendMessage(ErrorMessages.UsageError + "/repair <Player>");return true;} // Check if the Command
        return false;
    }

    private boolean repairItem(Player player){
        ItemStack getItem = player.getInventory().getItemInMainHand();
        if(getItem.getType().isAir()){
            player.sendMessage(ErrorMessages.UsageError + "Du hast ein Item in der Hand");
            return true;
        }

        if (!(getItem.getItemMeta() instanceof Damageable damageable) || getItem.getType().getMaxDurability() <= 0) {
            player.sendMessage(ErrorMessages.UsageError + "Dieses Item kann nicht repariert werden.");
            return true;
        }

        if (damageable.getDamage() <= 0) {
            player.sendMessage(ErrorMessages.LOGO_Error + "Dieses Item ist bereits vollstäding repariert");
            return true;
        }

        damageable.setDamage(0);
        getItem.setItemMeta(damageable);
        return true;
    }
}
