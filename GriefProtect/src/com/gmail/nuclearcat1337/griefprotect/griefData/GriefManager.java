package com.gmail.nuclearcat1337.griefprotect.griefData;

import com.gmail.nuclearcat1337.griefprotect.griefItems.GriefBlock;
import com.gmail.nuclearcat1337.griefprotect.griefItems.GriefContainer;
import com.gmail.nuclearcat1337.griefprotect.main.GriefProtect;
import com.gmail.nuclearcat1337.griefprotect.queries.GriefCloseInventoryTask;
import com.gmail.nuclearcat1337.griefprotect.queries.GriefDropTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Furnace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Bed;
import org.bukkit.material.Dispenser;
import org.bukkit.material.Door;
import org.bukkit.material.FlowerPot;
import org.bukkit.material.MaterialData;
import org.bukkit.material.PistonBaseMaterial;
import org.bukkit.material.PistonExtensionMaterial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GriefManager
{
    private Map<UUID, List<GriefContainer>> containerCheck;

    private Map<UUID, List<GriefBlock>> griefedBlock;

    GriefManager()
    {
        this.griefedBlock = new HashMap<>();
        this.containerCheck = new HashMap<>();
    }

    public boolean hasGriefBlocks(UUID player)
    {
        return griefedBlock.containsKey(player);
    }

    //    public List<GriefBlock> getGriefBlocks(UUID player)
    //    {
    //        return griefedBlock.get(player);
    //    }

    public void addGriefContainer(UUID player, GriefContainer container)
    {
        List<GriefContainer> containers = containerCheck.get(player);
        if(containers == null)
        {
            containers = new ArrayList<>();
            containerCheck.put(player,containers);
        }
        containers.add(container);
    }


    public void addGriefBlock(UUID player, GriefBlock block)
    {
        if(!griefedBlock.containsKey(player))
            griefedBlock.put(player,new ArrayList<GriefBlock>());
        griefedBlock.get(player).add(block);
    }

    public void handleGrief(UUID player, UUID owner, BlockState blockState, boolean isBreak)
    {
        if(!isBreak && blockState instanceof InventoryHolder)
            Bukkit.getScheduler().scheduleSyncDelayedTask(GriefProtect.getInstance(), new GriefCloseInventoryTask(Bukkit.getPlayer(player)));

        rollback(player);
    }


    public int rollback(UUID player)
    {
        List<GriefBlock> blockList = griefedBlock.get(player);

        int count = 0;
        if (blockList != null && !blockList.isEmpty())
        {

            ArrayList<Item> itemList = new ArrayList<Item>();
            for (Entity e : blockList.get(0).getBlock().getWorld().getEntities())
            {
                if (e instanceof Item)
                {
                    itemList.add((Item) e);
                }
            }

            BlockState connected = null;
            for (GriefBlock b : blockList)
            {
                if (b.getConnectedBlock() != null)
                {
                    connected = b.getConnectedBlock();
                }
            }
            if (connected != null)
            {
                blockList.add(0, new GriefBlock(connected, UUID.randomUUID(), null)); //TODO----This is probably a really BAD HACK. IDK what a random UUID is going to do...
            }

            for (GriefBlock b : blockList)
            {
                BlockState block = b.getBlock();
                if (block.getBlock().getType() != block.getType())
                {
                    // sign text update workaround
                    String[] lines = null;
                    if (block instanceof Sign)
                    {
                        lines = ((Sign) block).getLines();
                    }

                    // spawner workaround
                    EntityType spawnEntity = null;
                    if (block instanceof CreatureSpawner)
                    {
                        spawnEntity = ((CreatureSpawner) block).getSpawnedType();
                    }

                    // rollback block
                    block.update(true);

                    ItemStack item1 = new ItemStack(block.getType(), 1,(short) 0, block.getData().getData());
                    ItemStack item2 = null;
                    ItemStack item3 = null;
                    switch (block.getTypeId())
                    {
                        // ignore
                        case 0:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                        case 19:
                        case 51:
                        case 52:
                        case 90:
                        case 95:
                            break;

                        // stone => cobblestone
                        case 1:
                            item2 = new ItemStack(4, 1);
                            break;

                        // grass, farmland => dirt
                        case 2:
                        case 60:
                            item2 = new ItemStack(3, 1);
                            break;

                        // bed
                        case 26:
                            item2 = new ItemStack(355, 1);
                            break;

                        // double slab => 2xslab
                        case 43:
                            item2 = new ItemStack(44, 2);
                            break;

                        // coal ore
                        case 16:
                            item2 = new ItemStack(263, 1);
                            break;

                        // lapis lazuli ore
                        case 21:
                            item2 = new ItemStack(351, 4);
                            break;

                        // redstone ore
                        case 55:
                            item2 = new ItemStack(331, 4);
                            break;

                        // diamond ore
                        case 56:
                            item2 = new ItemStack(264, 1);
                            break;

                        // wheat
                        case 59:
                            item2 = new ItemStack(295, 1);
                            break;

                        // signs
                        case 63:
                        case 68:
                            item2 = new ItemStack(323, 1);
                            break;

                        // wooden door
                        case 64:
                            item2 = new ItemStack(324, 1);
                            break;

                        // iron door
                        case 71:
                            item2 = new ItemStack(330, 1);
                            break;

                        // redstone ore
                        case 73:
                        case 74:
                            item2 = new ItemStack(331, 4);
                            break;

                        // redstone torch
                        case 75:
                            item2 = new ItemStack(76, 1);
                            break;

                        // sugarcane
                        case 83:
                            item2 = new ItemStack(338, 1);
                            break;

                        // glowstone
                        case 89:
                            item2 = new ItemStack(348, 4);
                            break;

                        // cake
                        case 92:
                            item2 = new ItemStack(354, 1);
                            break;

                        // repeater
                        case 93:
                        case 94:
                            item2 = new ItemStack(356, 1);
                            break;

                        // melon
                        case 103:
                            item2 = new ItemStack(360, 9);
                            break;

                        // melon stem
                        case 105:
                            item2 = new ItemStack(362, 1);
                            break;

                        // pumpkin stem
                        case 104:
                            item2 = new ItemStack(361, 1);
                            break;

                        // nether wart
                        case 115:
                            item2 = new ItemStack(372, 1);
                            break;

                        // brewing stand
                        case 117:
                            item2 = new ItemStack(379, 1);
                            break;

                        // cauldron
                        case 118:
                            item2 = new ItemStack(380, 1);
                            break;

                        // redstone lamp
                        case 124:
                            item2 = new ItemStack(123, 1);
                            break;

                        // wooden double slab
                        case 125:
                            item2 = new ItemStack(126, 2);
                            break;

                        // cocoa plant
                        case 127:
                            item2 = new ItemStack(351, 3);
                            break;

                        // emerald ore
                        case 129:
                            item2 = new ItemStack(388, 1);
                            break;

                        // ender chest
                        case 130:
                            item2 = new ItemStack(49, 8);
                            break;

                        // tripwire
                        case 132:
                            item2 = new ItemStack(287, 1);
                            break;

                        // flower pots
                        case 140:
                            item2 = new ItemStack(390, 1);
                            FlowerPot pot = (FlowerPot) block.getData();
                            if (pot.getContents() != null)
                            {
                                item3 = new ItemStack(pot.getContents()
                                                         .getItemTypeId(), 1);
                            }
                            break;

                        // carrots
                        case 141:
                            item2 = new ItemStack(391, 4);
                            break;

                        // potatoes
                        case 142:
                            item2 = new ItemStack(392, 4);
                            break;

                        // head
                        case 144:
                            item2 = new ItemStack(397, 1);
                            break;

                        // comparator
                        case 149:
                        case 150:
                            item2 = new ItemStack(404, 1);
                            break;
                    }

                    int amount2 = 0;
                    if (item2 != null)
                    {
                        amount2 = item2.getAmount();
                        item2.setAmount(1);
                    }
                    int amount3 = 0;
                    if (item3 != null)
                    {
                        amount3 = item3.getAmount();
                        item3.setAmount(1);
                    }

                    boolean found = false;
                    Iterator<Item> iterator = itemList.iterator();
                    while (iterator.hasNext())
                    {
                        Item i = iterator.next();
                        Location l = i.getLocation();

                        if ((Math.pow(l.getX() - block.getX(), 2)
                                + Math.pow(l.getY() - block.getY(), 2) + Math
                                .pow(l.getZ() - block.getZ(), 2)) < 6)
                        {
                            ItemStack ii = i.getItemStack();
                            if (ii.getType() == item1.getType())
                            {
                                i.remove();
                                iterator.remove();
                                found = true;
                                break;
                            }
                            else if (item2 != null || item3 != null)
                            {
                                if (item2 != null
                                        && ii.getTypeId() == item2.getTypeId())
                                {
                                    i.remove();
                                    iterator.remove();
                                    amount2--;
                                }
                                else if (item3 != null
                                        && ii.getTypeId() == item3.getTypeId())
                                {
                                    i.remove();
                                    iterator.remove();

                                    amount3--;
                                }

                                if (amount2 <= 0 && amount3 <= 0)
                                {
                                    found = true;
                                    break;
                                }
                            }
                        }

                        if (!found)
                        {
                            Player p = Bukkit.getPlayer(player);

                            if (p != null)
                            {
                                if (p.getInventory()
                                     .contains(item1.getTypeId())
                                        && p.getInventory().removeItem(item1)
                                            .size() == 0)
                                {
                                    found = true;
                                }
                                else
                                {
                                    if (item2 != null)
                                    {
                                        if (amount2 > 1)
                                        {
                                            item2.setAmount(amount2);
                                        }

                                        if (p.getInventory().contains(
                                                item2.getTypeId())
                                                && p.getInventory()
                                                    .removeItem(item2)
                                                    .size() == 0)
                                        {
                                            found = true;
                                        }

                                        item2.setAmount(1);
                                    }

                                    if (item3 != null)
                                    {
                                        if (amount3 > 1)
                                        {
                                            item3.setAmount(amount3);
                                        }

                                        if (p.getInventory().contains(
                                                item3.getTypeId())
                                                && p.getInventory()
                                                    .removeItem(item3)
                                                    .size() == 0)
                                        {
                                            found = true;
                                        }

                                        item3.setAmount(1);
                                    }
                                }

                                if (!found)
                                {
                                    Bukkit.getScheduler().scheduleSyncDelayedTask(GriefProtect.getInstance(), new GriefDropTask(block.getBlock().getLocation(), item1, item2, item3), 5);
                                }
                            }
                        }
                    }

                    block = block.getBlock().getState();
                    if (block instanceof Chest || block instanceof Furnace
                            || block instanceof Dispenser)
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

                        Iterator<Item> iterator2 = itemList.iterator();

                        while (iterator2.hasNext())
                        {
                            Item i = iterator2.next();
                            Location l = i.getLocation();

                            if ((Math.pow(l.getX() - block.getX(), 2)
                                    + Math.pow(l.getY() - block.getY(), 2) + Math
                                    .pow(l.getZ() - block.getZ(), 2)) < 6)
                            {
                                if (inv.addItem(i.getItemStack()).isEmpty())
                                {
                                    i.remove();
                                    iterator2.remove();
                                }
                                else
                                {
                                    break;
                                }
                            }
                        }
                    }

                    if (block instanceof Sign)
                    {
                        int i = 0;
                        for (String line : lines)
                        {
                            ((Sign) block).setLine(i++, line);
                        }
                        block.update();
                    }

                    if (block instanceof CreatureSpawner)
                    {
                        ((CreatureSpawner) block).setSpawnedType(spawnEntity);
                        block.update();
                    }

                    MaterialData m = block.getData();
                    if (m instanceof Door)
                    {
                        ((Door) m).setTopHalf(true);

                        block.getBlock()
                             .getRelative(BlockFace.UP)
                             .setTypeIdAndData(block.getTypeId(),
                                     m.getData(), true);
                    }
                    else if (m instanceof Bed)
                    {
                        ((Bed) m).setHeadOfBed(true);
                        block.getBlock()
                             .getRelative(((Bed) m).getFacing())
                             .setTypeIdAndData(block.getTypeId(),
                                     m.getData(), true);
                    }
                    else if (m instanceof PistonBaseMaterial)
                    {
                        if (((PistonBaseMaterial) m).isPowered())
                        {
                            PistonExtensionMaterial head = new PistonExtensionMaterial(
                                    m.getItemTypeId());
                            head.setSticky(((PistonBaseMaterial) m).isSticky());
                            head.setFacingDirection(((PistonBaseMaterial) m)
                                    .getFacing());
                            block.getBlock()
                                 .getRelative(
                                         ((PistonBaseMaterial) m)
                                                 .getFacing())
                                 .setTypeIdAndData(34, head.getData(), true);
                        }
                    }
                    count++;
                }
            }
            blockList.clear();
        }

        return count;
    }
}
