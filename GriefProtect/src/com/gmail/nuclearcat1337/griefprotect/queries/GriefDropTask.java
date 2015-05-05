package com.gmail.nuclearcat1337.griefprotect.queries;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class GriefDropTask implements Runnable
{
    private Location location;
    private ItemStack item1;
    private ItemStack item2;
    private ItemStack item3;

    public GriefDropTask(Location location, ItemStack item1, ItemStack item2, ItemStack item3)
    {
        this.location = location;
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
    }

    @Override
    public void run()
    {
        int amount2 = item2 != null ? item2.getAmount() : 0;
        int amount3 = item3 != null ? item3.getAmount() : 0;

        for (Entity e : location.getWorld().getEntities())
        {
            if (e instanceof Item)
            {
                if (location.distanceSquared(e.getLocation()) < 16)
                {
                    ItemStack ii = ((Item) e).getItemStack();

                    if (ii.getTypeId() == item1.getTypeId())
                    {
                        e.remove();
                        break;
                    }
                    else
                    {
                        if (amount2 > 0
                                && ii.getTypeId() == item2.getTypeId())
                        {
                            e.remove();
                            amount2--;
                        }
                        if (amount3 > 0
                                && ii.getTypeId() == item3.getTypeId())
                        {
                            e.remove();
                            amount3--;
                        }

                        if (amount2 <= 0 && amount3 <= 0)
                        {
                            break;
                        }
                    }
                }
            }
        }
    }
}
