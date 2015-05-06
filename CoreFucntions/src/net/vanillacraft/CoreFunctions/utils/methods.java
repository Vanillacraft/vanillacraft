package net.vanillacraft.CoreFunctions.utils;

import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by ryan on 5/5/2015.
 * This will contian random helper methods
 * so that things have a uniform appearance.
 */
public class methods {
    CoreFunctions plugin;

    public methods(CoreFunctions plugin){
        this.plugin = plugin;
    }

    public void teleport(Player player, Location loc, boolean isOpMode){

        if(isOpMode){
            player.sendMessage(ChatColor.GREEN + "You were teleported while in Mod Mode this does not count against your timers.");
            player.teleport(loc);
        } else {

        }

    }

    public boolean canTeleport(Player player){

        return true;
    }

}
