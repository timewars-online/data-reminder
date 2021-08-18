package com.timewars.datarm.classes;

import com.timewars.datarm.DataReminder;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import static org.bukkit.Bukkit.getServer;

public class Gelicopter {

    DataReminder dataReminder;

    BukkitScheduler schedulerStartParkour;
    BukkitScheduler schedulerStartParkour2;

    public Gelicopter(DataReminder dataReminder)
    {
        this.dataReminder = dataReminder;

        schedulerStartParkour = getServer().getScheduler();
        schedulerStartParkour2 = getServer().getScheduler();

        StartTheFly();
    }

    public void StartTheFly()
    {
        schedulerStartParkour.scheduleSyncDelayedTask(dataReminder, new Runnable() {
            @Override
            public void run()
            {
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();

                System.out.println("         :LOL:LOL:LOL");
                System.out.println("           A           ");
                System.out.println("       /--------       ");
                System.out.println(" LOL====      ()\\     ");
                System.out.println("  L    \\         \\        ");
                System.out.println("        \\_________]                ");
                System.out.println("           I     I      ");
                System.out.println("        -----------/              ");
                System.out.println("                       ");
                System.out.println("                    ");



                StartTheFlyLeft();
            }
        }, 2);
    }

    public void StartTheFlyLeft()
    {

        schedulerStartParkour.scheduleSyncDelayedTask(dataReminder, new Runnable() {
            @Override
            public void run()
            {
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();

                System.out.println(" :LOL:LOL:LOL          ");
                System.out.println("           A           ");
                System.out.println("  L    /--------       ");
                System.out.println(" LOL====      ()\\     ");
                System.out.println("      \\         \\        ");
                System.out.println("        \\_________]                ");
                System.out.println("           I     I      ");
                System.out.println("        -----------/              ");
                System.out.println("                       ");
                System.out.println("                    ");



                StartTheFly();
            }
        }, 2);
    }

}
