package com.gmail.nuclearcat1337.griefprotect.queries;

import com.gmail.nuclearcat1337.griefprotect.interfaces.DBQuery;

import java.sql.ResultSet;
import java.util.UUID;

public class GriefProtectPlayerAccessAdd implements DBQuery
{
    public String query;

    public GriefProtectPlayerAccessAdd(UUID owner, UUID player, int x,
                                       int y, int z, int range, String allowedBlocks)
    {
        query = String
                .format("INSERT INTO tbl_player_access SET col_owner='%s', col_player='%s', col_x=%d, col_y=%d, col_z=%d, col_range=%d, col_allowed_blocks='%s'",
                        owner.toString(), player.toString(), x, y, z, range, allowedBlocks);
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
