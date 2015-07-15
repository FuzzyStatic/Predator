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
	
	private PredatorManagement pm;
	
	public void onEnable() {		
		//configDefaults();	
		
		tp = new TeleportPlayers(this);
		this.pm = new PredatorManagement(this);
		
		registerListeners();
	}
	
	public void registerListeners() {
		PluginManager manager = getServer().getPluginManager();
		manager.registerEvents(this.pm, this);
	}
	
	public void configDefaults() {
		getDataFolder().mkdir();
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
}
