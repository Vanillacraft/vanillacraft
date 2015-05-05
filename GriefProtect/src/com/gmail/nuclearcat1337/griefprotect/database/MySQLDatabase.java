package com.gmail.nuclearcat1337.griefprotect.database;

import com.gmail.nuclearcat1337.griefprotect.interfaces.DBLogQuery;
import com.gmail.nuclearcat1337.griefprotect.interfaces.DBQuery;
import com.gmail.nuclearcat1337.griefprotect.interfaces.Database;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MySQLDatabase implements Database
{
    private final String user;
    private final String database;
    private final String password;
    private final String port;
    private final String hostname;

    private Connection connection;
    private ConcurrentLinkedQueue<DBQuery> queries;
    private ConcurrentLinkedQueue<DBLogQuery> logQueries;

    public MySQLDatabase(String hostname, String port, String database, String username, String password)
    {
        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.user = username;
        this.password = password;
        queries = new ConcurrentLinkedQueue<>();
        logQueries = new ConcurrentLinkedQueue<>();
    }

    @Override
    public boolean isUseable()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database, this.user, this.password);
            return true;
        }
        catch (Exception e)
        {
            Bukkit.getLogger().info("[GriefProtect] Could not locate MySQL driver!");
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

    @Override
    public void runSyncUpdate(final String query)
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
                connection = DriverManager.getConnection("jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database, this.user, this.password);
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
