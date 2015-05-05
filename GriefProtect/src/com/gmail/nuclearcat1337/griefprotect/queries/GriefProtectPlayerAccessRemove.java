package com.gmail.nuclearcat1337.griefprotect.queries;

import com.gmail.nuclearcat1337.griefprotect.interfaces.DBQuery;

import java.sql.ResultSet;
import java.util.UUID;

public class GriefProtectPlayerAccessRemove implements DBQuery
{
    public String query;

    public GriefProtectPlayerAccessRemove(UUID owner, UUID player)
    {
        query = String
                .format("DELETE FROM tbl_player_access WHERE col_owner='%s' AND col_player='%s'",
                        owner.toString(), player.toString());
    }

    @Override
    public void run()
    {

    }

    @Override
    public String getQuery()
    {
        return query;
    }

    @Override
    public boolean isCallback()
    {
        return false;
    }

    @Override
    public void setResult(ResultSet result)
    {

    }
}
