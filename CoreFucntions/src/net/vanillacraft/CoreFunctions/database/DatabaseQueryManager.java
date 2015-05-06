package net.vanillacraft.CoreFunctions.database;

import net.vanillacraft.CoreFunctions.interfaces.Database;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Mr_Little_Kitty on 5/3/2015.
 */
public class DatabaseQueryManager
{
    private ScheduledExecutorService executor;
    private Database database;

    public DatabaseQueryManager(Database database, int threads)
    {
        this.database = database;
        if(threads % 2 != 0)
            threads++;
        executor = Executors.newScheduledThreadPool(threads);

        int x;
        for(x = 0; x < threads/2; x++)
            executor.submit(new DBQueryAsync(database,executor));
        for(x = 0; x <threads/2;x++)
            executor.submit(new DBQueryAsync(database,executor));
    }
}
