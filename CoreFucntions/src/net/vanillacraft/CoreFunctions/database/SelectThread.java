package net.vanillacraft.CoreFunctions.database;

import net.vanillacraft.CoreFunctions.interfaces.Database;
import net.vanillacraft.CoreFunctions.interfaces.InsertRecord;
import net.vanillacraft.CoreFunctions.interfaces.SelectRecord;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/*
Created by Mr_Little_Kitty on 5/7/2015
*/
public class SelectThread extends Thread implements DisableableThread
{
    private Database database;
    private boolean enabled;

    public SelectThread(Database database)
    {
        this.database = database;
        enabled = true;

    }

    public PreparedStatement getStatement(String key, String text, Map<String,PreparedStatement> cache)
    {
        PreparedStatement statement = cache.get(key.toLowerCase());
        if(statement == null)
        {
            try
            {
                statement = database.getConnection(this.getId()).prepareStatement(text);
                cache.put(key.toLowerCase(),statement);
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return statement;
    }

    @Override
    public void run()
    {
        Map<String,PreparedStatement> statementCache = new HashMap<>();
        SelectRecord record;
        while (enabled)
        {
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                CoreFunctions.logWarning("The logging thread has been interrupted: " + e.getMessage());
                break;
            }

            while((record = database.nextSelectRecord()) != null)
            {
                PreparedStatement statement = getStatement(record.getCacheKey(),record.getQuery(),statementCache);
                try
                {
                    record.setParameters(statement);
                    statement.execute();
                    record.callbackAsync(statement.getResultSet());
                    statement.getResultSet().close();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(CoreFunctions.getInstance(),new SyncRun(record));
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }

            for(PreparedStatement statement : statementCache.values())
            {
                try
                {
                    statement.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            statementCache.clear();
        }
    }

    @Override
    public void disable()
    {
        this.enabled = false;
    }

    private class SyncRun implements Runnable
    {
        private SelectRecord record;
        public SyncRun(SelectRecord record)
        {
            this.record = record;
        }

        @Override
        public void run()
        {
            record.runSynchronously();
            record = null;
        }
    }
}


