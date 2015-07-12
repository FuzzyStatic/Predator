package com.fuzzycraft.fuzzy;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.fuzzycraft.fuzzy.constants.Defaults;

public class RandomEnderEggs {
	
	public Predator plugin;
	public World world;
	
	/**
	 * Constructor.
	 * @param plugin
	 */
	public RandomEnderEggs(Predator plugin) {
		this.plugin = plugin;
		this.world = plugin.getServer().getWorld(Defaults.WORLD);
	}

	/**
	 * Create random location based on specified defaults.
	 * @return 
	 */
	public Location randomLocation() {
		Random rand = new Random();
		double x = rand.nextInt((Defaults.MAX_X - Defaults.MIN_X) + 1) + Defaults.MIN_X; 
		double y = rand.nextInt((Defaults.MAX_X - Defaults.MIN_X) + 1) + Defaults.MIN_X; 
		double z = rand.nextInt((Defaults.MAX_X - Defaults.MIN_X) + 1) + Defaults.MIN_X;
		return new Location(this.world, x, y, z);
	}
	
	/**
	 * Spawn EnderEggs at random locations.
	 */
	public void spawn() {
		for (int i = 0; i < Defaults.AMOUNT; i++) {
			Location loc = randomLocation();
			Location eggLoc = loc;
			
			//Set egg location one Y block above random location.
			eggLoc.setY(loc.getY()+1);
			
			if (loc.getBlock().getType() != Material.AIR && eggLoc.getBlock().getType() == Material.AIR) {
				eggLoc.getBlock().setType(Material.DRAGON_EGG);
			} else {
				i--;
			}
		}
	}
	
	/**
	 * Remove all EnderEggs from world.
	 */
	public void removeAll() {
		for (int x = Defaults.MIN_X; x <= Defaults.MAX_X; x++) {
			for (int y = Defaults.MIN_Y; y <= Defaults.MAX_Y; y++) {
				for (int z = Defaults.MIN_Z; z <= Defaults.MAX_Z; z++) {
					Location loc = new Location(this.world, x, y, z);
					Block block = this.world.getBlockAt(loc);
					
					if (block.getType() == Material.DRAGON_EGG) {
						block.setType(Material.AIR);
					}
				}
			}
		}
	}
	
	/**
	 * Start the event.
	 */
	public void start() {
		this.plugin.getServer().broadcastMessage(ChatColor.GREEN + "Eggs will spawn in 30 seconds");
		
		new BukkitRunnable() {
        	
			public void run() {
				spawn();
				clean();
			}
			
		}.runTaskLater(this.plugin, 30 * 20);
	}
	
	/**
	 * clean up for next event.
	 */
	public void clean() {
		this.plugin.getServer().broadcastMessage(ChatColor.GREEN + "Eggs will despawn in 60 seconds");
		
		new BukkitRunnable() {
        	
			public void run() {
				start();
			}
			
		}.runTaskLater(this.plugin, 60 * 20);
	}
}
