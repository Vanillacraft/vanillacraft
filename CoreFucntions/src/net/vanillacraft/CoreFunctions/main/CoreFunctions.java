package net.vanillacraft.CoreFunctions.main;

import net.vanillacraft.CoreFunctions.datastores.CoreData;
import net.vanillacraft.CoreFunctions.utils.CoreMethods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

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

    public CoreData coredata;
    public CoreMethods coremethods;

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

        coredata = new CoreData(this);
        coremethods = new CoreMethods(this);
    }

    @Override
    public void onDisable()
    {
        Bukkit.getScheduler().cancelTasks(this);
    }


}