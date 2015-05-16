package net.vanillacraft.Zones.main;

import net.vanillacraft.CoreFunctions.datastores.PlayerProfile;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import net.vanillacraft.Factions.datastore.Faction;
import net.vanillacraft.Zones.datastores.Zone;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by ryan on 5/6/2015.
 */
public class Zones extends JavaPlugin
{

    private static Zones instance;

    private HashMap<UUID, Long> warningTimeout = new HashMap<UUID, Long>();

    private ArrayList<String> worldsWithZones = new ArrayList<>();
    private ArrayList<Zone> zoneList = new ArrayList<>();

    private CoreFunctions coreFunctions;

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
        coreFunctions = (CoreFunctions) getServer().getPluginManager().getPlugin("CoreFunctions");
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

    public Boolean playerHasPermissionBuild(Player player, Zone zone)
    {
        PlayerProfile profile = coreFunctions.getCoreData().getProfile(player);
        if (zone.isOpOnly())
        {
            if (profile.isModMode())
            {
                return true;
            }

            return false;
        }
        else if (zone.isDonor())
        {
            if (profile.is("Donor") || profile.isModMode())
            {
                return true;
            }

            return false;
        }

        if (zone.getFactionName().equalsIgnoreCase(profile.getData("Faction", Faction.class).getName()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void notifyPlayer(Player player, Zone zone)
    {
        Long time = warningTimeout.get(player.getUniqueId());

        if (time == null || time < System.currentTimeMillis())
        {
            warningTimeout.put(player.getUniqueId(), System.currentTimeMillis() + 10000);
            coreFunctions.getCoreErrors().notifyPlayerModifyPlace(player, zone.getName());
        }
    }

    public void warnPlayer(Player player, Zone zone)
    {
        Long time = warningTimeout.get(player.getUniqueId());

        if(time == null || time < System.currentTimeMillis()){
            warningTimeout.put(player.getUniqueId(), System.currentTimeMillis() + 10000);
            coreFunctions.getCoreErrors().notifyPlayerCantModifyPlace(player, zone.getName());
        }
    }

}
