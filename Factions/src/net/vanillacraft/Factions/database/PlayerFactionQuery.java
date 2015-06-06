package net.vanillacraft.Factions.database;

import net.vanillacraft.CoreFunctions.interfaces.SelectRecord;
import net.vanillacraft.Factions.datastore.Faction;
import net.vanillacraft.Factions.main.Factions;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by ryan on 6/3/2015.
 */
public class PlayerFactionQuery implements SelectRecord
{
    private UUID uuid;
    private String factionName;
    private Factions plugin;

    public PlayerFactionQuery(UUID uuid, Factions plugin)
    {
        this.uuid = uuid;
        this.plugin = plugin;
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
        return "SELECT * FROM tbl_faction_members WHERE col_UUID = ?;";
    }

    @Override
    public String getCacheKey()
    {
        return "GetFaction";
    }

    @Override
    public void callbackAsync(final ResultSet result)
    {
        try
        {
            while (result.next())
            {
                uuid = UUID.fromString(result.getString("col_owner"));
                factionName = result.getString("col_faction_name");
            }
            plugin.mysqlDBcallback(this);
        }
        catch (SQLException e)
        {

        }
    }

    @Override
    public void runSynchronously()
    {

    }

    public UUID getUUID()
    {
        return uuid;
    }

    public String getFactionName()
    {
        return factionName;
    }
}
