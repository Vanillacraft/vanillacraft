package net.vanillacraft.CoreFunctions.interfaces;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*
Created by Mr_Little_Kitty on 5/7/2015
*/
public interface SelectRecord
{
    String getQuery();
    void callbackAsync(ResultSet set);
    void runSynchronously();
    String getCacheKey();
    void setParameters(PreparedStatement statement);
}
