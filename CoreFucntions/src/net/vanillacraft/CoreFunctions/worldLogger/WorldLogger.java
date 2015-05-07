package net.vanillacraft.CoreFunctions.worldLogger;

import net.vanillacraft.CoreFunctions.interfaces.ILogger;
import net.vanillacraft.CoreFunctions.interfaces.WorldLogRecord;
import net.vanillacraft.CoreFunctions.interfaces.Database;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import net.vanillacraft.CoreFunctions.utils.Provider;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

import java.util.concurrent.ConcurrentLinkedQueue;

public class WorldLogger implements ILogger, Provider<WorldLogRecord>,Listener
{
    private Database database;
    private LogThread thread;

    private ConcurrentLinkedQueue<WorldLogRecord> recordQueue;

    public WorldLogger(Database database)
    {
        Bukkit.getPluginManager().registerEvents(this, CoreFunctions.getInstance());
        this.database = database;
        recordQueue = new ConcurrentLinkedQueue<>();
        thread = new LogThread(database,this);
        thread.start();
        new WorldLogListeners(CoreFunctions.getInstance(),this);
    }

    @Override
    public void log(final WorldLogRecord record)
    {
        recordQueue.add(record);
    }

    @Override
    public WorldLogRecord provide()
    {
        return recordQueue.poll();
    }

    @EventHandler
    public void onDisable(PluginDisableEvent event)
    {
        if(event.getPlugin().getName().equals(CoreFunctions.getInstance().getName()))
            thread.enabled = false;
    }

}
