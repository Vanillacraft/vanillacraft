package net.vanillacraft.CoreFunctions.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Attachable;
import org.bukkit.material.Directional;
import org.bukkit.material.Rails;

import java.util.ArrayList;
import java.util.List;

public class BlockHelper
{
    public enum ConnectedBlockType
    {
        Above,
        Side,
        Below
    }

    public static List<Block> getConnectedBlocks(Loc loc)
    {
        return getConnectedBlocks(loc.toLocation());
    }

    public static Block getConnectedAbove(Location location)
    {
        Block b = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() + 1, location.getBlockZ());
        switch (b.getType())
        {
            case SAPLING:
                //Rails
            case RAILS:
            case POWERED_RAIL:
            case DETECTOR_RAIL:
            case ACTIVATOR_RAIL:

            case LONG_GRASS:
            case DEAD_BUSH:
            case YELLOW_FLOWER:
            case RED_ROSE:
            case BROWN_MUSHROOM:
            case RED_MUSHROOM:
            case TORCH:
            case REDSTONE_WIRE:
            case WHEAT:
            case SIGN_POST:
                //Doors
            case WOOD_DOOR:
            case ACACIA_DOOR:
            case BIRCH_DOOR:
            case DARK_OAK_DOOR:
            case IRON_DOOR:
            case JUNGLE_DOOR:
            case SPRUCE_DOOR:

            case LEVER:
                //Pressure Plates
            case GOLD_PLATE:
            case IRON_PLATE:
            case STONE_PLATE:
            case WOOD_PLATE:

            case REDSTONE_TORCH_ON:
            case REDSTONE_TORCH_OFF:
            case SUGAR_CANE_BLOCK:
            case CACTUS:
            case REDSTONE_COMPARATOR_OFF:
            case REDSTONE_COMPARATOR_ON:
            case MELON_STEM:
            case PUMPKIN_STEM:
            case NETHER_STALK:
            case TRIPWIRE:
            case FLOWER_POT:
            case CARROT:
            case POTATO:
            case ANVIL:
            case SKULL:
            case CARPET:
            case DOUBLE_PLANT:
            case STANDING_BANNER:
            case ARMOR_STAND:
                //Block b = block.getWorld().getBlockAt(x, y, z);
                // handle torches, levers, etc. above but connected to another block
                switch (b.getType())
                {
                    case TORCH:
                    case REDSTONE_TORCH_OFF:
                    case REDSTONE_TORCH_ON:
                    case LEVER:
                        Attachable t = (Attachable) b.getState().getData();
                        if (t.getAttachedFace() != BlockFace.DOWN)
                            return null;
                        else
                            return b;
                    default:
                        return b;
                }

            default:
                return null;
        }
    }

    public static Block getConnectedSide(Location location, BlockFace side)
    {
        //        int x = location.getBlockX();
        //        int y = location.getBlockY();
        //        int z = location.getBlockZ();
        Block block = location.getBlock().getRelative(side);
        Block b = location.getBlock();
        Bukkit.getLogger().info("Block: " + block.getX() + ", " + block.getY() + ", " + block.getZ());
        Bukkit.getLogger().info("Side block: " + b.getX() + ", " + b.getY() + ", " + b.getZ());
        switch (block.getType())
        {
            // case 50:
            //case 65:
            // case 75:
            //case 76:
            // case 68:
            // case 69:
            // case 77:
            // case 96:
            case TORCH:
            case LADDER:
            case WALL_SIGN:
            case LEVER:
            case REDSTONE_TORCH_ON:
            case REDSTONE_TORCH_OFF:
            case STONE_BUTTON:
            case WOOD_BUTTON:
            case TRAP_DOOR:
            case IRON_TRAPDOOR:
            case WALL_BANNER:
                BlockFace face = ((Attachable) (block.getState().getData())).getAttachedFace().getOppositeFace();
                if (b.getRelative(face).getX() == block.getX() && b.getRelative(face).getZ() == block.getZ())
                {
                    return block;
                }
                return null;

            case TRIPWIRE_HOOK:
                BlockFace face2 = ((Directional) (block.getState().getData())).getFacing().getOppositeFace();
                if (b.getRelative(face2).getX() == block.getX() && b.getRelative(face2).getZ() == block.getZ())
                {
                    return block;
                }
                return null;
            //case 27:
            //case 28:
            //case 66:
            case RAILS:
            case ACTIVATOR_RAIL:
            case DETECTOR_RAIL:
            case POWERED_RAIL:
                Rails rail = (Rails) block.getState().getData();
                if (rail.isOnSlope())
                {
                    BlockFace face1 = rail.getDirection().getOppositeFace();
                    if (b.getRelative(face1).getX() == block.getX() && b.getRelative(face1).getZ() == block.getZ())
                    {
                        return block;
                    }
                }

            default:
                return null;
        }
    }

    public static Block getConnectedBelow(Location location)
    {
        Block block = location.getBlock().getWorld().getBlockAt(location.getBlockX(), location.getBlockY() - 1, location.getBlockZ());
        switch (block.getType())
        {
            case LEVER:
            case STONE_BUTTON:
            case WOOD_BUTTON:
                Attachable at = (Attachable) block.getState().getData();
                if (at.getAttachedFace() == BlockFace.UP)
                    return block;
                return null;
            default:
                return null;
        }
    }

    public static List<Block> getConnectedBlocks(Location location)
    {
        List<Block> blocks = new ArrayList<>();

        Block b;

        b = getConnectedAbove(location);
        if (b != null)
            blocks.add(b);

        b = getConnectedBelow(location);
        if (b != null)
            blocks.add(b);

        b = getConnectedSide(location, BlockFace.SOUTH);
        if (b != null)
            blocks.add(b);

        b = getConnectedSide(location, BlockFace.NORTH);
        if (b != null)
            blocks.add(b);

        b = getConnectedSide(location, BlockFace.EAST);
        if (b != null)
            blocks.add(b);

        b = getConnectedSide(location, BlockFace.WEST);
        if (b != null)
            blocks.add(b);

        if (blocks.isEmpty())
            return null;
        return blocks;
    }
}
