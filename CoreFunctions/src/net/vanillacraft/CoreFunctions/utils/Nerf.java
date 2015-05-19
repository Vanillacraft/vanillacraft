package net.vanillacraft.CoreFunctions.utils;

import net.vanillacraft.CoreFunctions.datastores.CoreData;
import net.vanillacraft.CoreFunctions.datastores.PlayerProfile;
import net.vanillacraft.CoreFunctions.fanciful.Title;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.List;

/**
 * Created by ryan on 5/17/2015.
 */
public class Nerf implements Listener
{
    CoreFunctions plugin;
    static Title frozenTitle = new Title("You have been frozen.", "By a moderator please read chat.", 1, Integer.MAX_VALUE, 1);

    public Nerf(CoreFunctions plugin)
    {
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
                    if (command.length > 0)
                    {

                        List<Player> target = plugin.getServer().matchPlayer(command[1]);
                        if (target == null || target.size() != 1)
                        {
                            plugin.getCoreErrors().playerNotFound(player);
                        }
                        else if ((Player) target.get(0) == player)
                        {
                            plugin.getCoreErrors().youCannotNerfThatPlayer(player);
                        }
                        else
                        {
                            Player targetPlayer = ((Player) target.get(0));
                            PlayerProfile targetProfile = CoreData.getProfile(targetPlayer);
                            Long antiSpamDelay = targetProfile.getData("Nerfed", long.class);

                            if (antiSpamDelay == null)
                            {
                                nerfPlayer(player, targetPlayer, targetProfile);
                            }
                            else
                            {
                                if (antiSpamDelay <= System.currentTimeMillis())
                                {
                                    unnerfPlayer(player, targetPlayer, targetProfile);
                                }
                                else
                                {
                                    plugin.getCoreErrors().playerAlreadyFrozen(player);
                                }
                            }

                        }
                    }
                }
                else
                {
                    plugin.getCoreErrors().enableModMode(player);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent event)
    {
        if (!event.isCancelled())
        {
            PlayerProfile profile = CoreData.getProfile(event.getPlayer());
            if (profile.getData("Nerfed", long.class) != null)
            {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if (!event.isCancelled())
        {
            PlayerProfile profile = CoreData.getProfile(event.getPlayer());
            if (profile.getData("Nerfed", long.class) != null)
            {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if (!event.isCancelled())
        {
            PlayerProfile profile = CoreData.getProfile(event.getPlayer());

            if (profile.getData("Nerfed", long.class) != null)
            {
                Location to = event.getTo();
                Location from = event.getFrom();
                // only teleport back if player moved less than 1 square away (ignores summon to moderator)
                if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ())
                {
                    event.setTo(event.getFrom());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (!event.isCancelled())
        {
            PlayerProfile profile = CoreData.getProfile(event.getPlayer());

            if (profile.getData("Nerfed", long.class) != null)
            {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerPickupItem(PlayerPickupItemEvent event)
    {
        if (!event.isCancelled())
        {
            PlayerProfile profile = CoreData.getProfile(event.getPlayer());

            if (profile.getData("Nerfed", long.class) != null)
            {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamage(EntityDamageEvent event)
    {
        if (!event.isCancelled() && event.getEntity() instanceof Player)
        {
            PlayerProfile profile = CoreData.getProfile((Player) event.getEntity());

            if (profile.getData("Nerfed", long.class) != null)
            {
                event.setCancelled(true);
            }
            else if (event instanceof EntityDamageByEntityEvent)
            {
                EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
                Entity damager = e.getDamager();
                if (damager instanceof Projectile)
                {
                    damager = (Entity) ((Projectile) damager).getShooter();
                }

                if (damager instanceof Player && CoreData.getProfile((Player) damager).getData("Nerfed", long.class) != null)
                {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityTarget(EntityTargetEvent event)
    {
        if (!event.isCancelled() && event.getEntity() instanceof Player)
        {
            PlayerProfile profile = CoreData.getProfile((Player) event.getEntity());

            if (profile.getData("Nerfed", long.class) != null)
            {
                event.setCancelled(true);
            }
        }
    }

    private void nerfPlayer(Player mod, Player targetPlayer, PlayerProfile target)
    {
        target.setData("Nerfed", System.currentTimeMillis() + 5000);
        plugin.getCoreErrors().notifyModNerfedPlayer(mod, targetPlayer, true);
        frozenTitle.send(targetPlayer);
        //TODO: Title shit & channels
    }

    private void unnerfPlayer(Player mod, Player targetPlayer, PlayerProfile target)
    {
        target.setData("Nerfed", null);
        plugin.getCoreErrors().notifyModNerfedPlayer(mod, targetPlayer, false);
        frozenTitle.clearTitle(targetPlayer);
        //TODO: Title shit & channels
    }

}
