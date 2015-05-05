package com.gmail.nuclearcat1337.griefprotect.queries;

import org.bukkit.entity.Player;

public class GriefCloseInventoryTask implements Runnable
{
    private Player player;

    public GriefCloseInventoryTask(Player player)
    {
        this.player = player;

        // close inventory
        player.closeInventory();
    }

    @Override
    public void run()
    {
        if (player != null && player.isOnline())
        {
            // close inventory
            player.closeInventory();
        }
    }
}
