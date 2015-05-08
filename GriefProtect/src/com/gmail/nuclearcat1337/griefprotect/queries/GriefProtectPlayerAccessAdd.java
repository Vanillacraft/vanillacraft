package com.gmail.nuclearcat1337.griefprotect.queries;

import net.vanillacraft.CoreFunctions.interfaces.InsertRecord;

import java.sql.PreparedStatement;
import java.util.UUID;

public class GriefProtectPlayerAccessAdd implements InsertRecord
{
    private String query;

    public GriefProtectPlayerAccessAdd(UUID owner, UUID player, int x,int y, int z, String world, int range,  String allowedBlocks)
    {
        query = String
                .format("INSERT INTO tbl_player_access SET col_owner='%s', col_player='%s', col_x=%d, col_y=%d, col_z=%d, col_world='%s', col_range=%d, col_allowed_blocks='%s'",
                        owner.toString(), player.toString(), x, y, z, world, range, allowedBlocks);
    }

    @Override
    public void setParameters(final PreparedStatement statement)
    {

    }

    @Override
    public String getQuery()
    {
        return query;
    }

    @Override
    public String getCacheKey()
    {
        return "AccessAdd";
    }
}
