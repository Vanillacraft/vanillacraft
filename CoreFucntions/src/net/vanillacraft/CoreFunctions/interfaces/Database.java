package net.vanillacraft.CoreFunctions.interfaces;

import java.sql.Connection;

/**
 * Created by Mr_Little_Kitty on 5/3/2015.
 */
public interface Database
{
    boolean isUseable();

    void submitSelectRecord(SelectRecord record);
    void submitInsertRecord(InsertRecord record);
    
    void runSyncUpdate(String query);

    Connection getConnection(long threadID);
    //Connection getConnection();

    InsertRecord nextInsertRecord();
    SelectRecord nextSelectRecord();
}
