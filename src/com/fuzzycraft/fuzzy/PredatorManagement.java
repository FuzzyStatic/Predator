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
	private World world;
	private PredatorLocation pl;
	private Material material;
	private int eventTime, lobbyTime, minPlayers, materialAmount;
	private boolean running = false;
	
	/**
	 * Constructor.
	 * @param plugin
	 */
	public PredatorManagement(Predator plugin, World world, int minPlayers) {
		this.plugin = plugin;
		this.world = this.plugin.getServer().getWorld(Defaults.WORLD);
		this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
		this.pl = new PredatorLocation(this.plugin);
		this.material = Defaults.MATERIAL;
		this.eventTime = Defaults.MIN_PLAYERS;
		this.lobbyTime = Defaults.MIN_PLAYERS;
		this.minPlayers = Defaults.MIN_PLAYERS;
		this.materialAmount = Defaults.MATERIAL_AMOUNT;
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
		
		if (!this.running && playersInWorld > this.minPlayers) {
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
			
		}.runTaskLater(this.plugin, this.eventTime * 20);
	}
	
	/**
	 * clean up for next event.
	 */
	public void clean() {
		this.plugin.getServer().broadcastMessage(ChatColor.GREEN + "Eggs will despawn in 30 seconds");
		
		new BukkitRunnable() {
        	
			public void run() {
				pl.removeAll(material);
				start();
			}
			
		}.runTaskLater(this.plugin, this.lobbyTime * 20);
	}
}
