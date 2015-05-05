package com.gmail.nuclearcat1337.griefprotect.griefData;

import com.gmail.nuclearcat1337.griefprotect.interfaces.BlockWatcher;
import com.gmail.nuclearcat1337.griefprotect.interfaces.Database;
import com.gmail.nuclearcat1337.griefprotect.interfaces.ILogger;

public class GriefData
{
    private ILogger logger;
    private Database database;
    private BlockWatcher watcher;

//    private HashMap<String, Integer> playerAlert;
//    private HashMap<String, Integer> playerGriefAlert;
//    private HashMap<String, HashSet<String>> playerFriend;
//
//    //private HashSet<String> worldSet;
//
//    private HashMap<String, GriefChest> chestOwner;
//    private ArrayList<Loc> chestList;
//    private HashMap<String, Long> playerContainerAlert;
//
//    private HashSet<String> playerNotified;

    private AllowMap allows;
    private ChestCache chests;
    private GriefManager manager;
//


    public GriefData(ILogger logger, Database database, BlockWatcher watcher)
    {
        assert logger != null && database != null && watcher != null;

        this.logger = logger;
        this.database = database;
        this.watcher = watcher;

        this.allows = new AllowMap();
        this.chests = new ChestCache(this);
        this.manager = new GriefManager();
    }

    public GriefManager getGriefManager()
    {
        return manager;
    }

    public AllowMap getPlayerAccess()
    {
        return allows;
    }

    public ILogger getLogger()
    {
        return logger;
    }

    public Database getDatabase()
    {
        return database;
    }

    public BlockWatcher getWatcher()
    {
        return watcher;
    }

    public ChestCache getChestCache()
    {
        return chests;
    }
}
