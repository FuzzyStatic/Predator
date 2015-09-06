package com.fuzzycraft.fuzzy;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.fuzzycraft.fuzzy.commands.Quit;
import com.fuzzycraft.fuzzy.constants.Defaults;
import com.fuzzycraft.fuzzy.utilities.GamemodeChecker;

/**
 * 
 * @author FuzzyStatic (fuzzy@fuzzycraft.com)
 *
 */

public class Predator extends JavaPlugin {
	
	public static Location spawn;
	
	private Predator plugin = this;
	
	public void onEnable() {				
		new BukkitRunnable() {
        	
			public void run() {
				spawn = new Location(getServer().getWorld(Defaults.SPAWN_WORLD), 
		                Defaults.SPAWN_X, 
		                Defaults.SPAWN_Y, 
		                Defaults.SPAWN_Z);
				
				PluginManager manager = getServer().getPluginManager();
				
				for (String world : Defaults.GAME_WORLDS) {
					World gameWorld = getServer().getWorld(world);
					PredatorManagement pm = new PredatorManagement(plugin, gameWorld);
					manager.registerEvents(pm, plugin);
				}
				
				manager.registerEvents(new GamemodeChecker(), plugin);	
			}
			
		}.runTaskLater(this, 1);
		
		getCommand(Quit.CMD).setExecutor(new Quit());
	}
}
