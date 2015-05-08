package net.vanillacraft.CoreFunctions.datastores;

import net.vanillacraft.CoreFunctions.interfaces.Database;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by ryan on 5/5/2015.
 */
public class CoreData
{

    private Database database;

    private CoreFunctions plugin;

    private HashMap<UUID, Long> teleportTimers = new HashMap<>();
    private HashMap<UUID, opMode> modMode = new HashMap<>();

    private HashMap<UUID, Location> playerHomeLocations = new HashMap<>();
    private HashMap<UUID, Long> playerSetHomeCoolDown = new HashMap<>();

    private final long SETHOMECOOLDOWN = (1000 * 60) * 60;
    private final long TELEPORTCOOLDOWN = (1000 * 60) * 30;


    private Location spawnLocation = new Location(plugin.getServer().getWorld("world"), 0, 0, 0);

    public CoreData(CoreFunctions plugin, Database database)
    {
        this.plugin = plugin;
        this.database = database;
    }

    public Database getDatabase()
    {
        return database;
    }


    public Location getSpawnLocation()
    {
        return spawnLocation;
    }

    public boolean isModMode(UUID uuid)
    {
        if (modMode.containsKey(uuid))
        {
            if (modMode.get(uuid).isEnabled())
            {
                return true;
            }
        }
        return false;
    }

    public int getTeleportCooldownDuration()
    {
        return (int) ((TELEPORTCOOLDOWN / 1000) / 60);
    }

    public int getMinutesRemainTeleport(UUID uuid)
    {
        if (teleportTimers.containsKey(uuid))
        {
            long milsReamin = teleportTimers.get(uuid) - System.currentTimeMillis();
            return (int) ((milsReamin / 1000) / 60);
        }
        else
        {
            return 0;
        }
    }

    public int getMinutesRemainSetHome(UUID uuid)
    {
        if (playerSetHomeCoolDown.containsKey(uuid))
        {
            long milsRemain = playerSetHomeCoolDown.get(uuid) - System.currentTimeMillis();
            return (int) ((milsRemain / 1000) / 60);
        }
        else
        {
            return 0;
        }
    }

    public Location getPlayerHomeLocation(UUID uuid)
    {
        return playerHomeLocations.get(uuid);
    }

    public long getPlayerSetHomeCoolDown(UUID uuid)
    {
        if (playerSetHomeCoolDown.containsKey(uuid))
        {
            return playerSetHomeCoolDown.get(uuid);
        }
        else
        {
            return 0;
        }
    }

    public void setPlayerHome(UUID uuid, Location location)
    {

        if (playerHomeLocations.containsKey(uuid))
        {
            playerHomeLocations.remove(uuid);
        }

        if (playerSetHomeCoolDown.containsKey(uuid))
        {
            playerSetHomeCoolDown.remove(uuid);
        }

        SetHomeRecord record = new SetHomeRecord(uuid, location);
        getDatabase().submitInsertRecord(record);

        playerSetHomeCoolDown.put(uuid, System.currentTimeMillis() + SETHOMECOOLDOWN);
        playerHomeLocations.put(uuid, location);
    }

}
