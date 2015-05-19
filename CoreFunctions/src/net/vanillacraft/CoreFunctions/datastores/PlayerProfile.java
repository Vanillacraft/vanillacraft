package net.vanillacraft.CoreFunctions.datastores;

import net.vanillacraft.CoreFunctions.utils.Loc;
import net.vanillacraft.Factions.datastore.Faction;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
Created by Mr_Little_Kitty on 5/8/2015
*/
public class PlayerProfile
{
    private Map<Delay, Long> delays;
    private Map<Object, Object> data;

    private UUID id;
    private String name;

    PlayerProfile(UUID id, String name)
    {
        this.id = id;
        this.name = name;
        delays = new EnumMap<Delay, Long>(Delay.class);
        data = new HashMap<>();
    }

    public UUID getID()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public <T> T getData(Object key, Class<T> returnType)
    {
        Object b = data.get(key);
        if (b == null)
        {
            return null;
        }
        return (T) b;
    }

    /*
        So far the values we're using
        "Faction"  : returns player's faction                   todo: still needs to be implemented in factions plugin
        "Stick"    : returns if the mod stick is enabled        todo: still need to make a command to enable this
        "ModeMode" : returns bool if moderator mode is enabled  todo: still need to make a command to enable this
        "Home"     : returns a loc class                        todo: still need to make a method to insert home into this on player login or server load
        "hasPerm"  : returns bool if player is allowed to do    todo: still need to make a groups plugin
        "Nerfed"   : returns long if nerfed
     */

    public Boolean is(Object key)
    {
        Object obj = data.get(key);
        if (obj == null || !(obj instanceof Boolean))
        {
            return false;
        }
        return (Boolean) obj;
    }

    public void setData(Object key, Object value)
    {
        data.put(key, value);
    }

    public boolean hasActiveDelay(Delay delay)
    {
        Long l = delays.get(delay);
        if (l == null || l.longValue() < System.currentTimeMillis())
        {
            return false;
        }
        return true;
    }

    public Cooldown getRemainingDelay(Delay delay)
    {
        if (!hasActiveDelay(delay))
        {
            return new Cooldown(0);
        }
        return new Cooldown(delays.get(delay) - System.currentTimeMillis());
    }

    public void addDelay(Delay delay)
    {
        delays.put(delay, System.currentTimeMillis() + delay.getDelayTime().getAsMiliseconds());
    }

    public boolean isModMode()
    {
        return is("ModeMode");
    }

    public Loc getHomeLocation()
    {
        return getData("Home", Loc.class);
    }

    public boolean hasPermision(String permison)
    {
        //TODO: need to make this be a basic permision system
        return false;
    }
    /*
        Perms so far
        "nerf" : used to see if they are allowed to nerf players.
     */

}
