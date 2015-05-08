package net.vanillacraft.CoreFunctions.commands;

import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerBedEnterEvent;

/**
 * Created by ryan on 5/5/2015.
 */
public class HomeCommand implements CommandExecutor
{

    private CoreFunctions plugin;

    public HomeCommand(CoreFunctions plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String s, final String[] args)
    {
        final String cmd = command.getName();

        if (sender instanceof Player)
        {
            Player player = (Player) sender;
            if (cmd.equalsIgnoreCase("home"))
            {
                if (plugin.getCoreMethods().canTeleport(player))
                {
                    plugin.getCoreMethods().teleport(player, plugin.getCoreMethods().getHomeLocation(player), plugin.getCoreMethods().isModMode(player));
                    return true;
                }
                else
                {
                    plugin.getCoreErrors().teleportTimerNotDone(player);
                    return true;
                }
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event)
    {
        if (!event.isCancelled())
        {

        }
    }

}
