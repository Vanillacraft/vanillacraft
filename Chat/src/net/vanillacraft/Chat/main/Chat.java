package net.vanillacraft.Chat.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import net.vanillacraft.Chat.utils.Channel;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Jiibrael on 5/27/2015.
 */
public class Chat extends JavaPlugin implements Listener {
	
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
		// TODO: puts all connected players UUID in the activeChannelMap
		for(UUID id: allConnectedPlayersID){
			activeChannelMap.put(id, globalChannel.getChannelName());
		}
	}
	
	@Override
	public void onDisable(){
		
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event){
		Player player = event.getPlayer();
		String msg = event.getMessage();
		String chName = activeChannelMap.get(player.getUniqueId());
		
		event.setFormat("["+chName+"]["+player.getDisplayName()+"] "+msg);
	}
	
	public static Chat getInstance(){
		return instance;
	}

}
