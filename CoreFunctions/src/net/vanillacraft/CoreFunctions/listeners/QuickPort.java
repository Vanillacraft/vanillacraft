package net.vanillacraft.CoreFunctions.listeners;

import net.vanillacraft.CoreFunctions.datastores.Delay;
import net.vanillacraft.CoreFunctions.datastores.PlayerProfile;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashSet;

/**
 * Created by ryan on 6/5/2015.
 */
public class QuickPort implements Listener
{
    private CoreFunctions plugin;

    public QuickPort(CoreFunctions plugin)
    {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, CoreFunctions.getInstance());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        PlayerProfile profile = plugin.getCoreData().getProfile(player);

        if (profile.isModMode())
        {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
            {
                switch (event.getClickedBlock().getType())
                {
                    case DISPENSER:
                    case NOTE_BLOCK:
                    case BED:
                    case CHEST:
                    case ENDER_CHEST:
                    case WORKBENCH:
                    case FURNACE:
                    case BURNING_FURNACE:
                    case WOODEN_DOOR:
                    case LEVER:
                    case STONE_BUTTON:
                    case JUKEBOX:
                    case CAKE:
                    case DIODE:
                    case TRAP_DOOR:
                    case FENCE_GATE:
                    case ENCHANTMENT_TABLE:
                    case BREWING_STAND:
                        return;
                }
            }

            if (event.getMaterial() == Material.COMPASS && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK))
            {
                if (!profile.hasActiveDelay(Delay.COMPASS))
                {
                    HashSet<Byte> temp = new HashSet<Byte>();
                    Block targetBlock = player.getTargetBlock(temp, 256);
                    Location playerLoc = player.getLocation();

                    if (targetBlock == null || (Math.max(Math.abs(targetBlock.getX() - playerLoc.getBlockX()), Math.abs(targetBlock.getZ() - playerLoc.getBlockZ()))) > 5)
                    {
                        //todo error message to far
                        return;
                    }

                    int i = 0;
                    if (targetBlock.getX() == playerLoc.getBlockX() && (targetBlock.getY() == (playerLoc.getBlockY() - 1) ||
                            targetBlock.getY() == playerLoc.getBlockY()) && targetBlock.getZ() == playerLoc.getBlockZ())
                    {
                        Block t;
                        while (true)
                        {
                            while (!testBlock(targetBlock.getRelative(BlockFace.DOWN, 1)))
                            {
                                targetBlock = targetBlock.getRelative(BlockFace.DOWN, 1);
                                if (++i >= 5)
                                {
                                    break;
                                }
                            }
                            targetBlock = targetBlock.getRelative(BlockFace.DOWN, 1);

                            if (i >= 5)
                            {
                                //TODO cant find block within i blocks error
                                return;
                            }

                            int j = 0;
                            t = targetBlock;
                            while (testBlock(t.getRelative(BlockFace.DOWN, 1)))
                            {
                                t = t.getRelative(BlockFace.DOWN, 1);
                                j++;
                            }

                            if (j >= 1)
                            {
                                targetBlock = targetBlock;
                                break;
                            }
                        }
                    }
                    else
                    {
                        while (targetBlock.getY() < 255 && (!testBlock(targetBlock.getRelative(BlockFace.UP, 1)) || !testBlock(targetBlock.getRelative(BlockFace.UP, 2))))
                        {
                            targetBlock = targetBlock.getRelative(BlockFace.UP, 1);

                            if (i++ >= 255)
                            {
                                //todo move this to coreErrors player.sendMessage("Something weird happened, teleporting aborted");
                                return;
                            }
                        }
                    }

                    Location blockLoc = targetBlock.getLocation();
                    blockLoc.setX(blockLoc.getX() + 0.5);
                    blockLoc.setY(blockLoc.getY() + (testBlock(targetBlock) ? 0 : 1));
                    blockLoc.setZ(blockLoc.getZ() + 0.5);
                    blockLoc.setPitch(playerLoc.getPitch());
                    blockLoc.setYaw(playerLoc.getYaw());

                    player.teleport(blockLoc);
                    profile.addDelay(Delay.COMPASS);

                    //TODO add a tp log (should be a global log [aka everything including home, spawn, etc])
                }
                else
                {
                    //TODO send error message about using to soon
                }
            }
        }

    }

    private boolean testBlock(Block b)
    {
        switch (b.getType())
        {
            case AIR:
            case WATER:
            case STATIONARY_WATER:
            case RED_ROSE:
            case YELLOW_FLOWER:
            case SAPLING:
            case BROWN_MUSHROOM:
            case RED_MUSHROOM:
            case TORCH:
            case REDSTONE_WIRE:
            case CROPS:
            case SIGN_POST:
            case WOODEN_DOOR:
            case IRON_DOOR_BLOCK:
            case LADDER:
            case RAILS:
            case WALL_SIGN:
            case LEVER:
            case STONE_PLATE:
            case WOOD_PLATE:
            case REDSTONE_TORCH_OFF:
            case REDSTONE_TORCH_ON:
            case STONE_BUTTON:
            case SNOW:
            case SUGAR_CANE_BLOCK:
            case DEAD_BUSH:
            case LONG_GRASS:
                return true;

            default:
                return false;
        }
    }

}