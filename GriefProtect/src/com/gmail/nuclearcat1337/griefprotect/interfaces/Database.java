package com.gmail.nuclearcat1337.griefprotect.interfaces;

import java.sql.Connection;

public interface Database
{
    //Turns out I missed the entire point of this.
    //This is to actually query something from the logs,
    //not to log something into the database... awkward

    boolean isUseable();

    void submitQuery(DBQuery query);
    void submitLogQuery(DBLogQuery logQuery);
    void runSyncUpdate(String query);

    Connection getConnection();

    DBLogQuery getNextLogQuery();
    DBQuery getNextQuery();
}
