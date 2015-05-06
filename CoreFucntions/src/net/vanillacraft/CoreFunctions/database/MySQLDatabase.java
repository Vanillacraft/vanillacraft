package net.vanillacraft.CoreFunctions.database;


import net.vanillacraft.CoreFunctions.interfaces.DBLogQuery;
import net.vanillacraft.CoreFunctions.interfaces.DBQuery;
import net.vanillacraft.CoreFunctions.interfaces.Database;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Mr_Little_Kitty on 5/3/2015.
 */
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
            getConnection();
            return true;
        }
        catch (Exception e)
        {
            CoreFunctions.logInfoMessage("Could not locate MySQL Driver!");
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
        try
        {
            if (connection == null || connection.isClosed())
            {
                connection = DriverManager.getConnection("jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database, this.user, this.password);
            }
        }
        catch (SQLException e)
        {
            CoreFunctions.logInfoMessage("Error connecting to the MySQL database!");
            e.printStackTrace();
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
