package com.fuzzycraft.fuzzy.utilities;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.ScoreboardManager;

import com.fuzzycraft.fuzzy.Predator;

/**
 * 
 * @author FuzzyStatic (fuzzy@fuzzycraft.com)
 *
 */

public class TeleportPlayers {
	
    public static final int GAME_X = 1;
    public static final int GAME_Y = 48;
    public static final int GAME_Z = 0;
    
	private Location spawnLoc;
	private Location start;
	
	public static final ScoreboardManager manager = Bukkit.getScoreboardManager();
		
	/**
	 * Constructor.
	 * @param plugin
	 */
	public TeleportPlayers(World gameWorld) {
		this.spawnLoc = Predator.spawnLoc;
		this.start = new Location(gameWorld, 
				GAME_X, 
				GAME_Y, 
				GAME_Z);
	}
	
	/**
	 * Teleport players to spawn.
	 * @param players
	 */
	public void teleportPlayersToSpawn(List<Player> players) {
		for (Player player : players) {
		    GameModeChecker.setSurvival(player);
			player.setScoreboard(manager.getNewScoreboard());
			player.teleport(this.spawnLoc);
		}
	}
	
	/**
	 * Teleport players to spawn.
	 * @param players
	 */
	public void teleportPlayerToStart(Player player) {
		player.teleport(this.start);
	}
}
