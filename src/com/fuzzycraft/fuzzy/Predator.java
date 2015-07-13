package com.fuzzycraft.fuzzy;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.fuzzycraft.fuzzy.utilities.TeleportPlayers;

/**
 * 
 * @author FuzzyStatic (fuzzy@fuzzycraft.com)
 *
 */

public class Predator extends JavaPlugin {
	
	public static TeleportPlayers tp;
	
	public void onEnable() {		
		configDefaults();	
		
		tp = new TeleportPlayers(this);
		
		registerListeners();
	}
	
	public void registerListeners() {
		PluginManager pm = getServer().getPluginManager();
		//pm.registerEvents(new PlayerJoin(this), this);
		//pm.registerEvents(new PlayerPearled(this), this);
		//pm.registerEvents(new PlayerTagged(this), this);
	}
	
	public void configDefaults() {
		getDataFolder().mkdir();
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
}
