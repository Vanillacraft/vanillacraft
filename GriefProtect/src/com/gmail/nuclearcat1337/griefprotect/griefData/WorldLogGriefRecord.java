package com.gmail.nuclearcat1337.griefprotect.griefData;

import net.vanillacraft.CoreFunctions.interfaces.InsertRecord;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

public class WorldLogGriefRecord implements InsertRecord
{
    private long timestamp;

    private String action;
    private UUID player;
    private String world;

    private UUID owner;
    private String content;

    private Material blockMaterial;
    private byte blockData;

    private int x;
    private int y;
    private int z;
    private boolean allowed;

    public WorldLogGriefRecord(String action, UUID player, UUID owner, BlockState block, boolean allowed, String content) {
        timestamp = System.currentTimeMillis();
        this.action = action;
        this.player = player;
        this.owner = owner;
        this.allowed = allowed;
        this.content = content;

        world = block.getWorld().getName();

        blockMaterial = block.getType();
        blockData = block.getData().getData();
        x = block.getX();
        y = block.getY();
        z = block.getZ();
    }

    public WorldLogGriefRecord(String action, UUID player, UUID owner, BlockState block, boolean allowed) {
        this(action, player, owner, block, allowed,  null);
    }

    public WorldLogGriefRecord(String action, UUID player, String content, Location location) {
        timestamp = System.currentTimeMillis();
        this.action = action;
        this.player = player;
        this.content = content;

        world = location.getWorld().getName();

        this.owner = null;
        blockMaterial = Material.AIR;
        blockData = 0;

        x = location.getBlockX();
        y = location.getBlockY();
        z = location.getBlockZ();
    }

    @Override
    public void setParameters(final PreparedStatement statement)
    {
        try
        {
            statement.setTimestamp(1, new Timestamp(timestamp));
            statement.setString(2, action);
            statement.setString(3, player.toString());
            statement.setString(4, owner == null ? null : owner.toString());
            statement.setString(5, blockMaterial.toString());
            statement.setByte(6, blockData);
            statement.setString(7, content);
            statement.setInt(8, x);
            statement.setInt(9, y);
            statement.setInt(10, z);
            statement.setBoolean(11, allowed);
            statement.setString(12, world);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String getQuery()
    {
        return "INSERT INTO tbl_griefprotect_log (col_timestamp, col_action, col_player, col_owner, col_block_material, col_block_data, col_content, col_x, col_y, col_z, col_allowed, col_world) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    public String getCacheKey()
    {
        return "GriefLogRec";
    }
}
