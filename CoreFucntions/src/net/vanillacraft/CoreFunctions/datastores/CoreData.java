package net.vanillacraft.CoreFunctions.datastores;

import net.vanillacraft.CoreFunctions.interfaces.Database;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import net.vanillacraft.CoreFunctions.utils.Loc;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by ryan on 5/5/2015.
 */
public class CoreData implements Listener
{

    private Database database;

    private CoreFunctions plugin;

    private static Map<UUID,PlayerProfile> profiles;

    public static PlayerProfile getProfile(Player player)
    {
        return getProfile(player.getUniqueId());
    }

    public static PlayerProfile getProfile(UUID id)
    {
        return profiles.get(id);
    }

    public static void clearProfiles()
    {
        profiles.clear();
    }

    //    private HashMap<UUID, Long> teleportTimers = new HashMap<>();
    //    private HashMap<UUID, opMode> modMode = new HashMap<>();
    //
    //    private HashMap<UUID, Location> playerHomeLocations = new HashMap<>();
    //    private HashMap<UUID, Long> playerSetHomeCoolDown = new HashMap<>();

    //    private final long SETHOMECOOLDOWN = (1000 * 60) * 60;
    //    private final long TELEPORTCOOLDOWN = (1000 * 60) * 30;


    private Location spawnLocation = new Location(plugin.getServer().getWorld("world"), 0, 0, 0);

    public CoreData(CoreFunctions plugin, Database database)
    {
        Bukkit.getPluginManager().registerEvents(this,plugin);
        this.plugin = plugin;
        this.database = database;
        this.profiles = new HashMap<>();
    }

    public Database getDatabase()
    {
        return database;
    }

    public Location getSpawnLocation()
    {
        return spawnLocation;
    }

    @EventHandler(priority = EventPriority.NORMAL.LOWEST,ignoreCancelled = true)
    public void playerProfileLoad(PlayerJoinEvent event)
    {
        Player p = event.getPlayer();
        if(!profiles.containsKey(p.getUniqueId()))
        {
            profiles.put(p.getUniqueId(), new PlayerProfile(p.getUniqueId(), p.getName()));
            CoreFunctions.logInfoMessage("Created player profle for "+p.getName());
        }
    }


    //    public boolean isModMode(UUID uuid)
    //    {
    //        if (modMode.containsKey(uuid))
    //        {
    //            if (modMode.get(uuid).isEnabled())
    //            {
    //                return true;
    //            }
    //        }
    //        return false;
    //    }
    //
    //    public int getMinutesRemainTeleport(UUID uuid)
    //    {
    //        if (teleportTimers.containsKey(uuid))
    //        {
    //            long milsReamin = teleportTimers.get(uuid) - System.currentTimeMillis();
    //            return (int) ((milsReamin / 1000) / 60);
    //        }
    //        else
    //        {
    //            return 0;
    //        }
    //    }
    //
    //    public int getMinutesRemainSetHome(UUID uuid)
    //    {
    //        if (playerSetHomeCoolDown.containsKey(uuid))
    //        {
    //            long milsRemain = playerSetHomeCoolDown.get(uuid) - System.currentTimeMillis();
    //            return (int) ((milsRemain / 1000) / 60);
    //        }
    //        else
    //        {
    //            return 0;
    //        }
    //    }
    //
    //    public Location getPlayerHomeLocation(UUID uuid)
    //    {
    //        return playerHomeLocations.get(uuid);
    //    }
    //
    //    public long getPlayerSetHomeCoolDown(UUID uuid)
    //    {
    //        if (playerSetHomeCoolDown.containsKey(uuid))
    //        {
    //            return playerSetHomeCoolDown.get(uuid);
    //        }
    //        else
    //        {
    //            return 0;
    //        }
    //    }
    //
        public void setPlayerHome(UUID uuid, Location location)
        {
//            if (playerHomeLocations.containsKey(uuid))
//            {
//                playerHomeLocations.remove(uuid);
//            }
//
//            if (playerSetHomeCoolDown.containsKey(uuid))
//            {
//                playerSetHomeCoolDown.remove(uuid);
//            }

            getProfile(uuid).addDelay(Delay.SETHOME);
            getProfile(uuid).setData("Home",new Loc(location,true));

            SetHomeRecord record = new SetHomeRecord(uuid, location);
            getDatabase().submitInsertRecord(record);

        }

}
