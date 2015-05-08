package net.vanillacraft.CoreFunctions.utils;

import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by ryan on 5/7/2015.
 */
public class CoreErrors {

    private CoreFunctions plugin;

    public CoreErrors (CoreFunctions plugin){
        this.plugin = plugin;
    }

    private String preFix = ChatColor.RED + "[Error] : ";

    public void sendError(Player player, String errorMessage){
        player.sendMessage(preFix + errorMessage);
    }

    public void teleportTimerNotDone(Player player){
        sendError(player, "You can't teleport so soon, please wait " + ChatColor.GREEN
                + plugin.getCoreMethods().getMinutesRemainTeleport(player) + ChatColor.RED + " minutes and try again.");
    }

}
