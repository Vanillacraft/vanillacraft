package com.gmail.nuclearcat1337.griefprotect.griefData;

import com.gmail.nuclearcat1337.griefprotect.interfaces.BlockWatcher;
import net.vanillacraft.CoreFunctions.interfaces.Database;

public class GriefData
{
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


    public GriefData(Database database, BlockWatcher watcher)
    {
        assert database != null && watcher != null;

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
