package com.fuzzycraft.fuzzy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import com.fuzzycraft.fuzzy.constants.Defaults;

public class PredatorManagement implements Listener {
	
	public Predator plugin;
	private boolean running = false;
	private PredatorLocation pl;
	private World world;
	private Material material;
	private int minPlayers, materialAmount;
	
	/**
	 * Constructor.
	 * @param plugin
	 */
	public PredatorManagement(Predator plugin, World world, int minPlayers, int materialAmount) {
		this.plugin = plugin;
		this.world = this.plugin.getServer().getWorld(Defaults.WORLD);
		this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
		this.pl = new PredatorLocation(this.plugin);
		this.material = Material.DRAGON_EGG;
		this.minPlayers = 2;
		this.materialAmount = materialAmount;
	}
	
	
	/**
	 * Create board for joining player.
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
		if (event.getPlayer().getWorld() != this.world) {
			return;
		}
		
		int playersInWorld = this.world.getPlayers().size();
		
		if (!this.isRunning() && playersInWorld > this.minPlayers) {
			this.start();
		}
	}
	
	/**
	 * Start the event.
	 */
	public void start() {
		this.running = true;
		this.plugin.getServer().broadcastMessage(ChatColor.GREEN + "Eggs will spawn in 30 seconds");
		
		new BukkitRunnable() {
        	
			public void run() {
				pl.spawnMaterial(material, materialAmount);;
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
				pl.removeAll(material);
				start();
			}
			
		}.runTaskLater(this.plugin, 60 * 20);
	}
	
	public boolean isRunning() {
		return running;
	}
}
