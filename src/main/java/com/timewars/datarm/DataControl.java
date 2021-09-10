package com.timewars.datarm;


import org.apache.commons.lang.ObjectUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.Formatter;


public class DataControl {
    public Block fSpot, sSpot;
    public World fw, sw;
    ArrayList<Location> chests, spawnSpots, superChests, randomSuperChests;
    private int ID = -1;

    private Location center;
    private int radius;
    private int timeToShrink;
    private int endSizeOfZone;

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

    public void setMapSize(Location center, int radius, int timeToShrink, int endSizeOfZone) {
        this.center = center;
        this.radius = radius;
        this.timeToShrink = timeToShrink;
        this.endSizeOfZone = endSizeOfZone;
    }

    public void createMap(String mapname) {
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/fDB?autoReconnect=true&useSSL=false", "root", "");
            Statement statement = connection.createStatement(); Formatter a = new Formatter()) {
            String s = a.format("INSERT INTO maps(name) VALUES ('%s')", mapname).toString();
            statement.execute(s);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int getMapID(String mapname) {
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/fDB?autoReconnect=true&useSSL=false", "root", "");
            Statement statement = connection.createStatement(); Formatter a = new Formatter()) {
            String s = a.format("SELECT id FROM maps WHERE name = '%s'", mapname).toString();
            ResultSet mapID = statement.executeQuery(s);
            if(!mapID.next()) return -1;
            ID = mapID.getInt(1);
            return ID;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public int uploadMapSize() {
        if(center == null || radius == 0 || timeToShrink == 0 || endSizeOfZone == 0) return 0;
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/data?autoReconnect=true&useSSL=false", "root", "");
            Statement statement = connection.createStatement();
            Formatter q = new Formatter(); Formatter l = new Formatter(); Formatter j = new Formatter();) {
            String s = q.format("SELECT * FROM map_border WHERE id = %d", ID).toString();
            if(statement.executeQuery(s).next()) {
                s = l.format("DELETE FROM map_border WHERE map_id = %d", ID).toString();
                statement.execute(s);
            }
            s = j.format("INSERT map_border(map_id, x_cord, y_cord, z_cord, radius, time, final_radius) VALUES (%d, %d, %d, %d, %d, %d, %d)",
                    ID, (int) center.getX(), (int) center.getY(), (int) center.getZ(), radius, (int) timeToShrink, (int) endSizeOfZone).toString();
            statement.execute(s);
            return 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public void uploadData(CommandSender sender) throws NullPointerException {
        if(ID == -1) throw new NullPointerException();
        int mapSizeCode = uploadMapSize();
        int sChestPushed = addValuesToTable("shulkers", superChests),
                randomSChestPushed = addValuesToTable("random_shulkers", randomSuperChests),
                chestsPushed = addValuesToTable("chests", chests),
                spawnspotsPushed = addValuesToTable("spawnpoints", spawnSpots);
        System.out.println("Pushed to the database random shulkers(white) : " + randomSChestPushed + " shulkers(default) : " + sChestPushed + ", chests : " + chestsPushed + ", spawnspots : " + spawnspotsPushed);
        if(sender instanceof Player) {
            if(mapSizeCode == 1) sender.sendMessage(ChatColor.GREEN + "Map border updated!");
            if(mapSizeCode == 0) sender.sendMessage("Map border wasn't selected!");
            if(mapSizeCode == -1) sender.sendMessage(ChatColor.RED + "Something went wrong while pushing map border.");
            if(randomSChestPushed == -1 || sChestPushed == -1 || chestsPushed == -1 || spawnspotsPushed == -1){
                sender.sendMessage(ChatColor.RED + "Something went wrong while pushing info!");
            }
            else{
                sender.sendMessage(ChatColor.GREEN + "You pushed to the database random shulkers(white) : " + randomSChestPushed + ", shulkers(default) : " + sChestPushed + ", chests : " + chestsPushed + ", spawnspots : " + spawnspotsPushed);
            }
        }
        clearData();
    }

    int addValuesToTable(String tableName, ArrayList<Location> a) {
        Formatter q = null;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/data?autoReconnect=true&useSSL=false", "root", "");
             Statement statement = connection.createStatement();) {
            String s;
            int cnt = 0;
            if (a.size() != 0) {
                q = new Formatter();
                s = q.format("INSERT %s(map_id, x_cord, y_cord, z_cord) VALUES", tableName).toString();
                for (Location it : a) {
                    q = new Formatter();
                    if (!statement.executeQuery(q.format("select * from %s where map_id = %d and x_cord = %d and y_cord = %d and z_cord = %d",
                            tableName, ID, (int) it.getX(), (int) it.getY(), (int) it.getZ()).toString()).next()) {
                        Formatter f = new Formatter();
                        s += (f.format(" (%d, %d, %d, %d), ",
                                ID, (int) it.getX(), (int) it.getY(), (int) it.getZ()).toString());
                        cnt++;
                        f.close();
                    }
                }
                q = new Formatter();
                if (!s.equals(q.format("INSERT %s(map_id, x_cord, y_cord, z_cord) VALUES", tableName).toString())) {
                    s = s.substring(0, s.length() - 2);
                    statement.executeUpdate(s);
                }
                return cnt;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
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
                        }
                        if(spot.getBlock().getType() == Material.CHEST) {
                            chests.add(spot);
                        }
                        if(spot.getBlock().getType() == Material.WHITE_SHULKER_BOX) {
                            randomSuperChests.add(spot);
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
