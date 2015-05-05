package com.gmail.nuclearcat1337.griefprotect.interfaces;

import java.sql.ResultSet;

public interface DBQuery extends Runnable
{
    public void setResult(ResultSet result);
    public String getQuery();
    public boolean isCallback();
}
