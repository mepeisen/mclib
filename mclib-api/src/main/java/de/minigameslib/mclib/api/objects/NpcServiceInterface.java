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

package de.minigameslib.mclib.api.objects;

import org.bukkit.Location;
import org.bukkit.entity.Villager;

import de.minigameslib.mclib.api.McException;

/**
 * Interface to support npc spawning.
 * 
 * @author mepeisen
 */
public interface NpcServiceInterface
{
    
    /**
     * Returns the npc services instance.
     * 
     * @return npc services instance.
     */
    static NpcServiceInterface instance()
    {
        return NpcServiceCache.get();
    }
    
    /**
     * Creates a npc builder to spawn a npc.
     * 
     * @return npc builder.
     */
    VillagerBuilderInterface villager();
    
    /**
     * Creates a player builder to spawn a human.
     * 
     * @return human builder.
     */
    HumanBuilderInterface human();
    
    /**
     * Interface to help creating npcs.
     */
    public interface VillagerBuilderInterface extends NpcBuilderInterface<VillagerBuilderInterface>
    {
        
        /**
         * Sets the given profession.
         * 
         * @param profession
         *            humans profession
         * @return this object for chaining.
         */
        VillagerBuilderInterface profession(Villager.Profession profession);
        
    }
    
    /**
     * Interface to help creating humans.
     */
    public interface HumanBuilderInterface extends NpcBuilderInterface<HumanBuilderInterface>
    {
        
        /**
         * Uses the skin from given player.
         * 
         * @param player
         *            player to take the skin from.
         * @return this object for chaining.
         */
        HumanBuilderInterface skin(McPlayerInterface player);
        
        /**
         * Sets Display name.
         * 
         * @param name
         *            the display name
         * @return this object for chaining
         */
        HumanBuilderInterface name(String name);
        // TODO set to serializable to support localized names
        
    }
    
    /**
     * Interface to help creating npcs.
     * 
     * @param <T>
     *            builder class
     */
    public interface NpcBuilderInterface<T extends NpcBuilderInterface<T>>
    {
        
        /**
         * Creates a persistent npc.
         * 
         * @return this object for chaining.
         */
        T persistent();
        
        /**
         * Creates the npc entity.
         * 
         * @return npc entity
         * @throws McException
         *             thrown for invalid configurations or creation errors
         */
        EntityInterface create() throws McException;
        
        /**
         * Creates a npc with given type handler.
         * 
         * @param type
         *            entity type
         * @param handler
         *            entity type handler
         * @return this object for chaining.
         */
        T handler(EntityTypeId type, EntityHandlerInterface handler);
        
        /**
         * Sets the npc location/ yaw/ pitch.
         * 
         * @param location the location.
         * @return this object for chaining.
         */
        T location(Location location);
        
    }
    
}
