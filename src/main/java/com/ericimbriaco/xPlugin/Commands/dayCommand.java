package com.ericimbriaco.xPlugin.Commands;

import com.ericimbriaco.xPlugin.Utils.ErrorMessages;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class dayCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.hasPermission("xplugin.command.day")) {sender.sendMessage(ErrorMessages.NoPermission);return true;} //has Permission
        if(args.length > 0){sender.sendMessage(ErrorMessages.UsageError + "/day");return true;} // Check if the Command
        if (!(sender instanceof Player player)) {sender.sendMessage(ErrorMessages.NotaPlayer);return true;} // Check if Player is a Player
        player.getWorld().setTime(1000);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
        return true;
    }
}
