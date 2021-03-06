/*
    Copyright 2016 by minigameslib.de
    All rights reserved.
    If you do not own a hand-signed commercial license from minigames.de
    you are not allowed to use this software in any way except using
    GPL (see below).

------

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

package de.minigames.mclib.nms.v185.entity;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.entity.EntityType;

import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityTypes;
import net.minecraft.server.v1_8_R3.EntityVillager;

/**
 * A custom entity type.
 * 
 * @author mepeisen
 * @see EntityTypes
 */
public enum CustomEntityType1_8_5
{
    
    /** a dummy villager. */
    DUMMY_VILLAGER("Villager", 120, EntityType.VILLAGER, EntityVillager.class, DummyVillager1_8_5.class); //$NON-NLS-1$
    
    /** string name. */
    private String                            name;
    /** numeric id. */
    private int                               id;
    /** entity type. */
    private EntityType                        entityType;
    /** nms class. */
    private Class<? extends EntityInsentient> nmsClass;
    /** custom class. */
    private Class<? extends EntityInsentient> customClass;
    
    /** logger. */
    private static final Logger               LOGGER = Logger.getLogger(CustomEntityType1_8_5.class.getName());
    
    /**
     * Constructor.
     * 
     * @param name
     *            string name
     * @param id
     *            numeric id
     * @param entityType
     *            entity type
     * @param nmsClass
     *            nms class
     * @param customClass
     *            custom class
     */
    private CustomEntityType1_8_5(String name, int id, EntityType entityType, Class<? extends EntityInsentient> nmsClass, Class<? extends EntityInsentient> customClass)
    {
        this.name = name;
        this.id = id;
        this.entityType = entityType;
        this.nmsClass = nmsClass;
        this.customClass = customClass;
    }
    
    /**
     * Returns the string name.
     * 
     * @return the string name
     */
    public String getName()
    {
        return this.name;
    }
    
    /**
     * Returns the numeric id.
     * 
     * @return the numeric id
     */
    public int getId()
    {
        return this.id;
    }
    
    /**
     * Returns the bukkit entity type.
     * 
     * @return the entity type
     */
    public EntityType getEntityType()
    {
        return this.entityType;
    }
    
    /**
     * Returns the (original) nms class.
     * 
     * @return the nms class
     */
    public Class<? extends EntityInsentient> getNmsClass()
    {
        return this.nmsClass;
    }
    
    /**
     * Returns the custom (replaced) nms class.
     * 
     * @return the custom class
     */
    public Class<? extends EntityInsentient> getCustomClass()
    {
        return this.customClass;
    }
    
    /**
     * Register our entities.
     */
    public static void registerEntities()
    {
        for (CustomEntityType1_8_5 entity : values())
        {
            entity.registerEntity();
        }
    }
    
    /**
     * Register our entities.
     */
    public void registerEntity()
    {
        register(this.getCustomClass(), this.getName(), this.getId());
        
        // // BiomeBase#biomes became private.
        // BiomeBase[] biomes;
        // try
        // {
        // biomes = (BiomeBase[]) getPrivateStatic(BiomeBase.class, "biomes");
        // }
        // catch (Exception exc)
        // {
        // // Unable to fetch.
        // return;
        // }
        // for (BiomeBase biomeBase : biomes)
        // {
        // if (biomeBase == null)
        // break;
        //
        // // This changed names from J, K, L and M.
        // for (String field : new String[] { "as", "at", "au", "av" })
        // try
        // {
        // Field list = BiomeBase.class.getDeclaredField(field);
        // list.setAccessible(true);
        // @SuppressWarnings("unchecked")
        // List<BiomeMeta> mobList = (List<BiomeMeta>) list.get(biomeBase);
        //
        // // Write in our custom class.
        // for (BiomeMeta meta : mobList)
        // for (CustomEntityType1_10_1 entity : values())
        // if (entity.getNMSClass().equals(meta.b))
        // meta.b = entity.getCustomClass();
        // }
        // catch (Exception e)
        // {
        // e.printStackTrace();
        // }
        // }
    }
    
    /**
     * Unregister our entities to prevent memory leaks. Call on disable.
     */
    public static void unregisterEntities()
    {
        for (CustomEntityType1_8_5 entity : values())
        {
            entity.unregisterEntity();
        }
    }
    
    /**
     * Unregister our entities to prevent memory leaks. Call on disable.
     */
    @SuppressWarnings("rawtypes")
    public void unregisterEntity()
    {
        // Remove our class references.
        try
        {
            ((Map) getPrivateStatic(EntityTypes.class, "d")).remove(this.getCustomClass()); //$NON-NLS-1$
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        try
        {
            ((Map) getPrivateStatic(EntityTypes.class, "f")).remove(this.getCustomClass()); //$NON-NLS-1$
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        try
        {
            // Unregister each entity by writing the NMS back in place of the custom class.
            register(this.getNmsClass(), this.getName(), this.getId());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        // // Biomes#biomes was made private so use reflection to get it.
        // BiomeBase[] biomes;
        // try
        // {
        // biomes = (BiomeBase[]) getPrivateStatic(BiomeBase.class, "biomes");
        // }
        // catch (Exception exc)
        // {
        // // Unable to fetch.
        // return;
        // }
        // for (BiomeBase biomeBase : biomes)
        // {
        // if (biomeBase == null)
        // break;
        //
        // // The list fields changed names but update the meta regardless.
        // for (String field : new String[] { "as", "at", "au", "av" })
        // try
        // {
        // Field list = BiomeBase.class.getDeclaredField(field);
        // list.setAccessible(true);
        // @SuppressWarnings("unchecked")
        // List<BiomeMeta> mobList = (List<BiomeMeta>) list.get(biomeBase);
        //
        // // Make sure the NMS class is written back over our custom class.
        // for (BiomeMeta meta : mobList)
        // for (CustomEntityType1_10_1 entity : values())
        // if (entity.getCustomClass().equals(meta.b))
        // meta.b = entity.getNMSClass();
        // }
        // catch (Exception e)
        // {
        // e.printStackTrace();
        // }
        // }
    }
    
    /**
     * A convenience method.
     * 
     * @param clazz
     *            The class.
     * @param f
     *            The string representation of the private static field.
     * @return The object found
     * @throws Exception
     *             if unable to get the object.
     */
    private static Object getPrivateStatic(Class<?> clazz, String f) throws Exception
    {
        Field field = clazz.getDeclaredField(f);
        field.setAccessible(true);
        return field.get(null);
    }
    
    /**
     * Register entity.
     * 
     * @param paramClass
     *            entity type class
     * @param paramString
     *            entity type name
     * @param paramInt
     *            entity type id
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static void register(Class<?> paramClass, String paramString, int paramInt)
    {
        try
        {
            ((Map) getPrivateStatic(EntityTypes.class, "c")).put(paramString, paramClass); //$NON-NLS-1$
            ((Map) getPrivateStatic(EntityTypes.class, "d")).put(paramClass, paramString); //$NON-NLS-1$
            ((Map) getPrivateStatic(EntityTypes.class, "e")).put(Integer.valueOf(paramInt), paramClass); //$NON-NLS-1$
            ((Map) getPrivateStatic(EntityTypes.class, "f")).put(paramClass, Integer.valueOf(paramInt)); //$NON-NLS-1$
            ((Map) getPrivateStatic(EntityTypes.class, "g")).put(paramString, Integer.valueOf(paramInt)); //$NON-NLS-1$
        }
        catch (Exception exc)
        {
            // Unable to register the new class.
            LOGGER.log(Level.WARNING, "unable to register entity " + paramInt + "/" + paramString, exc); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }
}
