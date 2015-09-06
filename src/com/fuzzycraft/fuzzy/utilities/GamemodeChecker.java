package com.fuzzycraft.fuzzy.utilities;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import com.fuzzycraft.fuzzy.Predator;

public class GamemodeChecker implements Listener {

    private World world;
    
    /**
     * Constructor.
     * @param plugin
     */
    public GamemodeChecker() {
        this.world = Predator.spawnWorld;
    }
    
    /**
     * Create board for joining player.
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();        
        
        if (player.getWorld() == this.world) {
            player.setGameMode(GameMode.SURVIVAL);
        }
    }
}
