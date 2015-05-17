package net.vanillacraft.CoreFunctions.listeners;

import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;

/**
 * Created by ryan on 5/15/2015.
 */
public class MobControl implements Listener
{

    private CoreFunctions plugin;

    public MobControl(CoreFunctions plugin)
    {
        Bukkit.getPluginManager().registerEvents(this, CoreFunctions.getInstance());
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityChangeBlock(EntityChangeBlockEvent event)
    {
        if (!event.isCancelled())
        {
            if (event.getEntity() instanceof Enderman || event.getEntity() instanceof Wither || event.getEntity() instanceof Ghast)
            {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCreatureSpawn(CreatureSpawnEvent event)
    {
        if (!event.isCancelled())
        {
            Location loc = event.getLocation();
            if (event.getEntity() instanceof Monster)
            {
                if (Math.abs(loc.getBlockX()) < plugin.getCoreData().getSpawnSize() && Math.abs(loc.getBlockZ()) < plugin.getCoreData().getSpawnSize())
                {
                    event.setCancelled(true);
                }
            }
        }
    }


}
