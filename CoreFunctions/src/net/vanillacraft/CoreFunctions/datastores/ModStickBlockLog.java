package net.vanillacraft.CoreFunctions.datastores;

import net.vanillacraft.CoreFunctions.interfaces.InsertRecord;
import org.bukkit.Location;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by ryan on 6/6/2015.
 */
public class ModStickBlockLog implements InsertRecord
{
    private UUID uuid;
    private Location location;

    public ModStickBlockLog(UUID uuid, Location location)
    {
        this.uuid = uuid;
        this.location = location;
    }

    @Override
    public void setParameters(final PreparedStatement statement)
    {
        //todo set params
    }

    @Override
    public String getQuery()
    {
        //todo make query
        return "";
    }

    @Override
    public String getCacheKey()
    {
        return "ModStickBreak";
    }
}
