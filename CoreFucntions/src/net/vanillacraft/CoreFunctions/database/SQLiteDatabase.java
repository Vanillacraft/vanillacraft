package net.vanillacraft.CoreFunctions.database;


import net.vanillacraft.CoreFunctions.interfaces.DBLogQuery;
import net.vanillacraft.CoreFunctions.interfaces.DBQuery;
import net.vanillacraft.CoreFunctions.interfaces.Database;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Mr_Little_Kitty on 5/3/2015.
 */
public class SQLiteDatabase implements Database
{
    private File file;
    private Connection connection;
    private ConcurrentLinkedQueue<DBQuery> queries;
    private ConcurrentLinkedQueue<DBLogQuery> logQueries;

    public SQLiteDatabase(File file)
    {
        this.file = file;
        queries = new ConcurrentLinkedQueue<>();
        logQueries = new ConcurrentLinkedQueue<>();
    }

    @Override
    public boolean isUseable()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(new StringBuilder().append("jdbc:sqlite:").append(file == null ? "memory:" : file.getAbsolutePath()).toString());
            return true;
        }
        catch (ClassNotFoundException e)
        {
            CoreFunctions.logInfoMessage("Could not locate SQLite Driver!");
            return false;
        }
        catch (SQLException e)
        {
            CoreFunctions.logInfoMessage("Error establishing the SQLite connection!");
            return false;
        }
    }

    @Override
    public void submitQuery(final DBQuery query)
    {
        queries.add(query);
    }

    @Override
    public void submitLogQuery(final DBLogQuery logQuery)
    {
        logQueries.add(logQuery);
    }

    //TODO---Dont know if synchronized will help here at all
    @Override
    public synchronized void runSyncUpdate(final String query)
    {
        try
        {
            connection.createStatement().execute(query);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection()
    {
        if (connection == null)
        {
            try
            {
                connection = DriverManager.getConnection(new StringBuilder().append("jdbc:sqlite:").append(file == null ? "memory:" : file.getAbsolutePath()).toString());
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return connection;
    }

    @Override
    public DBLogQuery getNextLogQuery()
    {
        return logQueries.poll();
    }

    @Override
    public DBQuery getNextQuery()
    {
        return queries.poll();
    }
}
