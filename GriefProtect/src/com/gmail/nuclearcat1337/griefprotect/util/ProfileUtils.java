package com.gmail.nuclearcat1337.griefprotect.util;

import com.gmail.nuclearcat1337.griefprotect.main.GriefProtect;
import net.vanillacraft.CoreFunctions.utils.Receiver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utilities to lookup player names and uuids from mojang
 * This caches results so you won't have issues with the rate limit
 * 
 * <p>
 * <b>DONT Rely on Bukkit.getOfflinePlayer()</b>
 * It doesn't cache and is a workaround solution
 * 
 * evilmidgets fetchers are a fair solution, but they don't cache so you can run into ratelimits
 * 
 * @author Techcable
 */
public class ProfileUtils
{
    private ProfileUtils() {}

    private static Cache<String, PlayerProfile> nameCache = new Cache<>();
    private static Cache<UUID, PlayerProfile> idCache = new Cache<>();

    public static PlayerProfile quickLookup(String name)
    {
        if(nameCache.contains(name.toLowerCase()))
            return nameCache.get(name.toLowerCase());

        Player player = Bukkit.getPlayer(name);
        if(player != null)
        {
            PlayerProfile p = new PlayerProfile(player.getUniqueId(),player.getName());
            cacheProfile(p);
            return p;
        }
        return null;
    }

    public static PlayerProfile quickLookup(UUID id)
    {
        if(idCache.contains(id))
            return idCache.get(id);

        Player player = Bukkit.getPlayer(id);
        if(player != null)
        {
            PlayerProfile p = new PlayerProfile(player.getUniqueId(),player.getName());
            cacheProfile(p);
            return p;
        }
        return null;
    }

    public static void lookup(final String name, final Receiver<PlayerProfile> provider)
    {
        if(nameCache.contains(name.toLowerCase()))
        {
            provider.receive(nameCache.get(name.toLowerCase()));
            return;
        }

        Player player = Bukkit.getPlayer(name);
        if(player != null)
        {
            PlayerProfile p = new PlayerProfile(player.getUniqueId(),player.getName());
            cacheProfile(p);
            provider.receive(p);
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(GriefProtect.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                final PlayerProfile profile;
                //if (nameCache.contains(name.toLowerCase())) return nameCache.get(name.toLowerCase());
                List<PlayerProfile> response = postNames(new String[] {name});
                if (response == null || response.isEmpty())
                    profile = null;
                else
                    profile =  response.get(0);

                safeProvide(provider,profile);
            }
        });
    }

    public static void lookup(final UUID id, final Receiver<PlayerProfile> provider)
    {
        if(idCache.contains(id))
        {
            provider.receive(idCache.get(id));
            return;
        }

        Player player = Bukkit.getPlayer(id);
        if(player != null)
        {
            PlayerProfile p = new PlayerProfile(player.getUniqueId(),player.getName());
            cacheProfile(p);
            provider.receive(p);
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(GriefProtect.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                safeProvide(provider,lookupProperties(id));
            }
        });
    }
//    /**
//     * Lookup a profile with the given name
//     *
//     * The reuturned player profile doesn't include properties
//     * If properties are neaded, proceed to use a uuid lookup
//     *
//     * @param name look for a profile with this name
//     * @return a profile with the given name
//     */
//    public static PlayerProfile unSafeLookup(String name)
//    {
//        if (nameCache.contains(name.toLowerCase())) return nameCache.get(name.toLowerCase());
//        List<PlayerProfile> response = postNames(new String[] {name});
//        if (response == null) return null;
//        if (response.isEmpty()) return null;
//        return response.get(0);
//    }

//    /**
//     * Lookup a profile with the given uuid
//     *
//     * The reuturned player profile includes properties
//     *
//     * @param id look for a profile with this uuid
//     * @return a profile with the given id
//     */
//    public static PlayerProfile unSafeLookup(UUID id) {
//        return lookupProperties(id);
//    }
//
    private static void safeProvide(final Receiver<PlayerProfile> provider, final PlayerProfile profile)
    {
        Bukkit.getScheduler().scheduleSyncDelayedTask(GriefProtect.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                provider.receive(profile);
            }
        });
    }

    private static void cacheProfile(PlayerProfile profile)
    {
        nameCache.put(profile.getName().toLowerCase(),profile);
        idCache.put(profile.getId(),profile);
    }

    private static List<PlayerProfile> postNames(String[] names) { //This one doesn't cache
        JSONArray request = new JSONArray();
        for (String name : names) {
            request.add(name);
        }
        Object rawResponse = postJson("https://api.mojang.com/profiles/minecraft", request);
        if (!(rawResponse instanceof JSONArray)) return null;
        JSONArray response = (JSONArray) rawResponse;
        List<PlayerProfile> profiles = new ArrayList<>();
        for (Object rawEntry : response) {
            if (!(rawEntry instanceof JSONObject)) return null;
            JSONObject entry = (JSONObject) rawEntry;
            PlayerProfile profile = deserializeProfile(entry);
            if (profile != null)
            {
                cacheProfile(profile);
                profiles.add(profile);
            }
        }
        return profiles;
    }
    
    private static PlayerProfile lookupProperties(UUID id) {
        if (idCache.contains(id)) return idCache.get(id);
        Object rawResponse = getJson("https://sessionserver.mojang.com/session/minecraft/profile/" + id.toString().replace("-", ""));
        if (rawResponse == null || !(rawResponse instanceof JSONObject)) return null;
        JSONObject response = (JSONObject) rawResponse;
        PlayerProfile profile = deserializeProfile(response);
        if (profile == null) return null;
        cacheProfile(profile);
        return profile;
    }
    
    //Json Serialization
    
    private static PlayerProfile deserializeProfile(JSONObject json) {
        if (!json.containsKey("name") || !json.containsKey("id")) return null;
        if (!(json.get("name") instanceof String) || !(json.get("id") instanceof String)) return null;
        String name = (String) json.get("name");
        UUID id = toUUID((String)json.get("id"));
        if (id == null) return null;
        PlayerProfile profile = new PlayerProfile(id, name);
        if (json.containsKey("properties") && json.get("properties") instanceof JSONArray) {
            profile.setProperties((JSONArray) json.get("properties"));
        }
        return profile;
    }
    
    //Utilities
    
    private static String toString(UUID id) {
        return id.toString().replace("-", "");
    }
    
    private static UUID toUUID(String raw) {
        String dashed;
        if (raw.length() == 32) {
            dashed = raw.substring(0, 8) + "-" + raw.substring(8, 12) + "-" + raw.substring(12, 16) + "-" + raw.substring(16, 20) + "-" + raw.substring(20, 32);
        } else {
            dashed = raw;
        }
        return UUID.fromString(dashed);
    }
    
    private static JSONParser PARSER = new JSONParser();
    
    private static Object getJson(String rawUrl) {
        BufferedReader reader = null;
        try {
            URL url = new URL(rawUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer result = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) result.append(line);
            return PARSER.parse(result.toString());
        } catch (Exception ex) {
            return null;
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }
    
    private static Object postJson(String url, JSONArray body) {
        String rawResponse = post(url, body.toJSONString());
        if (rawResponse == null) return null;
        try {
            return PARSER.parse(rawResponse);
        } catch (Exception e) {
            return null;
        }
    }
    
    private static String post(String rawUrl, String body) {
        BufferedReader reader = null;
        OutputStream out = null;
        
        try {
            URL url = new URL(rawUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            out = connection.getOutputStream();
            out.write(body.getBytes());
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer result = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) result.append(line);
            return result.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                if (out != null) out.close();
                if (reader != null) reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }
    
    private static class Cache<K, V> {

        private long expireTime = 1000 * 60 * 5; //default 5 min
        private Map<K, CachedEntry<V>> map = new ConcurrentHashMap<>();

        public boolean contains(K key) {
            return map.containsKey(key) && get(key) != null;
        }

        public V get(K key) {
            CachedEntry<V> entry = map.get(key);
            if (entry == null) return null;
            if (entry.isExpired()) {
                map.remove(key);
                return null;
            } else {
                return entry.getValue();
            }
        }
    
        public void put(K key, V value) {
            map.put(key, new CachedEntry(value, expireTime));
        }

        private static class CachedEntry<V> {

            public CachedEntry(V value, long expireTime) {
                this.value = new SoftReference(value);
                this.expires = expireTime + System.currentTimeMillis();
            }

            private final SoftReference<V> value; //Caching is low memory priortiy
            private final long expires;

            public V getValue() {
                if (isExpired()) {
                    return null;
                }
                return value.get();
            }

            public boolean isExpired() {
                if (value.get() == null) return true;
                return expires != -1 && expires > System.currentTimeMillis();
            }
        }
    }
}
