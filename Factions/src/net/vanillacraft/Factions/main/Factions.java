package net.vanillacraft.Factions.main;

import net.vanillacraft.CoreFunctions.datastores.PlayerProfile;
import net.vanillacraft.CoreFunctions.interfaces.DBLogQuery;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import net.vanillacraft.Factions.database.JoinFactionRecord;
import net.vanillacraft.Factions.database.LeaveFactionQuery;
import net.vanillacraft.Factions.database.PlayerFactionLoadedTask;
import net.vanillacraft.Factions.database.PlayerFactionQuery;
import net.vanillacraft.Factions.datastore.Faction;
import net.vanillacraft.Factions.listeners.Movement;
import net.vanillacraft.Factions.listeners.PlayerJoin;
import net.vanillacraft.Zones.datastores.Zone;
import net.vanillacraft.Zones.main.Zones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * Created by ryan on 5/7/2015.
 */
public class Factions extends JavaPlugin
{

    private HashMap<String, Faction> factionList;
    private HashMap<UUID, Faction> playerFactionList;

    private static Factions instance;

    private Zones coreZones;
    private CoreFunctions coreFunctions;

    private Movement movementListener;
    private PlayerJoin joinListener;

    public static JavaPlugin getInstance()
    {
        return instance;
    }

    @Override
    public void onEnable()
    {
        List<String> allyList = new ArrayList<>();
        allyList.add("Worker");
        factionList.put("Red", new Faction("Red", ChatColor.RED, allyList));
        factionList.put("Blue", new Faction("Blue", ChatColor.BLUE, allyList));
        allyList.add("Red");
        allyList.add("Blue");
        factionList.put("Worker", new Faction("Worker", ChatColor.DARK_PURPLE, allyList));

        instance = this;

        coreFunctions = (CoreFunctions) getServer().getPluginManager().getPlugin("CoreFunctions");
        coreZones = (Zones) getServer().getPluginManager().getPlugin("Zones");

        movementListener = new Movement(this);
        joinListener = new PlayerJoin(this);
    }

    public CoreFunctions getCoreFunctions()
    {
        return coreFunctions;
    }

    public Zones getCoreZones()
    {
        return coreZones;
    }

    @Override
    public void onDisable()
    {

    }

    public void joinFaction(Player player, Faction fac)
    {
        JoinFactionRecord record = new JoinFactionRecord(player.getUniqueId(), fac);
        getCoreFunctions().getCoreData().getDatabase().submitInsertRecord(record);

        player.sendMessage(ChatColor.WHITE + "You've just joined " + fac.getColor() + fac.getName() + ChatColor.WHITE + " Faction.");
        player.sendMessage(ChatColor.WHITE + "You will now have to wait at least 1 day before moving to another faction.");
    }

    public void leaveFaction(Player player)
    {
        //I didn't know if this should be done on insert record but idk it should work based on code but we should be able
        //to do it on either thread.
        LeaveFactionQuery query = new LeaveFactionQuery(player.getUniqueId());
        getCoreFunctions().getCoreData().getDatabase().submitInsertRecord(query);

        player.sendMessage(ChatColor.WHITE + "You've just left your faction old faction. You will now have to wait at least a 1 day before joing another faction.");
        player.sendMessage(ChatColor.WHITE + "Next time to directly switch factions with out a cool down period you can just do /faction join to switch.");
    }

    public Faction[] getFactions()
    {
        Faction[] output = new Faction[factionList.size()];
        int i = 0;
        for (Map.Entry<String, Faction> entry : factionList.entrySet())
        {
            output[i] = entry.getValue();
            i += 1;
        }
        return output;
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

    public Faction getFaction(Location location)
    {
        Zone zone = getCoreZones().getZone(location);
        if (zone != null && getFaction(zone.getFactionName()) != null)
        {
            return getFaction(zone.getFactionName());
        }

        return null;
    }

    public void playerEnterNewFaction(Player player, Faction fac)
    {
        player.sendMessage(ChatColor.GREEN + " You've now entered " + ChatColor.WHITE + fac.getName() + ChatColor.GREEN + " zone.");
    }

    public void sendFactionHelp(Player player)
    {
        getCoreFunctions().getCoreErrors().commandSantaxError(player, "faction", "[join] [faction] | this will let you join a faction");
        getCoreFunctions().getCoreErrors().commandSantaxError(player, "faction", "[switch] [faction] | this will let you switch to a faction instantly");
        getCoreFunctions().getCoreErrors().commandSantaxError(player, "faction", "[leave] | this will let you join a faction");
        getCoreFunctions().getCoreErrors().commandSantaxError(player, "faction", "[list] | this will print out a list of factions");
        getCoreFunctions().getCoreErrors().commandSantaxError(player, "faction", "[help] | this will print out this list");
    }

    public void sendFactionHelp(Player player, String arg)
    {
        if (arg.equalsIgnoreCase("all"))
        {
            sendFactionHelp(player);
        }
        else if (arg.equalsIgnoreCase("join"))
        {
            getCoreFunctions().getCoreErrors().commandSantaxError(player, "faction", "[join] [faction] | this will let you join a faction");
        }
        else if (arg.equalsIgnoreCase("switch"))
        {
            getCoreFunctions().getCoreErrors().commandSantaxError(player, "faction", "[switch] [faction] | this will let you switch to a faction instantly");
        }
        else if (arg.equalsIgnoreCase("leave"))
        {
            getCoreFunctions().getCoreErrors().commandSantaxError(player, "faction", "[leave] | this will let you join a faction");
        }
        else if (arg.equalsIgnoreCase("list"))
        {
            getCoreFunctions().getCoreErrors().commandSantaxError(player, "faction", "[list] | this will print out a list of factions");
        }
        else if (arg.equalsIgnoreCase("help"))
        {
            getCoreFunctions().getCoreErrors().commandSantaxError(player, "faction", "[help] | this will tell you how to use the faction command");
        }
    }

    public void mysqlDBcallback(PlayerFactionQuery query)
    {
        if (query != null && query.getFactionName() != null && query.getUUID() != null)
        {
            Bukkit.getScheduler().scheduleSyncDelayedTask(this, new PlayerFactionLoadedTask(Bukkit.getPlayer(query.getUUID()), getFaction(query.getFactionName()), this));
        }
        else
        {
            //TODO throw error some how
        }


    }
}
