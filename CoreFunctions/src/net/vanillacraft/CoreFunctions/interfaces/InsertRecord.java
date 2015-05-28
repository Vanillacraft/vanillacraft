package net.vanillacraft.CoreFunctions.interfaces;

import java.sql.PreparedStatement;

/*
Created by Mr_Little_Kitty on 5/7/2015
*/
public interface InsertRecord
{
    /**
     * Set the parameters of your query.
     * @param statement The PreparedStatement object to set your parameters in
     */
    void setParameters(PreparedStatement statement);

    /**
     * The actual query you want to execute
     * @return The query you want executed. In string form.
     */
    String getQuery();

    /**
     * This method is used to cache certain statements that are run often.
     * @return A unique string that can be used to cache this type of query.
     */
    String getCacheKey();
}
