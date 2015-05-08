package net.vanillacraft.Zones.main;

import net.vanillacraft.Zones.datastores.Zone;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

/**
 * Created by ryan on 5/6/2015.
 */
public class Zones extends JavaPlugin
{

    private static Zones instance;

    private ArrayList<String> worldsWithZones = new ArrayList<>();
    private ArrayList<Zone> zoneList = new ArrayList<>();

    public static JavaPlugin getInstance()
    {
        return instance;
    }

    public static void logInfoMessage(String message)
    {
        Bukkit.getLogger().info("[Zones] " + message);
    }

    @Override
    public void onEnable()
    {
        instance = this;
    }

    @Override
    public void onDisable()
    {
        Bukkit.getScheduler().cancelTasks(this);
    }

    public Zone getZone(Player player)
    {
        return getZone(player.getLocation());
    }

    public Zone getZone(Location loc)
    {
        if (!worldsWithZones.contains(loc.getWorld()))
        {
            return null;
        }

        int loc_x = loc.getBlockX();
        int loc_y = loc.getBlockY();
        int loc_z = loc.getBlockZ();

        for (Zone z : zoneList)
        {
            if (loc_x >= z.getMin_x() && loc_x <= z.getMax_x() && loc_y >= z.getMin_y() && loc_y <= z.getMax_y() && loc_z >= z.getMin_z() && loc_z <= z.getMax_z())
            {
                if (!z.isCircle())
                {
                    return z;
                }
                else if ((Math.pow(loc.getX() - z.getCenter_x(), 2) + Math.pow(loc.getZ() - z.getCenter_z(), 2)) < Math.pow(z.getRadius(), 2))
                {
                    return z;
                }
            }
        }

        return null;
    }

}
