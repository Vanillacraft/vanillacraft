package net.vanillacraft.CoreFunctions.datastores;

import net.vanillacraft.CoreFunctions.interfaces.WorldLogRecord;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by ryan on 5/7/2015.
 */
public class SetHomeRecord implements WorldLogRecord {

    private UUID uuid;
    private Location location;

    public SetHomeRecord(UUID uuid, Location location){
        this.uuid = uuid;
        this.location = location;
    }

    @Override
    public String getType(){
        return "Set Home";
    }

    @Override
    public String getStatementText(){
        return "INSERT INTO tbl_homes (col_UUID, col_world, col_x, col_y, col_z) VALUES (?,?,?,?,?)";
    }

    @Override
    public void executeRecord(final PreparedStatement statement){
        try {
            statement.setString(1, uuid.toString());
            statement.setString(2, location.getWorld().getName());
            statement.setInt(3, location.getBlockX());
            statement.setInt(4, location.getBlockY());
            statement.setInt(5, location.getBlockZ());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
