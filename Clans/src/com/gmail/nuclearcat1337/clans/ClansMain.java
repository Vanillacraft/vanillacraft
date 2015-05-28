package com.gmail.nuclearcat1337.clans;

import net.vanillacraft.CoreFunctions.datastores.PlayerProfile;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;

/*
Created by Mr_Little_Kitty on 5/27/2015
*/
public class ClansMain extends JavaPlugin
{
    private Map<UUID,Clan> clans;
    private CoreFunctions functions;

    @Override
    public void onEnable()
    {
        functions = Bukkit.getServicesManager().load(CoreFunctions.class);


    }

    @Override
    public void onDisable()
    {

    }

    public void setClan(PlayerProfile profile, Clan clan)
    {
        profile.setData("Clan",clan);
    }

    public Clan getClan(PlayerProfile profile)
    {
        return profile.getData("Clan",Clan.class);
    }
}
