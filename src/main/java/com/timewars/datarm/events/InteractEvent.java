package com.timewars.datarm.events;

import com.timewars.datarm.DataReminder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractEvent implements Listener {

    @EventHandler
    void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(event.getAction() == Action.LEFT_CLICK_BLOCK & player.getInventory().getItemInMainHand().getType() == Material.BLAZE_ROD) {
            Block fSpot = event.getClickedBlock();
            player.sendMessage(ChatColor.LIGHT_PURPLE + "First spot was marked");
            DataReminder.sp.fw = player.getWorld();
            DataReminder.sp.fSpot = fSpot;
        }
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK & player.getInventory().getItemInMainHand().getType() == Material.BLAZE_ROD) {
            Block sSpot = event.getClickedBlock();
            player.sendMessage(ChatColor.DARK_PURPLE + "Second spot was marked");
            DataReminder.sp.sw = player.getWorld();
            DataReminder.sp.sSpot = sSpot;
        }
    }
}
