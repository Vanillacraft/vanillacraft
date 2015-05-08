package net.vanillacraft.Factions.main;

import net.vanillacraft.Factions.datastore.Faction;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by ryan on 5/7/2015.
 */
public class Factions extends JavaPlugin {

    private Faction[] factionList;

    @Override
    public void onEnable(){

    }

    @Override
    public void onDisable(){

    }

    public Faction[] getFactionList(){
        return factionList;
    }

}
