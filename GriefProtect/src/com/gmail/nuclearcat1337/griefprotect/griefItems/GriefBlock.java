package com.gmail.nuclearcat1337.griefprotect.griefItems;

import org.bukkit.block.BlockState;

import java.util.UUID;

public class GriefBlock
{
    private BlockState connectedBlock;
    private BlockState block;
    private UUID owner;

    public GriefBlock(BlockState block, UUID owner, BlockState connectedBlock)
    {
        this.block = block;
        this.owner = owner;
        this.connectedBlock = connectedBlock;
    }

    public BlockState getBlock()
    {
        return block;
    }

    public BlockState getConnectedBlock()
    {
        return connectedBlock;
    }

    public UUID getOwner()
    {
        return owner;
    }
}
