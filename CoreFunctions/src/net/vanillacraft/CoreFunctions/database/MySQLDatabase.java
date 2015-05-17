package net.vanillacraft.CoreFunctions.database;

import net.vanillacraft.CoreFunctions.interfaces.Database;
import net.vanillacraft.CoreFunctions.interfaces.InsertRecord;
import net.vanillacraft.CoreFunctions.interfaces.SelectRecord;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
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

    //private Connection connection;
    private Map<Long,Connection> connectionMap;
    private ConcurrentLinkedQueue<SelectRecord> selectRecords;
    private ConcurrentLinkedQueue<InsertRecord> insertRecords;

    public MySQLDatabase(String hostname, String port, String database, String username, String password)
    {
        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.user = username;
        this.password = password;
        connectionMap = new HashMap<>();
        selectRecords = new ConcurrentLinkedQueue<>();
        insertRecords = new ConcurrentLinkedQueue<>();
    }

    @Override
    public boolean isUseable()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            //getConnection();
            return true;
        }
        catch (Exception e)
        {
            CoreFunctions.logInfoMessage("Could not locate MySQL Driver!");
            return false;
        }
    }

    @Override
    public void submitSelectRecord(final SelectRecord record)
    {
        selectRecords.add(record);
    }

    @Override
    public void submitInsertRecord(final InsertRecord record)
    {
        insertRecords.add(record);
    }

    @Override
    public void runSyncUpdate(final String query)
    {
        try
        {
            getConnection(Long.MAX_VALUE).createStatement().execute(query);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection(long threadID)
    {
        //TODO---Keep a timestamp of the last query time. Then if its like greater than 4 hours ago, close the connection and reconnect.

        Connection connection = connectionMap.get(new Long(threadID));
        if(connection == null)
        {
            try
            {
                connection = DriverManager.getConnection("jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database, this.user, this.password);
                connectionMap.put(new Long(threadID),connection);
            }
            catch (SQLException e)
            {
                CoreFunctions.logInfoMessage("Error connecting to the MySQL database!");
                e.printStackTrace();
            }
        }
        return connection;
    }

    @Override
    public InsertRecord nextInsertRecord()
    {
        return insertRecords.poll();
    }

    @Override
    public SelectRecord nextSelectRecord()
    {
        return selectRecords.poll();
    }
}
