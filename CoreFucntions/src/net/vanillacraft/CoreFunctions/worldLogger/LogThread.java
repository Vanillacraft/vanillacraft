package net.vanillacraft.CoreFunctions.worldLogger;

import net.vanillacraft.CoreFunctions.interfaces.Database;
import net.vanillacraft.CoreFunctions.interfaces.WorldLogRecord;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import net.vanillacraft.CoreFunctions.utils.Provider;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class LogThread extends Thread
{
    private final Logger log;
    private Provider<WorldLogRecord> provider;

    public boolean enabled = true;

    private Database database;

    public LogThread(Database database, Provider<WorldLogRecord> provider)
    {
        this.database = database;
        this.log = Bukkit.getLogger();
        this.provider = provider;

        //"INSERT INTO tbl_block_log (col_timestamp, col_action, col_player, col_item_type, col_player_x, col_player_y, col_player_z, col_player_pitch, col_player_yaw, col_block_material, col_block_data, col_block_x, col_block_y, col_block_z, col_content, col_cancelled, col_world, col_public) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
       // "INSERT INTO tbl_griefprotect_log (col_timestamp, col_action, col_player, col_owner, col_block_material, col_block_data, col_content, col_x, col_y, col_z, col_allowed, col_world) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
    }

    public PreparedStatement getStatement(String type, String text, Map<String,PreparedStatement> cache)
    {
        PreparedStatement statement = cache.get(type.toLowerCase());
        if(statement == null)
        {
            try
            {
                statement = database.getConnection().prepareStatement(text);
                cache.put(type.toLowerCase(),statement);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return statement;
    }

    public void run()
    {
        //log.info("[GriefProtect] WorldLogThread started");
        CoreFunctions.logInfoMessage("The logging thread has started.");

        while (enabled)
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                //log.warning("WorldLogThread interrupted: " + e.getMessage());
                //GriefProtect.logWarning("The logging thread has been interrupted: "+e.getMessage(), Level.WARNING);
                CoreFunctions.logWarning("The logging thread has been interrupted: "+e.getMessage());
                break;
            }
            Map<String,PreparedStatement> statementCache = new HashMap<>();

            //Bukkit.getLogger().info("We did this");
            WorldLogRecord record;
            while((record = provider.provide()) != null)
            {
                PreparedStatement statement = getStatement(record.getType(),record.getStatementText(),statementCache);
                record.executeRecord(statement);
            }

            for(PreparedStatement statement : statementCache.values())
            {
                try
                {
                    statement.close();
                } catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            statementCache.clear();
            statementCache = null;
        }

        CoreFunctions.logInfoMessage("The logging thread has ended.");
    }
}
