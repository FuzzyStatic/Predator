package com.fuzzycraft.fuzzy.utilities;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.ScoreboardManager;

import com.fuzzycraft.fuzzy.Predator;
import com.fuzzycraft.fuzzy.constants.Defaults;

public class TeleportPlayers {
	
	private Predator plugin;
	public Location spawn;
	public Location start;
	
	public static final ScoreboardManager manager = Bukkit.getScoreboardManager();
		
	/**
	 * Constructor.
	 * @param plugin
	 */
	public TeleportPlayers(Predator plugin) {
		this.plugin = plugin;
		this.spawn = new Location(this.plugin.getServer().getWorld(Defaults.SPAWN_WORLD), 
				Defaults.SPAWN_X, 
				Defaults.SPAWN_Y, 
				Defaults.SPAWN_Z);
		this.start = new Location(this.plugin.getServer().getWorld(Defaults.GAME_WORLD), 
				Defaults.GAME_X, 
				Defaults.GAME_Y, 
				Defaults.GAME_Z);
	}
	
	/**
	 * Teleport players to spawn.
	 * @param players
	 */
	public void teleportPlayersToSpawn(List<Player> players) {
		for (Player player : players) {
			player.setScoreboard(manager.getNewScoreboard());
			player.teleport(this.spawn);
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
