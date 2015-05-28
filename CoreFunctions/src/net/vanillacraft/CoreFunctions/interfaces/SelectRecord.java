package net.vanillacraft.CoreFunctions.interfaces;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*
Created by Mr_Little_Kitty on 5/7/2015
*/
public interface SelectRecord
{
    /**
     * The actual query you want to execute
     * @return The query you want executed. In string form.
     */
    String getQuery();

    /**
     * This is called back with the results of the query.
     * This method will be called in the database thread.
     * @param set The ResultSet from the executed query
     */
    void callbackAsync(ResultSet set);

    /**
     * This is where you should use the values set in the async call back method.
     * This method is run in the normal game thread.
     */
    void runSynchronously();

    /**
     * This method is used to cache certain statements that are run often.
     * @return A unique string that can be used to cache this type of query.
     */
    String getCacheKey();

    /**
     * Set the parameters of your query.
     * @param statement The PreparedStatement object to set your parameters in
     */
    void setParameters(PreparedStatement statement);
}
