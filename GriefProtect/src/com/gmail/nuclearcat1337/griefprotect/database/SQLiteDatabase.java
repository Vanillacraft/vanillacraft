package com.gmail.nuclearcat1337.griefprotect.database;

import com.gmail.nuclearcat1337.griefprotect.interfaces.DBLogQuery;
import com.gmail.nuclearcat1337.griefprotect.interfaces.DBQuery;
import com.gmail.nuclearcat1337.griefprotect.interfaces.Database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;

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
        catch (Exception e)
        {
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
