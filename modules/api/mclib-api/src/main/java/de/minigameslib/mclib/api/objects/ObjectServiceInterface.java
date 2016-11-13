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

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import de.minigameslib.mclib.api.McException;

/**
 * A service to register enumerations with plugins.
 * 
 * @author mepeisen
 */
public interface ObjectServiceInterface
{
    
    /**
     * Returns the enumeration services instance.
     * 
     * @return enumeration services instance.
     */
    static ObjectServiceInterface instance()
    {
        return ObjectServiceCache.get();
    }
    
    // player api
    
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
     * Finds a component by block.
     * 
     * @param block
     *            bukkit block
     * 
     * @return Component or {@code null} if no component was found.
     */
    ComponentInterface findComponent(Block block);
    
    /**
     * Finds component by id.
     * 
     * @param id
     *            the component id to search for
     * @return Component or {@code null} if no component was found.
     */
    ComponentInterface findComponent(ComponentIdInterface id);
    
    /**
     * Creates a new component with given handler.
     * 
     * @param type
     *            the type enumeration value
     * @param location
     *            the initial location of the component.
     * @param handler
     *            handler
     * @return created component
     * @throws McException
     *             thrown if the component could not be created
     */
    ComponentInterface createComponent(ComponentTypeId type, Location location, ComponentHandlerInterface handler) throws McException;
    
    // entity api
    
    /**
     * Finds aa entity by bukkit entity.
     * 
     * @param entity
     *            bukkit entity
     * 
     * @return Entity or {@code null} if no entity was found.
     */
    EntityInterface findEntity(Entity entity);
    
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
     *            handler
     * @return created entity
     * @throws McException
     *             thrown if the entity could not be created
     */
    EntityInterface createEntity(EntityTypeId type, Entity entity, EntityHandlerInterface handler) throws McException;
    
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
     * Finds a sign by block.
     * 
     * @param block
     *            bukkit block
     * 
     * @return Block or {@code null} if no sign was found.
     */
    SignInterface findSign(Block block);
    
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
     *            handler
     * @return created sign
     * @throws McException
     *             thrown if the sign could not be created
     */
    SignInterface createSign(SignTypeId type, Sign sign, SignHandlerInterface handler) throws McException;
    
    // zone api
    
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
    Iterable<ZoneInterface> findZones(Location location);
    
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
    Iterable<ZoneInterface> findZonesWithoutY(Location location);
    
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
    Iterable<ZoneInterface> findZonesWithoutYD(Location location);
    
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
     *            handler
     * @return created zone
     * @throws McException
     *             thrown if the zone could not be created
     */
    ZoneInterface createZone(ZoneTypeId type, Cuboid cuboid, ZoneHandlerInterface handler) throws McException;
    
}