package net.vanillacraft.CoreFunctions.methods;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by ryan on 5/5/2015.
 * This is the teleport function.
 */
public class teleport {

    public teleport(Player player, Location location, boolean isOp){
        if(player.isOp()){
            player.teleport(location);
            player.sendMessage(ChatColor.GREEN + "You've been teleported via mod powers and as such have no cool down.");
        } else {

        }
    }

}
