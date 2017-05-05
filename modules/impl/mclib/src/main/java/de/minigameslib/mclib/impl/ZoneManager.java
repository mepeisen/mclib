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

package de.minigameslib.mclib.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Location;

import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.api.objects.ZoneTypeId;
import de.minigameslib.mclib.impl.comp.ZoneId;

/**
 * Helper for managing zones of moveable entities.
 * 
 * @author mepeisen
 */
abstract class ZoneManager
{
    
    // TODO remove zones upon deletion
    // TODO react on zone relocations
    
    /**
     * the current zone the player is within.
     */
    private ZoneId      primaryZone;
    
    /**
     * The previous location (blocks).
     */
    private Location    oldLocation = null;
    
    /**
     * The current zones the entity is within.
     */
    private Set<ZoneId> zones       = new HashSet<>();
    
    /**
     * Registers entity movement.
     * 
     * @param target
     *            new location of the entity
     */
    void registerMovement(Location target)
    {
        final Location newLocation = target == null ? null : new Location(target.getWorld(), target.getBlockX(), target.getBlockY(), target.getBlockZ());
        if (this.oldLocation == null)
        {
            if (newLocation == null)
            {
                // nothing to do
                return;
            }
            // entering zones.
            final Set<ZoneId> newZones = this.getZones(newLocation);
            this.zones = newZones;
            this.oldLocation = newLocation;
            this.fireZonesEntered(newZones);
            return;
        }
        if (newLocation == null)
        {
            final Set<ZoneId> oldZones = this.zones;
            this.zones = new HashSet<>();
            this.oldLocation = newLocation;
            this.fireZonesLeft(oldZones);
            return;
        }
        if (!this.oldLocation.equals(newLocation))
        {
            final Set<ZoneId> enteredZones = this.getZones(newLocation);
            final Set<ZoneId> leftZones = new HashSet<>(this.zones);
            // caluclate left and entered zones
            leftZones.removeAll(enteredZones);
            enteredZones.removeAll(this.zones);
            // calculate new situation
            this.zones.removeAll(leftZones);
            this.zones.addAll(enteredZones);
            this.fireZonesLeft(leftZones);
            this.fireZonesEntered(enteredZones);
        }
        
        if (this.zones.size() == 0)
        {
            this.primaryZone = null;
        }
        else if (!this.zones.contains(this.primaryZone))
        {
            this.primaryZone = this.zones.iterator().next();
        }
    }
    
    /**
     * Method to be implemented for fireing zone entered events.
     * 
     * @param newZones
     *            the zones that were entered by entity
     */
    protected abstract void fireZonesEntered(Set<ZoneId> newZones);
    
    /**
     * Method to be implemented for fireing zone left events.
     * 
     * @param oldZones
     *            the zones that were left by entity
     */
    protected abstract void fireZonesLeft(Set<ZoneId> oldZones);
    
    /**
     * Returns the zones for given location.
     * 
     * @param loc
     *            location
     * @return zone id sets
     */
    private Set<ZoneId> getZones(Location loc)
    {
        final ObjectServiceInterface osi = ObjectServiceInterface.instance();
        return osi.findZones(loc).stream().map(z -> (ZoneId) z.getZoneId()).collect(Collectors.toSet());
    }
    
    /**
     * Returns the zone the player is within.
     * 
     * @return zone list
     */
    Collection<ZoneInterface> getZones()
    {
        final ObjectServiceInterface osi = ObjectServiceInterface.instance();
        return this.zones.stream().map(osi::findZone).collect(Collectors.toList());
    }
    
    /**
     * Returns the zone of given types the player is within.
     * 
     * @param type zone type array.
     * @return zone list
     */
    Collection<ZoneInterface> getZones(ZoneTypeId... type)
    {
        final Set<ZoneTypeId> types = Arrays.stream(type).collect(Collectors.toSet());
        final ObjectServiceInterface osi = ObjectServiceInterface.instance();
        return this.zones.stream().filter(z -> types.contains(osi.getType(z))).map(osi::findZone).collect(Collectors.toList());
    }
    
    /**
     * Returns the zone this player is currently in.
     * 
     * <p>
     * This will always be the last zone the player entered if the player is within multiple zones. If the players leaves this zones the framework will check if he is still inside another zone and
     * will return that zone. If the player is still inside multiple zones when leaving the returned zone will be random.
     * </p>
     * 
     * @return zone or {@code null} if this player is currently not within any zone.
     */
    ZoneInterface getZone()
    {
        return this.primaryZone == null ? null : ObjectServiceInterface.instance().findZone(this.primaryZone);
    }
    
    /**
     * Returns a zone the player is within and that is of given type.
     * 
     * <p>
     * This method returns the "primary zone". If the zones are overlapping this may be a random
     * </p>
     * 
     * @param type zone type array
     * @return the zone or {@code null} if no matching zone was found.
     */
    ZoneInterface getZone(ZoneTypeId... type)
    {
        final ObjectServiceInterface osi = ObjectServiceInterface.instance();
        for (final ZoneTypeId t : type)
        {
            for (final ZoneId id : this.zones)
            {
                if (t.equals(osi.getType(id)))
                {
                    return osi.findZone(id);
                }
            }
        }
        return null;
    }
    
    /**
     * Checks if the player is within given zone.
     * 
     * @param zone zone to test
     * @return {@code true} if player is inside given zone.
     */
    boolean isInsideZone(ZoneInterface zone)
    {
        return this.zones.contains(zone.getZoneId());
    }
    
    /**
     * Checks if the player is inside at least one of the given zones.
     * 
     * @param zone zone to test
     * @return {@code true} if player is at least inside one of the given zones.
     */
    boolean isInsideRandomZone(ZoneInterface... zone)
    {
        for (final ZoneInterface z : zone)
        {
            if (this.isInsideZone(z))
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if the player is inside at least one of the given zones of given type.
     * 
     * @param type zone type array
     * @return {@code true} if player is at least inside one of the given zones.
     */
    boolean isInsideRandomZone(ZoneTypeId... type)
    {
        return getZone(type) != null;
    }
    
    /**
     * Checks if the player is inside every of the given zones.
     * 
     * @param zone zone to test
     * @return {@code true} if player is inside of the given zones.
     */
    boolean isInsideAllZones(ZoneInterface... zone)
    {
        for (final ZoneInterface z : zone)
        {
            if (!this.isInsideZone(z))
            {
                return false;
            }
        }
        return true;
    }
    
}
