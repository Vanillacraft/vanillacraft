package com.gmail.nuclearcat1337.griefprotect.griefItems;

import org.bukkit.inventory.ItemStack;

public class GriefDrop
{
    private ItemStack item1;
    private ItemStack item2;
    private int x;
    private int y;

    public ItemStack getItem1()
    {
        return item1;
    }

    public ItemStack getItem2()
    {
        return item2;
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

    public long getTimestamp()
    {
        return timestamp;
    }

    private int z;
    private long timestamp;

    public GriefDrop(ItemStack item1, ItemStack item2, int x, int y, int z)
    {
        this.item1 = item1;
        this.item2 = item2;
        this.x = x;
        this.y = y;
        this.z = z;

        timestamp = System.currentTimeMillis() + 3000;
    }
}
