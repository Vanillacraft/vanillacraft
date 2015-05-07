package com.gmail.nuclearcat1337.griefprotect.worldLogger;

import com.gmail.nuclearcat1337.griefprotect.interfaces.ILogger;
import com.gmail.nuclearcat1337.griefprotect.interfaces.WorldLogRecord;
import com.gmail.nuclearcat1337.griefprotect.main.GriefProtect;
import com.gmail.nuclearcat1337.griefprotect.util.Provider;
import net.vanillacraft.CoreFunctions.interfaces.Database;
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
        Bukkit.getPluginManager().registerEvents(this, GriefProtect.getInstance());
        this.database = database;
        recordQueue = new ConcurrentLinkedQueue<>();
        thread = new LogThread(database.getConnection(),this);
        thread.start();
        new WorldLogListeners(GriefProtect.getInstance(),this);
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
        if(event.getPlugin().getName().equals(GriefProtect.getInstance().getName()))
            thread.enabled = false;
    }

}
