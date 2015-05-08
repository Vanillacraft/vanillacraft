package com.gmail.nuclearcat1337.griefprotect.commands;

import com.gmail.nuclearcat1337.griefprotect.griefData.GriefData;
import com.gmail.nuclearcat1337.griefprotect.griefItems.ProtectData;
import com.gmail.nuclearcat1337.griefprotect.queries.GriefProtectPlayerAccessAdd;
import com.gmail.nuclearcat1337.griefprotect.util.PlayerProfile;
import com.gmail.nuclearcat1337.griefprotect.util.ProfileUtils;
import net.vanillacraft.CoreFunctions.utils.Receiver;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;

public class AllowCommand implements CommandExecutor
{
    private GriefData data;
    public AllowCommand(JavaPlugin plugin, GriefData data)
    {
        plugin.getCommand("allow").setExecutor(this);
        plugin.getCommand("tempallow").setExecutor(this);
        this.data = data;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String s, final String[] args)
    {
            final String cmd = command.getName();
            if (sender.isOp())
            {
                if (args.length < 2)
                    sender.sendMessage("Usage: /" + cmd + " [Player] [Owner] <range> <block type list>");
                else
                {
                    ProfileUtils.lookup(args[0], new Receiver<PlayerProfile>()
                    {
                        @Override
                        public void receive(final PlayerProfile player)
                        {
                            if (player == null)
                                sender.sendMessage(ChatColor.RED + "Player not found.");
                            else
                            {
                                ProfileUtils.lookup(args[1], new Receiver<PlayerProfile>()
                                {
                                    @Override
                                    public void receive(final PlayerProfile owner)
                                    {

                                        if (owner == null)
                                            sender.sendMessage(ChatColor.RED + "Owner not found.");
                                        else if (owner.getId().equals(player.getId()))
                                            sender.sendMessage(ChatColor.RED+ "Player and owner cannot be the same.");
                                        else
                                        {
                                            int x = 0;
                                            int y = 0;
                                            int z = 0;

                                            int range = 100000;
                                            if (args.length >= 3)
                                            {
                                                try
                                                {
                                                    if (sender instanceof Player)
                                                    {
                                                        Location loc = ((Player) sender)
                                                                .getLocation();
                                                        x = loc.getBlockX();
                                                        y = loc.getBlockY();
                                                        z = loc.getBlockZ();
                                                    }
                                                    range = Integer.parseInt(args[2]);

                                                    if (range < 1 || range > 100000)
                                                    {
                                                        sender.sendMessage(ChatColor.RED+ "Range must be between 1 and 100000.");
                                                        return;
                                                    }

                                                }
                                                catch (NumberFormatException e)
                                                {
                                                    sender.sendMessage("Usage: /"+ cmd+ " [Player] [Owner] <range> <block type list>");
                                                    return;
                                                }
                                            }

                                            HashSet<Material> blockMap = new HashSet<Material>();
                                            if (args.length >= 4)
                                            {
                                                for (int i = 3; i < args.length; i++)
                                                {
                                                    Material material = Material.matchMaterial(args[i]);

                                                    if (material != null)
                                                    {
                                                        blockMap.add(material);
                                                    }
                                                }
                                            }

//                                            if (!playerAccess.containsKey(ownerName))
//                                            {
//                                                playerAccess
//                                                        .put(ownerName,
//                                                                new HashMap<String, ArrayList<ProtectData>>());
//                                            }
//                                            HashMap<String, ArrayList<ProtectData>> playerMap = playerAccess
//                                                    .get(ownerName);
//
//                                            if (!playerMap.containsKey(playerName))
//                                            {
//                                                playerMap.put(playerName,
//                                                        new ArrayList<ProtectData>());
//                                            }
//                                            ArrayList<ProtectData> rangeMap = playerMap
//                                                    .get(playerName);
//
//                                            rangeMap.add(new ProtectData(x, y, z, range,
//                                                    blockMap));
                                            data.getPlayerAccess().addProtectData(owner.getId(),player.getId(),new ProtectData(x, y, z, range, blockMap));

                                            if (cmd.equals("allow"))
                                            {
                                                data.getDatabase().submitInsertRecord(new GriefProtectPlayerAccessAdd(owner.getId(), player.getId(), x, y, z, range, blockMap.toString().substring(1, blockMap.toString().length() - 1)));

                                                sender.sendMessage(ChatColor.GREEN
                                                        + "Access granted to " + player.getName()
                                                        + " on " + owner.getName() + ".");
                                            }
                                            else
                                            {
                                                sender.sendMessage(ChatColor.GREEN
                                                        + "Temporary access granted to "
                                                        + player.getName() + " on " + owner.getName()
                                                        + " until next reboot.");
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
            else if (sender instanceof Player)
            {
                if (args.length < 1)
                {
                    sender.sendMessage("Usage: /" + cmd  + " [Player] <range> <block type list>");
                }
                else
                {
                   final Player owner = (Player)sender;
                   ProfileUtils.lookup(args[0], new Receiver<PlayerProfile>()
                   {
                       @Override
                       public void receive(final PlayerProfile player)
                       {

                           if (player == null)
                               sender.sendMessage(ChatColor.RED + "Player not found.");
                           else if (owner.getName().equals(player.getName()))
                               sender.sendMessage(ChatColor.RED+ "You cannot give yourself permission.");
                           else
                           {
                               Location loc = ((Player) sender).getLocation();
                               int x = loc.getBlockX();
                               int y = loc.getBlockY();
                               int z = loc.getBlockZ();
                               int range = 100000;
                               if (args.length >= 2)
                               {
                                   try
                                   {
                                       range = Integer.parseInt(args[1]);

                                       if (range < 1 || range > 100000)
                                       {
                                           sender.sendMessage(ChatColor.RED
                                                   + "Range must be between 1 and 100000.");
                                           return;
                                       }
                                   }
                                   catch (NumberFormatException e)
                                   {
                                       sender.sendMessage("Usage: /" + cmd
                                               + " [Player] <range> <block type list>");
                                       return;
                                   }
                               }

                               HashSet<Material> blockMap = new HashSet<Material>();
                               if (args.length >= 3)
                               {
                                   for (int i = 2; i < args.length; i++)
                                   {
                                       Material material = Material
                                               .matchMaterial(args[i]);

                                       if (material != null)
                                       {
                                           blockMap.add(material);
                                       }
                                   }
                               }

//                               if (!playerAccess.containsKey(ownerName))
//                               {
//                                   playerAccess
//                                           .put(ownerName,
//                                                   new HashMap<String, ArrayList<ProtectData>>());
//                               }
//                               HashMap<String, ArrayList<ProtectData>> playerMap = playerAccess
//                                       .get(ownerName);
//
//                               if (!playerMap.containsKey(playerName))
//                               {
//                                   playerMap.put(playerName,
//                                           new ArrayList<ProtectData>());
//                               }
//                               ArrayList<ProtectData> rangeMap = playerMap
//                                       .get(playerName);
//
//                               rangeMap.add(new ProtectData(x, y, z, range, blockMap));
                               data.getPlayerAccess().addProtectData(owner.getUniqueId(),player.getId(),new ProtectData(x, y, z, range, blockMap));

                               if (cmd.equals("allow"))
                               {
                                   data.getDatabase().submitInsertRecord(new GriefProtectPlayerAccessAdd(owner.getUniqueId(), player.getId(), x, y, z, range, blockMap.toString().substring(1, blockMap.toString().length() - 1)));

                                   sender.sendMessage(ChatColor.GREEN
                                           + "Access granted to " + player.getName() + ".");
                               }
                               else
                               {
                                   sender.sendMessage(ChatColor.GREEN
                                           + "Temporary access granted to "
                                           + player.getName() + " until next reboot.");
                               }
                           }
                       }
                   });

                }
            }
        return true;
    }
}
