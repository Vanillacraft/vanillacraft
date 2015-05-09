package net.vanillacraft.CoreFunctions.commands;

import net.vanillacraft.CoreFunctions.datastores.CoreData;
import net.vanillacraft.CoreFunctions.datastores.PlayerProfile;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Created by ryan on 5/8/2015.
 */
public class ModTPcommand implements Listener
{
    private CoreFunctions plugin;

    public ModTPcommand(CoreFunctions plugin)
    {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, CoreFunctions.getInstance());
    }

    //@SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        Player player = event.getPlayer();
        String command[] = event.getMessage().split(" ");

        if (command[0].equalsIgnoreCase("/tp"))
        {
            if (command.length == 1)
            {
                PlayerProfile profile = CoreData.getProfile(player);
                if (profile.isModMode())
                {
                    if (plugin.getServer().getPlayer(command[1]) != null)
                    {
                        plugin.getCoreMethods().teleport(player, plugin.getServer().getPlayer(command[1]).getLocation());
                    }
                    else
                    {
                        plugin.getCoreErrors().playerNotOnline(player);
                    }
                }
                else
                {
                    plugin.getCoreErrors().enableModMode(player);
                }
            }
            else
            {
                plugin.getCoreErrors().commandSantaxError(player, "/tp", "[PlayerName] | This will teleport you to the player.");
            }
        }
    }
}