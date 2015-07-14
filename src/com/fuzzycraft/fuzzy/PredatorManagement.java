package com.fuzzycraft.fuzzy;

import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.fuzzycraft.fuzzy.constants.Defaults;

public class PredatorManagement implements Listener {
	
	public Predator plugin;
	private World world;
	private PredatorLocation pl;
	private Material material;
	private int eventTime, finishTime, lobbyTime, minPlayers, materialAmount, pointsEgg, pointsKill;
	private BukkitTask gameTask;
	private boolean running = false;
	private HashMap<Player, Integer> playerEggs = new HashMap<Player, Integer>();
	private HashMap<Player, Integer> playerKills = new HashMap<Player, Integer>();
	
	/**
	 * Constructor.
	 * @param plugin
	 */
	public PredatorManagement(Predator plugin, World world) {
		this.plugin = plugin;
		this.world = world;
		this.pl = new PredatorLocation(this.plugin);
		this.material = Defaults.MATERIAL;
		this.eventTime = Defaults.EVENT_TIME;
		this.finishTime = Defaults.FINISH_TIME;
		this.lobbyTime = Defaults.LOBBY_TIME;
		this.minPlayers = Defaults.MIN_PLAYERS;
		this.materialAmount = Defaults.MATERIAL_AMOUNT;
		this.pointsEgg = Defaults.POINTS_EGG;
		this.pointsKill = Defaults.POINTS_KILL;
	}
	
	/**
	 * Create board for joining player.
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
		if (this.running || event.getPlayer().getWorld() != this.world) {
			return;
		}
		
		int playersInWorld = this.world.getPlayers().size();
				
		if (playersInWorld >= this.minPlayers) {
			this.start();
		}
	}
	
	/**
	 * Create check for material break.
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		Player player;
		
		if (event.getPlayer() != null) {
			player = event.getPlayer();
		} else {
			return;
		}
		
		if (this.gameTask == null || block.getWorld() != this.world) {
			return;
		}
		
		// Give block breaker a block point.
		if (block.getType() == this.material) {
			this.playerEggs.put(player, this.playerEggs.get(player) + 1);
		}
	}
	
	/**
	 * Check for player death.
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onEntityDeath(EntityDeathEvent event) {
        if (this.gameTask == null || !(event.getEntity() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getEntity();
        Player killer = null;
        
        if (player.getKiller() instanceof Player) {
            killer = (Player) player.getKiller();
        } else if (player.getKiller() instanceof Projectile) {
            Projectile projectile = (Projectile) player.getKiller();
            
            if (projectile.getShooter() instanceof Player) {
            	killer = (Player) projectile.getShooter();
            }
        } else {
        	return;
        }
        
        // Give killer a kill.
        if (killer != null && killer != event.getEntity()) {
        	this.playerKills.put(killer, this.playerKills.get(killer) + 1);
        }
        
        // Teleport player to random start point.
        player.teleport(this.pl.getRandomLocation());
    }
	
	/**
	 * Start the event.
	 */
	public void start() {
		this.pl.removeAll(material);
		
		this.sendMassMessage(this.world.getPlayers(), ChatColor.GREEN + "Game will start in " + this.lobbyTime + " seconds!");
		
		this.gameTask = new BukkitRunnable() {
        	
			public void run() {
				cleanHashMap(playerEggs);
				cleanHashMap(playerKills);
				running = true;
				pl.spawnMaterial(material, materialAmount);
				sendMassMessage(world.getPlayers(), ChatColor.GREEN + "Game on!");
				
				// Teleport players to random start point.
				for (Player player : world.getPlayers()) {
					player.teleport(pl.getRandomLocation());
				}
				
				finish();
			}
			
		}.runTaskLater(this.plugin, this.lobbyTime * 20);
	}
	
	/**
	 * Finish event after evenTime and name winners.
	 */
	public void finish() {
		System.out.println(this.playerEggs);
		System.out.println(this.playerKills);

		new BukkitRunnable() {
        	
			public void run() {
				sendMassMessage(world.getPlayers(), ChatColor.GREEN + "Game is over!");
				sendMassMessage(world.getPlayers(), ChatColor.GREEN + "Thanks for playing!");
				clean();
			}
			
		}.runTaskLater(this.plugin, this.eventTime * 20);
	}
	
	/**
	 * Clean up for next event.
	 */
	public void clean() {
		new BukkitRunnable() {
        	
			public void run() {
				sendMassMessage(world.getPlayers(), ChatColor.GREEN + "You are being teleported back to the hub...!");
				Predator.tp.teleportPlayersToSpawn(world.getPlayers());
				pl.removeAll(material);
				running = false;
			}
			
		}.runTaskLater(this.plugin, this.finishTime * 20);
	}
	
	/**
	 * Set all keys of player to values of 0.
	 * @param map
	 */
	public void cleanHashMap(HashMap<Player, Integer> map) {
		map.clear();
		
		for (Player player : this.world.getPlayers()) {
			map.put(player, 0);
		}
	}
	
	/**
	 * Send message to list of players
	 */
	public void sendMassMessage(List<Player> players, String msg) {
		for (Player player : players) {
			player.sendMessage(msg);
		}
	}
}