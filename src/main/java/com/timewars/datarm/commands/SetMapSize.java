package com.timewars.datarm.commands;

import com.timewars.datarm.DataReminder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class SetMapSize implements TabExecutor {

    DataReminder dataReminder;

    public SetMapSize(DataReminder dataReminder)
    {
        this.dataReminder = dataReminder;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        if ( args.length == 1 ) list.add("<radius>");
        return list;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            Location center = p.getLocation();
            int radius = Integer.valueOf(args[0]);

            BlockData block = Bukkit.createBlockData(Material.GLASS);
            World world = p.getWorld();
            ArrayList<Location> locations = new ArrayList<>();

            Location xzLeftBottom = new Location(p.getWorld(),center.getX()-radius, center.getY(), center.getZ()-radius);
            Location xzRightTop = new Location(p.getWorld(),center.getX()+radius, center.getY(), center.getZ()+radius);

            for ( int i = xzLeftBottom.getBlockX(); i < xzRightTop.getBlockX(); i++)
            {
                for ( int j = -3; j <= 3 ; j++ )
                {
                    Location location = new Location(world, i, p.getLocation().getY() + j, xzLeftBottom.getZ() );
                    p.sendBlockChange(location, block);
                    locations.add(location);

                    location = new Location(world, i,p.getLocation().getY() +  j, xzRightTop.getZ() );
                    p.sendBlockChange(location, block);
                    locations.add(location);
                }
            }

            for ( int i = xzLeftBottom.getBlockZ(); i < xzRightTop.getBlockZ(); i++)
            {
                for ( int j = -3; j <= 3 ; j++ )
                {
                    Location location = new Location(world, xzLeftBottom.getX(), p.getLocation().getY() + j, i );
                    p.sendBlockChange(location, block);
                    locations.add(location);

                    location = new Location(world, xzRightTop.getX(), p.getLocation().getY() + j, i );
                    p.sendBlockChange(location, block);
                    locations.add(location);
                }
            }

            BukkitScheduler scheduler;
            scheduler = dataReminder.getServer().getScheduler();

            scheduler.scheduleSyncDelayedTask(dataReminder, new Runnable() {
                @Override
                public void run() {
                    for (Location location : locations)
                    {
                        p.sendBlockChange(location, world.getBlockAt(location).getBlockData());
                    }
                }
            }, 100);

            DataReminder.sp.setMapSize(center, radius);

            return true;
        }
        return false;
    }
}
