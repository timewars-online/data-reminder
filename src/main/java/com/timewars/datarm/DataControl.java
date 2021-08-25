package com.timewars.datarm;


import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Formatter;


public class DataControl {
    public Block fSpot, sSpot;
    public World fw, sw;
    private Location center;
    private int radius;
    ArrayList<Location> chests, spawnSpots, superChests, randomSuperChests;

    DataControl() {
        chests = new ArrayList<>();
        spawnSpots = new ArrayList<>();
        superChests = new ArrayList<>();
        randomSuperChests = new ArrayList<>();
    }

    public void addSpawn(Location spot) {
        spawnSpots.add(spot);
    }

    public void rmSpot() {
        spawnSpots.remove(spawnSpots.size() - 1);
    }

    public void clearData() {
        superChests.clear();
        chests.clear();
        spawnSpots.clear();
    }

    public void setMapSize(Location center, int radius) {
        this.center = center;
        this.radius = radius;
    }

    public void uploadMapSize(String mapname) {
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/data?autoReconnect=true&useSSL=false", "root", "");
            Statement statement = connection.createStatement();
            Formatter q = new Formatter(); Formatter l = new Formatter(); Formatter j = new Formatter();) {
            String s = q.format("SELECT * FROM map_border WHERE mapname = %s", mapname).toString();
            if(statement.executeQuery(s).next()) {
                s = q.format("DELETE FROM map_border WHERE mapname = %s", mapname).toString();
                statement.execute(s);
            }
            s = q.format("INSERT map_border(mapname, xcord, ycord, zcord, radius) VALUES (%s, %d, %d, %d, %d)", mapname, (int) center.getX(), (int) center.getY(), (int) center.getZ(), radius).toString();
            statement.execute(s);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void uploadData(CommandSender sender, String mapname) {
        int sChestPushed = addPlaces("super_chests", superChests, mapname),
                randomSChestPushed = addPlaces("random_super_chests", randomSuperChests, mapname),
                chestsPushed =  addPlaces("chests", chests, mapname),
                spawnspotsPushed = addPlaces("spawnspot", spawnSpots, mapname);
        System.out.println("Pushed to the database random shulkers(white) : " + randomSChestPushed + " shulkers(default) : " + sChestPushed + ", chests : " + chestsPushed + ", spawnspots : " + spawnspotsPushed);
        if(sender instanceof Player) {
            sender.sendMessage( ChatColor.GREEN + "You pushed to the database random shulkers(white) : " + randomSChestPushed + ", shulkers(default) : " + sChestPushed + ", chests : " + chestsPushed + ", spawnspots : " + spawnspotsPushed);
        }
        clearData();
    }

    int addPlaces(String tableName, ArrayList<Location> a, String mapname) {
        Formatter q = null;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/data?autoReconnect=true&useSSL=false", "root", "");
             Statement statement = connection.createStatement();) {
            String s;
            int cnt = 0;
            if (a.size() != 0) {
                q = new Formatter();
                s = q.format("INSERT %s(mapname, xcord, ycord, zcord) VALUES", tableName).toString();
                for (Location it : a) {
                    q = new Formatter();
                    if (!statement.executeQuery(q.format("select * from %s where mapname = '%s' and xcord = %d and ycord = %d and zcord = %d",
                            tableName, mapname, (int) it.getX(), (int) it.getY(), (int) it.getZ()).toString()).next()) {
                        Formatter f = new Formatter();
                        s += (f.format(" ('%s', %d, %d, %d), ",
                                mapname, (int) it.getX(), (int) it.getY(), (int) it.getZ()).toString());
                        cnt++;
                        f.close();
                    }
                }
                q = new Formatter();
                if (!s.equals(q.format("INSERT %s(mapname, xcord, ycord, zcord) VALUES", tableName).toString())) {
                    s = s.substring(0, s.length() - 2);
                    statement.executeUpdate(s);
                }
                return cnt;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                q.close();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public void startResearches(CommandSender sender) {
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
                        if(spot.getBlock().getType() == Material.SHULKER_BOX) {
                            superChests.add(spot);
                            System.out.println(spot.getX() + " " + spot.getY() + " " + spot.getZ());
                        }
                        if(spot.getBlock().getType() == Material.CHEST) {
                            chests.add(spot);
                            System.out.println(spot.getX() + " " + spot.getY() + " " + spot.getZ());
                        }
                        if(spot.getBlock().getType() == Material.WHITE_SHULKER_BOX) {
                            randomSuperChests.add(spot);
                            System.out.println(spot.getX() + " " + spot.getY() + " " + spot.getZ());
                        }
                    }
                }
            }
        }
        if(sender instanceof Player) {
            sender.sendMessage(ChatColor.DARK_PURPLE + "White shulkers : " + randomSuperChests.size() + ", shulkers : " + superChests.size() + " , chests : " + chests.size() + " was marked to push");
        }
    }
}
