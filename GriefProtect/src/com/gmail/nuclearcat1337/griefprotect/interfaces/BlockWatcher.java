package com.gmail.nuclearcat1337.griefprotect.interfaces;

import com.gmail.nuclearcat1337.griefprotect.util.Loc;
import org.bukkit.entity.Player;

public abstract class BlockWatcher
{
    public void notifyOPs(Player player, String action, String material, Loc location)
    {
        notifyOps(player, action, material, null, location);
    }

    public abstract void notifyOps(Player player, String action, String material, String owner, Loc location);
}
