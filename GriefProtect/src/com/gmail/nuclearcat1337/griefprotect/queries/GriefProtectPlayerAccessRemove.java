package com.gmail.nuclearcat1337.griefprotect.queries;


import net.vanillacraft.CoreFunctions.interfaces.InsertRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class GriefProtectPlayerAccessRemove implements InsertRecord
{
    private String query;

    public GriefProtectPlayerAccessRemove(UUID owner, UUID player)
    {
        query = String
                .format("DELETE FROM tbl_player_access WHERE col_owner='%s' AND col_player='%s'",
                        owner.toString(), player.toString());
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
        return "AccessRemove";
    }
}
