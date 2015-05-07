package net.vanillacraft.CoreFunctions.interfaces;

import java.sql.ResultSet;

/**
 * Created by Mr_Little_Kitty on 5/3/2015.
 */
public interface DBQuery extends Runnable
{
    public void setResult(ResultSet result);
    public String getQuery();
    public boolean isCallback();
}
