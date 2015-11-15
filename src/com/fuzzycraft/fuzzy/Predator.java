package com.fuzzycraft.fuzzy;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.fuzzycraft.fuzzy.commands.Quit;
import com.fuzzycraft.fuzzy.constants.Defaults;
import com.fuzzycraft.fuzzy.constants.NPCVulture;
import com.fuzzycraft.fuzzy.npcs.CustomEntityPigZombie;
import com.fuzzycraft.fuzzy.npcs.CustomEntityType;
import com.fuzzycraft.fuzzy.utilities.GameModeChecker;

/**
 * 
 * @author FuzzyStatic (fuzzy@fuzzycraft.com)
 *
 */

public class Predator extends JavaPlugin {
	
    public static World spawnWorld;
	public static Location spawnLoc;
	
	private Predator plugin = this;
	
	public void onEnable() {	
	    CustomEntityType.registerEntities();
	    
		new BukkitRunnable() {
        	
			public void run() {
			    // Set spawn world and spawn location.
			    spawnWorld = getServer().getWorld(Defaults.SPAWN_WORLD);
				spawnLoc = new Location(spawnWorld, 
		                Defaults.SPAWN_X, 
		                Defaults.SPAWN_Y, 
		                Defaults.SPAWN_Z,
		                Defaults.SPAWN_YAW,
		                Defaults.SPAWN_PITCH);
				
				// Create class NPCs through custom entity classes and NMS.
				net.minecraft.server.v1_8_R3.World nmsWorld = ((CraftWorld) spawnLoc.getWorld()).getHandle();
				CustomEntityPigZombie cepz = new CustomEntityPigZombie(nmsWorld);
				cepz.setPosition(NPCVulture.X, NPCVulture.Y, NPCVulture.Z);
			    nmsWorld.addEntity(cepz);
												
				PluginManager manager = getServer().getPluginManager();
				
				for (String world : Defaults.GAME_WORLDS) {
					World gameWorld = getServer().getWorld(world);
					PredatorManagement pm = new PredatorManagement(plugin, gameWorld);
					manager.registerEvents(pm, plugin);
				}
				
				manager.registerEvents(new GameModeChecker(), plugin);	
			}
			
		}.runTaskLater(this, 1);
		
		getCommand(Quit.CMD).setExecutor(new Quit());
	}
	
	public void onDisable() {
	    CustomEntityType.unregisterEntities();
	}
}
