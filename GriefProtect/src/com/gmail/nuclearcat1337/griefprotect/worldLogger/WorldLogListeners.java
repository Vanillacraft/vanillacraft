package com.gmail.nuclearcat1337.griefprotect.worldLogger;

import com.gmail.nuclearcat1337.griefprotect.interfaces.ILogger;
import com.gmail.nuclearcat1337.griefprotect.util.BlockHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WorldLogListeners implements Listener
{
    private ILogger log;
    private Map<UUID,Long> physicalInteractTimers;

    public WorldLogListeners(Plugin plugin, ILogger log)
    {
        this.log = log;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        physicalInteractTimers = new HashMap<>();
    }

    private void logAction(WorldLogAction action, Player player, Block block, boolean cancelled)
    {
        //Bukkit.getLogger().info("Attempted to log a block break");
        log.log(new WorldLogBlockRecord(action, player, block, cancelled));
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event)
    {
        // ignore signs
        if (event.getPlayer().getItemInHand().getType() != Material.SIGN)
            logAction(WorldLogAction.PLACE, event.getPlayer(), event.getBlock(), event.isCancelled());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event)
    {
        Block b = event.getBlock();
        if (b.getType().isSolid())
        {
            List<Block> blocks = BlockHelper.getConnectedBlocks(b.getLocation());
            if(blocks != null)
            {
                for(Block block : blocks)
                    logAction(WorldLogAction.BREAK, event.getPlayer(), block, event.isCancelled());
            }
            //TODO----Were also going to need to check the block below to see if there is a button or a lever attached to the bottom of this block
        }

        logAction(WorldLogAction.BREAK, event.getPlayer(), b, event.isCancelled());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockIgnite(BlockIgniteEvent event)
    {
        if (event.getCause() == IgniteCause.FLINT_AND_STEEL)
        {
            logAction(WorldLogAction.USE, event.getPlayer(), event.getBlock(), event.isCancelled());
        }
    }

    //TODO--------------Were gonna go ahead and not log interact events until there is a system in place to only log some of them
    //TODO--------------Because logging all interact events really spams the logs
//    @EventHandler(priority = EventPriority.MONITOR)
//    public void onPlayerInteract(PlayerInteractEvent event)
//    {
//        //if (plugin.logOnInteract)
//        //{
//            Action action = event.getAction();
//            //String playerName = ChatColor.stripColor(event.getPlayer().getName());
//            // right click interact
//            if (action == Action.RIGHT_CLICK_BLOCK )//&& (plugin.interactRightClickSet.isEmpty() || plugin.interactRightClickSet.contains(event.getClickedBlock().getTypeId())))
//            {
//                //if (plugin.worldSet.contains(event.getClickedBlock().getWorld().getName()))
//               // {
//                    //plugin.logQueue.add((WorldLogRecord) new WorldLogBlockRecord(WorldLogAction.INTERACT, event.getPlayer(), event.getClickedBlock(), event.isCancelled()));
//                log.log(new WorldLogBlockRecord(WorldLogAction.INTERACT,event.getPlayer(),event.getClickedBlock(), event.isCancelled()));
//                //}
//            }
//            // left click interact
//            else if (action == Action.LEFT_CLICK_BLOCK )//&& (plugin.interactLeftClickSet.isEmpty() || plugin.interactLeftClickSet.contains(event.getClickedBlock().getTypeId())))
//            {
//                //if (plugin.worldSet.contains(event.getClickedBlock().getWorld().getName()))
//                //{
//                   // plugin.logQueue.add((WorldLogRecord) new WorldLogBlockRecord(WorldLogAction.INTERACT, event.getPlayer(), event.getClickedBlock(), event.isCancelled()));
//                log.log(new WorldLogBlockRecord(WorldLogAction.INTERACT,event.getPlayer(),event.getClickedBlock(), event.isCancelled()));
//                //}
//            }
//            // physical interact
//            else if (action == Action.PHYSICAL )//&& (plugin.interactPhysicalSet.isEmpty() || plugin.interactPhysicalSet.contains(event.getClickedBlock().getTypeId())))
//            {
//               // if (plugin.worldSet.contains(event.getClickedBlock().getWorld().getName()))
//                //{
//                    // throttled logging cancelled physical interact events... they spam too much
//                    if (event.isCancelled())
//                    {
//                        Long time = physicalInteractTimers.get(event.getPlayer().getUniqueId());
//                        if (time == null || System.currentTimeMillis() > time)
//                        {
//                            physicalInteractTimers.put(event.getPlayer().getUniqueId(), System.currentTimeMillis() + 1000);
//
//                            //plugin.logQueue.add((WorldLogRecord) new WorldLogBlockRecord(WorldLogAction.INTERACT, event.getPlayer(), event.getClickedBlock(), event.isCancelled()));
//                            log.log(new WorldLogBlockRecord(WorldLogAction.INTERACT,event.getPlayer(),event.getClickedBlock(), event.isCancelled()));
//                        }
//                    }
//                    else
//                    {
//                        //plugin.logQueue.add((WorldLogRecord) new WorldLogBlockRecord(WorldLogAction.INTERACT, event.getPlayer(), event.getClickedBlock(), event.isCancelled()));
//                        log.log(new WorldLogBlockRecord(WorldLogAction.INTERACT,event.getPlayer(),event.getClickedBlock(), event.isCancelled()));
//                    }
//                //}
//            }
//            // item use
//            else if (action == Action.RIGHT_CLICK_BLOCK && event.hasItem())// && (plugin.useSet.isEmpty() || plugin.useSet.contains(event.getItem().getTypeId())))
//            {
//               // if (plugin.worldSet.contains(event.getClickedBlock().getWorld().getName()))
//               // {
//                    //plugin.logQueue.add((WorldLogRecord) new WorldLogBlockRecord(WorldLogAction.USE, event.getPlayer(), event.getClickedBlock(), event.isCancelled()));
//                log.log(new WorldLogBlockRecord(WorldLogAction.USE,event.getPlayer(),event.getClickedBlock(), event.isCancelled()));
//               // }
//            }
//        //}
//    }
}
