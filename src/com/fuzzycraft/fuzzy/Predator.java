package com.fuzzycraft.fuzzy;

import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.fuzzycraft.fuzzy.constants.Defaults;

/**
 * 
 * @author FuzzyStatic (fuzzy@fuzzycraft.com)
 *
 */

public class Predator extends JavaPlugin {
	
	public static World spawnWorld;
	
	private Predator plugin = this;
	
	public void onEnable() {				
		new BukkitRunnable() {
        	
			public void run() {
				spawnWorld = getServer().getWorld(Defaults.SPAWN_WORLD);
				
				for (String world : Defaults.GAME_WORLDS) {
					World gameWorld = getServer().getWorld(world);
					PredatorManagement pm = new PredatorManagement(plugin, gameWorld);
					PluginManager manager = getServer().getPluginManager();
					manager.registerEvents(pm, plugin);
				}
			}
			
		}.runTaskLater(this, 1);
	}
}
