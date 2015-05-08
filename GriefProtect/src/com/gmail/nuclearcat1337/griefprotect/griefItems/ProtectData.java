package com.gmail.nuclearcat1337.griefprotect.griefItems;

import org.bukkit.Material;

import java.util.HashSet;

public class ProtectData
{
    private int x;
    private int y;
    private int z;
    private String world;
    private int range;
    private HashSet<Material> allowedBlocks;

    public ProtectData(int x, int y, int z, int range, String world, HashSet<Material> allowedBlocks)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.range = range;
        this.allowedBlocks = allowedBlocks;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getZ()
    {
        return z;
    }

    public String getWorld()
    {
        return world;
    }

    public int getRange()
    {
        return range;
    }

    public HashSet<Material> getAllowedBlocks()
    {
        return allowedBlocks;
    }

    public void addBlock(Material mat)
    {
        allowedBlocks.add(mat);
    }
}
