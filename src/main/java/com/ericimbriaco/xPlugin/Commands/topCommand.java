package com.ericimbriaco.xPlugin.Commands;

import com.ericimbriaco.xPlugin.Utils.ErrorMessages;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class topCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
       if (args.length == 0){
           if (!(sender instanceof Player)) {sender.sendMessage(ErrorMessages.NotaPlayer);return true;} // Check if Player is a Player
           if (!sender.hasPermission("xplugin.command.top")) {sender.sendMessage(ErrorMessages.NoPermission);return true;} //has Permission
           Player getplayer = (Player) sender;
           teleporter(getplayer);

           return true;
       }

       if(args.length == 1){
           if (!sender.hasPermission("xplugin.command.top.other")) {sender.sendMessage(ErrorMessages.NoPermission);return true;} //has Permission
           Player target = Bukkit.getPlayerExact(args[0]);
           if(target == null){sender.sendMessage(ErrorMessages.PlayerNotFound); return true;} // Check if Player exis
           if(!target.isOnline()){sender.sendMessage(ErrorMessages.PlayerNotOnline); return true;} //Check if target Player online
           teleporter(target);
           sender.sendMessage(ErrorMessages.LOGO + "Du hast den Spieler " + ChatColor.GOLD + target.getName() + ChatColor.GRAY + " an die Oberfläche Teleportiert.");
           return true;
       }

        if(args.length > 1){sender.sendMessage(ErrorMessages.UsageError + "/top <Player>");return true;} // Check if the Command

        return true;
    }

    private void teleporter(Player player) {
        World world = player.getWorld();
        Location current = player.getLocation();

        Block ground = world.getHighestBlockAt(current, HeightMap.MOTION_BLOCKING_NO_LEAVES);
        Location target = ground.getLocation().add(0.5, 1, 0.5);

        Block feet = target.getBlock();
        Block head = target.clone().add(0, 1, 0).getBlock();

        if (!feet.isEmpty() || !head.isEmpty()) {
            player.sendMessage("§cKein Platz über dem Block.");
            return;
        }

        player.teleport(target);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
    }

}
