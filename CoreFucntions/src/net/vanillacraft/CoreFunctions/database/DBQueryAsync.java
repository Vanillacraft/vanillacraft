package net.vanillacraft.CoreFunctions.database;


import net.vanillacraft.CoreFunctions.interfaces.DBQuery;
import net.vanillacraft.CoreFunctions.interfaces.Database;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import org.bukkit.Bukkit;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mr_Little_Kitty on 5/3/2015.
 */
public class DBQueryAsync implements Runnable
{
    private ScheduledExecutorService executorService;
    private Database database;

    DBQueryAsync(Database database, ScheduledExecutorService executorService)
    {
        this.executorService = executorService;
        this.database = database;
    }

    public void run()
    {
        DBQuery dbQuery;
        Statement statement;
        while((dbQuery = database.getNextQuery()) != null)
        {
            try
            {
                statement = database.getConnection().createStatement();
                if (dbQuery.isCallback())
                {
                    try
                    {
                        statement.execute(dbQuery.getQuery());
                        dbQuery.setResult(statement.getResultSet());
                        Bukkit.getScheduler().scheduleSyncDelayedTask(CoreFunctions.getInstance(), dbQuery);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        //Bukkit.getLogger().log(Level.SEVERE, "DBQueryPlugin: Error executing setResult: " + e.getMessage());
                        CoreFunctions.logError(e.getMessage(), this.getClass(), 1);
                    }
                }
                else
                {
                    statement.execute(dbQuery.getQuery());
                }

                statement.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
               // Bukkit.getLogger().log(Level.SEVERE, "DBQueryPlugin: Error executing query: " + e.getMessage() + " -- " + dbQuery.getQuery());
                CoreFunctions.logError(e.getMessage(), this.getClass(), 2);
                break;
            }
        }

        executorService.schedule(this,5, TimeUnit.MILLISECONDS);
    }
}
