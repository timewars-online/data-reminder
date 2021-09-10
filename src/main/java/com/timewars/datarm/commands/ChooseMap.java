package com.timewars.datarm.commands;

import com.timewars.datarm.DataControl;
import com.timewars.datarm.DataReminder;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class ChooseMap implements TabExecutor {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        if ( args.length == 1 ) list.add("<map_name>");
        return list;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(args.length == 1) {
            int code = DataReminder.sp.getMapID(args[0]);
            if(code == -1) commandSender.sendMessage(ChatColor.RED + "There is no map with name " +  args[0]);
            else if(code == 0) commandSender.sendMessage(ChatColor.RED + "Something went wrong while changing map");
            else {
                commandSender.sendMessage(ChatColor.GREEN + "Swapped map to " + args[0] + " with ID " + code);
            }
        } else {
            commandSender.sendMessage(ChatColor.RED + "No map name entered.");
        }
        return false;
    }
}
