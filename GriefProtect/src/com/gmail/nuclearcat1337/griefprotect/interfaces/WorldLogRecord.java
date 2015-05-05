package com.gmail.nuclearcat1337.griefprotect.interfaces;

import com.gmail.nuclearcat1337.griefprotect.worldLogger.RecordType;

import java.sql.PreparedStatement;

public interface WorldLogRecord
{
    void executeRecord(PreparedStatement statement);
    RecordType getType();
}
