package com.ericimbriaco.xPlugin.Commands;

import com.ericimbriaco.xPlugin.Utils.ErrorMessages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class xPluginCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!sender.hasPermission("xplugin.command.xplugin")) {sender.sendMessage(ErrorMessages.NoPermission);return true;} //has Permission

        if(args.length == 0){
            sender.sendMessage(ChatColor.WHITE + "--- xPlugin by Gio ---");
            sender.sendMessage("");
            sender.sendMessage(ChatColor.WHITE + "Installed Version: "+ ChatColor.GOLD + "v1.0");
            sender.sendMessage(ChatColor.WHITE + "Newest Version: " + ChatColor.GOLD + "v1.0");
            sender.sendMessage(ChatColor.WHITE + "Github Repo: " + ChatColor.GOLD + "https://github.com/gioimb/xPlugin");
            sender.sendMessage("");
            sender.sendMessage(ChatColor.WHITE + "--- Made with ♥ in " + ChatColor.GREEN + "It" + ChatColor.WHITE +"a" + ChatColor.RED +"ly" + ChatColor.WHITE + " ---");
            return true;
        }else{
            sender.sendMessage(ErrorMessages.UsageError + "/xplugin");
            return true;
        }
    }
}
