package net.vanillacraft.CoreFunctions.datastores;

import net.vanillacraft.CoreFunctions.interfaces.Database;
import net.vanillacraft.CoreFunctions.interfaces.ILogger;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created by ryan on 5/5/2015.
 */
public class CoreData {

    private Database database;

    private CoreFunctions plugin;
    private ILogger logger;

    private HashMap<Player, Long> teleportTimers = new HashMap<>();
    private HashMap<Player, opMode> modMode = new HashMap<Player, opMode>();

    private Location spawnLocation = new Location(plugin.getServer().getWorld("world"), 0,0,0);

    public CoreData(CoreFunctions plugin, Database database, ILogger logger){
        this.plugin = plugin;
        this.database = database;
        this.logger = logger;
    }

    public ILogger getLogger()
    {
        return logger;
    }


    public Database getDatabase()
    {
        return database;
    }


    public Location getSpawnLocation(){
        return spawnLocation;
    }

    public boolean isModMode(Player player){
        if(modMode.containsKey(player)){
            if(modMode.get(player).isEnabled()){
                return true;
            }
        }
        return false;
    }

    public int getMinutesRemainTeleport(Player player){
        if(teleportTimers.containsKey(player)){
            long milsReamin = teleportTimers.get(player) - System.currentTimeMillis();
            int minRemain = (int)((milsReamin / 1000) / 60);
            return minRemain;
        } else {
            return 0;
        }
    }

}
