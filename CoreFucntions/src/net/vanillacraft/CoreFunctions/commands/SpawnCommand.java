package net.vanillacraft.CoreFunctions.commands;

import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * Created by ryan on 5/5/2015.
 */
public class SpawnCommand implements CommandExecutor {

    private CoreFunctions plugin;

    public SpawnCommand(CoreFunctions plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String s, final String[] args)
    {
        final String cmd = command.getName();
        if(cmd.equalsIgnoreCase("spawn")){
            if(sender instanceof Player){
                Player player = (Player)sender;

                if(player.getLocation().getWorld() == plugin.getCoredata().getSpawnLocation().getWorld()){
                    if(plugin.getCoremethods().isModMode(player)){
                        normalTeleport(player, plugin.getCoredata().getSpawnLocation(), true);
                    } else {
                        normalTeleport(player, plugin.getCoredata().getSpawnLocation());
                        return true;
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You must be in the main world.");
                    return true;
                }
            }
        }
        return false;
    }

    public void normalTeleport(Player player, Location location){
        normalTeleport(player, location, false);
    }

    public void normalTeleport(Player player, Location location, Boolean op){
        plugin.getCoremethods().teleport(player, location,op);
    }

}