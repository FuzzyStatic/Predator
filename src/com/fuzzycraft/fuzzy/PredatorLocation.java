package com.fuzzycraft.fuzzy;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * 
 * @author FuzzyStatic (fuzzy@fuzzycraft.com)
 *
 */

public class PredatorLocation {
	
    public static final int MAX_X = 48;
    public static final int MAX_Y = 60;
    public static final int MAX_Z = 48;
    public static final int MIN_X = -48;
    public static final int MIN_Y = 32;
    public static final int MIN_Z = -48;
    
	private World world;

	/**
	 * Constructor.
	 * @param plugin
	 */
	public PredatorLocation(World world) {
		this.world = world;
	}

	/**
	 * Create random location based on specified defaults.
	 * @return 
	 */
	public Location getRandomLocation() {
		Random rand = new Random();
		double x = rand.nextInt((MAX_X - MIN_X) + 1) + MIN_X; 
		double y = rand.nextInt((MAX_Y - MIN_Y) + 1) + MIN_Y; 
		double z = rand.nextInt((MAX_Z - MIN_Z) + 1) + MIN_Z;
		return new Location(this.world, x, y, z);
	}
	
	/**
	 * Spawn specified material at location.
	 */
	public void spawnMaterial(Material material, int amount) {
		for (int i = 0; i < amount; i++) {
			Location location = this.getRandomLocation();
			Location materialLocation = new Location(location.getWorld(), location.getX(), location.getY() + 1, location.getZ());
			
			if (location.getBlock().getType() != Material.AIR
					&& location.getBlock().getType() != Material.LAVA 
					&& location.getBlock().getType() != Material.STATIONARY_LAVA
					&& materialLocation.getBlock().getType() == Material.AIR) {
				materialLocation.getBlock().setType(material);
			} else {
				i--;
			}
		}
	}
	
	/**
	 * Spawn specified player at location.
	 */
	public void spawnPlayer(Player player) {
		boolean teleport = true;
		
		do {
			Location location = this.getRandomLocation();
			Location firstLocation = new Location(location.getWorld(), location.getX(), location.getY() + 1, location.getZ());
			Location secondLocation = new Location(location.getWorld(), location.getX(), location.getY() + 2, location.getZ());
				
			if (location.getBlock().getType() != Material.AIR
					&& location.getBlock().getType() != Material.LAVA 
					&& location.getBlock().getType() != Material.STATIONARY_LAVA
					&& location.getBlock().getType() != Material.LADDER
					&& firstLocation.getBlock().getType() == Material.AIR
					&& secondLocation.getBlock().getType() == Material.AIR) {
				player.teleport(firstLocation);
				teleport = false;
			}
        } while (teleport);
	}
	
	/**
	 * Remove all of specified material from world.
	 */
	public void removeAll(Material material) {
		for (double x = MIN_X; x <= MAX_X; x++) {
			for (double y = MIN_Y; y <= MAX_Y; y++) {
				for (double z = MIN_Z; z <= MAX_Z; z++) {
					Location loc = new Location(this.world, x, y, z);
					Block block = this.world.getBlockAt(loc);
					
					if (block.getType() == material) {
						block.setType(Material.AIR);
					}
				}
			}
		}
	}
}
