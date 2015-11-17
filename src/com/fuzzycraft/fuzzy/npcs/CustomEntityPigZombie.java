package com.fuzzycraft.fuzzy.npcs;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;

import net.minecraft.server.v1_8_R3.DamageSource;
import net.minecraft.server.v1_8_R3.EntityPigZombie;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.World;

/**
 * 
 * @author FuzzyStatic (fuzzy@fuzzycraft.com)
 *
 */

public class CustomEntityPigZombie extends EntityPigZombie {

    public CustomEntityPigZombie(World world) {
        super(world);
        
        // Remove any attacks or movement from entity
        try {
            Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
            bField.setAccessible(true);
            Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
            cField.setAccessible(true);
            bField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
            bField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
            cField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
            cField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        
        // Make his movement speed 0.
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.0D);
        // Make his health to max a double can be.
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(Double.MAX_VALUE);
    }
    
    // Make NPC immovable.
    @Override
    public void move(double d0, double d1, double d2) { }
    
    // Remove sound.
    @Override
    public void makeSound(String s, float f0, float f1) { }
    
    // Override all damage to NPC.
    @Override
    public boolean damageEntity(DamageSource ds, float f) {
        return false;
    }
}