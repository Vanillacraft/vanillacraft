package net.vanillacraft.Chat.main;

import java.util.ArrayList;

import net.vanillacraft.Chat.utils.Channel;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Jiibrael on 5/27/2015.
 */
public class Chat extends JavaPlugin {
	
	private static Chat instance;
	
	private ArrayList<Channel> channelList = new ArrayList<>();
	
	@Override
	public void onEnable(){
		instance = this;
	}
	
	@Override
	public void onDisable(){
		
	}

}
