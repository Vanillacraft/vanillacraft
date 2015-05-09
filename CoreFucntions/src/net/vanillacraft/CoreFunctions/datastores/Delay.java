package net.vanillacraft.CoreFunctions.datastores;

import java.util.concurrent.TimeUnit;

/*
Created by Mr_Little_Kitty on 5/8/2015
*/
public enum Delay
{
    SETHOME(60),
    TELEPORT(30);

    private long delay;

    private Delay(int minutes)
    {
        delay = minutes*60*1000;
    }

    public Cooldown getDelayTime()
    {
        return new Cooldown(delay);
    }

    //    public static long Convert(long miliseconds,TimeUnit value)
    //    {
    //        return value.convert(miliseconds,TimeUnit.MILLISECONDS);
    //    }
}
