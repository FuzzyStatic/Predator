package com.fuzzycraft.fuzzy;

import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.fuzzycraft.fuzzy.constants.Defaults;
import com.fuzzycraft.fuzzy.utilities.TeleportPlayers;

/**
 * 
 * @author FuzzyStatic (fuzzy@fuzzycraft.com)
 *
 */

public class Predator extends JavaPlugin {
	
	public static TeleportPlayers tp;
	
	private Predator plugin = this;
	private PredatorManagement pm;
	private World spawnWorld, gameWorld;
	
	public void onEnable() {		
		new BukkitRunnable() {
        	
			public void run() {
				spawnWorld = getServer().getWorld(Defaults.SPAWN_WORLD);
				gameWorld = getServer().getWorld(Defaults.GAME_WORLD);
				tp = new TeleportPlayers(spawnWorld, gameWorld);
				pm = new PredatorManagement(plugin, gameWorld);
				registerListeners();
			}
			
		}.runTaskLater(this, 1);
	}
	
	public void registerListeners() {
		PluginManager manager = getServer().getPluginManager();
		manager.registerEvents(this.pm, this);
	}
}
