package net.vanillacraft.CoreFunctions.database;

import net.vanillacraft.CoreFunctions.interfaces.Database;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import org.bukkit.Bukkit;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Mr_Little_Kitty on 5/3/2015.
 */
public class DatabaseQueryManager
{
    public DatabaseQueryManager(Database database, int selectThreads, int insertThreads)
    {
        int i;
        for(i = 0; i < selectThreads; i++)
        {
            new SelectThread(database).start();
            CoreFunctions.logInfoMessage("Created Select Thread #"+(i+1));
        }
        for(i = 0; i < insertThreads; i++)
        {
            new InsertThread(database).start();
            CoreFunctions.logInfoMessage("Created Insert Thread #" + (i + 1));
        }
    }
}
