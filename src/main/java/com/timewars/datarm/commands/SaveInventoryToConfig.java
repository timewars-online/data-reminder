package com.timewars.datarm.commands;

import com.timewars.datarm.classes.myItem;
import com.timewars.datarm.files.ItemsOperations;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


import java.util.ArrayList;
import java.util.List;

public class SaveInventoryToConfig implements TabExecutor {

    ItemsOperations itemsOperations;

    public SaveInventoryToConfig(ItemsOperations itemsOperations) {
        this.itemsOperations = itemsOperations;
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

            ArrayList<myItem> items = itemsOperations.getItems();

            for (ItemStack item : p.getInventory())
            {

               if( item != null)
               {
                   myItem newItem = new myItem(item, 1);

                   items.add(newItem);
                   p.sendMessage(item.getType().name());
               }

            }

            itemsOperations.saveItems();

            return true;
        }
        return false;
    }

}
