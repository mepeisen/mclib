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

import java.util.Collection;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.McException;

/**
 * A service to register objects (signs, zones etc.).
 * 
 * @author mepeisen
 */
public interface ObjectServiceInterface
{
    
    /**
     * Returns the object services instance.
     * 
     * @return object services instance.
     */
    static ObjectServiceInterface instance()
    {
        return ObjectServiceCache.get();
    }
    
    // handler registrations
    
    /**
     * Registers an object handler.
     * 
     * @param type
     * @param handler
     * @throws McException
     */
    <T extends ObjectHandlerInterface> void register(ObjectTypeId type, Class<T> handler) throws McException;
    
    /**
     * Registers a component handler.
     * 
     * @param type
     * @param handler
     * @throws McException
     */
    <T extends ComponentHandlerInterface> void register(ComponentTypeId type, Class<T> handler) throws McException;
    
    /**
     * Registers an entity handler.
     * 
     * @param type
     * @param handler
     * @throws McException
     */
    <T extends EntityHandlerInterface> void register(EntityTypeId type, Class<T> handler) throws McException;
    
    /**
     * Registers a sign handler.
     * 
     * @param type
     * @param handler
     * @throws McException
     */
    <T extends SignHandlerInterface> void register(SignTypeId type, Class<T> handler) throws McException;
    
    /**
     * Registers a zone handler.
     * 
     * @param type
     * @param handler
     * @throws McException
     */
    <T extends ZoneHandlerInterface> void register(ZoneTypeId type, Class<T> handler) throws McException;
    
    /**
     * Returns the component type for given id.
     * @param id
     * @return type or {@code null} if the type is not registered/ unknown
     */
    ComponentTypeId getType(ComponentIdInterface id);
    
    /**
     * Returns the object type for given id.
     * @param id
     * @return type or {@code null} if the type is not registered/ unknown
     */
    ObjectTypeId getType(ObjectIdInterface id);
    
    /**
     * Returns the entity type for given id.
     * @param id
     * @return type or {@code null} if the type is not registered/ unknown
     */
    EntityTypeId getType(EntityIdInterface id);
    
    /**
     * Returns the zone type for given id.
     * @param id
     * @return type or {@code null} if the type is not registered/ unknown
     */
    ZoneTypeId getType(ZoneIdInterface id);
    
    /**
     * Returns the sign type for given id.
     * @param id
     * @return type or {@code null} if the type is not registered/ unknown
     */
    SignTypeId getType(SignIdInterface id);
    
    /**
     * Tries to resume objects.
     * 
     * @param plugin
     * @return report of resuming components.
     */
    ResumeReport resumeObjects(Plugin plugin);
    
    /**
     * The report during resumes.
     */
    interface ResumeReport
    {
        
        /**
         * checks if the object can be loaded successful
         * 
         * @return {@code true} if the objects are loaded successful
         */
        boolean isOk();
        
        /**
         * Returns the list of broken components.
         * 
         * @return broken components
         */
        Iterable<ComponentIdInterface> getBrokenComponents();
        
        /**
         * Returns the list of broken objects.
         * 
         * @return broken objects
         */
        Iterable<ObjectIdInterface> getBrokenObjects();
        
        /**
         * Returns the list of broken entities.
         * 
         * @return broken entities
         */
        Iterable<EntityIdInterface> getBrokenEntities();
        
        /**
         * Returns the list of broken signs.
         * 
         * @return broken signs
         */
        Iterable<SignIdInterface> getBrokenSigns();
        
        /**
         * Returns the list of broken zones.
         * 
         * @return broken zones
         */
        Iterable<ZoneIdInterface> getBrokenZones();
        
        /**
         * Returns the exception that was caught during component loading
         * 
         * @param id
         *            the component id of the broken component
         * @return caught exception
         */
        McException getException(ComponentIdInterface id);
        
        /**
         * Returns the exception that was caught during object loading
         * 
         * @param id
         *            the object id of the broken component
         * @return caught exception
         */
        McException getException(ObjectIdInterface id);
        
        /**
         * Returns the exception that was caught during entity loading
         * 
         * @param id
         *            the entity id of the broken entity
         * @return caught exception
         */
        McException getException(EntityIdInterface id);
        
        /**
         * Returns the exception that was caught during sign loading
         * 
         * @param id
         *            the sign id of the broken sign
         * @return caught exception
         */
        McException getException(SignIdInterface id);
        
        /**
         * Returns the exception that was caught during zone loading
         * 
         * @param id
         *            the zone id of the broken zone
         * @return caught exception
         */
        McException getException(ZoneIdInterface id);
        
    }
    
    // player api
    
    /**
     * Checks if given player is a human
     * @param player
     * @return checks for human npcs
     */
    boolean isHuman(Player player);
    
    /**
     * Returns the player for given bukkit player.
     * 
     * @param player
     *            bukkit online player
     * @return arena player.
     */
    McPlayerInterface getPlayer(Player player);
    
    /**
     * Returns the player for given bukkit player.
     * 
     * @param player
     *            bukkit offline player
     * @return arena player.
     */
    McPlayerInterface getPlayer(OfflinePlayer player);
    
    /**
     * Returns the player for given bukkit player uuid.
     * 
     * @param uuid
     *            player uuid
     * @return arena player.
     */
    McPlayerInterface getPlayer(UUID uuid);
    
    // object api
    
    /**
     * Finds objects by id.
     * 
     * @param id
     *            the object id to search for
     * @return Object or {@code null} if no object was found.
     */
    ObjectInterface findObject(ObjectIdInterface id);
    
    /**
     * Finds objects by type.
     * 
     * @param type
     * @return objects by type.
     */
    Collection<ObjectInterface> findObjects(ObjectTypeId... type);
    
    /**
     * Creates a new object with given handler.
     * 
     * @param type
     *            the type enumeration value
     * @param handler
     *            handler or {@code null} to create a default handler
     * @param persist
     *            {@code true} to persist this object within configuration files and restore during server restart
     * @return created object
     * @throws McException
     *             thrown if the object could not be created
     */
    ObjectInterface createObject(ObjectTypeId type, ObjectHandlerInterface handler, boolean persist) throws McException;
    
    // component api
    
    /**
     * Finds a component by location.
     * 
     * @param location
     *            bukkit location
     * 
     * @return Component or {@code null} if no component was found.
     */
    ComponentInterface findComponent(Location location);
    
    /**
     * Finds components by location.
     * 
     * @param location
     *            bukkit location
     * 
     * @return Components or empty collection if no component was found.
     */
    Collection<ComponentInterface> findComponents(Location location);
    
    /**
     * Finds a component by block.
     * 
     * @param block
     *            bukkit block
     * 
     * @return Component or {@code null} if no component was found.
     */
    ComponentInterface findComponent(Block block);
    
    /**
     * Finds a components by block.
     * 
     * @param block
     *            bukkit block
     * 
     * @return Components or empty collection if no component was found.
     */
    Collection<ComponentInterface> findComponents(Block block);
    
    /**
     * Finds component by id.
     * 
     * @param id
     *            the component id to search for
     * @return Component or {@code null} if no component was found.
     */
    ComponentInterface findComponent(ComponentIdInterface id);
    
    /**
     * Finds components by type.
     * 
     * @param type
     * @return components by type.
     */
    Collection<ComponentInterface> findComponents(ComponentTypeId... type);
    
    /**
     * Creates a new component with given handler.
     * 
     * @param type
     *            the type enumeration value
     * @param location
     *            the initial location of the component.
     * @param handler
     *            handler or {@code null} to create a default handler
     * @param persist
     *            {@code true} to persist this component within configuration files and restore during server restart
     * @return created component
     * @throws McException
     *             thrown if the component could not be created
     */
    ComponentInterface createComponent(ComponentTypeId type, Location location, ComponentHandlerInterface handler, boolean persist) throws McException;
    
    // entity api
    
    /**
     * Finds an entity by bukkit entity.
     * 
     * @param entity
     *            bukkit entity
     * 
     * @return Entity or {@code null} if no entity was found.
     */
    EntityInterface findEntity(Entity entity);
    
    /**
     * Finds entities by bukkit entity.
     * 
     * @param entity
     *            bukkit entity
     * 
     * @return Entity or empty collection if no entity was found.
     */
    Collection<EntityInterface> findEntities(Entity entity);
    
    /**
     * Finds entity by id.
     * 
     * @param id
     *            the entity id to search for
     * @return Entity or {@code null} if no entity was found.
     */
    EntityInterface findEntity(EntityIdInterface id);
    
    /**
     * Creates a new entity with given handler.
     * 
     * @param type
     *            the type enumeration value
     * @param entity
     *            the bukkit entity.
     * @param handler
     *            handler or {@code null} to create a default handler
     * @param persist
     *            {@code true} to persist this component within configuration files and restore during server restart
     * @return created entity
     * @throws McException
     *             thrown if the entity could not be created
     */
    EntityInterface createEntity(EntityTypeId type, Entity entity, EntityHandlerInterface handler, boolean persist) throws McException;
    
    /**
     * Finds entites by type.
     * 
     * @param type
     * @return entities by type
     */
    Collection<EntityInterface> findEntities(EntityTypeId... type);
    
    // sign api
    
    /**
     * Finds a sign by location.
     * 
     * @param location
     *            bukkit location
     * 
     * @return Sign or {@code null} if no sign was found.
     */
    SignInterface findSign(Location location);
    
    /**
     * Finds a signs by location.
     * 
     * @param location
     *            bukkit location
     * 
     * @return Signs or empty collection if no sign was found
     */
    Collection<SignInterface> findSigns(Location location);
    
    /**
     * Finds a sign by block.
     * 
     * @param block
     *            bukkit block
     * 
     * @return Sign or {@code null} if no sign was found.
     */
    SignInterface findSign(Block block);
    
    /**
     * Finds a signs by block.
     * 
     * @param block
     *            bukkit block
     * 
     * @return Signs or empty collection if no sign was found
     */
    Collection<SignInterface> findSigns(Block block);
    
    /**
     * Finds a sign by bukkit sign.
     * 
     * @param sign
     *            bukkit sign
     * 
     * @return Sign or {@code null} if no sign was found.
     */
    SignInterface findSign(Sign sign);
    
    /**
     * Finds a signs by bukkit sign.
     * 
     * @param sign
     *            bukkit sign
     * 
     * @return Signs or empty collection if no sign was found
     */
    Collection<SignInterface> findSigns(Sign sign);
    
    /**
     * Finds sign by id.
     * 
     * @param id
     *            the sign id to search for
     * @return Sign or {@code null} if no sign was found.
     */
    SignInterface findSign(SignIdInterface id);
    
    /**
     * Creates a new sign with given handler.
     * 
     * @param type
     *            the type enumeration value
     * @param sign
     *            the bukkit sign
     * @param handler
     *            handler or {@code null} to create a default handler
     * @param persist
     *            {@code true} to persist this component within configuration files and restore during server restart
     * @return created sign
     * @throws McException
     *             thrown if the sign could not be created
     */
    SignInterface createSign(SignTypeId type, Sign sign, SignHandlerInterface handler, boolean persist) throws McException;
    
    /**
     * Find signs by type.
     * 
     * @param type
     * @return signs by type.
     */
    Collection<SignInterface> findSigns(SignTypeId... type);
    
    // zone api
    
    /**
     * a special mode enumeration for searching zones by cuboid
     * 
     */
    public enum CuboidMode
    {
        /** finds matching components (identical) */
        FindMatching,
        /** finds child components */
        FindChildren,
        /** finds parent components */
        FindParents,
        /** finds overlapping components */
        FindOverlapping
    }
    
    /**
     * Finds a zone by location.
     * 
     * <p>
     * Zones are parts of a minigame arena having bounds. If the given location is inside the bounds (inclusive) this method will return the zone.
     * </p>
     * 
     * <p>
     * The method will return the first zone it finds.
     * </p>
     * 
     * @param location
     *            bukkit location
     * 
     * @return Zone or {@code null} if no zone was found.
     * 
     * @see Cuboid#containsLoc(Location)
     */
    ZoneInterface findZone(Location location);
    
    /**
     * Finds a zone by location.
     * 
     * <p>
     * Zones are parts of a minigame arena having bounds. If the given location is inside the bounds (inclusive) this method will return the zone.
     * </p>
     * 
     * <p>
     * The method will return the first zone it finds.
     * </p>
     * 
     * @param cuboid
     *            cuboid location
     * @param mode
     *            cuboid search mode
     * 
     * @return Zone or {@code null} if no zone was found.
     * 
     * @see Cuboid#containsLoc(Location)
     */
    ZoneInterface findZone(Cuboid cuboid, CuboidMode mode);
    
    /**
     * Finds a zone by cuboid.
     * 
     * <p>
     * Zones are parts of a minigame arena having bounds. If the given location is inside the bounds (inclusive) this method will return the zone.
     * </p>
     * 
     * <p>
     * The method will return the first zone it finds.
     * </p>
     * 
     * @param cuboid
     *            cuboid location
     * @param mode
     *            cuboid search mode
     * @param type
     *            type filter
     * 
     * @return Zone or {@code null} if no zone was found.
     * 
     * @see Cuboid#containsLoc(Location)
     */
    ZoneInterface findZone(Cuboid cuboid, CuboidMode mode, ZoneTypeId... type);
    
    /**
     * Finds a zone by location.
     * 
     * <p>
     * Zones are parts of a minigame arena having bounds. If the given location is inside the bounds (inclusive) this method will return the zone.
     * </p>
     * 
     * <p>
     * The method will return the first zone it finds.
     * </p>
     * 
     * @param location
     *            bukkit location
     * @param type
     *            type filter
     * 
     * @return Zone or {@code null} if no zone was found.
     * 
     * @see Cuboid#containsLoc(Location)
     */
    ZoneInterface findZone(Location location, ZoneTypeId... type);
    
    /**
     * Finds all zones by location.
     * 
     * <p>
     * Zones are parts of a minigame arena having bounds. If the given location is inside the bounds (inclusive) this method will return the zone.
     * </p>
     * 
     * <p>
     * The method will return every zone that contains given location. Even if multiple zones are overlapping.
     * </p>
     * 
     * @param location
     *            bukkit location
     * 
     * @return Zone or {@code null} if no zone was found.
     * 
     * @see Cuboid#containsLoc(Location)
     */
    Collection<ZoneInterface> findZones(Location location);
    
    /**
     * Finds all zones by cuboid.
     * 
     * <p>
     * Zones are parts of a minigame arena having bounds. If the given location is inside the bounds (inclusive) this method will return the zone.
     * </p>
     * 
     * <p>
     * The method will return every zone that contains given location. Even if multiple zones are overlapping.
     * </p>
     * 
     * @param cuboid
     *            cuboid location
     * @param mode
     *            cuboid search mode
     * 
     * @return Zone or {@code null} if no zone was found.
     * 
     * @see Cuboid#containsLoc(Location)
     */
    Collection<ZoneInterface> findZones(Cuboid cuboid, CuboidMode mode);
    
    /**
     * Finds all zones by cuboid.
     * 
     * <p>
     * Zones are parts of a minigame arena having bounds. If the given location is inside the bounds (inclusive) this method will return the zone.
     * </p>
     * 
     * <p>
     * The method will return every zone that contains given location. Even if multiple zones are overlapping.
     * </p>
     * 
     * @param cuboid
     *            cuboid location
     * @param mode
     *            cuboid search mode
     * @param type
     *            type filter
     * 
     * @return Zone or {@code null} if no zone was found.
     * 
     * @see Cuboid#containsLoc(Location)
     */
    Collection<ZoneInterface> findZones(Cuboid cuboid, CuboidMode mode, ZoneTypeId... type);
    
    /**
     * Finds all zones by location.
     * 
     * <p>
     * Zones are parts of a minigame arena having bounds. If the given location is inside the bounds (inclusive) this method will return the zone.
     * </p>
     * 
     * <p>
     * The method will return every zone that contains given location. Even if multiple zones are overlapping.
     * </p>
     * 
     * @param location
     *            bukkit location
     * @param type
     *            type filter
     * 
     * @return Zone or {@code null} if no zone was found.
     * 
     * @see Cuboid#containsLoc(Location)
     */
    Collection<ZoneInterface> findZones(Location location, ZoneTypeId... type);
    
    /**
     * Finds a zone by location.
     * 
     * <p>
     * Zones are parts of a minigame arena having bounds. If the given location is inside the bounds (inclusive) this method will return the zone.
     * </p>
     * 
     * <p>
     * The method will return the first zone it finds.
     * </p>
     * 
     * @param location
     *            bukkit location
     * 
     * @return Zone or {@code null} if no zone was found.
     * 
     * @see Cuboid#containsLocWithoutY(Location)
     */
    ZoneInterface findZoneWithoutY(Location location);
    
    /**
     * Finds a zone by location.
     * 
     * <p>
     * Zones are parts of a minigame arena having bounds. If the given location is inside the bounds (inclusive) this method will return the zone.
     * </p>
     * 
     * <p>
     * The method will return the first zone it finds.
     * </p>
     * 
     * @param location
     *            bukkit location
     * @param type
     *            type filter
     * 
     * @return Zone or {@code null} if no zone was found.
     * 
     * @see Cuboid#containsLocWithoutY(Location)
     */
    ZoneInterface findZoneWithoutY(Location location, ZoneTypeId... type);
    
    /**
     * Finds all zones by location.
     * 
     * <p>
     * Zones are parts of a minigame arena having bounds. If the given location is inside the bounds (inclusive) this method will return the zone.
     * </p>
     * 
     * <p>
     * The method will return every zone that contains given location. Even if multiple zones are overlapping.
     * </p>
     * 
     * @param location
     *            bukkit location
     * 
     * @return Zone or {@code null} if no zone was found.
     * 
     * @see Cuboid#containsLocWithoutY(Location)
     */
    Collection<ZoneInterface> findZonesWithoutY(Location location);
    
    /**
     * Finds all zones by location.
     * 
     * <p>
     * Zones are parts of a minigame arena having bounds. If the given location is inside the bounds (inclusive) this method will return the zone.
     * </p>
     * 
     * <p>
     * The method will return every zone that contains given location. Even if multiple zones are overlapping.
     * </p>
     * 
     * @param location
     *            bukkit location
     * @param type
     *            type filter
     * 
     * @return Zone or {@code null} if no zone was found.
     * 
     * @see Cuboid#containsLocWithoutY(Location)
     */
    Collection<ZoneInterface> findZonesWithoutY(Location location, ZoneTypeId... type);
    
    /**
     * Finds a zone by location.
     * 
     * <p>
     * Zones are parts of a minigame arena having bounds. If the given location is inside the bounds (inclusive) this method will return the zone.
     * </p>
     * 
     * <p>
     * The method will return the first zone it finds.
     * </p>
     * 
     * @param location
     *            bukkit location
     * 
     * @return Zone or {@code null} if no zone was found.
     * 
     * @see Cuboid#containsLocWithoutYD(Location)
     */
    ZoneInterface findZoneWithoutYD(Location location);
    
    /**
     * Finds a zone by location.
     * 
     * <p>
     * Zones are parts of a minigame arena having bounds. If the given location is inside the bounds (inclusive) this method will return the zone.
     * </p>
     * 
     * <p>
     * The method will return the first zone it finds.
     * </p>
     * 
     * @param location
     *            bukkit location
     * @param type
     *            type filter
     * 
     * @return Zone or {@code null} if no zone was found.
     * 
     * @see Cuboid#containsLocWithoutYD(Location)
     */
    ZoneInterface findZoneWithoutYD(Location location, ZoneTypeId... type);
    
    /**
     * Finds all zones by location.
     * 
     * <p>
     * Zones are parts of a minigame arena having bounds. If the given location is inside the bounds (inclusive) this method will return the zone.
     * </p>
     * 
     * <p>
     * The method will return every zone that contains given location. Even if multiple zones are overlapping.
     * </p>
     * 
     * @param location
     *            bukkit location
     * 
     * @return Zone or {@code null} if no zone was found.
     * 
     * @see Cuboid#containsLocWithoutYD(Location)
     */
    Collection<ZoneInterface> findZonesWithoutYD(Location location);
    
    /**
     * Finds all zones by location.
     * 
     * <p>
     * Zones are parts of a minigame arena having bounds. If the given location is inside the bounds (inclusive) this method will return the zone.
     * </p>
     * 
     * <p>
     * The method will return every zone that contains given location. Even if multiple zones are overlapping.
     * </p>
     * 
     * @param location
     *            bukkit location
     * @param type
     *            type filter
     * 
     * @return Zone or {@code null} if no zone was found.
     * 
     * @see Cuboid#containsLocWithoutYD(Location)
     */
    Collection<ZoneInterface> findZonesWithoutYD(Location location, ZoneTypeId... type);
    
    /**
     * Finds zone by id.
     * 
     * @param id
     *            the zone id to search for
     * @return Zone or {@code null} if no zone was found.
     */
    ZoneInterface findZone(ZoneIdInterface id);
    
    /**
     * Creates a new zone with given handler.
     * 
     * @param type
     *            the type enumeration value
     * @param cuboid
     *            the initial location of the zone.
     * @param handler
     *            handler or {@code null} to create a default handler
     * @param persist
     *            {@code true} to persist this component within configuration files and restore during server restart
     * @return created zone
     * @throws McException
     *             thrown if the zone could not be created
     */
    ZoneInterface createZone(ZoneTypeId type, Cuboid cuboid, ZoneHandlerInterface handler, boolean persist) throws McException;
    
    /**
     * Find zones by type
     * 
     * @param type
     * @return zones by type
     */
    Collection<ZoneInterface> findZones(ZoneTypeId... type);
    
}
