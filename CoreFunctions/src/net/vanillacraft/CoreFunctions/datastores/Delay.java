package net.vanillacraft.CoreFunctions.datastores;

import java.util.concurrent.TimeUnit;

/*
Created by Mr_Little_Kitty on 5/8/2015
*/
public enum Delay
{
    SETHOME(60),
    TELEPORT(30);

    private final Cooldown time;

    private Delay(int minutes)
    {
        time = new Cooldown(minutes*60*1000);
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
