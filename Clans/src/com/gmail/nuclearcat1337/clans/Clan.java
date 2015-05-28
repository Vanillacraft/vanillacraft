package com.gmail.nuclearcat1337.clans;

import net.vanillacraft.CoreFunctions.datastores.Cooldown;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/*
Created by Mr_Little_Kitty on 5/27/2015
*/
public class Clan
{
    private UUID clanID;
    private String name;
    private String tag;
    private UUID owner;
    private Set<UUID> members;

    //This is the map of timestamps for killing people. Example: If MLK kills Raytunes, no one in this clan would
    //be able to attack Raytunes until his cooldown is up
    private Map<UUID,Cooldown> playerKillCooldown;

    public Clan(UUID clanID, String name, String tag, UUID owner)
    {
        this.clanID = clanID;
        this.name= name;
        this.tag =tag;
        this.owner= owner;
    }

    public void addMember(UUID member)
    {
        this.members.add(member);
    }


    public Cooldown getCooldown(UUID player)
    {
        return playerKillCooldown.get(player);
    }

    public boolean hasCooldown(UUID player)
    {
        Cooldown c = getCooldown(player);
        if(c == null || c.getAsMiliseconds() >= System.currentTimeMillis())
            return false;
        return true;
    }
}
