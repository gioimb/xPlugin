package com.ericimbriaco.xPlugin.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class bankTabComplete implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        List<String> list = new ArrayList<>();

        if (args.length == 1) {
            // First argument
            if (sender.hasPermission("xplugin.command.bank.help")) list.add("help");
            if (sender.hasPermission("xplugin.command.bank.pay")) list.add("pay");
            if (sender.hasPermission("xplugin.command.bank.balance")) list.add("balance");
            if (sender.hasPermission("xplugin.command.bank.admin")) {
                list.add("set");
                list.add("give");
            }
        }

        if (args.length == 2) {
            // Player names
            for (Player p : org.bukkit.Bukkit.getOnlinePlayers()) {
                list.add(p.getName());
            }
        }

        if (args.length == 3) {
            // Amount suggestions
            list.add("10");
            list.add("100");
            list.add("1000");
        }

        return list.stream()
                .filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                .toList();
    }
}
