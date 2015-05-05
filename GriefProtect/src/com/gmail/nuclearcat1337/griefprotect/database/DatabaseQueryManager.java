package com.gmail.nuclearcat1337.griefprotect.database;

import com.gmail.nuclearcat1337.griefprotect.interfaces.Database;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseQueryManager
{
    private ExecutorService executor;
    private Database database;

    public DatabaseQueryManager(Database database, int threads)
    {
        this.database = database;
        executor = Executors.newFixedThreadPool(threads);
    }


}
