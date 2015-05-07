package net.vanillacraft.Zones.main;

import net.vanillacraft.Zones.datastores.Zone;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by ryan on 5/6/2015.
 */
public class Zones extends JavaPlugin {
    private static Zones instance;

    public static JavaPlugin getInstance()
    {
        return instance;
    }

    public static void logInfoMessage(String message)
    {
        Bukkit.getLogger().info("[Zones] "+message);
    }

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable()
    {
        Bukkit.getScheduler().cancelTasks(this);
    }

    public Zone getZone(Player player){

    }
}
