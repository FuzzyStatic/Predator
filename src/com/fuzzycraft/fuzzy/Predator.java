package com.fuzzycraft.fuzzy;

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
		
	public void onEnable() {		
		tp = new TeleportPlayers(this);
		new PredatorManagement(this, this.getServer().getWorld(Defaults.GAME_WORLD));
	}
}
