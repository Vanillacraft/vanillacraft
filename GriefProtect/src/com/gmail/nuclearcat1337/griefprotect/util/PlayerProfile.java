package com.gmail.nuclearcat1337.griefprotect.util;

import org.json.simple.JSONArray;

import java.util.UUID;

/**
 * Represnents a player
 * Contains their uuid and username
 *
 * This may or may not have properties
 *
 * @author Techcable
 */
public class PlayerProfile {

    public PlayerProfile(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    private JSONArray properties;
    private final UUID id;
    private final String name;

    /**
     * Get this player's uuid
     *
     * @return this players uuid
     */
    public UUID getId() {
        return id;
    }

    /**
     * Get this player's name
     *
     * @return this player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Get a json array with this players properties
     * Returns null if this players propeties haven't been retreived
     *
     * @return a json array with this player's properties or null if not retreived
     */
    public JSONArray getProperties() {
        return properties;
    }

    public void setProperties(JSONArray properties)
    {
        this.properties = properties;
    }
}
