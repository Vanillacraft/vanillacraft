package com.gmail.nuclearcat1337.griefprotect.queries;

import com.gmail.nuclearcat1337.griefprotect.griefData.GriefData;
import com.gmail.nuclearcat1337.griefprotect.griefData.WorldLogGriefRecord;
import com.gmail.nuclearcat1337.griefprotect.griefItems.GriefBlock;
import com.gmail.nuclearcat1337.griefprotect.griefItems.GriefChest;
import com.gmail.nuclearcat1337.griefprotect.griefItems.GriefContainer;
import com.gmail.nuclearcat1337.griefprotect.main.GriefProtect;
import net.vanillacraft.CoreFunctions.interfaces.DBLogQuery;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

public class GriefProtectBlockCheck implements DBLogQuery
{
    private GriefData data;

    private BlockState block;
    private BlockState connectedBlock;
    private UUID player;

    public UUID owner;
    public long timestamp;

    public Material blockMaterial;
    public byte blockData;

    private boolean isBreak;

    public GriefProtectBlockCheck(UUID player, BlockState block, BlockState connectedBlock, boolean isBreak, GriefData data)
    {
        this.player = player;
        this.block = block;
        this.connectedBlock = connectedBlock;
        //this.plugin = plugin;
        this.isBreak = isBreak;
        this.data = data;
    }

    @Override
    public void run()
    {
        long current = System.currentTimeMillis();

        // ignore block ownership in protected zones
//        Zone blockZone = plugin.zone.getZone(block.getBlock().getLocation());
//        if (blockZone != null && blockZone.protect)
//        {
//            return;
//        }

        // remove chest drop freeze
        if (block instanceof InventoryHolder)
        {
            data.getChestCache().getChestList().remove(block.getBlock().getLocation());
        }

        // update chest cache
        if ((block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) && owner != null)
        {
            String key = block.getX() + " " + block.getY() + " " + block.getZ();
            if (!data.getChestCache().isGriefChest(key))
            {
                data.getChestCache().addGriefChest(key, new GriefChest(owner, timestamp));
            }
        }

        if (owner != null && !owner.equals(player))
        {
            if (block.getType() ==  blockMaterial
                    || (block.getType() == Material.GRASS && blockMaterial == Material.DIRT) //These are checking if the block that was originally placed changed into something else before it was broken.
                    || (block.getType() == Material.MYCEL && blockMaterial == Material.DIRT) //EX: You place dirt. The dirt is logged into the db. The dirt changes into grass.
                    || (block.getType() == Material.REDSTONE_TORCH_OFF && blockMaterial == Material.REDSTONE_TORCH_ON)     //Now when the grass is broken we stop it because the original logged block is dirt
                    || (block.getType() == Material.REDSTONE_TORCH_ON && blockMaterial == Material.REDSTONE_TORCH_OFF)      //Some of these might be unnecessary but im adding them just incase
                    || (block.getType() == Material.DIODE_BLOCK_ON && blockMaterial == Material.DIODE_BLOCK_OFF)
                    || (block.getType() == Material.DIODE_BLOCK_OFF && blockMaterial == Material.DIODE_BLOCK_ON)
                    || (block.getType() == Material.GLOWING_REDSTONE_ORE && blockMaterial == Material.REDSTONE_ORE)
                    || (block.getType() == Material.REDSTONE_ORE && blockMaterial == Material.GLOWING_REDSTONE_ORE)
                    || (block.getType() == Material.BURNING_FURNACE && blockMaterial == Material.FURNACE)
                    || (block.getType() == Material.FURNACE && blockMaterial == Material.BURNING_FURNACE)
                    || (block.getType() == Material.REDSTONE_LAMP_ON && blockMaterial == Material.REDSTONE_LAMP_OFF)
                    || (block.getType() == Material.REDSTONE_LAMP_OFF && blockMaterial == Material.REDSTONE_LAMP_ON))
            {
                boolean allowed = data.getPlayerAccess().isAllowed(owner, player, block.getLocation(), block.getType());

                // Check containers
                if (!isBreak && block instanceof InventoryHolder)
                {
                    data.getGriefManager().addGriefContainer(player, new GriefContainer(block,owner,allowed));
                }

                // Log action
                if (block instanceof Sign)
                {
                    if (isBreak)
                    {
                        Sign s = (Sign) block;
                        data.getLogger().log(new WorldLogGriefRecord("BREAK", player, owner, block, allowed, s.getLine(0) + "\n" + s.getLine(1) + "\n" + s.getLine(2) + "\n" + s.getLine(3)));
                        //Bukkit.getLogger().info("[GriefProtect] " + (allowed ? "(ok) " : "") + player + " BREAK " + block.getType() + " [" + s.getLine(0) + " _ " + s.getLine(1) + " _ " + s.getLine(2) + " _ " + s.getLine(3) + "] placed by " + owner + " at (" + block.getX() + "," + block.getY() + "," + block.getZ() + ")");
                        GriefProtect.logInfoMessage((allowed ? "(ok) " : "") + player + " BREAK " + block.getType() + " [" + s.getLine(0) + " _ " + s.getLine(1) + " _ " + s.getLine(2) + " _ " + s.getLine(3) + "] placed by " + owner + " at (" + block.getX() + "," + block.getY() + "," + block.getZ() + ")");
                    }
                    else
                    {

                    }
                }
                else if (block instanceof InventoryHolder)
                {
                    Inventory inv;
                    if (block instanceof Chest)
                    {
                        inv = ((Chest) block).getBlockInventory();
                    }
                    else
                    {
                        inv = ((InventoryHolder) block).getInventory();
                    }
                    String content = "";
                    for (ItemStack i : inv.getContents())
                    {
                        if (i != null && i.getType() != Material.AIR)
                        {
                            content += i.getType() + " ";
                        }
                    }

                    data.getLogger().log(new WorldLogGriefRecord((isBreak ? "BREAK" : "OPEN"), player, owner, block, allowed, content));
                    if (isBreak || !allowed)
                    {
                        //Bukkit.getLogger().info("[GriefProtect] " + (allowed ? "(ok) " : "") + player + (isBreak ? " BREAK " : " OPEN ") + block.getType() + " [" + content + "] placed by " + owner + " at (" + block.getX() + "," + block.getY() + "," + block.getZ() + ")");
                        GriefProtect.logInfoMessage((allowed ? "(ok) " : "") + player + (isBreak ? " BREAK " : " OPEN ") + block.getType() + " [" + content + "] placed by " + owner + " at (" + block.getX() + "," + block.getY() + "," + block.getZ() + ")");
                    }
                }
                else
                {
                    data.getLogger().log(new WorldLogGriefRecord((isBreak ? "BREAK" : "BUCKET"), player, owner, block, allowed));
                    //Bukkit.getLogger().info("[GriefProtect] " + (allowed ? "(ok) " : "") + player + (isBreak ? " BREAK " : " BUCKET ") + block.getType() + " placed by " + owner + " at (" + block.getX() + "," + block.getY() + "," + block.getZ() + ")");
                    GriefProtect.logInfoMessage((allowed ? "(ok) " : "") + player + (isBreak ? " BREAK " : " BUCKET ") + block.getType() + " placed by " + owner + " at (" + block.getX() + "," + block.getY() + "," + block.getZ() + ")");
                }

                // notify owner & ops, warn/kick/ban player
                //TODO----This is like the most important part. This handles the actual rolling back of any griefed blocks that were found to be not allowed
                if (!allowed)
                {
//                    Player p = Bukkit.getPlayer(player);
//                    Player o = Bukkit.getPlayer(owner);
//
//                    if (o != null && !o.isOp() )//&& !plugin.zone.isBreakingAllowed(player, owner, block.getBlock().getLocation()))
//                    {
//                        plugin.notifyOwner(o, player, block, isBreak);
//                    }


                    //if ((current - timestamp) > 300000) //TODO----Currently disabling the 5-minute rule for debugging
                    //{
                        if (isBreak)
                        {
                            data.getGriefManager().addGriefBlock(player,new GriefBlock(block, owner, connectedBlock));
                        }

                        //TODO---The point of this if statment is really to only warn if its a break, a bucket, or if stealing isnt allowed
                        //TODO---And since we never want stealing, we can just always do this
                        //if (isBreak || !(block instanceof InventoryHolder) )//|| !plugin.zone.isStealingAllowed(player, owner, block.getBlock().getLocation()))
                       // {
                            //plugin.warnKickBanPlayer(p, player, owner, block, isBreak);
                            data.getGriefManager().handleGrief(player,owner,block,isBreak);
                        //}
                   // }
                }
            }
        }
    }

    @Override
    public void setResult(ResultSet result)
    {
        try
        {
            if (result.next())
            {
                //timestamp = result.getTimestamp("col_timestamp").getTime();
                //owner = UUID.fromString(result.getString("col_player"));
                //blockMaterial = Material.matchMaterial(result.getString("col_block_material"));
                //blockData = result.getByte("col_block_data");

                setTimestamp(result.getTimestamp("col_timestamp").getTime());
                setOwner(UUID.fromString(result.getString("col_player")));
                setBlockMaterial(Material.matchMaterial(result.getString("col_block_material")));
                setBlockData(result.getByte("col_block_data"));
            }
        } catch (SQLException e)
        {
           //Bukkit.getLogger().log(Level.SEVERE, "GriefProtectBreak: Error fetching results: " + e.getMessage());
            //GriefProtect.logWarning("class={"+this.getClass().getSimpleName()+"} Error: "+e.getMessage(),Level.SEVERE);
            GriefProtect.logError(e.getMessage(),this.getClass());
        }
    }

    @Override
    public int getX()
    {
        return block.getX();
    }

    @Override
    public int getY()
    {
        return block.getY();
    }

    @Override
    public int getZ()
    {
        return block.getZ();
    }

    @Override
    public String getWorld()
    {
        return block.getWorld().getName();
    }

    public void setOwner(UUID owner)
    {
        this.owner = owner;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    public void setBlockMaterial(Material material)
    {
        this.blockMaterial = material;
    }

    public void setBlockData(byte data)
    {
        this.blockData = data;
    }

}
