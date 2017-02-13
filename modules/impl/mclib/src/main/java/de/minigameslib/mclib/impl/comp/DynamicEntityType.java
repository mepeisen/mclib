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

package de.minigameslib.mclib.impl.comp;

import java.util.function.BiConsumer;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;

import de.minigameslib.mclib.nms.api.EntityHelperInterface;
import de.minigameslib.mclib.nms.api.NmsFactory;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.LocationData;
import de.minigameslib.mclib.shared.api.com.LocationDataFragment;

/**
 * Type of entities created on demand
 * 
 * @author mepeisen
 */
public enum DynamicEntityType
{

    /** a dummy villager. */
    DUMMY_VILLAGER(DummyVillager::onResume, DummyVillager::onStore),

    /** a dummy human. */
    DUMMY_HUMAN(DummyHuman::onResume, DummyHuman::onStore)
    ;
    
    /** resume function. */
    private final Function<DataSection, Entity> resume;
    
    /** store function. */
    private final BiConsumer<DataSection, Entity> store;
    
    /**
     * @param resume
     * @param store
     */
    private DynamicEntityType(Function<DataSection, Entity> resume, BiConsumer<DataSection, Entity> store)
    {
        this.resume = resume;
        this.store = store;
    }

    /**
     * Resumes entity from data section
     * @param section
     * @return entity
     */
    public Entity onResume(DataSection section)
    {
        return this.resume.apply(section);
    }
    
    /**
     * Stores data to data section
     * @param section
     * @param entity
     */
    public void onStore(DataSection section, Entity entity)
    {
        this.store.accept(section, entity);
    }
    
    /**
     * dummy villager helper
     */
    private static final class DummyVillager
    {
        
        /**
         * Resumes entity from data section
         * @param section
         * @return entity
         */
        static Entity onResume(DataSection section)
        {
            final Profession profession = section.getEnum(Profession.class, "profession"); //$NON-NLS-1$
            final LocationDataFragment locdf = section.getLocation("location"); //$NON-NLS-1$
            final Location loc = new Location(Bukkit.getWorld(locdf.getWorld()), locdf.getX(), locdf.getY(), locdf.getZ(), locdf.getYaw(), locdf.getPitch());
            final EntityHelperInterface helper = Bukkit.getServicesManager().load(NmsFactory.class).create(EntityHelperInterface.class);
            final Villager bukkitEntity = helper.spawnDummyVillager(loc, profession);
            return bukkitEntity;
        }
        
        /**
         * Stores data to data section
         * @param section
         * @param entity
         */
        static void onStore(DataSection section, Entity entity)
        {
            final Location loc = entity.getLocation();
            final LocationData locdf = new LocationData(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch(), loc.getWorld().getName());
            section.set("location", locdf); //$NON-NLS-1$
            section.set("profession", ((Villager)entity).getProfession()); //$NON-NLS-1$
        }
        
    }
    
    /**
     * dummy player helper
     */
    private static final class DummyHuman
    {
        
        /**
         * Resumes entity from data section
         * @param section
         * @return entity
         */
        static Entity onResume(DataSection section)
        {
            final String skin = section.getString("skin"); //$NON-NLS-1$
            final String name = section.getString("name"); //$NON-NLS-1$
            final LocationDataFragment locdf = section.getLocation("location"); //$NON-NLS-1$
            final Location loc = new Location(Bukkit.getWorld(locdf.getWorld()), locdf.getX(), locdf.getY(), locdf.getZ(), locdf.getYaw(), locdf.getPitch());
            
            final EntityHelperInterface helper = Bukkit.getServicesManager().load(NmsFactory.class).create(EntityHelperInterface.class);
            final HumanEntity bukkitEntity = helper.spawnDummyHuman(loc, name, skin);
            
            return bukkitEntity;
        }
        
        /**
         * Stores data to data section
         * @param section
         * @param entity
         */
        static void onStore(DataSection section, Entity entity)
        {
            final EntityHelperInterface helper = Bukkit.getServicesManager().load(NmsFactory.class).create(EntityHelperInterface.class);
            final String skin = helper.getSkin((HumanEntity) entity);
            if (skin != null)
            {
                section.set("skin", skin); //$NON-NLS-1$
            }
            section.set("name", ((HumanEntity) entity).getName()); //$NON-NLS-1$
            final Location loc = entity.getLocation();
            final LocationData locdf = new LocationData(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch(), loc.getWorld().getName());
            section.set("location", locdf); //$NON-NLS-1$
        }
        
    }
    
}
