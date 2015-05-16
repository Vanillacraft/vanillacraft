package net.vanillacraft.Zones.listeners;

import net.vanillacraft.Zones.datastores.Zone;
import net.vanillacraft.Zones.main.Zones;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Created by ryan on 5/13/2015.
 */
public class onBlockPlace implements Listener
{
    private Zones plugin;

    public onBlockPlace(Zones plugin)
    {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, Zones.getInstance());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if (!event.isCancelled())
        {
            Zone blockZone = plugin.getZone(event.getBlock().getLocation());
            Player player = event.getPlayer();

            if (blockZone != null)
            {
                if (blockZone.isProtect())
                {
                    if (!blockZone.getIgnoreBlock().contains(event.getBlock().getType()))
                    {
                        if (plugin.playerHasPermissionBuild(player, blockZone))
                        {
                            plugin.notifyPlayer(player, blockZone);
                        }
                        else
                        {
                            plugin.warnPlayer(player, blockZone);
                            event.setCancelled(true);
                        }
                    }
                }
                else
                {
                    if(!plugin.playerHasPermissionBuild(player, blockZone)){
                        plugin.warnPlayer(player,blockZone);
                        event.setCancelled(true);
                    }
                }
            } else {
                //TODO: We need to make a log of some sort so that we know where this is
                event.setCancelled(true);
            }

        }
    }


}
