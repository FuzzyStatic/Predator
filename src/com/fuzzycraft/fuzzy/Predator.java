package com.fuzzycraft.fuzzy;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.fuzzycraft.fuzzy.constants.Defaults;
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
		configDefaults();	
		
		tp = new TeleportPlayers(this);
		this.pm = new PredatorManagement(this, this.getServer().getWorld(Defaults.WORLD));
		
		registerListeners();
	}
	
	public void registerListeners() {
		PluginManager manager = getServer().getPluginManager();
		manager.registerEvents(this.pm, this);
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
