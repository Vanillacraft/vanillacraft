package com.gmail.nuclearcat1337.griefprotect.commands;

import com.gmail.nuclearcat1337.griefprotect.griefData.GriefData;
import com.gmail.nuclearcat1337.griefprotect.queries.GriefProtectPlayerAccessRemove;
import com.gmail.nuclearcat1337.griefprotect.util.PlayerProfile;
import com.gmail.nuclearcat1337.griefprotect.util.ProfileUtils;
import net.vanillacraft.CoreFunctions.utils.Receiver;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RevokeCommand implements CommandExecutor
{
    private GriefData data;

    public RevokeCommand(JavaPlugin plugin, GriefData data)
    {
        plugin.getCommand("revoke").setExecutor(this);
        this.data = data;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String s, final String[] args)
    {
        if (sender.isOp())
        {
            if (args.length != 2)
                sender.sendMessage("Usage: /revoke [Player] [Owner]");
            else
            {
                ProfileUtils.lookup(args[0], new Receiver<PlayerProfile>()
                {
                    @Override
                    public void receive(final PlayerProfile player)
                    {
                        if (player == null)
                            sender.sendMessage(ChatColor.RED + "Player not found");
                        else
                        {
                            ProfileUtils.lookup(args[1], new Receiver<PlayerProfile>()
                            {
                                @Override
                                public void receive(final PlayerProfile owner)
                                {
                                    if (owner == null)
                                        sender.sendMessage(ChatColor.RED+ "Owner not found");
                                    else
                                    {
                                        if (data.getPlayerAccess().hasAllow(owner.getId(),player.getId()))
                                        {
                                            data.getPlayerAccess().removeAllows(owner.getId(),player.getId());

                                            data.getDatabase().submitInsertRecord(new GriefProtectPlayerAccessRemove(owner.getId(), player.getId()));

                                            sender.sendMessage(ChatColor.RED
                                                    + "Access revoked for "
                                                    + player.getName() + " on " + owner.getName()
                                                    + ".");
                                        }
                                        else
                                        {
                                            sender.sendMessage(ChatColor.RED
                                                    + player.getName()
                                                    + " does not have access to "
                                                    + owner.getName() + ".");
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
            final Player owner = (Player)sender;
            if (args.length != 1)
                sender.sendMessage("Usage: /revoke [Player]");
            else
            {
                ProfileUtils.lookup(args[0], new Receiver<PlayerProfile>()
                {
                    @Override
                    public void receive(final PlayerProfile player)
                    {
                        if (player == null)
                            sender.sendMessage(ChatColor.RED + "Player not found");
                        else
                        {
                            if (data.getPlayerAccess().hasAllow(owner.getUniqueId(),player.getId()))
                            {
                               data.getPlayerAccess().removeAllows(owner.getUniqueId(),player.getId());

                                data.getDatabase().submitInsertRecord(new GriefProtectPlayerAccessRemove(
                                        owner.getUniqueId(), player.getId()));

                                sender.sendMessage(ChatColor.RED+ "Access revoked for " + player.getName()+ ".");
                            }
                            else
                            {
                                sender.sendMessage(ChatColor.RED + player.getName() + " does not have access.");
                            }
                        }
                    }
                });
            }
        }
        return true;
    }
}
