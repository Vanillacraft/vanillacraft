package net.vanillacraft.CoreFunctions.datastores;

import java.util.concurrent.TimeUnit;

/*
Created by Mr_Little_Kitty on 5/8/2015
*/
public enum Delay
{
    //changed this to seconds because it was not compatible with compass/clocks
    SETHOME(3600),
    FACTION(86400),
    TELEPORT(1800),
    COMPASS(5);

    private final Cooldown time;

    private Delay(int seconds)
    {
        time = new Cooldown(seconds*1000);
    }

    public Cooldown getDelayTime()
    {
        return time;
    }

    //    public static long Convert(long miliseconds,TimeUnit value)
    //    {
    //        return value.convert(miliseconds,TimeUnit.MILLISECONDS);
    //    }
}
