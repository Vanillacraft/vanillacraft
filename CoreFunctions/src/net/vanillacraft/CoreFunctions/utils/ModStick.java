package net.vanillacraft.CoreFunctions.utils;

import net.vanillacraft.CoreFunctions.datastores.CoreData;
import net.vanillacraft.CoreFunctions.datastores.ModStickBlockLog;
import net.vanillacraft.CoreFunctions.datastores.PlayerProfile;
import net.vanillacraft.CoreFunctions.main.CoreFunctions;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by ryan on 5/9/2015.
 * TODO: need to add a command to enable stick mode for the player profile.
 */
public class ModStick implements Listener
{

    private CoreFunctions plugin;

    private ArrayList<Material> nonStickableItems = new ArrayList<>();

    public ModStick(CoreFunctions plugin)
    {
        Bukkit.getPluginManager().registerEvents(this, CoreFunctions.getInstance());
        this.plugin = plugin;

        //todo: this is something that may change but im just throwing it there incase i want this.
        nonStickableItems.add(Material.CHEST);
        nonStickableItems.add(Material.ENDER_CHEST);
        nonStickableItems.add(Material.TRAPPED_CHEST);
        nonStickableItems.add(Material.HOPPER);
        nonStickableItems.add(Material.FURNACE);
        nonStickableItems.add(Material.DISPENSER);
        nonStickableItems.add(Material.DROPPER);
        nonStickableItems.add(Material.BEDROCK);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (!event.isCancelled())
        {

            Player player = event.getPlayer();
            PlayerProfile profile = CoreData.getProfile(player);

            if (player.getItemInHand().getType() == Material.STICK)
            {
                if (profile.is("Moderator"))
                {
                    if (profile.isModMode())
                    {
                        if (profile.is("Stick"))
                        {
                            if (player.getTargetBlock(null, 10) != null)
                            {
                                Block b = player.getTargetBlock(null, 10);
                                if (!nonStickableItems.contains(b.getType()))
                                {
                                    logModStickBreak(player, b);
                                }
                                else
                                {
                                    if (player.getName().equalsIgnoreCase("ryan00793"))
                                    {
                                        logModStickBreak(player, b);
                                    }
                                    else
                                    {
                                        //TODO: probably some alert logged somewhere incase of abuse??
                                        plugin.getCoreErrors().sendError(player, "You can not stick this item please contact ryan00793");
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
    }

    public void logModStickBreak(Player player, Block block)
    {
        //todo is this how this works?
        plugin.getCoreData().getDatabase().submitInsertRecord(new ModStickBlockLog(player.getUniqueId(), block.getLocation()));
        block.breakNaturally();
    }
}
