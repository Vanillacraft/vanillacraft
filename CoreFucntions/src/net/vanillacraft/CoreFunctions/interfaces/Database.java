package net.vanillacraft.CoreFunctions.interfaces;

import java.sql.Connection;

/**
 * Created by Mr_Little_Kitty on 5/3/2015.
 */
public interface Database
{
    boolean isUseable();

    void submitQuery(DBQuery query);
    void submitLogQuery(DBLogQuery logQuery);
    void runSyncUpdate(String query);

    Connection getConnection();

    DBLogQuery getNextLogQuery();
    DBQuery getNextQuery();
}
