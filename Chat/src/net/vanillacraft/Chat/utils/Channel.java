package net.vanillacraft.Chat.utils;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Jiibrael on 5/27/2015.
 */
public class Channel {

	private String channelName;
	private String access;  // Minimum group level/ID required to join. To be updated when Groups are ready
	private ArrayList<UUID> userList= new ArrayList<>();
	private UUID ownerID;  // Player that created the channel
	
	public Channel(String chName, Player chOwner){
		if (chOwner.isOnline()) {
			ownerID = chOwner.getUniqueId();
			userList.add(ownerID);
			channelName = chName; // TODO: Profanity filtering
		}
	}
	
	public Channel(String chName, ArrayList<UUID> users){
		if (chName == "Global" || chName == "Regional" || chName == "Local") {
			userList.addAll(users);
			ownerID = null;
			channelName = chName; // TODO: Profanity filtering
		}
	}
	
	public String getChannelName(){
		return channelName;
	}

	public UUID getOwner(){
		return ownerID;
	}
	
	public ArrayList<UUID> getUserList(){
		return userList;
	}
	
	public boolean contains(Player player){
		return userList.contains(player);
	}	
	
	public void addUser(Player player){
		/**
		 * NOTE: Exceptions user not online or user does not exists treated
		 * at the command event.
		 */ 
		if (!this.contains(player)) {
			userList.add(player.getUniqueId());
		} else {
			// Send info: user is already in the channel
			player.sendMessage(player.getDisplayName() +"is already in channel" 
								+ this.getChannelName());
		}
	}	
	
	public void removeUser(Player player){
		/**
		 * NOTE: Exceptions user not online or user does not exists treated
		 * at the command event.
		 */ 
		if (this.contains(player)){
			userList.remove(player);
		} else {
			// TODO: Send info: user is not in the channel
		}
	}
}
