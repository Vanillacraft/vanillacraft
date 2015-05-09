package net.vanillacraft.CoreFunctions.commands;

import net.vanillacraft.CoreFunctions.datastores.CoreData;
import net.vanillacraft.CoreFunctions.datastores.Delay;
import net.vanillacraft.CoreFunctions.datastores.PlayerProfile;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import net.vanillacraft.Factions.datastore.Faction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Created by ryan on 5/5/2015.
 */
public class HomeCommand implements Listener
{

    private CoreFunctions plugin;

    public HomeCommand(CoreFunctions plugin)
    {
        Bukkit.getPluginManager().registerEvents(this, CoreFunctions.getInstance());
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        if (!event.isCancelled())
        {
            Player player = event.getPlayer();
            String command[] = event.getMessage().split(" ");
            PlayerProfile profile = CoreData.getProfile(player);

            if (command[0].equalsIgnoreCase("/home"))
            {
                plugin.getCoreMethods().teleport(player,profile.getHomeLocation().toLocation());
//                if (plugin.getCoreMethods().canTeleport(profile))
//                {
//                    plugin.getCoreMethods().teleport(player, plugin.getCoreMethods().getHomeLocation(player));
//                }
//                else
//                {
//                    plugin.getCoreErrors().teleportTimerNotDone(player);
//                }
            }

        }
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event)
    {
        Player player = event.getPlayer();

        if (!event.isCancelled())
        {
            if (plugin.getCoreMethods().getFaction(player) != null)
            {
                Faction playerFaction = plugin.getCoreMethods().getFaction(player);
                Faction targetFaction = plugin.getCoreMethods().getFaction(event.getBed().getLocation());

                if (plugin.getCoreMethods().canSetHome(player, targetFaction, playerFaction))
                {
                    plugin.getCoreMethods().setHomeLocation(player);
                }
                else
                {
                    plugin.getCoreErrors().timerNotDone(player, "set home", CoreData.getProfile(player).getRemainingDelay(Delay.SETHOME).getFormatted());
                }
            }
        }
    }

}
