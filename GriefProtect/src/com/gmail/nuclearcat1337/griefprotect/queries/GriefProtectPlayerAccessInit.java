package com.gmail.nuclearcat1337.griefprotect.queries;

import com.gmail.nuclearcat1337.griefprotect.griefData.GriefData;
import com.gmail.nuclearcat1337.griefprotect.griefItems.ProtectData;
import com.gmail.nuclearcat1337.griefprotect.main.GriefProtect;
import net.vanillacraft.CoreFunctions.interfaces.DBQuery;
import org.bukkit.Material;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Level;

public class GriefProtectPlayerAccessInit implements DBQuery
{
    private GriefData data;
    private Map<UUID, Map<UUID, List<ProtectData>>> accessMap;

    public GriefProtectPlayerAccessInit(GriefData data)
    {
        this.data = data;
        accessMap = new HashMap<>();
    }

    @Override
    public void run()
    {
        for(Entry<UUID,Map<UUID,List<ProtectData>>> entry: accessMap.entrySet())
        {
            data.getPlayerAccess().addProtectData(entry.getKey(),entry.getValue());
        }
    }

    @Override
    public String getQuery()
    {
        return "SELECT col_owner, col_player, col_x, col_y, col_z, col_range, col_allowed_blocks FROM tbl_player_access ORDER BY col_owner, col_player, col_range DESC";
    }

    @Override
    public boolean isCallback()
    {
        return true;
    }

    @Override
    public void setResult(ResultSet result)
    {
        try
        {
            while (result.next())
            {
                UUID owner = UUID.fromString(result.getString("col_owner"));
                UUID player = UUID.fromString(result.getString("col_player"));
                String allowedBlocks = result.getString("col_allowed_blocks");
                if (!accessMap.containsKey(owner))
                {
                    accessMap.put(owner, new HashMap<UUID, List<ProtectData>>());
                }
                Map<UUID, List<ProtectData>> playerMap = accessMap.get(owner);

                if (!playerMap.containsKey(player))
                {
                    playerMap.put(player, new ArrayList<ProtectData>());
                }

                List<ProtectData> rangeMap = playerMap.get(player);

                HashSet<Material> blockMap = new HashSet<Material>();
                if (allowedBlocks != null && allowedBlocks.length() > 0)
                {
                    String[] blocks = allowedBlocks.split("--");

                    for (String block : blocks)
                    {
                        blockMap.add(Material.valueOf(block));
                    }
                }
                rangeMap.add(new ProtectData(result.getInt("col_x"),
                        result.getInt("col_y"),
                        result.getInt("col_z"),
                        result.getInt("col_range"), blockMap));
            }
        }
        catch (SQLException e)
        {
            //Bukkit.getLogger().log(Level.SEVERE, "GriefProtectPlayed: Error fetching results: " + e.getMessage());
            //GriefProtect.logWarning("Class={"+this.getClass().getSimpleName()+"} Error: "+e.getMessage(),Level.SEVERE);
            GriefProtect.logError(e.getMessage(),this.getClass());
        }
    }
}
