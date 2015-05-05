package com.gmail.nuclearcat1337.griefprotect.griefData;

import com.gmail.nuclearcat1337.griefprotect.griefItems.GriefChest;
import com.gmail.nuclearcat1337.griefprotect.griefItems.GriefContainer;
import com.gmail.nuclearcat1337.griefprotect.util.Loc;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChestCache
{
    private GriefData data;
    private Map<UUID, List<GriefContainer>> containerCheck;
    private Map<String, GriefChest> chestOwner;
    private List<Loc> chestList;

    ChestCache(GriefData data)
    {
        this.data = data;
        chestList = new ArrayList<>();
        chestOwner = new HashMap<>();
    }

    public void checkContainers(BlockState block, UUID player, UUID owner, boolean allowed)
    {
        if (!containerCheck.containsKey(player))
        {
            containerCheck.put(player, new ArrayList<GriefContainer>());
        }

        containerCheck.get(player).add(new GriefContainer(block, owner, allowed));
    }


    public boolean isChestListEmpty()
    {
        return chestList.isEmpty();
    }

    public List<Loc> getChestList()
    {
        //return Collections.unmodifiableList(chestList);
        return chestList;
    }

    public void addChest(Loc loc)
    {
        chestList.add(loc);
    }

    public void removeChest(Loc loc)
    {
        chestList.remove(loc);
    }

    public void removeChest(Location loc)
    {
        removeChest(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public void removeChest(int x, int y, int z)
    {
        for(int i =0; i < chestList.size(); i++)
        {
            Loc l =  chestList.get(i);
            if(l.getBlockX() == x && l.getBlockY() == y && l.getBlockZ() == z)
            {
                chestList.remove(i);
                return;
            }
        }
    }

    public Boolean isCachedChestAllowed(Block block, UUID player)
    {
        if(block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST)
        {
            String key = block.getX() + " " + block.getY() + " " + block.getZ();
            GriefChest chest = chestOwner.get(key);
            if (chest == null)
            {
                return null;
            }

            if (chest.getOwner().equals(player) || (System.currentTimeMillis() - chest.getTimestamp()) < 300000 //This is the 5 minute rule. Chests placed less than 5 minutes ago can be opened by anyone
                    || data.getPlayerAccess().isAllowed(chest.getOwner(), player, block.getLocation(),block.getType()))
            //|| zone.isStealingAllowed(p, chest.owner, b.getBlock().getLocation()))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        return null;
    }

    public void addGriefChest(String key, GriefChest chest)
    {
        chestOwner.put(key,chest);
    }

    public GriefChest getGriefChest(String key)
    {
        return chestOwner.get(key);
    }

    public void removeGriefChest(String key)
    {
        chestOwner.remove(key);
    }

    public boolean isGriefChest(String key)
    {
        return chestOwner.containsKey(key);
    }
}
