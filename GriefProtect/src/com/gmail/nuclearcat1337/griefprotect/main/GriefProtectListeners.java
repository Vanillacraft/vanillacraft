package com.gmail.nuclearcat1337.griefprotect.main;

import com.gmail.nuclearcat1337.griefprotect.griefData.GriefData;
import com.gmail.nuclearcat1337.griefprotect.griefItems.GriefChest;
import com.gmail.nuclearcat1337.griefprotect.queries.GriefProtectBlockCheck;
import com.gmail.nuclearcat1337.griefprotect.util.BlockHelper;
import com.gmail.nuclearcat1337.griefprotect.util.Loc;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Bed;
import org.bukkit.material.Door;
import org.bukkit.material.MaterialData;
import org.bukkit.material.PistonExtensionMaterial;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.UUID;

public class GriefProtectListeners implements Listener
{
    private GriefData data;

    public GriefProtectListeners(Plugin plugin, GriefData data)
    {
        this.data = data;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    //The goal of this is to check for recently broken chests
    //And make sure that the items cant be picked up until weve checked everything
    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemSpawn(ItemSpawnEvent event)
    {
        if (!data.getChestCache().isChestListEmpty() && event.getEntity() instanceof Item )//&& plugin.worldSet.contains(event.getEntity().getWorld().getName()))
        {
            for (Loc loc : data.getChestCache().getChestList())
            {
                Location l = loc.toLocation();
                if (event.getEntity().getLocation().getWorld().getName().equals(l.getWorld().getName()) && l.distanceSquared(event.getEntity().getLocation()) < 4)
                {
                    ((Item) event.getEntity()).setPickupDelay(20);
                }
            }
        }
    }
//
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if (!event.isCancelled() && (event.getBlock().getType() == Material.CHEST || event.getBlock().getType() == Material.TRAPPED_CHEST)) //&& plugin.worldSet.contains(event.getBlock().getWorld().getName()))
        {
            Block b = event.getBlock();
            String key = b.getX() + " " + b.getY() + " " + b.getZ();
            //String playerName = ChatColor.stripColor(event.getPlayer().getName());
            //plugin.chestOwner.put(key, new GriefChest(playerName, System.currentTimeMillis()));
            data.getChestCache().addGriefChest(key,new GriefChest(event.getPlayer().getUniqueId(),System.currentTimeMillis()));
        }
    }
//
    @EventHandler(priority = EventPriority.MONITOR,ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        Block b = event.getBlock();
        Boolean allowed = data.getChestCache().isCachedChestAllowed(b, player.getUniqueId());
        if (allowed != null && allowed == false)
            event.setCancelled(true);

        checkBlockBreak(player, b, null);


        if (b.getType().isSolid())
        {
            List<Block> connected = BlockHelper.getConnectedBlocks(b.getLocation());
            if(connected != null)
            {
                for(Block c : connected)
                    checkBlockBreak(player,c,b);
            }
        }
    }

    public void checkBlockBreak(Player player, Block block, Block connectedBlock)
    {
        //int played = getTimePlayed(player, false);
        BlockState b = block.getState();

        BlockState c = null;
        if (connectedBlock != null)
        {
            c = connectedBlock.getState();
        }

        //String playerName = ChatColor.stripColor(player.getName());

        // chest cache
        if (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST)
        {
            String key = b.getX() + " " + b.getY() + " " + b.getZ();
            GriefChest chest = data.getChestCache().getGriefChest(key);
            if (chest != null)
            {
                if (chest.getOwner().equals(player.getUniqueId()))
                {
                    return;
                }

                GriefProtectBlockCheck check = new GriefProtectBlockCheck(player.getUniqueId(), b, c,true,data);
//                check.owner = chest.owner;
//                check.blockId = 54;
//                check.timestamp = chest.timestamp;
                check.setOwner(chest.getOwner());
                check.setBlockMaterial(block.getType());
                check.setBlockData(block.getData());
                check.setTimestamp(chest.getTimestamp());
                check.run();

                return;
            }
        }

        switch (block.getType())
        {
            case SNOW:
                break;

            case STONE:
            case DIRT:
            case GRASS:
                //if (played < 18000)
                //{
                    data.getDatabase().submitLogQuery(new GriefProtectBlockCheck(player.getUniqueId(), b, c,  true,data));
                //}
                break;

            case SAND:
            case LOG:
            case GRAVEL:
            case LEAVES:
                //if (played < 43200)
                //{
                    data.getDatabase().submitLogQuery(new GriefProtectBlockCheck(player.getUniqueId(), b, c,  true,data));
                //}
                break;

            case CROPS:
            case SUGAR_CANE:
                //if (played < 86400)
                //{
                    data.getDatabase().submitLogQuery(new GriefProtectBlockCheck(player.getUniqueId(), b, c,  true,data));
                //}
                break;

            default:
                MaterialData m = b.getData();
                if (m instanceof Door)
                {
                    if (((Door) m).isTopHalf())
                    {
                        data.getDatabase().submitLogQuery(new GriefProtectBlockCheck(player.getUniqueId(), block.getRelative(BlockFace.DOWN).getState(), c,  true,data));
                    }
                    else
                    {
                        data.getDatabase().submitLogQuery(new GriefProtectBlockCheck(player.getUniqueId(), b, c,  true,data));
                    }
                }
                else if (m instanceof Bed)
                {
                    if (((Bed) m).isHeadOfBed())
                    {
                        data.getDatabase().submitLogQuery(new GriefProtectBlockCheck(player.getUniqueId(), block.getRelative(((Bed) m).getFacing().getOppositeFace()).getState(), c,  true,data));
                    }
                    else
                    {
                        data.getDatabase().submitLogQuery(new GriefProtectBlockCheck(player.getUniqueId(), b, c,  true,data));
                    }
                }
                else if (m instanceof PistonExtensionMaterial)
                {
                    data.getDatabase().submitLogQuery(new GriefProtectBlockCheck(player.getUniqueId(), block.getRelative(((PistonExtensionMaterial) m).getAttachedFace()).getState(), c,  true,data));
                }
                else if (b instanceof Chest || b instanceof Furnace
                        || b instanceof Dispenser)
                {
                    //chestList.add(block.getLocation());
                    data.getChestCache().addChest(new Loc(block.getLocation(),false));

                    data.getDatabase().submitLogQuery(new GriefProtectBlockCheck(player.getUniqueId(), b, c, true,data));
                }
                else
                {
                    data.getDatabase().submitLogQuery(new GriefProtectBlockCheck(player.getUniqueId(), b, c,  true,data));
                }

        }
    }
//
//    @EventHandler(priority = EventPriority.MONITOR)
//    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event)
//    {
//        if (!event.isCancelled())
//        {
//            plugin.checkBucketTarget(event.getPlayer(), event.getBlockClicked());
//        }
//    }
//
    @EventHandler(priority = EventPriority.HIGHEST,ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        // when opening chests, check for other chests right next and log their content too
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK )//&& plugin.worldSet.contains(event.getClickedBlock().getWorld().getName()))
        {
            checkBlockInventory(event.getPlayer().getUniqueId(), event.getClickedBlock());

            if (event.getClickedBlock().getType() == Material.CHEST || event.getClickedBlock().getType() == Material.TRAPPED_CHEST)
            {
                Boolean allowed = data.getChestCache().isCachedChestAllowed(event.getClickedBlock(), event.getPlayer().getUniqueId());
                if (allowed != null && allowed == false)
                {
                    event.setCancelled(true);
                }

                Block block = null;
                if (event.getClickedBlock().getRelative(BlockFace.NORTH).getType() == Material.CHEST || event.getClickedBlock().getRelative(BlockFace.NORTH).getType() == Material.TRAPPED_CHEST)
                {
                    block = event.getClickedBlock().getRelative(BlockFace.NORTH);
                }
                else if (event.getClickedBlock().getRelative(BlockFace.SOUTH).getType() == Material.CHEST || event.getClickedBlock().getRelative(BlockFace.SOUTH).getType() == Material.TRAPPED_CHEST)
                {
                    block = event.getClickedBlock().getRelative(BlockFace.SOUTH);
                }
                else if (event.getClickedBlock().getRelative(BlockFace.EAST).getType() == Material.CHEST || event.getClickedBlock().getRelative(BlockFace.EAST).getType() == Material.TRAPPED_CHEST)
                {
                    block = event.getClickedBlock().getRelative(BlockFace.EAST);
                }
                else if (event.getClickedBlock().getRelative(BlockFace.WEST).getType() == Material.CHEST || event.getClickedBlock().getRelative(BlockFace.WEST).getType() == Material.TRAPPED_CHEST)
                {
                    block = event.getClickedBlock().getRelative(BlockFace.WEST);
                }


                if (block != null)
                {
                    checkBlockInventory(event.getPlayer().getUniqueId(), block);

                    allowed = data.getChestCache().isCachedChestAllowed(block, event.getPlayer().getUniqueId());
                    if (allowed != null && allowed == false)
                    {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    public void checkBlockInventory(UUID player, Block block)
    {
        switch (block.getType())
        {
            case TRAPPED_CHEST:
            case CHEST:
                // chest cache
                String key = block.getX() + " " + block.getY() + " "
                        + block.getZ();
                GriefChest chest = data.getChestCache().getGriefChest(key);
                if (chest != null)
                {
                    if (chest.getOwner().equals(player))
                    {
                        return;
                    }

                    GriefProtectBlockCheck check = new GriefProtectBlockCheck(player, block.getState(), null, false,data);
                    check.setOwner(chest.getOwner());
                    check.setBlockMaterial(block.getType());
                    check.setBlockData(block.getData());
                    check.setTimestamp(chest.getTimestamp());
                    check.run();

                    return;
                }
            case FURNACE:
            case DISPENSER:
            case BREWING_STAND:
            case HOPPER:
            case DROPPER:
                data.getDatabase().submitLogQuery(new GriefProtectBlockCheck(player, block.getState(), null, false, data));
        }
    }
}
