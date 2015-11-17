package com.fuzzycraft.fuzzy.classes;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.fuzzycraft.fuzzy.Predator;

/**
 * 
 * @author FuzzyStatic (fuzzy@fuzzycraft.com)
 *
 */

public class Default implements Listener {

    public static final String NAME = "Default";
    public static final double X = -2.5;
    public static final double Y = 67.5;
    public static final double Z = -2.5;
    public static final float YAW = -45;
    public static final float PITCH = 20;
    
    private Predator plugin;

    public Default(Predator plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Give player hunter equipment.
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        if (event.getRightClicked().equals(this.plugin.getNpcIds()[0])) {
            player.getInventory().clear();
        }
    }
}
