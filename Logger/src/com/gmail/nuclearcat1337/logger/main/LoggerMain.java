package com.gmail.nuclearcat1337.logger.main;

import net.vanillacraft.CoreFunctions.interfaces.Database;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/*
Created by Mr_Little_Kitty on 5/8/2015
*/
public class LoggerMain extends JavaPlugin
{
    private static LoggerMain instance;
    public static JavaPlugin getInstance()
    {
        return instance;
    }

    public static void logError(String message, Class<?> clazz, int errorNumber)
    {
        Bukkit.getLogger().log(Level.SEVERE, "[Logger] Class={" + clazz.getSimpleName() + "} Error " + errorNumber + ": " + message);
    }

    public static void logError(String message, Class<?> clazz)
    {
        logError(message, clazz, 1);
    }

    public static void logInfoMessage(String message)
    {
        Bukkit.getLogger().info("[Logger] " + message);
    }

    public static void logWarning(String message)
    {
        Bukkit.getLogger().warning("[Logger] " + message);
    }

    @Override
    public void onEnable()
    {
        if(!Bukkit.getServicesManager().isProvidedFor(CoreFunctions.class))
        {
            logInfoMessage("Could not get CoreFunctions!");
            return;
        }

        CoreFunctions functions = Bukkit.getServicesManager().load(CoreFunctions.class);

        Database db = functions.getCoreData().getDatabase();
        tableCheck(db);

        new WorldLogListeners(this,db);
    }

    @Override
    public void onDisable()
    {
        Bukkit.getScheduler().cancelTasks(this);
    }

    private void tableCheck(Database database)
    {
        //TODO----------Add "IF NOT EXISTS" to all of the table creations

        database.runSyncUpdate("CREATE TABLE IF NOT EXISTS tbl_player_access (col_id INTEGER AUTO_INCREMENT, col_owner CHAR(36), col_player CHAR(36), col_x INTEGER , col_y INTEGER , col_z INTEGER, col_world VARCHAR(36), col_range INTEGER , col_allowed_blocks VARCHAR(100), PRIMARY KEY (col_id));");
        database.runSyncUpdate("CREATE TABLE IF NOT EXISTS tbl_block_log (col_id INTEGER AUTO_INCREMENT, col_timestamp TIMESTAMP, col_action VARCHAR(17), col_player CHAR(36), col_item_type VARCHAR(50), col_player_x DOUBLE PRECISION, col_player_y DOUBLE PRECISION, col_player_z DOUBLE PRECISION, col_player_pitch FLOAT, col_player_yaw FLOAT, col_block_material VARCHAR(30), col_block_data SMALLINT, col_block_x INTEGER, col_block_y INTEGER, col_block_z INTEGER, col_content VARCHAR(255), col_cancelled Boolean, col_world VARCHAR(50), col_player_clan INTEGER, col_abandoned Boolean, col_public Boolean, PRIMARY KEY (col_id));");
        database.runSyncUpdate("CREATE TABLE IF NOT EXISTS tbl_griefprotect_log (col_id INTEGER AUTO_INCREMENT, col_timestamp TIMESTAMP, col_action VARCHAR(17), col_player CHAR(36), col_owner CHAR(36), col_block_material VARCHAR(50), col_block_data SMALLINT, col_content VARCHAR(255), col_x INTEGER, col_y INTEGEr, col_z INTEGER, col_allowed Boolean, col_world VARCHAR(50), PRIMARY KEY (col_id));");
    }
}
