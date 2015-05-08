package net.vanillacraft.CoreFunctions.database;

import net.vanillacraft.CoreFunctions.interfaces.Database;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Mr_Little_Kitty on 5/3/2015.
 */
public class DatabaseQueryManager implements Listener
{
    private List<DisableableThread> threads;

    public DatabaseQueryManager(Database database, int selectThreads, int insertThreads)
    {
        Bukkit.getPluginManager().registerEvents(this,CoreFunctions.getInstance());
        threads = new ArrayList<>();
        int i;
        for(i = 0; i < selectThreads; i++)
        {
            SelectThread t = new SelectThread(database);
            threads.add(t);
            t.start();
            CoreFunctions.logInfoMessage("Created Select Thread #"+(i+1));
        }
        for(i = 0; i < insertThreads; i++)
        {
            InsertThread t = new InsertThread(database);
            threads.add(t);
            t.start();
            CoreFunctions.logInfoMessage("Created Insert Thread #" + (i + 1));
        }
    }

    @EventHandler
    public void onDisable(PluginDisableEvent event)
    {
        if(event.getPlugin().getName().equals(CoreFunctions.getInstance().getName()))
        {
            for(DisableableThread t : threads)
                t.disable();
        }
    }
}
