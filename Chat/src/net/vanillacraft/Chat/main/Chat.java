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
	
	private HashMap<UUID, String> activeChannelMap = new HashMap<UUID, String>();
	private ArrayList<Channel> channelList = new ArrayList<>();
	
	@Override
	public void onEnable(){
		instance = this;
		ArrayList<UUID> allConnectedPlayersID = new ArrayList<UUID>();
		for(Player p: this.getServer().getOnlinePlayers()){
			allConnectedPlayersID.add(p.getUniqueId());
		}
		Channel globalChannel = new Channel("Global", allConnectedPlayersID);

		channelList.add(globalChannel);
		// TODO: puts all connected players UUID in the channelMap
		for(UUID id: allConnectedPlayersID){
			activeChannelMap.put(id, globalChannel.getChannelName());
		}
	}
	
	@Override
	public void onDisable(){
		
	}
	
	public static Chat getInstance(){
		return instance;
	}

}
