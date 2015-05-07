package com.gmail.nuclearcat1337.griefprotect.main;

import com.gmail.nuclearcat1337.griefprotect.griefData.GriefData;
import com.gmail.nuclearcat1337.griefprotect.interfaces.ILogger;
import com.gmail.nuclearcat1337.griefprotect.queries.GriefProtectPlayerAccessInit;
import com.gmail.nuclearcat1337.griefprotect.worldLogger.WorldLogger;
import net.vanillacraft.CoreFunctions.interfaces.Database;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
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
        Database db = core.getCoredata().getDatabase();

        ILogger l = new WorldLogger(db); //Then create the logger using the database and the created tables

        data = new GriefData(l, db, null); //TODO---Actual or fake block watcher implementation

        db.submitQuery(new GriefProtectPlayerAccessInit(data));

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


    private void tableCheck(Database database)
    {
        //TODO----------Add "IF NOT EXISTS" to all of the table creations

        database.runSyncUpdate("CREATE TABLE IF NOT EXISTS tbl_player_access (col_id INTEGER AUTO_INCREMENT, col_owner CHAR(36), col_player CHAR(36), col_x INTEGER , col_y INTEGER , col_z INTEGER , col_range INTEGER , col_allowed_blocks VARCHAR(100), PRIMARY KEY (col_id));");
        database.runSyncUpdate("CREATE TABLE IF NOT EXISTS tbl_block_log (col_id INTEGER AUTO_INCREMENT, col_timestamp TIMESTAMP, col_action VARCHAR(17), col_player CHAR(36), col_item_type VARCHAR(50), col_player_x DOUBLE PRECISION, col_player_y DOUBLE PRECISION, col_player_z DOUBLE PRECISION, col_player_pitch FLOAT, col_player_yaw FLOAT, col_block_material VARCHAR(30), col_block_data SMALLINT, col_block_x INTEGER, col_block_y INTEGER, col_block_z INTEGER, col_content VARCHAR(255), col_cancelled Boolean, col_world VARCHAR(50), col_public Boolean, PRIMARY KEY (col_id));");
        database.runSyncUpdate("CREATE TABLE IF NOT EXISTS tbl_griefprotect_log (col_id INTEGER AUTO_INCREMENT, col_timestamp TIMESTAMP, col_action VARCHAR(17), col_player CHAR(36), col_owner CHAR(36), col_block_material VARCHAR(50), col_block_data SMALLINT, col_content VARCHAR(255), col_x INTEGER, col_y INTEGEr, col_z INTEGER, col_allowed Boolean, col_world VARCHAR(50), PRIMARY KEY (col_id));");
    }
}
