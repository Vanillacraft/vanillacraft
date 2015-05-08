package com.gmail.nuclearcat1337.griefprotect.griefData;

import com.gmail.nuclearcat1337.griefprotect.griefItems.ProtectData;
import net.vanillacraft.CoreFunctions.utils.Loc;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AllowMap
{
    private Map<UUID, Map<UUID, List<ProtectData>>> playerAccess; //Key = Owner, Key = Player
    AllowMap()
    {
        playerAccess = new HashMap<>();
    }

    public void addProtectData(UUID owner, UUID player, ProtectData data)
    {
        Map<UUID,List<ProtectData>> map = playerAccess.get(owner);
        if(map == null)
        {
            map = new HashMap<UUID, List<ProtectData>>();
            playerAccess.put(owner, map);
        }
        List<ProtectData> pData = map.get(player);
        if(pData == null)
        {
            pData = new ArrayList<>();
            map.put(player,pData);
        }
        pData.add(data);
    }

    public void addProtectData(UUID owner, Map<UUID,List<ProtectData>> map)
    {
        playerAccess.put(owner, map);
    }

    public boolean hasAllows(UUID owner)
    {
        return playerAccess.containsKey(owner);
    }

    public boolean hasAllow(UUID owner, UUID player)
    {
        if(!hasAllows(owner))
            return false;
        return playerAccess.get(owner).containsKey(player);
    }

    public void removeAllows(UUID owner, UUID player)
    {
        if(hasAllow(owner,player))
        {
            playerAccess.get(owner).remove(player);
        }
    }

    private List<ProtectData> getAllows(UUID owner, UUID player)
    {
        if(!hasAllow(owner,player))
            return null;
        return playerAccess.get(owner).get(player);
    }

    public boolean isAllowed(UUID owner, UUID player, Loc loc, Material material)
    {
        return isAllowed(owner,player,loc.toLocation(),material);
    }

    public boolean isAllowed(UUID owner, UUID player, Location loc, Material material)
    {
        //Player p = getPlayer(player);

       // if (p != null) //&& p.isOp()) //TODO-------Insert mod powers check
        //{
        //    return true;
        //}
        List<ProtectData> rangeMap = this.getAllows(owner,player);
        if(rangeMap != null)
        {
            for (ProtectData data : rangeMap)
            {
                if ((loc.getBlockX() >= (data.getX() - data.getRange()) && loc.getBlockX() <= (data.getX() + data.getRange())) && (loc.getBlockY() >= (data.getY() - data.getRange()) && loc.getBlockY() <= (data.getY() + data.getRange())) && (loc.getBlockZ() >= (data.getZ() - data.getRange()) && loc.getBlockZ() <= (data.getZ() + data.getRange())))
                {
                    if (data.getAllowedBlocks().isEmpty() || data.getAllowedBlocks().contains(material))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
