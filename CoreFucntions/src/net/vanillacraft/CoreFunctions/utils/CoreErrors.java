package net.vanillacraft.CoreFunctions.utils;

import net.vanillacraft.CoreFunctions.datastores.CoreData;
import net.vanillacraft.CoreFunctions.datastores.Delay;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by ryan on 5/7/2015.
 */
public class CoreErrors
{

    private CoreFunctions plugin;

    public CoreErrors(CoreFunctions plugin)
    {
        this.plugin = plugin;
    }

    private String preFix = ChatColor.RED + "[Error] : ";

    public void sendError(Player player, String errorMessage)
    {
        player.sendMessage(preFix + errorMessage);
    }

//    public void teleportTimerNotDone(Player player)
//    {
//        sendError(player, "You can't teleport so soon, please wait " + ChatColor.GREEN + CoreData.getProfile(player).getRemainingDelay(Delay.TELEPORT).getFormatted() + ChatColor.RED + "and try again.");
//    }
//
//    public void setHomeTimerNotDone(Player player)
//    {
//        sendError(player, "You can't set home so soon. Please wait "+ChatColor.GREEN+CoreData.getProfile(player).getRemainingDelay(Delay.SETHOME).getFormatted()+ChatColor.RED+" and try again");
//    }

    public void timerNotDone(Player player, String errorMessage, String formattedTime)
    {
        sendError(player, "You can't " + errorMessage + " so soon. Please wait "
                + ChatColor.GREEN + formattedTime + ChatColor.RED
                + " and try again.");
    }

    public void mustBeInWorld(Player player, String worldName)
    {
        sendError(player, "You must be in the " + worldName + " to use this command");
    }
}
