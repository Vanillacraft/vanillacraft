package com.gmail.nuclearcat1337.griefprotect.database;

import com.gmail.nuclearcat1337.griefprotect.interfaces.DBQuery;
import com.gmail.nuclearcat1337.griefprotect.interfaces.Database;
import com.gmail.nuclearcat1337.griefprotect.main.GriefProtect;
import org.bukkit.Bukkit;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class DBQueryAsync implements Runnable
{
    private Database database;

    public DBQueryAsync(Database database)
    {
        this.database = database;
        Bukkit.getScheduler().runTaskLaterAsynchronously(GriefProtect.getInstance(), this, 5);
    }

    public void run()
    {
        DBQuery dbQuery;
        while((dbQuery = database.getNextQuery()) != null)
        {
            try
            {
                Statement statement = database.getConnection().createStatement();
                if (dbQuery.isCallback())
                {
                    try
                    {
                        statement.execute(dbQuery.getQuery());
                        dbQuery.setResult(statement.getResultSet());
                        Bukkit.getScheduler().scheduleSyncDelayedTask(GriefProtect.getInstance(), dbQuery);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        Bukkit.getLogger().log(Level.SEVERE, "DBQueryPlugin: Error executing setResult: " + e.getMessage());
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
                Bukkit.getLogger().log(Level.SEVERE, "DBQueryPlugin: Error executing query: " + e.getMessage() + " -- " + dbQuery.getQuery());
                break;
            }
        }

        Bukkit.getScheduler().runTaskLaterAsynchronously(GriefProtect.getInstance(), this, 5);
    }
}
