package com.gmail.nuclearcat1337.griefprotect.griefItems;

import java.util.UUID;

public class GriefChest
{
    public long getTimestamp()
    {
        return timestamp;
    }

    public UUID getOwner()
    {
        return owner;
    }

    private UUID owner;
    private long timestamp;

    public GriefChest(UUID owner, long timestamp)
    {
        this.owner = owner;
        this.timestamp = timestamp;
    }
}
