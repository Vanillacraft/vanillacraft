package net.vanillacraft.CoreFunctions.database;

import net.vanillacraft.CoreFunctions.interfaces.Database;
import net.vanillacraft.CoreFunctions.interfaces.InsertRecord;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/*
Created by Mr_Little_Kitty on 5/7/2015
*/
public class InsertThread extends Thread implements DisableableThread
{
    private Database database;
    private boolean enabled;

    public InsertThread(Database database)
    {
        this.database = database;
        enabled = true;

    }

    public PreparedStatement getStatement(String key, String text, Map<String,PreparedStatement> cache)
    {
        PreparedStatement statement = cache.get(key);
        if(statement == null)
        {
            try
            {
                statement = database.getConnection(this.getId()).prepareStatement(text);
                cache.put(key,statement);
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
        InsertRecord record;
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

            while((record = database.nextInsertRecord()) != null)
            {
                PreparedStatement statement = getStatement(record.getCacheKey(),record.getQuery(),statementCache);
                try
                {
                    record.setParameters(statement);
                    statement.execute();
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

    public void disable()
    {
        enabled = false;
    }
}
