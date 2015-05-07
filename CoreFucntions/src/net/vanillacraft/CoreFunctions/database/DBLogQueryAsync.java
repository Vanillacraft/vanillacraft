package net.vanillacraft.CoreFunctions.database;

import net.vanillacraft.CoreFunctions.interfaces.DBLogQuery;
import net.vanillacraft.CoreFunctions.interfaces.Database;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mr_Little_Kitty on 5/3/2015.
 */
class DBLogQueryAsync implements Runnable
{
    private ScheduledExecutorService executor;
    private Database database;
    private String logStatement;

    public DBLogQueryAsync(Database database,ScheduledExecutorService executor)
    {
        this.executor = executor;
        this.database = database;
        logStatement = "SELECT col_player, col_block_material, col_block_data, col_timestamp FROM tbl_block_log WHERE col_action='PLACE' AND col_cancelled=0 AND col_public=0 AND col_world=? AND col_block_x=? AND col_block_y=? AND col_block_z=? ORDER BY col_id DESC LIMIT 1";

        //Bukkit.getScheduler().runTaskLaterAsynchronously(VCMain.getInstance(), this, 5);
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
                    Bukkit.getScheduler().scheduleSyncDelayedTask(CoreFunctions.getInstance(), dbQuery);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    //Bukkit.getLogger().log(Level.SEVERE, "DBQueryPlugin: Error executing setResult: " + e.getMessage());
                    //VCMain.logWarning("Class={"+this.getClass().getSimpleName()+"} Error executing setResult: "+e.getMessage(),Level.SEVERE);
                    CoreFunctions.logError(e.getMessage(), this.getClass(), 1);
                }
            }
            catch (SQLException e)
            {
                //Bukkit.getLogger().log(Level.SEVERE, "DBQueryPlugin: Error executing query: " + e.getMessage());
                //VCMain.logWarning("Class={"+this.getClass().getSimpleName()+"} Error executing query: "+e.getMessage(),Level.SEVERE);
                CoreFunctions.logError(e.getMessage(), this.getClass(), 2);
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

        executor.schedule(this,5, TimeUnit.MILLISECONDS);
        //Bukkit.getScheduler().runTaskLaterAsynchronously(VCMain.getInstance(), this, 5);
    }
}
