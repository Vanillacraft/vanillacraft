package com.gmail.nuclearcat1337.griefprotect.worldLogger;

import com.gmail.nuclearcat1337.griefprotect.interfaces.WorldLogRecord;
import com.gmail.nuclearcat1337.griefprotect.main.GriefProtect;
import com.gmail.nuclearcat1337.griefprotect.util.Provider;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogThread extends Thread
{
    private final Logger log;
    private Provider<WorldLogRecord> provider;

    public boolean enabled = true;

    private Map<RecordType,String> statements;
    private Connection connection;

    public LogThread(Connection connection, Provider<WorldLogRecord> provider)
    {
        this.connection = connection;
        this.log = Bukkit.getLogger();
        this.provider = provider;
        statements = new EnumMap<RecordType, String>(RecordType.class);

        statements.put(RecordType.BlockLog,"INSERT INTO tbl_block_log (col_timestamp, col_action, col_player, col_item_type, col_player_x, col_player_y, col_player_z, col_player_pitch, col_player_yaw, col_block_material, col_block_data, col_block_x, col_block_y, col_block_z, col_content, col_cancelled, col_world, col_public) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        statements.put(RecordType.GriefLog,"INSERT INTO tbl_griefprotect_log (col_timestamp, col_action, col_player, col_owner, col_block_material, col_block_data, col_content, col_x, col_y, col_z, col_allowed, col_world) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
    }

//    private PreparedStatement getStatementForType(RecordType type)
//    {
//        try
//        {
//            return connection.prepareStatement(statements.get(type));
//        } catch (SQLException e)
//        {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public PreparedStatement getStatement(RecordType type, Map<RecordType,PreparedStatement> cache)
    {
        PreparedStatement statement = cache.get(type);
        if(statement == null)
        {
            try
            {
                statement = connection.prepareStatement(statements.get(type));
                cache.put(type,statement);
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
        GriefProtect.logInfoMessage("The logging thread has started.");

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
                GriefProtect.logWarning("The logging thread has been interrupted: "+e.getMessage());
                break;
            }
            Map<RecordType,PreparedStatement> statementCache = new HashMap<>();

            //Bukkit.getLogger().info("We did this");
            WorldLogRecord record;
            while((record = provider.provide()) != null)
            {
                PreparedStatement statement = getStatement(record.getType(),statementCache);
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

//        try
//        {
//           for(PreparedStatement statement : statements.values())
//               statement.close();
//        }
//        catch (SQLException e)
//        {
//            log.log(Level.SEVERE, "WorldLogThread: Error closing connection to database: " + e.getMessage());
//        }

        //log.info("WorldLogThread ended");
        GriefProtect.logInfoMessage("The logging thread has ended.");
    }
}
