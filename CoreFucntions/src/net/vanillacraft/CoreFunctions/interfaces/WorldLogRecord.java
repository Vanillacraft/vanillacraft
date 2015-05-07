package net.vanillacraft.CoreFunctions.interfaces;

import java.sql.PreparedStatement;

public interface WorldLogRecord
{
    void executeRecord(PreparedStatement statement);
    String getType();
    String getStatementText();
}
