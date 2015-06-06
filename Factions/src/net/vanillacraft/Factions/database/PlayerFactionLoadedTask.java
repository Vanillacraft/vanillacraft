package net.vanillacraft.Factions.database;

import net.vanillacraft.CoreFunctions.datastores.PlayerProfile;
import net.vanillacraft.CoreFunctions.utils.CoreErrors;
import net.vanillacraft.Factions.datastore.Faction;
import net.vanillacraft.Factions.main.Factions;
import org.bukkit.entity.Player;
import sun.plugin2.main.server.Plugin;

/**
 * Created by ryan on 6/3/2015.
 */
public class PlayerFactionLoadedTask implements Runnable
{
    private Player player;
    private Factions plugin;
    private Faction faction;

    public PlayerFactionLoadedTask(Player player, Faction faction, Factions plugin){
        this.player = player;
        this.plugin = plugin;
        this.faction = faction;
    }

    @Override
    public void run(){
        plugin.getCoreFunctions().getCoreErrors().playerFactionLoaded(player);
        PlayerProfile profile = plugin.getCoreFunctions().getCoreData().getProfile(player);
        profile.setData("Faction", faction);
    }
}
