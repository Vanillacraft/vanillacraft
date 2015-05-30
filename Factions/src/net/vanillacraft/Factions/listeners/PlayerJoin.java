package net.vanillacraft.Factions.listeners;

import net.vanillacraft.CoreFunctions.datastores.PlayerProfile;
import net.vanillacraft.Factions.datastore.Faction;
import net.vanillacraft.Factions.main.Factions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by ryan on 5/30/2015.
 */
public class PlayerJoin implements Listener
{
    private Factions plugin;

    public PlayerJoin(Factions plugin)
    {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin.getInstance());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        PlayerProfile profile = plugin.getCoreFunctions().getCoreData().getProfile(player);

        if (profile.getData("Faction", Faction.class) == null)
        {
            //todo ask mlk about how he does his thing async as im to tired to figure it out at 6am
            //this will make it so that if the players profile doesn't have a faction, check to see
            //if they have a faction in the database.
        }
    }
}
