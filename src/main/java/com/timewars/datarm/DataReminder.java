package com.timewars.datarm;

import com.timewars.datarm.classes.MainCommand;
import com.timewars.datarm.classes.myItem;
import com.timewars.datarm.commands.*;
import com.timewars.datarm.events.InteractEvent;
import com.timewars.datarm.files.ItemsOperations;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class DataReminder extends JavaPlugin {

    public static DataControl sp;

    {
        ConfigurationSerialization.registerClass(ItemStack.class);
        ConfigurationSerialization.registerClass(myItem.class);
    }

    @Override
    public void onEnable() {

        System.out.println("DataReminder Plugin Starts...");

        ItemsOperations itemsOperations = new ItemsOperations();

        MainCommand dataReminder = new MainCommand();

        dataReminder.registerCommand("tool", new GetOperatingTool());
        dataReminder.registerCommand("findChests", new FindChests());
        dataReminder.registerCommand("clearData", new ClearData());
        dataReminder.registerCommand("addSpawnSpot", new AddSpawnSpot());
        dataReminder.registerCommand("upload", new UploadData());
        dataReminder.registerCommand("rmSpot", new RemoveSpawnSpot());
        dataReminder.registerCommand("saveInventory", new SaveInventoryToConfig(itemsOperations));
        dataReminder.registerCommand("gelicopter", new CallHelicopter(this));

        getCommand("dataReminder").setExecutor(dataReminder);

        getServer().getPluginManager().registerEvents(new InteractEvent(), this);

        sp = new DataControl();
    }

}
