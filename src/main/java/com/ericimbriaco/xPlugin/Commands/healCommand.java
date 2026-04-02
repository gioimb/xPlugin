package com.ericimbriaco.xPlugin.Commands;

import com.ericimbriaco.xPlugin.Utils.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class healCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {


        if(args.length == 0){
            if (!(sender instanceof Player)) {sender.sendMessage(ErrorMessages.NotaPlayer);return true;} // Check if Player is a Player
            if (!sender.hasPermission("xplugin.command.heal")) {sender.sendMessage(ErrorMessages.NoPermission);return true;} //has Permission

            Player getplayer = (Player) sender;

            healPlayer(getplayer);
            //getplayer.sendMessage(ErrorMessages.LOGO + "Du wurdest geheilt");
            return true;
        }

        if(args.length == 1){
            if (!sender.hasPermission("xplugin.command.heal.other")) {sender.sendMessage(ErrorMessages.NoPermission);return true;} //has Permission

            Player target = Bukkit.getPlayerExact(args[0]);
            if(target == null){sender.sendMessage(ErrorMessages.PlayerNotFound); return true;} // Check if Player exis
            if(!target.isOnline()){sender.sendMessage(ErrorMessages.PlayerNotOnline); return true;} //Check if target Player online
            healPlayer(target);
            sender.sendMessage(ErrorMessages.LOGO + "Du hast den Spieler " + ChatColor.GOLD + target.getName() + ChatColor.GRAY + " geheilt.");
            return true;

        }

        if(args.length > 1){sender.sendMessage(ErrorMessages.UsageError + "/heal <Player>");return true;} // Check if the Command

        return false;
    }



    private void healPlayer(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);

        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setFireTicks(0);

        for (PotionEffect effect : player.getActivePotionEffects()) {
            PotionEffectType type = effect.getType();

            if (type == PotionEffectType.POISON ||
                    type == PotionEffectType.WITHER ||
                    type == PotionEffectType.BLINDNESS ||
                    type == PotionEffectType.HUNGER ||
                    type == PotionEffectType.WEAKNESS ||
                    type == PotionEffectType.UNLUCK) {

                player.removePotionEffect(type);
            }
        }

        int healthRounded = (int) Math.round(player.getHealth());
        SimpleWsServer.send(player.getName(), "health", healthRounded);
    }
}
