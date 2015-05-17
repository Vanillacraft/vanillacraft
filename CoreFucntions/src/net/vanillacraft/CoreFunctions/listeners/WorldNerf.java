package net.vanillacraft.CoreFunctions.listeners;


import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by ryan on 5/16/2015.
 */
public class WorldNerf implements Listener
{
    private CoreFunctions plugin;

    public WorldNerf(CoreFunctions plugin)
    {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, CoreFunctions.getInstance());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCraftItem(CraftItemEvent event)
    {
        if (!event.isCancelled())
        {
            ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, (short) 1);
            if (event.getRecipe().getResult() == gapple)
            {
                if (event.getWhoClicked() instanceof Player)
                {
                    plugin.getCoreErrors().cantCraftGoldenApples((Player) event.getWhoClicked());
                }
                event.setCancelled(true);
            }
        }
    }



}
