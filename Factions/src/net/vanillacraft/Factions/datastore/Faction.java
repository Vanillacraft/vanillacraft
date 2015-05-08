package net.vanillacraft.Factions.datastore;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryan on 5/7/2015.
 */
public class Faction {

    private String name;
    private ChatColor color;
    private List<String> allyList;

    public Faction(String name, ChatColor color, List<String> allyList){
        this.name = name;
        this.color = color;
        this.allyList = allyList;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public List<String> getAllyList() {
        return allyList;
    }

    public boolean isAlly(String FactionName){
        if(allyList.contains(FactionName)){
            return true;
        } else {
            return false;
        }
    }

}
