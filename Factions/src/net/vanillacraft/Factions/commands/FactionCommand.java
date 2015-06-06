package net.vanillacraft.Factions.commands;

import net.vanillacraft.CoreFunctions.datastores.CoreData;
import net.vanillacraft.CoreFunctions.datastores.Delay;
import net.vanillacraft.CoreFunctions.datastores.PlayerProfile;
import net.vanillacraft.Factions.datastore.Faction;
import net.vanillacraft.Factions.main.Factions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashMap;

/**
 * Created by ryan on 5/23/2015.
 */
public class FactionCommand implements Listener
{
    private Factions plugin;
    private HashMap<String, Faction> factionList;

    public FactionCommand(Factions plugin)
    {
        Bukkit.getPluginManager().registerEvents(this, plugin.getInstance());
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

            // /faction join/leave color

            if (command[0].equalsIgnoreCase("/faction"))
            {
                //leave join list
                if (command[1].equalsIgnoreCase("join"))
                {
                    if (profile.hasActiveDelay(Delay.FACTION))
                    {
                        if (command.length == 2)
                        {
                            Faction target = plugin.getFaction(command[2]);
                            if (target != null)
                            {
                                if (!target.getName().equalsIgnoreCase("worker"))
                                {
                                    profile.addDelay(Delay.FACTION);
                                    if (profile.getData("Faction", Faction.class) != null)
                                    {
                                        plugin.leaveFaction(player);
                                    }
                                    plugin.joinFaction(player, target);
                                }
                                else
                                {
                                    //todo add some special thing here
                                }
                            }
                            else
                            {
                                //todo: can't find that faction
                            }
                        }
                        else
                        {
                            plugin.sendFactionHelp(player, "join");
                        }
                    }
                    else
                    {
                        plugin.getCoreFunctions().getCoreErrors().timerNotDone(player, "join a faction", profile.getRemainingDelay(Delay.FACTION).getFormatted());
                    }
                }
                else if (command[1].equalsIgnoreCase("leave"))
                {
                    if (!profile.hasActiveDelay(Delay.FACTION))
                    {
                        profile.addDelay(Delay.FACTION);
                        plugin.leaveFaction(player);
                    }
                    else
                    {
                        plugin.getCoreFunctions().getCoreErrors().timerNotDone(player, "leave a faction", profile.getRemainingDelay(Delay.FACTION).getFormatted());
                    }
                }
                else if (command[1].equalsIgnoreCase("list"))
                {
                    Faction[] list = plugin.getFactions();
                    player.sendMessage(ChatColor.YELLOW + "Factions:");
                    for (Faction fac : list)
                    {
                        player.sendMessage(fac.getColor() + " [" + ChatColor.WHITE + fac.getName() + fac.getColor() + "]" + ChatColor.GRAY + " is allied with " + (fac.getAllyList().isEmpty() ? "Nobody" : fac.getAllyList()));
                    }
                }
                else if (command[1].equalsIgnoreCase("help"))
                {
                    plugin.sendFactionHelp(player);
                }
            }
        }
    }
}
