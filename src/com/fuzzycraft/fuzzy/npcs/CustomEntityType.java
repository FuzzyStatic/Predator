package com.fuzzycraft.fuzzy.npcs;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.EntityType;

import net.minecraft.server.v1_8_R3.BiomeBase;
import net.minecraft.server.v1_8_R3.BiomeBase.BiomeMeta;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityPigZombie;
import net.minecraft.server.v1_8_R3.EntityTypes;

/**
 * 
 * @author FuzzyStatic (fuzzy@fuzzycraft.com)
 *
 */

public enum CustomEntityType {
    
    PIG_ZOMBIE("PigZombie", 57, EntityType.PIG_ZOMBIE, EntityPigZombie.class, CustomEntityPigZombie.class);
 
    private String name;
    private int id;
    private EntityType entityType;
    private Class<? extends EntityInsentient> nmsClass;
    private Class<? extends EntityInsentient> customClass;
 
    private CustomEntityType(String name, int id, EntityType entityType, Class<? extends EntityInsentient> nmsClass, Class<? extends EntityInsentient> customClass) {
        this.name = name;
        this.id = id;
        this.entityType = entityType;
        this.nmsClass = nmsClass;
        this.customClass = customClass;
    }
 
    public String getName() {
        return this.name;
    }
 
    public int getID() {
        return this.id;
    }
 
    public EntityType getEntityType() {
        return this.entityType;
    }
 
    public Class<? extends EntityInsentient> getNMSClass() {
        return this.nmsClass;
    }
 
    public Class<? extends EntityInsentient> getCustomClass() {
        return this.customClass;
    }
    
    /**
    * Register our entities.
    */
    public static void registerEntities() {
        for (CustomEntityType entity : values()) {
            a(entity.getCustomClass(), entity.getName(), entity.getID());
        }
        
        // BiomeBase#biomes became private.
        BiomeBase[] biomes;
        try {
            biomes = (BiomeBase[]) getPrivateStatic(BiomeBase.class, "biomes");
        } catch (Exception exc) {
            // Unable to fetch.
            return;
        }
        for (BiomeBase biomeBase : biomes) {
            if (biomeBase == null)
                break;
     
            // This changed names from J, K, L and M.
            for (String field : new String[] { "at", "au", "av", "aw" }) {
                try {
                    Field list = BiomeBase.class.getDeclaredField(field);
                    list.setAccessible(true);
                    @SuppressWarnings("unchecked")
                    List<BiomeMeta> mobList = (List<BiomeMeta>) list.get(biomeBase);
     
                    // Write in our custom class.
                    for (BiomeMeta meta : mobList) {
                        for (CustomEntityType entity : values()) {
                            if (entity.getNMSClass().equals(meta.b)) {
                                meta.b = entity.getCustomClass();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
     
    /**
    * Unregister our entities to prevent memory leaks. Call on disable.
    */
    @SuppressWarnings("rawtypes")
    public static void unregisterEntities() {
        for (CustomEntityType entity : values()) {
            // Remove our class references.
            try {
                ((Map) getPrivateStatic(EntityTypes.class, "d")).remove(entity.getCustomClass());
            } catch (Exception e) {
                e.printStackTrace();
            }
     
            try {
                ((Map) getPrivateStatic(EntityTypes.class, "f")).remove(entity.getCustomClass());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
     
        for (CustomEntityType entity : values())
            try {
                // Unregister each entity by writing the NMS back in place of the custom class.
                a(entity.getNMSClass(), entity.getName(), entity.getID());
            } catch (Exception e) {
                e.printStackTrace();
            }
     
        // Biomes#biomes was made private so use reflection to get it.
        BiomeBase[] biomes;
        try {
            biomes = (BiomeBase[]) getPrivateStatic(BiomeBase.class, "biomes");
        } catch (Exception exc) {
            // Unable to fetch.
            return;
        }
        
        for (BiomeBase biomeBase : biomes) {
            if (biomeBase == null) {
                break;
            }
     
            // The list fields changed names but update the meta regardless.
            for (String field : new String[] { "at", "au", "av", "aw" }) {
                try {
                    Field list = BiomeBase.class.getDeclaredField(field);
                    list.setAccessible(true);
                    @SuppressWarnings("unchecked")
                    List<BiomeMeta> mobList = (List<BiomeMeta>) list.get(biomeBase);
     
                    // Make sure the NMS class is written back over our custom class.
                    for (BiomeMeta meta : mobList) {
                        for (CustomEntityType entity : values()) {
                            if (entity.getCustomClass().equals(meta.b)) {
                                meta.b = entity.getNMSClass();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
     
    /**
    * A convenience method.
    * @param clazz The class.
    * @param f The string representation of the private static field.
    * @return The object found
    * @throws Exception if unable to get the object.
    */
    private static Object getPrivateStatic(@SuppressWarnings("rawtypes") Class c, String f) throws Exception {
        Field field = c.getDeclaredField(f);
        field.setAccessible(true);
        return field.get(null);
    }
     
    /*
    * Since 1.7.2 added a check in their entity registration, simply bypass it and write to the maps ourself.
    */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void a(Class<?> paramClass, String paramString, int paramInt) {
        try {
            ((Map) getPrivateStatic(EntityTypes.class, "c")).put(paramString, paramClass);
            ((Map) getPrivateStatic(EntityTypes.class, "d")).put(paramClass, paramString);
            ((Map) getPrivateStatic(EntityTypes.class, "e")).put(Integer.valueOf(paramInt), paramClass);
            ((Map) getPrivateStatic(EntityTypes.class, "f")).put(paramClass, Integer.valueOf(paramInt));
            ((Map) getPrivateStatic(EntityTypes.class, "g")).put(paramString, Integer.valueOf(paramInt));
        } catch (Exception exc) {
            // Unable to register the new class.
        }
    }
}