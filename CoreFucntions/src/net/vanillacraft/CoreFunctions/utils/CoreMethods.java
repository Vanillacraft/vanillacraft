package net.vanillacraft.CoreFunctions.utils;

import net.vanillacraft.CoreFunctions.datastores.CoreData;
import net.vanillacraft.CoreFunctions.datastores.Delay;
import net.vanillacraft.CoreFunctions.datastores.PlayerProfile;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import net.vanillacraft.Factions.datastore.Faction;
import net.vanillacraft.Zones.datastores.Zone;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by ryan on 5/5/2015.
 * This will contian random helper methods
 * so that things have a uniform appearance.
 */
public class CoreMethods
{
    private CoreFunctions plugin;

    public CoreMethods(CoreFunctions plugin)
    {
        this.plugin = plugin;
    }

    public void teleport(Player player, Location loc)
    {
        PlayerProfile profile = CoreData.getProfile(player);
        if (profile.isModMode())
        {
            player.sendMessage(ChatColor.GREEN + "You were teleported while in Mod Mode this does not count against " + "your timers.");
            player.teleport(loc);
        }
        else
        {
            if (canTeleport(profile))
            {
                player.teleport(loc);
                player.sendMessage(ChatColor.GREEN + "Your teleport timer has just had " + Delay.TELEPORT.getDelayTime().getAsMinutes() + " minutes added to it.");
            }
            else
            {
                plugin.getCoreErrors().timerNotDone(player,"teleport",profile.getRemainingDelay(Delay.TELEPORT).getFormatted());
            }
        }
    }

    public boolean canTeleport(PlayerProfile profile)
    {
        if (profile.isModMode())
        {
            return true;
        }
        else
        {
           return !profile.hasActiveDelay(Delay.TELEPORT);
        }
    }

    public boolean setHomeLocation(Player player)
    {
        PlayerProfile profile = CoreData.getProfile(player);
        if (!profile.hasActiveDelay(Delay.SETHOME))
        {
            plugin.getCoreData().setPlayerHome(player.getUniqueId(), player.getLocation());
            return true;
        }
        else
        {
            plugin.getCoreErrors().timerNotDone(player,"set home",profile.getRemainingDelay(Delay.SETHOME).getFormatted());
            return true;
        }
    }

    public Zone getZone(Location location)
    {
        return plugin.getCoreZones().getZone(location);
    }

    /**
     * This is old
     *
     * @deprecated please use player Profile instead
     */
    @Deprecated
    public Zone getZone(Player player)
    {
        return getZone(player.getLocation());
    }

    public Faction getFaction(Player player)
    {
        return plugin.getCoreFactions().getFaction(player);
    }

    public Faction getFaction(Location loc)
    {
        return plugin.getCoreFactions().getFaction(getZone(loc).getFactionName());
    }

    public boolean canSetHome(Player player, Faction targetFaction, Faction playerFaction)
    {
        if (CoreData.getProfile(player).hasActiveDelay(Delay.SETHOME))
        {
            if (playerFaction.isAlly(targetFaction.getName()))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

}
