package com.timewars.datarm.files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class CustomConfig {

    private static File file; //real file which we convert to customFile
    private static File fileForHungerGames; //real file which we convert to customFile
    private static FileConfiguration customFile;

    public static void setup()
    {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("DataReminder").getDataFolder(), "items.yml");
        fileForHungerGames = new File(Bukkit.getServer().getPluginManager().getPlugin("DataReminder").getDataFolder(), "itemsHg.yml");

        if(!file.exists())
        {
            System.out.println("Trying to Create New File");
            try
            {
                file.createNewFile();
            }
            catch (IOException e)
            {
                System.out.println(" Execption :(" + e.getMessage());
            }
        }

        if(!fileForHungerGames.exists())
        {
            System.out.println("Trying to Create New File");
            try
            {
                fileForHungerGames.createNewFile();
            }
            catch (IOException e)
            {
                System.out.println(" Execption :(" + e.getMessage());
            }
        }

        customFile = YamlConfiguration.loadConfiguration(file);
    }

    public static void save()
    {
        System.out.println("Trying to Save data..");
        try
        {
            customFile.save(file);
            System.out.println("Saved!");
            List<String> lines = Files.readAllLines(file.toPath());

            String text = "";
            for ( String line : lines)
                text += line + System.lineSeparator();;

            String newtext = text.replaceAll("com.timewars.datarm.classes.myItem",
                    "com.timewars.hungergames.classes.myItem");

            FileWriter writer = new FileWriter(fileForHungerGames);
            writer.write(newtext);
            writer.close();
            System.out.println("Written to second file!");
        }
        catch (IOException e)
        {
            System.out.println("Couldn't save CustomFile or write to second file :(" + e);
        }
    }

    public static void reload()
    {
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration getCustomFile() {
        return customFile;
    }

    public static void setCustomFile(FileConfiguration customFile) {
        CustomConfig.customFile = customFile;
    }
}
