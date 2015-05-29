package net.vanillacraft.Chat.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import net.vanillacraft.Chat.utils.Channel;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Jiibrael on 5/27/2015.
 */
public class Chat extends JavaPlugin {
	
	private static Chat instance;
	
	private HashMap<UUID, Channel> channelMap = new HashMap<UUID, Channel>();
	private ArrayList<Channel> channelList = new ArrayList<>();
	
	@Override
	public void onEnable(){
		instance = this;
		ArrayList<Player> allConnectedPlayers = 
				new ArrayList<Player>(this.getServer().getOnlinePlayers());
		Channel globalChannel = new Channel("Global", allConnectedPlayers);

		channelList.add(globalChannel);
		// TODO: puts all connected players UUID in the channelMap
		for(Player p: allConnectedPlayers){
			channelMap.put(p.getUniqueId(), globalChannel);
		}
	}
	
	@Override
	public void onDisable(){
		
	}
	
	public Chat getInstance(){
		return instance;
	}

}
