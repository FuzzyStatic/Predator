package com.fuzzycraft.fuzzy;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
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
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.fuzzycraft.fuzzy.constants.Defaults;

public class PredatorManagement implements Listener {
	
	public Predator plugin;
	private World world;
	private PredatorLocation pl;
	private Material material;
	private int eventTime, finishTime, lobbyTime, minPlayers, materialAmount, materialRemaining, pointsMaterial, pointsKill;
	private BukkitTask startTask;
	private boolean running = false;
	private HashMap<Player, Integer> playerMaterial = new HashMap<Player, Integer>();
	private HashMap<Player, Integer> playerKills = new HashMap<Player, Integer>();
	
	private static final ItemStack DIAMOND_SWORD_1 = new ItemStack(Material.DIAMOND_SWORD, 1);
	private static final ItemStack DIAMOND_PICKAXE_1 = new ItemStack(Material.DIAMOND_PICKAXE, 1);
	
	/**
	 * Constructor.
	 * @param plugin
	 */
	public PredatorManagement(Predator plugin) {
		this.plugin = plugin;
		this.world = this.plugin.getServer().getWorld(Defaults.GAME_WORLD);
		this.pl = new PredatorLocation(this.plugin);
		this.material = Defaults.MATERIAL;
		this.eventTime = Defaults.EVENT_TIME;
		this.finishTime = Defaults.FINISH_TIME;
		this.lobbyTime = Defaults.LOBBY_TIME;
		this.minPlayers = Defaults.MIN_PLAYERS;
		this.materialAmount = Defaults.MATERIAL_AMOUNT;
		this.pointsMaterial = Defaults.POINTS_EGG;
		this.pointsKill = Defaults.POINTS_KILL;
	}
	
	/**
	 * Create board for joining player.
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		
		System.out.println(player);
		System.out.println(this.running);
		System.out.println(this.world);
		System.out.println(player.getWorld());
		
		if (this.running || player.getWorld() != this.world) {
			System.out.println("I shouldn't be here");
			return;
		}
		
		System.out.println("I made it");
		
		if (this.startTask == null) {
			Predator.tp.teleportPlayerToStart(player);
		} else {
			latePlayer(player);
		}
		
		
		int playersInWorld = this.world.getPlayers().size();
		
		System.out.println(playersInWorld);
		System.out.println(this.minPlayers);
				
		if (playersInWorld >= this.minPlayers) {
			this.start();
		}
	}
	
	/**
	 * Create check for material break.
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		Player player;
		
		if (this.startTask == null || block.getWorld() != this.world) {
			return;
		}
		
		if (event.getPlayer() != null) {
			player = event.getPlayer();
		} else {
			return;
		}
		
		// Give block breaker a block point.
		if (block.getType() == this.material) {
			this.playerMaterial.put(player, this.playerMaterial.get(player) + 1);
			this.materialRemaining--;
		}
		
		// Update scoreboard.
        for (Player participants : world.getPlayers()) {
        	setPlayerBoard(participants);
		}
	}
	
	/**
	 * Check for player death.
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityDeath(EntityDeathEvent event) {
        if (this.startTask == null || !(event.getEntity() instanceof Player)) {
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
        
        setupPlayer(player);
        
        // Update scoreboard.
        for (Player participants : world.getPlayers()) {
        	setPlayerBoard(participants);
		}
    }
	
	/**
	 * Check for player respawn.
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerRespawn(final PlayerRespawnEvent event) {
        if (!this.running) {
            return;
        }
        
        // Respawn in game after death.
		new BukkitRunnable() {
        	
			public void run() {
				pl.spawnPlayer(event.getPlayer());
			}
			
		}.runTaskLater(this.plugin, 1);
    }
	
	/**
	 * Display start timer.
	 * @param player
	 * @param cooldownTime
	 */
	public void startTimer(int timer) {
		if (timer <= 0) {
			cleanHashMap(playerMaterial);
			cleanHashMap(playerKills);
			pl.spawnMaterial(material, materialAmount);
			this.materialRemaining = this.materialAmount;
			sendMassMessage(world.getPlayers(), Defaults.GAME_TAG + " Game on!");
				
			for (Player player : world.getPlayers()) {
				setupPlayer(player);
			}
			
			finish();
			return;
		}
		
		if (timer <= 5) {
			sendMassMessage(world.getPlayers(), Defaults.GAME_TAG + " " + ChatColor.GREEN + timer);
		}
				
		// Decrement timer.
		final int newTimer = --timer;
		
		// Create the task anonymously to decrement timer.
		this.startTask =  new BukkitRunnable() {
		      
			public void run() {
				startTimer(newTimer);
			}
				
		}.runTaskLater(this.plugin, 20);
	}
	
	/**
	 * Start the event.
	 */
	public void start() {
		this.running = true;
		this.pl.removeAll(material);
		this.sendMassMessage(this.world.getPlayers(), Defaults.GAME_TAG + " Game will start in " + this.lobbyTime + " seconds!");		
		this.startTimer(this.lobbyTime);
	}
	
	/**
	 * Display start timer.
	 * @param player
	 * @param cooldownTime
	 */
	public void finishTimer(int timer) {
		if (timer <= 0) {
			sendMassMessage(world.getPlayers(), Defaults.GAME_TAG + " Game is over! Thanks for playing!");
			
			// Show everyone their score
			for (Player player : world.getPlayers()) {
				player.sendMessage(Defaults.GAME_TAG + " Your score is " + ChatColor.GREEN + getPlayerScore(player) + "!");
			}
			
			clean();
			return;
		}
		
		if (timer == 10) {
			sendMassMessage(world.getPlayers(), Defaults.GAME_TAG + " Game ends in " + ChatColor.GREEN + timer + " seconds!");
		}
		
		if (timer <= 5) {
			sendMassMessage(world.getPlayers(), Defaults.GAME_TAG + " " + ChatColor.GREEN + timer);
		}
				
		// Decrement timer.
		final int newTimer = --timer;
		
		// Create the task anonymously to decrement timer.
		new BukkitRunnable() {
		      
			public void run() {
				finishTimer(newTimer);
			}
				
		}.runTaskLater(this.plugin, 20);
	}
	
	/**
	 * Finish event after evenTime and name winners.
	 */
	public void finish() {
		this.finishTimer(this.eventTime);
	}
	
	/**
	 * Clean up for next event.
	 */
	public void clean() {
		new BukkitRunnable() {
        	
			public void run() {
				sendMassMessage(world.getPlayers(), Defaults.GAME_TAG + " You are being teleported back to the hub...!");
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
	 * Setup the player when he joins the game. Give full health, give items, set scoreboard.
	 */
	public void setupPlayer(Player player) {
		player.setHealth((double) player.getMaxHealth());
		player.getInventory().clear();
		player.getInventory().addItem(DIAMOND_SWORD_1);
		player.getInventory().addItem(DIAMOND_PICKAXE_1);
		this.pl.spawnPlayer(player);
		setPlayerBoard(player);
	}
	
	/**
	 * Initialize maps of player.
	 * @param map
	 */
	public void latePlayer(Player player) {
		if (this.playerMaterial.get(player) == null) {
			this.playerMaterial.put(player, 0);
		}
		
		if (this.playerKills.get(player) == null) {
			this.playerKills.put(player, 0);
		}
		
		setupPlayer(player);
	}
	
	/**
	 * Send message to list of players
	 */
	public void sendMassMessage(List<Player> players, String msg) {
		for (Player player : players) {
			player.sendMessage(msg);
		}
	}
	
	/**
	 * Create Scoreboard for player.
	 */
	public void setPlayerBoard(Player player) {
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective objective = board.registerNewObjective("timers", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName("Eggs " + this.materialRemaining + "/" + this.materialAmount);
		
		for (Player participant : this.world.getPlayers()) {
			objective.getScore(participant.getName().toString()).setScore(getPlayerScore(participant));
		}
		
		player.setScoreboard(board);
	}
	
	/**
	 * Return current player score. If no score exists return 0.
	 * @param player
	 * @return
	 */
	public int getPlayerScore(Player player) {
		if (this.playerMaterial.get(player) != null && this.playerKills.get(player) != null) {
			return (this.playerMaterial.get(player) * this.pointsMaterial) + (this.playerKills.get(player) * this.pointsKill);
		} else {
			return 0;
		}
	}
}