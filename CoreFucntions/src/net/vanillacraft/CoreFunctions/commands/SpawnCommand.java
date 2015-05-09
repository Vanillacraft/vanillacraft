package net.vanillacraft.CoreFunctions.commands;

import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Created by ryan on 5/5/2015.
 */
public class SpawnCommand implements Listener
{

    private CoreFunctions plugin;

    public SpawnCommand(CoreFunctions plugin)
    {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, CoreFunctions.getInstance());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        if (!event.isCancelled())
        {
            Player player = event.getPlayer();
            String command[] = event.getMessage().split(" ");

            if (command[0].equalsIgnoreCase("/spawn"))
            {
                if (player.getLocation().getWorld() == plugin.getCoreData().getSpawnLocation().getWorld())
                {
                    if (plugin.getCoreMethods().isModMode(player))
                    {
                        normalTeleport(player, plugin.getCoreData().getSpawnLocation(), true);
                    }
                    else
                    {
                        normalTeleport(player, plugin.getCoreData().getSpawnLocation());
                    }
                }
                else
                {
                    plugin.getCoreErrors().mustBeInWorld(player, "main world");
                }
            }
        }
    }

    public void normalTeleport(Player player, Location location)
    {
        normalTeleport(player, location, false);
    }

    public void normalTeleport(Player player, Location location, Boolean op)
    {
        plugin.getCoreMethods().teleport(player, location, op);
    }

}