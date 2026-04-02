package com.ericimbriaco.xPlugin.Commands;

import com.ericimbriaco.xPlugin.Utils.ConfigManager;
import com.ericimbriaco.xPlugin.Utils.ErrorMessages;
import com.ericimbriaco.xPlugin.Utils.SimpleWsServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class bankCommand implements CommandExecutor {

    public static String bankCurrency = ConfigManager.getString("bankAccounts.currency");
    public static String commandUsage = "/bank <pay,info,help> <Player> <amount>";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        // /bank payer <Player> <Amount>
        if(args[0].equalsIgnoreCase("pay")){
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Nur Spieler können Geld senden!");
                return true;
            }
            if (!sender.hasPermission("xplugin.command.bank.pay")) {sender.sendMessage(ErrorMessages.NoPermission);return true;} //has Permission
            if (args.length != 3){sender.sendMessage(ErrorMessages.UsageError + commandUsage); return true;}

            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {sender.sendMessage(ErrorMessages.NotaPlayer);return true;}
            if (target == player) {player.sendMessage("Du kannst dir nicht selbst Geld senden!");return true;}

            int amount;
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ErrorMessages.UsageError + "Amount muss eine Zahl sein.");
                return true;
            }

            int playerMoney = ConfigManager.get().getInt("Money." + player.getUniqueId());

            if (amount <= 0) {
                sender.sendMessage("Der Betrag muss größer als 0 sein!");
                return true;
            }

            int targetMoney = ConfigManager.get().getInt("Money." + target.getUniqueId());


            removeMoney(player.getUniqueId(), amount);
            addMoney(target.getUniqueId(), amount);

            SimpleWsServer.send(player.getName(), "money", playerMoney);
            SimpleWsServer.send(target.getName(), "money", targetMoney);

            sender.sendMessage(ErrorMessages.LOGO_BANK + "Du hast " + ChatColor.GOLD + target.getName() + " " + amount + bankCurrency + ChatColor.WHITE + "gesendet.");
            target.sendMessage(ErrorMessages.LOGO_BANK + "Du hast von " + ChatColor.GOLD + sender.getName() + " " + amount + bankCurrency + ChatColor.WHITE + "erhalten.");
            target.playSound(target.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
            return true;
        }

        // /bank balance
        if(args[0].equalsIgnoreCase("balance")){
            if(args.length == 1){
                if (!(sender instanceof Player player)) {sender.sendMessage(ErrorMessages.NotaPlayer);return true;} // Check if Player is a Player
                if (!sender.hasPermission("xplugin.command.bank.balance")) {sender.sendMessage(ErrorMessages.NoPermission);return true;} //has Permission

                int money = getMoney(player.getUniqueId());
                sender.sendMessage(ErrorMessages.LOGO_BANK + "Du hast derzeit " + ChatColor.GOLD + money + bankCurrency + ChatColor.WHITE + " auf deinem Konto");
                return true;
            }
            // /bank balance <Spieler>
            if(args.length == 2){
                if (!sender.hasPermission("xplugin.command.bank.balance.other")) {sender.sendMessage(ErrorMessages.NoPermission);return true;} //has Permission
                Player target = Bukkit.getPlayerExact(args[1]);
                if(target == null){sender.sendMessage(ErrorMessages.PlayerNotFound); return true;} // Check if Player exis
                if(!target.isOnline()){sender.sendMessage(ErrorMessages.PlayerNotOnline); return true;} //Check if target Player online
                //int money = ConfigManager.get().getInt("Money." + target.getUniqueId());
                Player player = (Player) sender;
                int money = getMoney(player.getUniqueId());

                sender.sendMessage(ErrorMessages.LOGO_BANK + ChatColor.GOLD + target.getName() + ChatColor.WHITE + " hat " + ChatColor.GOLD + money + bankCurrency + ChatColor.WHITE + " auf dem Konto");
                return true;
            }

            sender.sendMessage(ErrorMessages.UsageError + commandUsage);
            return true;
        }

        // /bank give Player Amount
        if(args[0].equalsIgnoreCase("give")){
            if (!sender.hasPermission("xplugin.command.bank.give")) {sender.sendMessage(ErrorMessages.NoPermission);return true;} //has Permission
            if (args.length != 3){sender.sendMessage(ErrorMessages.UsageError + "/bank give <Player> <Amount>"); return true;}

            Player target = Bukkit.getPlayerExact(args[1]);
            if(target == null){sender.sendMessage(ErrorMessages.PlayerNotFound); return true;} // Check if Player exis
            if(!target.isOnline()){sender.sendMessage(ErrorMessages.PlayerNotOnline); return true;} //Check if target Player online

            int amount;
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ErrorMessages.UsageError + "Amount muss eine Zahl sein.");
                return true;
            }

            //int targetMoney = ConfigManager.get().getInt("Money." + target.getUniqueId());
            //ConfigManager.get().set("Money." + target.getUniqueId(), targetMoney + amount);
            //ConfigManager.saveDataFile();

            int targetMoney = getMoney(target.getUniqueId());

            setMoney(target.getUniqueId(), targetMoney + amount);

            SimpleWsServer.send(target.getName(),"money", targetMoney + amount);

            sender.sendMessage(ErrorMessages.LOGO_BANK + "Du hast " + amount + bankCurrency + " gesendet");
            target.sendMessage(ErrorMessages.LOGO_BANK + "Du hast " + ChatColor.GOLD + amount + bankCurrency + ChatColor.WHITE + " erhalten");
            target.playSound(target.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);
            return true;
        }

        if(args[0].equalsIgnoreCase("set")){
            if (!sender.hasPermission("xplugin.command.bank.set")) {sender.sendMessage(ErrorMessages.NoPermission);return true;} //has Permission
            if (args.length != 3){sender.sendMessage(ErrorMessages.UsageError + "/bank set <Player> <Amount>"); return true;}

            Player target = Bukkit.getPlayerExact(args[1]);
            if(target == null){sender.sendMessage(ErrorMessages.PlayerNotFound); return true;} // Check if Player exis
            if(!target.isOnline()){sender.sendMessage(ErrorMessages.PlayerNotOnline); return true;} //Check if target Player online

            int amount;
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ErrorMessages.UsageError + "Amount muss eine Zahl sein.");
                return true;
            }

            setMoney(target.getUniqueId(), amount);

            SimpleWsServer.send(target.getName(),"money", amount);

            sender.sendMessage(ErrorMessages.LOGO_BANK + "Du hast das Konto von " + target.getName() + " auf " + amount + bankCurrency + " gesetzt.");
            target.sendMessage(ErrorMessages.LOGO_BANK + "Dein Kontostand hat sich geändert.");
            return true;
        }

        if(args[0].equalsIgnoreCase("help")){
            if (!sender.hasPermission("xplugin.command.bank.help")) {sender.sendMessage(ErrorMessages.NoPermission);return true;} //has Permission
            sender.sendMessage(ChatColor.WHITE + " -- xPlugin Bank System -- ");
            sender.sendMessage(ChatColor.WHITE + "Commands:");
            sender.sendMessage(ChatColor.WHITE + "/bank pay <Player> <Amount> - Send money to another player");
            sender.sendMessage(ChatColor.WHITE + "/bank balance <Player> - Check your balance or another player's balance");
            if (sender.hasPermission("xplugin.command.bank.set") && sender.hasPermission("xplugin.command.bank.give")) {
                sender.sendMessage(ChatColor.RED + "Admin Commands");
                sender.sendMessage(ChatColor.WHITE + "/bank set <Player> <Amount> - Set a player's bank balance ");
                sender.sendMessage(ChatColor.WHITE + "/bank give <Player> <Amount> - Add money to a player's bank account ");
            }
        return true;
        }
        return false;
    }

    public static int getMoney(UUID uuid){

        return ConfigManager.get().getInt(uuid.toString() + ".money");
    }

    public static void setMoney(UUID uuid, int amount){
        ConfigManager.get().set(uuid.toString() + ".money", amount);
        ConfigManager.saveDataFile();
    }

    public static void addMoney(UUID uuid, int amount){
        setMoney(uuid, getMoney(uuid) + amount);
    }

    public static void removeMoney(UUID uuid, int amount){
        setMoney(uuid, getMoney(uuid) - amount);
    }

}
