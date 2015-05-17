package net.vanillacraft.CoreFunctions.interfaces;

import java.sql.PreparedStatement;

/*
Created by Mr_Little_Kitty on 5/7/2015
*/
public interface InsertRecord
{
    void setParameters(PreparedStatement statement);
    String getQuery();
    String getCacheKey();
}
