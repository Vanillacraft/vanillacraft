package net.vanillacraft.CoreFunctions.datastores;

import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by ryan on 5/5/2015.
 */
public class CoreData {

    private CoreFunctions plugin;

    HashMap<Player, Long> teleportTimers = new HashMap<>();
    HashMap<Player, opMode> modMode = new HashMap<Player, opMode>();

    Location spawnLocation = new Location(plugin.getServer().getWorld("world"), 0,0,0);

    public CoreData(CoreFunctions plugin){
        this.plugin = plugin;
    }

    public Location getSpawnLocation(){
        return spawnLocation;
    }

    public boolean isModMode(Player player){
        if(modMode.containsKey(player)){
            if(modMode.get(player).enabled){
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
