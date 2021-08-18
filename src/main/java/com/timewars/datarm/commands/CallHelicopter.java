package com.timewars.datarm.commands;

import com.timewars.datarm.DataReminder;
import com.timewars.datarm.classes.Gelicopter;
import com.timewars.datarm.classes.myItem;
import com.timewars.datarm.files.ItemsOperations;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CallHelicopter implements TabExecutor {

    DataReminder dataReminder;

    public CallHelicopter(DataReminder dataReminder)
    {
        this.dataReminder = dataReminder;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if( sender instanceof Player)
        {
            Player p = (Player) sender;

            Gelicopter gelicopter = new Gelicopter(dataReminder);

            return true;
        }
        return false;
    }

}
