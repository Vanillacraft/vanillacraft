package net.vanillacraft.CoreFunctions.main;

import net.vanillacraft.CoreFunctions.datastores.opMode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

/**
 * Created by ryan on 5/5/2015.
 * This plugin is ment to provide an interface for all VC plugins to talk to each other.
 *
 * For example if you want to teleport a player you would ref a function in this plugin
 * which would handle the checking of how soon a player is allowed to TP so that all tp
 * methods would have the exact same output and already the time checking is already
 * delt with.
 */
public class CoreFunctions extends JavaPlugin
{
    private static CoreFunctions instance;

    public HashMap<Player, opMode> modMode;
    public Location spawnLocation = new Location(getServer().getWorld("world"), 0,0,0);

    public static JavaPlugin getInstance()
    {
        return instance;
    }

    public static void logInfoMessage(String message)
    {
        Bukkit.getLogger().info("[CoreFunctions] "+message);
    }

    @Override
    public void onEnable() {
        instance = this;
        modMode = new HashMap<Player, opMode>();
    }

    @Override
    public void onDisable()
    {
        Bukkit.getScheduler().cancelTasks(this);
    }


}