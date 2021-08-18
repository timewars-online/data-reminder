package com.timewars.datarm;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Formatter;


public class DataControl {
    public Block fSpot, sSpot;
    public World fw, sw;
    ArrayList<Location> chests, spawnSpots;

    DataControl() {
        chests = new ArrayList<>();
        spawnSpots = new ArrayList<>();
    }

    public void addSpawn(Location spot) {
        spawnSpots.add(spot);
    }

    public void rmSpot() {
        spawnSpots.remove(spawnSpots.size() - 1);
    }

    public void clearData() {
        chests.clear();
        spawnSpots.clear();
    }

    public void uploadData(String mapname) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/data?autoReconnect=true&useSSL=false", "root", "");) {
            System.out.println(chests.size() + " " + spawnSpots.size());
            Statement statement = connection.createStatement();
            String s;
            if(chests.size() != 0) {
                s = "INSERT chests(mapname, xcord, ycord, zcord) VALUES";
                for (Location it : chests) {
                    Formatter q = new Formatter();
                    if(!statement.executeQuery(q.format("select * from chests where mapname = '%s' and xcord = %d and ycord = %d and zcord = %d",
                            mapname, (int) it.getX(), (int) it.getY(), (int) it.getZ()).toString()).next()) {
                        Formatter f = new Formatter();
                        s += (f.format(" ('%s', %d, %d, %d), ",
                                mapname, (int) it.getX(), (int) it.getY(), (int) it.getZ()).toString());
                        f.close();
                    }
                    q.close();
                }
                if (!s.equals("INSERT chests(mapname, xcord, ycord, zcord) VALUES")) {
                    s = s.substring(0, s.length() - 2);
                    statement.executeUpdate(s);
                }
            }
            if(spawnSpots.size() != 0) {
                s = "INSERT spawnspot(mapname, xcord, ycord, zcord) VALUES";
                for (Location it : spawnSpots) {
                    Formatter q = new Formatter();
                    if(!statement.executeQuery(q.format("select * from spawnspot where mapname = '%s' and xcord = %d and ycord = %d and zcord = %d",
                            mapname, (int) it.getX(), (int) it.getY(), (int) it.getZ()).toString()).next()) {
                        Formatter f = new Formatter();
                        s += (f.format(" ('%s', %d, %d, %d), ",
                                mapname, (int) it.getX(), (int) it.getY(), (int) it.getZ()).toString());
                        f.close();
                    }
                    q.close();
                }
                if (!s.equals("INSERT spawnspot(mapname, xcord, ycord, zcord) VALUES")) {
                    s = s.substring(0, s.length() - 2);
                    statement.executeUpdate(s);
                }
            }
            chests.clear();
            spawnSpots.clear();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void startResearches() {
        if(fw != null & fw == sw & fSpot != null & sSpot != null) {
            Location f = new Location(fw,
                    Math.min(fSpot.getLocation().getX(), sSpot.getLocation().getX()),
                    Math.min(fSpot.getLocation().getY(), sSpot.getLocation().getY()),
                    Math.min(fSpot.getLocation().getZ(), sSpot.getLocation().getZ())),
                     s = new Location(sw,
                    Math.max(fSpot.getLocation().getX(), sSpot.getLocation().getX()),
                    Math.max(fSpot.getLocation().getY(), sSpot.getLocation().getY()),
                    Math.max(fSpot.getLocation().getZ(), sSpot.getLocation().getZ()));
            for(int x = (int) f.getX(); x <= s.getX(); x++) {
                for(int y = (int) f.getY(); y <= s.getY(); y++) {
                    for(int z = (int) f.getZ(); z <= s.getZ(); z++) {
                        Location spot = new Location(fw, x, y, z);
                        if(spot.getBlock().getType() == Material.CHEST) {
                            chests.add(spot);
                            System.out.println(spot.getX() + " " + spot.getY() + " " + spot.getZ());
                        }
                    }
                }
            }
        }
    }
}
