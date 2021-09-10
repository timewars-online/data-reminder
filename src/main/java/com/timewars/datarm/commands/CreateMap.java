package com.timewars.datarm.commands;

import com.timewars.datarm.DataReminder;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CreateMap implements TabExecutor {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        if ( args.length == 1 ) list.add("<map_name>");
        return list;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length == 1) {
            DataReminder.sp.createMap(args[0]);
        } else {
            commandSender.sendMessage(ChatColor.RED +"No map name entered.");
        }
        return false;
    }
}
