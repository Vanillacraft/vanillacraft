package com.gmail.nuclearcat1337.griefprotect.database;

import com.gmail.nuclearcat1337.griefprotect.interfaces.DBLogQuery;
import com.gmail.nuclearcat1337.griefprotect.interfaces.Database;
import com.gmail.nuclearcat1337.griefprotect.main.GriefProtect;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

public class DBLogQueryAsync implements Runnable
{
    private Database database;
    private String logStatement;

    public DBLogQueryAsync(Database database)
    {
        this.database = database;
        //try
       // {
            logStatement = "SELECT col_player, col_block_material, col_block_data, col_timestamp FROM tbl_block_log WHERE col_action='PLACE' AND col_cancelled=0 AND col_public=0 AND col_world=? AND col_block_x=? AND col_block_y=? AND col_block_z=? ORDER BY col_id DESC LIMIT 1";
        //}
        //catch (SQLException e)
       // {
        //    e.printStackTrace();
        //    return;
        //}
        Bukkit.getScheduler().runTaskLaterAsynchronously(GriefProtect.getInstance(), this, 5);
    }

    public void run()
    {
        DBLogQuery dbQuery;
        PreparedStatement statement = null;
        try
        {
            statement = database.getConnection().prepareStatement(this.logStatement);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        while((dbQuery = database.getNextLogQuery()) != null)
        {

            try
            {
                statement.setString(1, dbQuery.getWorld());
                statement.setInt(2, dbQuery.getX());
                statement.setInt(3, dbQuery.getY());
                statement.setInt(4, dbQuery.getZ());

                try
                {
                    dbQuery.setResult(statement.executeQuery());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(GriefProtect.getInstance(), dbQuery);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    //Bukkit.getLogger().log(Level.SEVERE, "DBQueryPlugin: Error executing setResult: " + e.getMessage());
                    GriefProtect.logWarning("Class={"+this.getClass().getSimpleName()+"} Error executing setResult: "+e.getMessage(),Level.SEVERE);
                }
            }
            catch (SQLException e)
            {
                //Bukkit.getLogger().log(Level.SEVERE, "DBQueryPlugin: Error executing query: " + e.getMessage());
                GriefProtect.logWarning("Class={"+this.getClass().getSimpleName()+"} Error executing query: "+e.getMessage(),Level.SEVERE);
                break;
            }
        }

        try
        {
            statement.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        Bukkit.getScheduler().runTaskLaterAsynchronously(GriefProtect.getInstance(), this, 5);
    }
}
