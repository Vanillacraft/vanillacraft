package net.vanillacraft.Chat.utils;

import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Created by Jiibrael on 5/27/2015.
 */
public class Channel {

	private String channelName;
	private String access;  // Minimum group level/ID required to join. To be updated when Groups are ready
	private ArrayList<Player> userList= new ArrayList<>();
	private Player owner;  // Player that created the channel
	
	public Channel(String chName, Player chOwner){
		if (chOwner.isOnline()) {
			userList.add(chOwner);
			owner = chOwner;
			channelName = chName; // TODO: Profanity filtering
		}
	}
	
	public Channel(String chName, ArrayList<Player> users){
		if (chName == "Global" || chName == "Regional" || chName == "Local") {
			userList.addAll(users);
			owner = null;
			channelName = chName; // TODO: Profanity filtering
		}
	}
	
	public String getChannelName(){
		return channelName;
	}

	public Player getOwner(){
		return owner;
	}
	
	public ArrayList<Player> getUserList(){
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
			userList.add(player);
		} else {
			// TODO: Send info: user is already in the channel
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
