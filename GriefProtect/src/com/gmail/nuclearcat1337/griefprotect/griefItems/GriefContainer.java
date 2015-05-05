package com.gmail.nuclearcat1337.griefprotect.griefItems;

import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class GriefContainer
{
    private BlockState block;
    private ItemStack[] items;
    private UUID owner;
    private boolean allowed;

    public long getTimestamp()
    {
        return timestamp;
    }

    public boolean isAllowed()
    {
        return allowed;
    }

    public UUID getOwner()
    {
        return owner;
    }

    public ItemStack[] getItems()
    {
        return items;
    }

    public BlockState getBlock()
    {
        return block;
    }

    private long timestamp;

    public GriefContainer(BlockState block, UUID owner, boolean allowed)
    {
        this.block = block;
        this.owner = owner;
        this.allowed = allowed;
        if (block instanceof Chest)
        {
            items = ((Chest) block).getBlockInventory().getContents();
        }
        else
        {
            items = ((InventoryHolder) block).getInventory().getContents();
        }

        timestamp = System.currentTimeMillis();
    }
}
