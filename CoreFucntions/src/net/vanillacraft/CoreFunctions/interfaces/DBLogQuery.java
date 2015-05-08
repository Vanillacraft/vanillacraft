package net.vanillacraft.CoreFunctions.interfaces;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
Created by Mr_Little_Kitty on 5/7/2015
*/
public abstract class DBLogQuery implements SelectRecord
{

    @Override
    public String getQuery()
    {
        return "SELECT col_player, col_block_material, col_block_data, col_timestamp FROM tbl_block_log WHERE col_action='PLACE' AND col_cancelled=0 AND col_public=0 AND col_world=? AND col_block_x=? AND col_block_y=? AND col_block_z=? ORDER BY col_id DESC LIMIT 1";
    }

    @Override
    public String getCacheKey()
    {
        return "LogQuery";
    }

    @Override
    public void setParameters(final PreparedStatement statement)
    {
        try
        {
            statement.setString(1, getWorld());
            statement.setInt(2, getX());
            statement.setInt(3, getY());
            statement.setInt(4, getZ());
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    protected abstract int getX();
    protected abstract int getY();
    protected abstract int getZ();
    protected abstract String getWorld();
}
