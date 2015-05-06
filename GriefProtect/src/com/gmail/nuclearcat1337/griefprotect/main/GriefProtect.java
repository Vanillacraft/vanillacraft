package com.gmail.nuclearcat1337.griefprotect.main;

import com.gmail.nuclearcat1337.griefprotect.database.DatabaseQueryManager;
import com.gmail.nuclearcat1337.griefprotect.database.MySQLDatabase;
import com.gmail.nuclearcat1337.griefprotect.database.SQLiteDatabase;
import com.gmail.nuclearcat1337.griefprotect.griefData.GriefData;
import com.gmail.nuclearcat1337.griefprotect.interfaces.Database;
import com.gmail.nuclearcat1337.griefprotect.interfaces.ILogger;
import com.gmail.nuclearcat1337.griefprotect.queries.GriefProtectPlayerAccessInit;
import com.gmail.nuclearcat1337.griefprotect.worldLogger.WorldLogger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

public class GriefProtect extends JavaPlugin
{
    //Database Schema
    //------Player Allows
    //"CREATE TABLE tbl_player_access (col_owner VARCHAR(17), col_player VARCHAR(17), col_x INTEGER , col_y INTEGER , col_z INTEGER , col_range INTEGER , col_allowed_blocks VARCHAR(100));"
    private static GriefProtect instance;
    public static JavaPlugin getInstance()
    {
        return instance;
    }

    public static void logInfoMessage(String message)
    {
        Bukkit.getLogger().info("[GriefProtect] "+message);
    }

    public static void logWarning(String message, Level level)
    {
        Bukkit.getLogger().log(level,"[GriefProtect] "+message);
    }


    private GriefData data;

    @Override
    public void onEnable()
    {
        instance = this;

        Database database = setupDatabase(); //Establish the database connection and setup the tables

        if(database == null)
        {
            Bukkit.getPluginManager().disablePlugin(this);
            logInfoMessage("Database connection error. Disabling GriefProtect.");
            return;
        }

        ILogger l = new WorldLogger(setupDatabase()); //Then create the logger using the database and the created tables

        data = new GriefData(l, database, null); //TODO---Actual or fake block watcher implementation

        data.getDatabase().submitQuery(new GriefProtectPlayerAccessInit(data));

       // new AllowCommand(this,data);
       // new RevokeCommand(this,data);

        new GriefProtectListeners(this,data); //self registers
    }

    @Override
    public void onDisable()
    {
        Bukkit.getScheduler().cancelTasks(this);
    }

    public GriefData getData()
    {
        return data;
    }

    private Database setupDatabase()
    {
        createConfigDefaults();
        Database db = null;
        boolean mysql = getConfig().getBoolean("Database.Use-MySQL");
        if(mysql)
        {
            ConfigurationSection mysqlSec = getConfig().getConfigurationSection("Database.MySQL");

            String user,database,password,port,hostname;
            user = mysqlSec.getString("User");
            database = mysqlSec.getString("Database");
            password = mysqlSec.getString("Password");
            port = mysqlSec.getString("Port");
            hostname = mysqlSec.getString("Host-Name");

            db = new MySQLDatabase(hostname,port,database,user,password);
        }
        else
        {
            File file = new File(this.getDataFolder(),"GriefProtect.db");
//            if(!file.exists())
//            {
//                try
//                {
//                    file.createNewFile();
//                }
//                catch (IOException e)
//                {
//                    e.printStackTrace();
//                }
//            }
            db = new SQLiteDatabase(file);
            //logInfoMessage("MySQL is turned off. It is currently the only database option and must be enabled.");
        }

        if(db == null || !db.isUseable())
            return null;

        tableCheck(db); //Then make sure all tables are created

        int threads = getConfig().getInt("Database.Number-Of-DB-Threads");

        //Run the database handlers to prepare themselves to start logging queries
        new DatabaseQueryManager(db,threads);
        //new DBLogQueryAsync(db);
        //new DBLogQueryAsync(db);

        return db;
    }

    private void createConfigDefaults()
    {
        int counter = 0;
        ConfigurationSection config = getConfig();

        ConfigurationSection db = config.getConfigurationSection("Database");
        if(db == null)
        {
            db = config.createSection("Database");
            counter++;
        }
        counter += setIfNotSet(db,"Use-MySQL",true);
        counter += setIfNotSet(db, "Number-Of-DB-Threads",3);
        ConfigurationSection mysql = db.getConfigurationSection("MySQL");
        if(mysql == null)
        {
            mysql = db.createSection("MySQL");
            counter++;
        }
        counter += setIfNotSet(mysql,"Host-Name", "Test");
        counter += setIfNotSet(mysql,"Port", "Test");
        counter += setIfNotSet(mysql,"User", "Test");
        counter += setIfNotSet(mysql,"Password", "Test");
        counter += setIfNotSet(mysql,"Database", "Test");

        if(counter > 0)
            saveConfig();
    }

    private int setIfNotSet(ConfigurationSection section, String path, Object value)
    {
        if(!section.isSet(path))
        {
            section.set(path,value);
            return 1;
        }
        return 0;
    }


    private void tableCheck(Database database)
    {
        //TODO----------Add "IF NOT EXISTS" to all of the table creations

        database.runSyncUpdate("CREATE TABLE IF NOT EXISTS tbl_player_access (col_id INTEGER AUTO_INCREMENT, col_owner CHAR(36), col_player CHAR(36), col_x INTEGER , col_y INTEGER , col_z INTEGER , col_range INTEGER , col_allowed_blocks VARCHAR(100), PRIMARY KEY (col_id));");
        database.runSyncUpdate("CREATE TABLE IF NOT EXISTS tbl_block_log (col_id INTEGER AUTO_INCREMENT, col_timestamp TIMESTAMP, col_action VARCHAR(17), col_player CHAR(36), col_item_type VARCHAR(50), col_player_x DOUBLE PRECISION, col_player_y DOUBLE PRECISION, col_player_z DOUBLE PRECISION, col_player_pitch FLOAT, col_player_yaw FLOAT, col_block_material VARCHAR(30), col_block_data SMALLINT, col_block_x INTEGER, col_block_y INTEGER, col_block_z INTEGER, col_content VARCHAR(255), col_cancelled Boolean, col_world VARCHAR(50), col_public Boolean, PRIMARY KEY (col_id));");
        database.runSyncUpdate("CREATE TABLE IF NOT EXISTS tbl_griefprotect_log (col_id INTEGER AUTO_INCREMENT, col_timestamp TIMESTAMP, col_action VARCHAR(17), col_player CHAR(36), col_owner CHAR(36), col_block_material VARCHAR(50), col_block_data SMALLINT, col_content VARCHAR(255), col_x INTEGER, col_y INTEGEr, col_z INTEGER, col_allowed Boolean, col_world VARCHAR(50), PRIMARY KEY (col_id));");
    }
}
