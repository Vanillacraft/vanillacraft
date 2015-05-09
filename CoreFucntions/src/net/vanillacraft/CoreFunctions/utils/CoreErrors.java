package net.vanillacraft.CoreFunctions.utils;

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

    public void teleportTimerNotDone(Player player)
    {
        sendError(player, "You can't teleport so soon, please wait " + ChatColor.GREEN + plugin.getCoreMethods().getMinutesRemainTeleport(player) + ChatColor.RED + " minutes and try again.");
    }

    public void setHomeTimerNotDone(Player player)
    {
        sendError(player, "You can't set home so soon, please wait");
    }

    public void timerNotDone(Player player, String errorMessage, int minutes)
    {
        sendError(player, "You can't " + errorMessage + " so soon, please wait "
                + ChatColor.GREEN + plugin.getCoreMethods().getMinutesRemainSetHome(player) + ChatColor.RED
                + " minutes and try again.");
    }

    public void mustBeInWorld(Player player, String worldName)
    {
        sendError(player, "You must be in the " + worldName + " to use this command");
    }
}
