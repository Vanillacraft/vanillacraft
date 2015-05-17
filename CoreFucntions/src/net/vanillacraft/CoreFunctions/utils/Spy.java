package net.vanillacraft.CoreFunctions.utils;

import net.vanillacraft.CoreFunctions.datastores.CoreData;
import net.vanillacraft.CoreFunctions.datastores.PlayerProfile;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by ryan on 5/16/2015.
 */
public class Spy implements Listener
{
    private CoreFunctions plugin;

    public Spy(CoreFunctions plugin){
        Bukkit.getPluginManager().registerEvents(this, CoreFunctions.getInstance());
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        if (!event.isCancelled())
        {
            Player player = event.getPlayer();
            String command[] = event.getMessage().split(" ");
            PlayerProfile profile = CoreData.getProfile(player);

            if (command[0].equalsIgnoreCase("/spy"))
            {
                if(profile.isModMode()){
                    plugin.getCoreMethods().hidePlayer(player);
                    player.sendMessage(ChatColor.GREEN + "You're now hidden from other players except people in mod mode.");
                } else {
                    plugin.getCoreErrors().enableModMode(player);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event){
        plugin.getCoreMethods().resetSpiesForPlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event){
        plugin.getCoreMethods().showAllSpiesForPlayer(event.getPlayer());
    }
}
