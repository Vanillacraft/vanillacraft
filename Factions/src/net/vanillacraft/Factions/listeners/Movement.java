package net.vanillacraft.Factions.listeners;

import net.vanillacraft.CoreFunctions.datastores.CoreData;
import net.vanillacraft.CoreFunctions.datastores.PlayerProfile;
import net.vanillacraft.Factions.datastore.Faction;
import net.vanillacraft.Factions.main.Factions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by ryan on 5/23/2015.
 */
//todo : i need to move this to zones since it will only be one they move to another zone and then just call
//todo : some method from factions plugin when the move into another faction or just use zones to aleart them
public class Movement implements Listener
{
    private Factions plugin;

    public Movement(Factions plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin.getInstance());
    }

    //this might cause some lag so we might want to change this to sync task every X amount of time
    //instead of being onMoveEvent
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event){
        if(!event.isCancelled()){
            Player player = event.getPlayer();
            PlayerProfile profile = CoreData.getProfile(player);
            Faction faction = plugin.getFaction(event.getTo());

            if(profile.getData("locFaction", Faction.class) != faction){
                profile.setData("locFaction", faction);
                plugin.playerEnterNewFaction(player, faction);
            }

        }
    }

}
