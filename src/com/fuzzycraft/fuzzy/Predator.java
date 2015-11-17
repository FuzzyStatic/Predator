package com.fuzzycraft.fuzzy;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.fuzzycraft.fuzzy.classes.BugRepellent;
import com.fuzzycraft.fuzzy.classes.Default;
import com.fuzzycraft.fuzzy.classes.Hunter;
import com.fuzzycraft.fuzzy.classes.Vulture;
import com.fuzzycraft.fuzzy.commands.Quit;
import com.fuzzycraft.fuzzy.npcs.CustomEntityPigZombie;
import com.fuzzycraft.fuzzy.npcs.CustomEntityType;
import com.fuzzycraft.fuzzy.utilities.GameModeChecker;

/**
 * 
 * @author FuzzyStatic (fuzzy@fuzzycraft.com)
 *
 */

public class Predator extends JavaPlugin {
	
	public static final String[] GAME_WORLDS = {"predator-1", "predator-2", "predator-3", "predator-4"};
	public static final String SPAWN_WORLD = "world";
    public static final int SPAWN_X = 0;
    public static final int SPAWN_Y = 65;
    public static final int SPAWN_Z = 0;
    public static final float SPAWN_YAW = (float) -179.4;
    public static final float SPAWN_PITCH = (float) -1.2;
    
    public static final ItemStack FEATHER = new ItemStack(Material.FEATHER, 1);
    public static final ItemStack COMPASS = new ItemStack(Material.COMPASS, 1);
    public static final ItemStack POTION = new ItemStack(Material.POTION, 1);
    
    public static World spawnWorld;
    public static Location spawnLoc;
	
	private Predator plugin = this;
	private int npcs[];
	
	public void onEnable() {
	    CustomEntityType.registerEntities();
	    
		new BukkitRunnable() {
        	
			public void run() {
			    // Set spawn world and spawn location.
			    spawnWorld = getServer().getWorld(SPAWN_WORLD);
				spawnLoc = new Location(spawnWorld, 
		                SPAWN_X, 
		                SPAWN_Y, 
		                SPAWN_Z,
		                SPAWN_YAW,
		                SPAWN_PITCH);
				
				createNPCs(spawnWorld);
												
				PluginManager manager = getServer().getPluginManager();
				
				for (String world : GAME_WORLDS) {
					World gameWorld = getServer().getWorld(world);
					PredatorManagement pm = new PredatorManagement(plugin, gameWorld);
					manager.registerEvents(pm, plugin);
				}
				
				registerEventListeners();
			}
			
		}.runTaskLater(this, 1);
		
		getCommand(Quit.CMD).setExecutor(new Quit());
	}
	
	public void onDisable() {
	    CustomEntityType.unregisterEntities();
	}
	
	/**
	 * Register event listeners.
	 */
	public void registerEventListeners() {
	    PluginManager manager = getServer().getPluginManager();
	    manager.registerEvents(new GameModeChecker(), plugin);
        manager.registerEvents(new Default(plugin), plugin);
        manager.registerEvents(new Vulture(plugin), plugin);
        manager.registerEvents(new BugRepellent(plugin), plugin);
        manager.registerEvents(new Hunter(plugin), plugin);
	}
	
	/**
	 * Create class NPCs through custom entity classes and NMS.
	 * @param world
	 */
	public void createNPCs(World world) {
	    
        net.minecraft.server.v1_8_R3.World nmsWorld = ((CraftWorld) world).getHandle();

        CustomEntityPigZombie defaultNPC = new CustomEntityPigZombie(nmsWorld);
        //defaultNPC.setCustomName(Default.NAME);
        //defaultNPC.setCustomNameVisible(false);
        defaultNPC.setLocation(Default.X, Default.Y, Default.Z, Default.YAW, Default.PITCH);
        // Fix for issue with yaw and pitch.
        defaultNPC.teleportTo(new Location(world, Default.X, 
                Default.Y, 
                Default.Z, 
                Default.YAW, 
                Default.PITCH), 
                false);
        nmsWorld.addEntity(defaultNPC);
        
        CustomEntityPigZombie vultureNPC = new CustomEntityPigZombie(nmsWorld);
        //vultureNPC.setCustomName(Vulture.NAME);
        //vultureNPC.setCustomNameVisible(false);
        vultureNPC.setEquipment(0, CraftItemStack.asNMSCopy(FEATHER));
        vultureNPC.setLocation(Vulture.X, Vulture.Y, Vulture.Z, Vulture.YAW, Vulture.PITCH);
        // Fix for issue with yaw and pitch.
        vultureNPC.teleportTo(new Location(world, Vulture.X, 
                Vulture.Y, 
                Vulture.Z, 
                Vulture.YAW, 
                Vulture.PITCH), 
                false);
        nmsWorld.addEntity(vultureNPC);
        
        CustomEntityPigZombie bugRepellentNPC = new CustomEntityPigZombie(nmsWorld);
        //bugRepellentNPC.setCustomName(BugRepellent.NAME);
        //bugRepellentNPC.setCustomNameVisible(false);
        bugRepellentNPC.setEquipment(0, CraftItemStack.asNMSCopy(COMPASS));
        bugRepellentNPC.setLocation(BugRepellent.X, BugRepellent.Y, BugRepellent.Z, BugRepellent.YAW, BugRepellent.PITCH);
        // Fix for issue with yaw and pitch.
        bugRepellentNPC.teleportTo(new Location(world, BugRepellent.X, 
                BugRepellent.Y, 
                BugRepellent.Z, 
                BugRepellent.YAW, 
                BugRepellent.PITCH), 
                false);
        nmsWorld.addEntity(bugRepellentNPC);
        
        CustomEntityPigZombie hunterNPC = new CustomEntityPigZombie(nmsWorld);
        //hunterNPC.setCustomName(Hunter.NAME);
        //hunterNPC.setCustomNameVisible(false);
        hunterNPC.setEquipment(0, CraftItemStack.asNMSCopy(POTION));
        hunterNPC.setLocation(Hunter.X, Hunter.Y, Hunter.Z, Hunter.YAW, Hunter.PITCH);
        // Fix for issue with yaw and pitch.
        hunterNPC.teleportTo(new Location(world, Hunter.X, 
                Hunter.Y, 
                Hunter.Z, 
                Hunter.YAW, 
                Hunter.PITCH), 
                false);
        nmsWorld.addEntity(hunterNPC);
        
        // 0 = default, 1 = vulture, 2 = bug repellent, 3 = hunter. Probably a better way to do this but this is quick.
        this.npcs = new int[] { defaultNPC.getId(), vultureNPC.getId(), bugRepellentNPC.getId(), hunterNPC.getId() };
	}
	
	public int[] getNpcIds() {
	    return this.npcs;
	}
}
