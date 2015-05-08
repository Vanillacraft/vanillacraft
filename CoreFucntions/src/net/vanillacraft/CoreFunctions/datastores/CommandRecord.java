package net.vanillacraft.CoreFunctions.datastores;

import net.vanillacraft.CoreFunctions.interfaces.InsertRecord;
import org.bukkit.command.Command;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.LogRecord;

/**
 * Created by ryan on 5/8/2015.
 */
public class CommandRecord implements InsertRecord
{
    private String command;
    private String[] args;
    private UUID uuid;

    public CommandRecord(UUID uuid, String command, String[] args)
    {
        this.uuid = uuid;
        this.command = command;
        this.args = args;
    }

    @Override
    public void setParameters(final PreparedStatement statement)
    {
        try
        {
            statement.setString(1, uuid.toString());
            statement.setString(2, command);
            String temp = "";
            for (String s : args)
            {
                temp += s + " ";
            }
            statement.setString(3, temp);
        } catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public String getQuery()
    {
        return "INSERT INTO tbl_log_commands (col_timestamp, col_UUID, col_command, col_args) VALUES (UTCNOW(),?,?,?);";
    }

    @Override
    public String getCacheKey()
    {
        return "Command";
    }

}
