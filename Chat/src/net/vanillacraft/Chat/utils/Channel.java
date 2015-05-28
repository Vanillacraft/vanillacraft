package net.vanillacraft.Chat.utils;

import org.bukkit.entity.Player;

import java.util.ArrayList;

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
		return false;
	}	
	
	public void addUser(Player player){
		// TODO: check if player is allowed to enter the channel
		if (player.isOnline()) {
			userList.add(player);
		}
	}	
	
	public void removeUser(Player player){
		
	}
}
