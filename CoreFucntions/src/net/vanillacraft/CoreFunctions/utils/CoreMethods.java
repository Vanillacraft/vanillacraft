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
public class CoreMethods {
    CoreFunctions plugin;

    public CoreMethods(CoreFunctions plugin){
        this.plugin = plugin;
    }

    public void teleport(Player player, Location loc, boolean isOpMode){
        if(isOpMode){
            player.sendMessage(ChatColor.GREEN + "You were teleported while in Mod Mode this does not count against "
                    + "your timers.");
            player.teleport(loc);
        } else {
            if (canTeleport(player)) {
                player.teleport(loc);
                player.sendMessage(ChatColor.GREEN + "Your teleport timer has just had "
                        + plugin.getCoreData().getTeleportCooldownDuration() + " minutes added to it.");
            } else {
                player.sendMessage(ChatColor.RED + "Your teleport timer is not up yet please wait " +
                        getMinutesRemainTeleport(player) + " minutes till it expires.");
            }
        }
    }

    public boolean canTeleport(Player player){
        if(isModMode(player)){
            return true;
        } else {
            if (getMinutesRemainTeleport(player) == 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    public int getMinutesRemainTeleport(Player player){
        return plugin.getCoreData().getMinutesRemainTeleport(player.getUniqueId());
    }

    public boolean isModMode(Player player){
        return plugin.getCoreData().isModMode(player.getUniqueId());
    }

    public Location getHomeLocation(Player player){
        if(plugin.getCoreData().getPlayerHomeLocation(player.getUniqueId()) != null){
            return plugin.getCoreData().getPlayerHomeLocation(player.getUniqueId());
        } else {
            return null;
        }
    }

    public boolean setHomeLocation(Player player){
        if(plugin.getCoreData().getPlayerSetHomeCoolDown(player.getUniqueId()) > 0){
            if(plugin.getCoreData().getPlayerSetHomeCoolDown(player.getUniqueId()) < System.currentTimeMillis()){
                plugin.getCoreData().setPlayerHome(player.getUniqueId(), player.getLocation());
                return true;
            } else {
                //this is if the player's cool down isn't done.
                return false;
            }
        } else {
            plugin.getCoreData().setPlayerHome(player.getUniqueId(), player.getLocation());
            return true;
        }
    }

}
