package net.vanillacraft.CoreFunctions.datastores;

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
    private Map<Delay,Long> delays;
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
        checkDataMap();
        return (T)data.get(key);
    }

    public Boolean is(Object key)
    {
        Object obj = data.get(key);
        if(obj == null || !(obj instanceof Boolean))
            return false;
        return (Boolean)obj;
    }

    public void setData(Object key, Object value)
    {
        checkDataMap();
        data.put(key, value);
    }

    private void checkDataMap()
    {
        if(data == null)
            data = new HashMap<>();
    }

    public boolean hasActiveDelay(Delay delay)
    {
        Long l = delays.get(delay);
        if(l == null || l.longValue() < System.currentTimeMillis())
            return false;
        return true;
    }

    public Cooldown getRemainingDelay(Delay delay)
    {
        if(!hasActiveDelay(delay))
            return new Cooldown(0);
        return new Cooldown(delays.get(delay)-System.currentTimeMillis());
    }

    public void addDelay(Delay delay)
    {
        delays.put(delay,System.currentTimeMillis()+delay.getDelayTime().getAsMiliseconds());
    }

    public boolean isModMode()
    {
        return is("ModeMode");
    }
}
