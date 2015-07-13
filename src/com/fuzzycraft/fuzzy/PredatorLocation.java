package com.fuzzycraft.fuzzy;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.fuzzycraft.fuzzy.constants.Defaults;

public class PredatorLocation {
	
	public Predator plugin;
	public World world;
	private int minX, maxX, minY, maxY, minZ, maxZ;

	/**
	 * Constructor.
	 * @param plugin
	 */
	public PredatorLocation(Predator plugin) {
		this.plugin = plugin;
		this.world = plugin.getServer().getWorld(Defaults.WORLD);
		this.minX = Defaults.MIN_X;
		this.maxX = Defaults.MAX_X;
		this.minY = Defaults.MIN_Y;
		this.maxY = Defaults.MAX_Y;
		this.minZ = Defaults.MIN_Z;
		this.maxZ = Defaults.MAX_Z;
	}

	/**
	 * Create random location based on specified defaults.
	 * @return 
	 */
	public Location getRandomLocation() {
		Random rand = new Random();
		double x = rand.nextInt((this.maxX - this.minX) + 1) + this.minX; 
		double y = rand.nextInt((this.maxY - this.minY) + 1) + this.minY; 
		double z = rand.nextInt((this.maxZ - this.minZ) + 1) + this.minZ;
		return new Location(this.world, x, y, z);
	}
	
	/**
	 * Spawn specified material at location.
	 */
	public void spawnMaterial(Material material, int amount) {
		for (int i = 0; i < amount; i++) {
			Location location = this.getRandomLocation();
			Location materialLocation = new Location(location.getWorld(), location.getX(), location.getY()+1, location.getZ());
			
			if (location.getBlock().getType() != Material.AIR
					&& location.getBlock().getType() != Material.LAVA 
					&& location.getBlock().getType() != Material.STATIONARY_LAVA
					&& materialLocation.getBlock().getType() == Material.AIR) {
				System.out.println(location.getBlock().getType().toString());
				System.out.println(materialLocation.toString());
				materialLocation.getBlock().setType(material);
			} else {
				i--;
			}
		}
	}
	
	/**
	 * Remove all of specified material from world.
	 */
	public void removeAll(Material material) {
		for (double x = this.minX; x <= this.maxX; x++) {
			for (double y = this.minY; y <= this.maxY; y++) {
				for (double z = this.minZ; z <= this.maxZ; z++) {
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
