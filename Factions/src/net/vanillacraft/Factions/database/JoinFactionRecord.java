package net.vanillacraft.Factions.database;

import net.vanillacraft.CoreFunctions.interfaces.InsertRecord;
import net.vanillacraft.Factions.datastore.Faction;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by ryan on 5/30/2015.
 */
public class JoinFactionRecord implements InsertRecord
{
    private UUID uuid;
    private Faction faction;

    public JoinFactionRecord(UUID uuid, Faction faction)
    {
        this.uuid = uuid;
        this.faction = faction;
    }

    @Override
    public void setParameters(final PreparedStatement statement)
    {
        try
        {
            statement.setString(1, uuid.toString());
            statement.setString(2, faction.getName());
            statement.setString(3, "UTCNOW()");
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public String getQuery()
    {
        return "INSERT INTO tbl_faction_members (col_UUID, col_faction_name, col_timestamp) VALUES (?,?,?);";
    }

    @Override
    public String getCacheKey()
    {
        return "JoinFaction";
    }
}
