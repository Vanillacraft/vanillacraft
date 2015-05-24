package net.vanillacraft.Factions.main;

import net.vanillacraft.Factions.datastore.Faction;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by ryan on 5/7/2015.
 */
public class Factions extends JavaPlugin
{

    private HashMap<String, Faction> factionList;
    private HashMap<UUID, Faction> playerFactionList;
    private static Factions instance;

    public static JavaPlugin getInstance()
    {
        return instance;
    }

    @Override
    public void onEnable()
    {
        instance = this;
    }

    @Override
    public void onDisable()
    {

    }

    public Faction getFaction(Player player)
    {
        if (playerFactionList.containsKey(player.getUniqueId()))
        {
            return playerFactionList.get(player.getUniqueId());
        }
        else
        {
            return null;
        }
    }

    public Faction getFaction(String factionName)
    {
        if (factionList.containsKey(factionName))
        {
            return factionList.get(factionName);
        }
        else
        {
            return null;
        }
    }

    public Faction getFaction(Location location){
        //TODO: Need to work with zones plugin.
        return null;
    }

    public void playerEnterNewFaction(Player player, Faction fac){
        player.sendMessage(ChatColor.GREEN + " You've now entered " + ChatColor.WHITE + fac.getName() + ChatColor.GREEN + " zone.");
    }

}
