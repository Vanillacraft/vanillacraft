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

    @EventHandler(priority = EventPriority.LOWEST,ignoreCancelled = true)
    public void playerProfileLoad(PlayerJoinEvent event)
    {
        Player p = event.getPlayer();
        if(!profiles.containsKey(p.getUniqueId()))
        {
            profiles.put(p.getUniqueId(), new PlayerProfile(p.getUniqueId(), p.getName()));
            CoreFunctions.logInfoMessage("Created player profle for "+p.getName());
            //TODO: populate with more information {ex. home or faction}
        }
    }

    public void setPlayerHome(UUID uuid, Location location)
    {
        getProfile(uuid).addDelay(Delay.SETHOME);
        getProfile(uuid).setData("Home",new Loc(location,true));

        SetHomeRecord record = new SetHomeRecord(uuid, location);
        getDatabase().submitInsertRecord(record);
    }

}
