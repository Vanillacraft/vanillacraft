package net.vanillacraft.CoreFunctions.utils;

import net.vanillacraft.CoreFunctions.datastores.CoreData;
import net.vanillacraft.CoreFunctions.datastores.PlayerProfile;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

/**
 * Created by ryan on 5/17/2015.
 */
public class Nerf implements Listener
{
    CoreFunctions plugin;

    public Nerf(CoreFunctions plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, CoreFunctions.getInstance());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        if (!event.isCancelled())
        {
            Player player = event.getPlayer();
            String command[] = event.getMessage().split(" ");
            PlayerProfile profile = CoreData.getProfile(player);

            if (command[0].equalsIgnoreCase("/nerf"))
            {
                if (profile.isModMode())
                {
                    if(command.length > 0){

                        List<Player> target = plugin.getServer().matchPlayer(command[1]);
                        if(target == null || target.size() != 1){
                            plugin.getCoreErrors().playerNotFound(player);
                        } else if((Player)target.get(0)== player){
                            plugin.getCoreErrors().youCannotNerfThatPlayer(player);
                        } else {
                            Player targetPlayer = ((Player)target.get(0));
                            PlayerProfile targetProfile = CoreData.getProfile(targetPlayer);
                            Long antiSpamDelay = targetProfile.getData("Nerfed", long.class);

                            if(antiSpamDelay == null){
                                nerfPlayer(player, targetPlayer, targetProfile);
                            } else {
                                if(antiSpamDelay <= System.currentTimeMillis()){
                                    unnerfPlayer(player, targetPlayer, targetProfile);
                                } else {
                                    plugin.getCoreErrors().playerAlreadyFrozen(player);
                                }
                            }

                        }
                    }
                } else {
                    plugin.getCoreErrors().enableModMode(player);
                }
            }
        }
    }





    private void nerfPlayer(Player mod, Player targetPlayer, PlayerProfile target){
        target.setData("Nerfed", System.currentTimeMillis() + 5000);
        plugin.getCoreErrors().notifyModNerfedPlayer(mod, targetPlayer, true);
        //TODO: Title shit & channels
    }

    private void unnerfPlayer(Player mod, Player targetPlayer, PlayerProfile target){
        target.setData("Nerfed", null);
        plugin.getCoreErrors().notifyModNerfedPlayer(mod, targetPlayer, false);
        //TODO: Title shit & channels
    }

}
