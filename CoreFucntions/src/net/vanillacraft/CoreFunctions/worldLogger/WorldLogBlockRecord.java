package net.vanillacraft.CoreFunctions.worldLogger;

import net.vanillacraft.CoreFunctions.interfaces.WorldLogRecord;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Dispenser;
import org.bukkit.block.NoteBlock;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

public class WorldLogBlockRecord implements WorldLogRecord
{
    private long timestamp;
    private WorldLogAction action;

    private UUID player;
    private String world;
    private Material itemType;
    private double playerX;
    private double playerY;
    private double playerZ;
    private float pitch;
    private float yaw;

    private Material blockMaterial;
    private byte blockData;
    private int blockX;
    private int blockY;
    private int blockZ;

    private String content;
    private boolean cancelled;

    public WorldLogBlockRecord(WorldLogAction a, Player p, Block b, boolean c)
    {
        timestamp = System.currentTimeMillis();
        action = a;
        cancelled = c;

        if (p != null)
        {
            Location loc = p.getLocation();
            player = p.getUniqueId();
            itemType = p.getItemInHand().getType();
            playerX = loc.getX();
            playerY = loc.getY();
            playerZ = loc.getZ();
            pitch = loc.getPitch();
            yaw = loc.getYaw();
        }

        if (b != null)
        {
            BlockState blockState = b.getState();

            blockMaterial = blockState.getType();
            blockData = b.getData();
            blockX = blockState.getX();
            blockY = blockState.getY();
            blockZ = blockState.getZ();

            world = b.getWorld().getName();

            // exception for double slabs
            if (blockMaterial == Material.AIR && itemType == Material.STONE_SLAB2)
            {
                blockMaterial = Material.DOUBLE_STONE_SLAB2;
                blockData = (byte) p.getItemInHand().getDurability();
            }

            if (blockState instanceof InventoryHolder)
            {
                if (a != WorldLogAction.PLACE)
                {
                    try
                    {
                        if (blockState instanceof Dispenser)
                        {
                            content = "";
                        }
                        else
                        {
                            Inventory inv;
                            if (blockState instanceof Chest)
                            {
                                inv = ((Chest) blockState).getBlockInventory();
                            }
                            else
                            {
                                inv = ((InventoryHolder) blockState).getInventory();
                            }

                            StringBuffer sb = new StringBuffer();

                            for (ItemStack i : inv.getContents())
                            {
                                if (i != null && i.getType() != Material.AIR)
                                {
                                    sb.append(i.getAmount());
                                    sb.append("x");
                                    sb.append(i.getType());
                                    sb.append(" ");
                                }
                            }

                            content = sb.toString();
                        }
                    } catch (Exception e)
                    {
                        content = e.getMessage();
                    }
                }
                else
                {
                    content = "";
                }
            }
            else if (blockState instanceof Sign)
            {
                Sign s = (Sign) blockState;
                content = s.getLine(0) + "\n" + s.getLine(1) + "\n" + s.getLine(2) + "\n" + s.getLine(3);
            }
            else if (blockState instanceof CreatureSpawner)
            {
                CreatureSpawner m = (CreatureSpawner) blockState;
                content = "Creature: " + m.getCreatureTypeName() + " - Delay: " + m.getDelay();
            }
            else if (blockState instanceof NoteBlock)
            {
                content = "";
            }
        }
    }

    @Override
    public String getType()
    {
        return "Block";
    }

    @Override
    public String getStatementText()
    {
        return "INSERT INTO tbl_block_log (col_timestamp, col_action, col_player, col_item_type, col_player_x, col_player_y, col_player_z, col_player_pitch, col_player_yaw, col_block_material, col_block_data, col_block_x, col_block_y, col_block_z, col_content, col_cancelled, col_world, col_public) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    public void executeRecord(final PreparedStatement statement)
    {
        //col_timestamp,
        //col_action,
        //col_player,
        //col_item_type,
        //col_player_x,
        //col_player_y,
        //col_player_z,
        //col_player_pitch,
        //col_player_yaw,
        //col_block_material,
        //col_block_data,
        //col_block_x,
        //col_block_y,
        //col_block_z,
        //col_content,
        //col_cancelled,
        //col_world,
        //col_public

        try
        {
            statement.setTimestamp(1, new Timestamp(timestamp));
            statement.setString(2, action.toString());
            statement.setString(3, player.toString());//This is a UUID
            statement.setString(4, itemType.toString());
            statement.setDouble(5, playerX);
            statement.setDouble(6, playerY);
            statement.setDouble(7, playerZ);
            statement.setFloat(8, pitch);
            statement.setFloat(9, yaw);
            statement.setString(10, blockMaterial.toString());
            statement.setByte(11, blockData);
            statement.setInt(12, blockX);
            statement.setInt(13, blockY);
            statement.setInt(14, blockZ);
            statement.setString(15, content);
            statement.setBoolean(16, cancelled);
            statement.setString(17, world);
            statement.setBoolean(18,false); //This is like a temporary workaround or something
            statement.execute();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

}


