package com.fuzzycraft.fuzzy;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 
 * @author FuzzyStatic (fuzzy@fuzzycraft.com)
 *
 */

public class Predator extends JavaPlugin {

	public void onEnable() {		
		configDefaults();		
		registerListeners();
		RandomEnderEggs ree = new RandomEnderEggs(this);
		ree.removeAll();
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
