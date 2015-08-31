package net.vanillacraft.CoreFunctions.listeners;

import net.vanillacraft.CoreFunctions.datastores.CoreData;
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
            Entity entity = event.getEntity();
            if (entity instanceof Enderman || entity instanceof Wither || entity instanceof Ghast || entity instanceof Creeper)
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
                if (Math.abs(loc.getBlockX()) < CoreData.getSpawnSize() && Math.abs(loc.getBlockZ()) < CoreData.getSpawnSize())
                {
                    event.getEntity().remove(); //Supposedly this fixes the memory leak of just canceling the event.
                    event.setCancelled(true);
                }
            }
        }
    }
}