package net.vanillacraft.Factions.database;

import net.vanillacraft.CoreFunctions.interfaces.InsertRecord;
import net.vanillacraft.Factions.datastore.Faction;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by ryan on 5/30/2015.
 */
public class LeaveFactionQuery implements InsertRecord
{
    private UUID uuid;
    private Faction faction;

    public LeaveFactionQuery(UUID uuid)
    {
        this.uuid = uuid;
    }

    @Override
    public void setParameters(final PreparedStatement statement)
    {
        try
        {
            statement.setString(1, uuid.toString());
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public String getQuery()
    {
        return "DELETE FROM tbl_faction_members WHERE col_UUID = ?;";
    }

    @Override
    public String getCacheKey()
    {
        return "LeaveFaction";
    }
}
