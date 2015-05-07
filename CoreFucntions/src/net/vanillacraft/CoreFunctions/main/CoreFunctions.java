package net.vanillacraft.CoreFunctions.main;

import net.vanillacraft.CoreFunctions.database.DatabaseQueryManager;
import net.vanillacraft.CoreFunctions.database.MySQLDatabase;
import net.vanillacraft.CoreFunctions.database.SQLiteDatabase;
import net.vanillacraft.CoreFunctions.datastores.CoreData;
import net.vanillacraft.CoreFunctions.interfaces.Database;
import net.vanillacraft.CoreFunctions.utils.CoreMethods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import javax.xml.crypto.Data;
import java.io.File;
import java.util.logging.Level;

/**
 * Created by ryan on 5/5/2015.
 * This plugin is ment to provide an interface for all VC plugins to talk to each other.
 *
 * For example if you want to teleport a player you would ref a function in this plugin
 * which would handle the checking of how soon a player is allowed to TP so that all tp
 * methods would have the exact same output and already the time checking is already
 * delt with.
 */
public class CoreFunctions extends JavaPlugin
{
    //TODO----Handling database timeouts is annoying...Youre supposed to handle SQLExceptions and use those to determine if it timed out

    private static CoreFunctions instance;

    private CoreData coredata;
    private CoreMethods coremethods;

    public CoreData getCoredata()
    {
        //Youre Welcome. -MLK
        return coredata;
    }

    public CoreMethods getCoremethods()
    {
        //Youre Welcome. -MLK
        return coremethods;
    }

    public static JavaPlugin getInstance()
    {
        return instance;
    }

    public static void logError(String message, Class<?> clazz, int errorNumber)
    {
        Bukkit.getLogger().log(Level.SEVERE,"[CoreFunctions] Class={"+clazz.getSimpleName()+"} Error "+errorNumber+": "+message);
    }

    public static void logError(String message, Class<?> clazz)
    {
        logError(message, clazz, 1);
    }

    public static void logInfoMessage(String message)
    {
        Bukkit.getLogger().info("[CoreFunctions] " + message);
    }

    public static void logWarning(String message)
    {
        Bukkit.getLogger().warning("[CoreFunctions] " + message);
    }


    @Override
    public void onEnable() {
        instance = this;

        Database db = setupDatabase();

        if(db == null)
        {
            logWarning("Could not connect to a database. Disabling.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        coredata = new CoreData(this,db);
        coremethods = new CoreMethods(this);
    }

    @Override
    public void onDisable()
    {
        Bukkit.getScheduler().cancelTasks(this);
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
        boolean saveConfig = false;
        ConfigurationSection config = getConfig();

        ConfigurationSection db = config.getConfigurationSection("Database");
        if(db == null)
        {
            db = config.createSection("Database");
            saveConfig = true;
        }
        saveConfig = setIfNotSet(db,"Use-MySQL",true);
        saveConfig = setIfNotSet(db, "Number-Of-DB-Threads",3);
        ConfigurationSection mysql = db.getConfigurationSection("MySQL");
        if(mysql == null)
        {
            mysql = db.createSection("MySQL");
            saveConfig = true;
        }
        saveConfig = setIfNotSet(mysql,"Host-Name", "Test");
        saveConfig = setIfNotSet(mysql,"Port", "Test");
        saveConfig = setIfNotSet(mysql,"User", "Test");
        saveConfig = setIfNotSet(mysql,"Password", "Test");
        saveConfig = setIfNotSet(mysql,"Database", "Test");

        if(saveConfig)
            saveConfig();
    }

    private boolean setIfNotSet(ConfigurationSection section, String path, Object value)
    {
        if(!section.isSet(path))
        {
            section.set(path,value);
            return true;
        }
        return false;
    }


    private void tableCheck(Database database)
    {
        //TODO----------Add "IF NOT EXISTS" to all of the table creations

//        database.runSyncUpdate("CREATE TABLE IF NOT EXISTS tbl_player_access (col_id INTEGER AUTO_INCREMENT, col_owner CHAR(36), col_player CHAR(36), col_x INTEGER , col_y INTEGER , col_z INTEGER , col_range INTEGER , col_allowed_blocks VARCHAR(100), PRIMARY KEY (col_id));");
//        database.runSyncUpdate("CREATE TABLE IF NOT EXISTS tbl_block_log (col_id INTEGER AUTO_INCREMENT, col_timestamp TIMESTAMP, col_action VARCHAR(17), col_player CHAR(36), col_item_type VARCHAR(50), col_player_x DOUBLE PRECISION, col_player_y DOUBLE PRECISION, col_player_z DOUBLE PRECISION, col_player_pitch FLOAT, col_player_yaw FLOAT, col_block_material VARCHAR(30), col_block_data SMALLINT, col_block_x INTEGER, col_block_y INTEGER, col_block_z INTEGER, col_content VARCHAR(255), col_cancelled Boolean, col_world VARCHAR(50), col_public Boolean, PRIMARY KEY (col_id));");
//        database.runSyncUpdate("CREATE TABLE IF NOT EXISTS tbl_griefprotect_log (col_id INTEGER AUTO_INCREMENT, col_timestamp TIMESTAMP, col_action VARCHAR(17), col_player CHAR(36), col_owner CHAR(36), col_block_material VARCHAR(50), col_block_data SMALLINT, col_content VARCHAR(255), col_x INTEGER, col_y INTEGEr, col_z INTEGER, col_allowed Boolean, col_world VARCHAR(50), PRIMARY KEY (col_id));");
    }
}