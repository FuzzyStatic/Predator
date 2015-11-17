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

public class Vulture implements Listener {

    public static final String NAME = "Vulture";
    public static final double X = 2.5;
    public static final double Y = 67.5;
    public static final double Z = -2.5;
    public static final float YAW = 45;
    public static final float PITCH = 20;
    public static final String PERM = "predator.class.vulture";
    
    private Predator plugin;

    public Vulture(Predator plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Give player vulture equipment.
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        if (event.getRightClicked().equals(this.plugin.getNpcIds()[1]) && player.hasPermission(PERM)) {
            player.setItemInHand(Predator.FEATHER);
        }
    }   
}