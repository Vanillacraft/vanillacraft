package net.vanillacraft.Zones.datastores;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashSet;

/**
 * Created by ryan on 5/6/2015.
 */
public class Zone
{
    String name;

    double center_x;
    double center_z;
    double radius;

    double min_x;
    double max_x;
    double min_y;
    double max_y;
    double min_z;
    double max_z;

    boolean circle;
    boolean pvp;
    boolean ffaPVP;
    boolean protect;
    boolean opOnly;
    boolean donor;

    int warning;
    boolean permaBan;

    String factionName;

    HashSet<Material> ignoreBlock;

    Location spawn;

    public Zone(String name, double x1, double y1, double z1, double x2, double y2, double z2, boolean circle, boolean pvp, boolean protect, int warning, boolean permaBan)
    {
        min_x = x1;
        min_y = y1;
        min_z = z1;

        max_x = x2;
        max_y = y2;
        max_z = z2;

        center_x = min_x + ((max_x - min_x) / 2);
        center_z = min_z + ((max_z - min_z) / 2);

        this.radius = Math.abs(center_x - min_x);

        this.circle = circle;
        this.pvp = pvp;
        this.protect = protect;
        this.warning = warning;
        this.permaBan = permaBan;

        ignoreBlock = null;

        spawn = null;

        factionName = null;

        opOnly = false;

    }

    public String getName()
    {
        return name;
    }

    public double getCenter_x()
    {
        return center_x;
    }

    public double getCenter_z()
    {
        return center_z;
    }

    public double getRadius()
    {
        return radius;
    }

    public double getMin_x()
    {
        return min_x;
    }

    public double getMax_x()
    {
        return max_x;
    }

    public double getMin_y()
    {
        return min_y;
    }

    public double getMax_y()
    {
        return max_y;
    }

    public double getMin_z()
    {
        return min_z;
    }

    public double getMax_z()
    {
        return max_z;
    }

    public boolean isCircle()
    {
        return circle;
    }

    public boolean isPvp()
    {
        return pvp;
    }

    public boolean isFfaPVP()
    {
        return ffaPVP;
    }

    public boolean isProtect()
    {
        return protect;
    }

    public boolean isOpOnly()
    {
        return opOnly;
    }

    public boolean isDonor()
    {
        return donor;
    }

    public int getWarning()
    {
        return warning;
    }

    public boolean isPermaBan()
    {
        return permaBan;
    }

    public String getFactionName()
    {
        return factionName;
    }

    public HashSet<Material> getIgnoreBlock()
    {
        return ignoreBlock;
    }

    public Location getSpawn()
    {
        return spawn;
    }
}
