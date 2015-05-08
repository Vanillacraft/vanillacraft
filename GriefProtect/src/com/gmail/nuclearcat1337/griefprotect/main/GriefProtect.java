package com.gmail.nuclearcat1337.griefprotect.main;

import com.gmail.nuclearcat1337.griefprotect.griefData.GriefData;
import com.gmail.nuclearcat1337.griefprotect.queries.GriefProtectPlayerAccessInit;
import net.vanillacraft.CoreFunctions.interfaces.Database;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class GriefProtect extends JavaPlugin
{
    //Database Schema
    //------Player Allows
    //"CREATE TABLE tbl_player_access (col_owner VARCHAR(17), col_player VARCHAR(17), col_x INTEGER , col_y INTEGER , col_z INTEGER , col_range INTEGER , col_allowed_blocks VARCHAR(100));"
    private static GriefProtect instance;
    public static JavaPlugin getInstance()
    {
        return instance;
    }

    public static void logError(String message, Class<?> clazz, int errorNumber)
    {
        Bukkit.getLogger().log(Level.SEVERE, "[GriefProtect] Class={" + clazz.getSimpleName() + "} Error " + errorNumber + ": " + message);
    }

    public static void logError(String message, Class<?> clazz)
    {
        logError(message, clazz, 1);
    }

    public static void logInfoMessage(String message)
    {
        Bukkit.getLogger().info("[GriefProtect] " + message);
    }

    public static void logWarning(String message)
    {
        Bukkit.getLogger().warning("[GriefProtect] " + message);
    }


    private GriefData data;

    @Override
    public void onEnable()
    {
        instance = this;

        if(!Bukkit.getServicesManager().isProvidedFor(CoreFunctions.class))
        {
            logInfoMessage("Could not get the VC core. Disabling.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        CoreFunctions core = Bukkit.getServicesManager().load(CoreFunctions.class);//getRegistration(CoreFunctions.class).getProvider();
        Database db = core.getCoreData().getDatabase();

        //ILogger l = new WorldLogger(db); //Then create the logger using the database and the created tables

        data = new GriefData(db, null); //TODO---Actual or fake block watcher implementation

        db.submitSelectRecord(new GriefProtectPlayerAccessInit(data));

       // new AllowCommand(this,data);
       // new RevokeCommand(this,data);

        new GriefProtectListeners(this,data); //self registers
    }

    @Override
    public void onDisable()
    {
        Bukkit.getScheduler().cancelTasks(this);
    }

    public GriefData getData()
    {
        return data;
    }
}
