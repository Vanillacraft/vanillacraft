package net.vanillacraft.CoreFunctions.utils;

import net.vanillacraft.CoreFunctions.datastores.CoreData;
import net.vanillacraft.CoreFunctions.datastores.PlayerProfile;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Created by ryan on 5/19/2015.
 */
public class God implements Listener
{
    private CoreFunctions plugin;

    public God(CoreFunctions plugin)
    {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, CoreFunctions.getInstance());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        if (!event.isCancelled())
        {
            Player player = event.getPlayer();
            String command[] = event.getMessage().split(" ");
            PlayerProfile profile = CoreData.getProfile(player);

            if (command[0].equalsIgnoreCase("/god"))
            {
                if (profile.isModMode())
                {
                    //todo make this a var passed by user with reason
                    profile.setData("Godmode", System.currentTimeMillis() + 1800000);
                }
                else
                {
                    plugin.getCoreErrors().enableModMode(player);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerTakeDmg(EntityDamageEvent event)
    {
        if (!event.isCancelled())
        {
            if (event.getEntity() instanceof Player)
            {
                Player player = (Player) event.getEntity();
                PlayerProfile profile = CoreData.getProfile(player);
                Long timeLeft = profile.getData("Godmode", Long.class);

                if (timeLeft != null && timeLeft > System.currentTimeMillis())
                {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityTarget(EntityTargetEvent event)
    {
        if (!event.isCancelled())
        {
            if (event.getTarget() instanceof Player)
            {
                Player player = (Player) event.getTarget();
                PlayerProfile profile = CoreData.getProfile(player);
                Long timeLeft = profile.getData("Godmode", Long.class);

                if (timeLeft != null && timeLeft > System.currentTimeMillis())
                {
                    event.setCancelled(true);
                }
            }
        }
    }

}
