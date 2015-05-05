package com.gmail.nuclearcat1337.griefprotect.interfaces;

import java.sql.ResultSet;

public interface DBLogQuery extends Runnable
{
    public void setResult(ResultSet result);
    public String getWorld();
    public int getX();
    public int getY();
    public int getZ();
}
