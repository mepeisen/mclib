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

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.McListener;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.util.function.McConsumer;

/**
 * A zone/ cuboid component.
 * 
 * @author mepeisen
 *
 */
public interface ZoneInterface
{
    
    /**
     * Returns the unique id of this zone.
     * 
     * @return zone id.
     */
    ZoneIdInterface getZoneId();
    
    /**
     * Returns the type id.
     * @return type id of this object
     */
    ZoneTypeId getTypeId();
    
    /**
     * Returns the cuboid.
     * 
     * @return cuboid of this component.
     */
    Cuboid getCuboid();
    
    /**
     * Sets the cuboid
     * 
     * @param cub
     *            cuboid of the component.
     * @throws McException
     *             thrown if the location cannot be changed.
     */
    void setCuboid(Cuboid cub) throws McException;
    
    /**
     * Determines whether the this cuboid contains the passed location.
     * 
     * @param loc
     *            the location to check
     * @return true if the location is within this cuboid, otherwise false
     */
    boolean containsLoc(final Location loc);
    
    /**
     * Determines whether the this cuboid contains the passed location without y coord.
     * 
     * @param loc
     *            the location to check
     * @return true if the location is within this cuboid without y coord, otherwise false
     */
    boolean containsLocWithoutY(final Location loc);
    
    /**
     * Determines whether the this cuboid contains the passed location without y coord and by including the 2 blocks beyond the location.
     * 
     * @param loc
     *            the location to check
     * @return true if the location is within this cuboid without y coord, otherwise false
     */
    boolean containsLocWithoutYD(final Location loc);
    
    /**
     * Deletes this zone.
     * 
     * @throws McException
     *             thrown if the zone cannot be deleted.
     */
    void delete() throws McException;
    
    /**
     * Saves the configuration after handler changed data.
     * 
     * @throws McException
     *             thrown if the config cannot be saved
     */
    void saveConfig() throws McException;
    
    /**
     * Returns the handler.
     * @return associated handler.
     */
    ZoneHandlerInterface getHandler();
    
    /**
     * Returns the child zones.
     * 
     * @return child zones
     */
    Collection<ZoneInterface> getChildZones();
    
    /**
     * Returns the child zones.
     * 
     * @param type
     *            type filter
     * @return child zones
     */
    Collection<ZoneInterface> getChildZones(ZoneTypeId... type);
    
    /**
     * Returns the parent zones.
     * 
     * @return parent zones
     */
    Collection<ZoneInterface> getParentZones();
    
    /**
     * Returns the parent zones.
     * 
     * @param type
     *            type filter
     * @return child zones
     */
    Collection<ZoneInterface> getParentZones(ZoneTypeId... type);
    
    /**
     * Returns the matching zones.
     * 
     * @return matching zones
     */
    Collection<ZoneInterface> getMatchingZones();
    
    /**
     * Returns the matching zones.
     * 
     * @param type
     *            type filter
     * @return child zones
     */
    Collection<ZoneInterface> getMatchingZones(ZoneTypeId... type);
    
    /**
     * Returns the overlapping zones.
     * 
     * @return overlapping zones
     */
    Collection<ZoneInterface> getOverlappingZones();
    
    /**
     * Returns the overlapping zones.
     * 
     * @param type
     *            type filter
     * @return child zones
     */
    Collection<ZoneInterface> getOverlappingZones(ZoneTypeId... type);
    
    // event system
    
    /**
     * Register zone related event handlers only active if this zone is involved in events.
     * @param plugin
     * @param clazz
     * @param handler
     */
    <Evt extends MinecraftEvent<?, Evt>> void registerHandler(Plugin plugin, Class<Evt> clazz, McConsumer<Evt> handler);
    
    /**
     * Registers an event handler object for events on this zone. Methods tagged with McEventHandler are considered as event handlers.
     * @param plugin
     * @param listener
     */
    void registerHandlers(Plugin plugin, McListener listener);
    
    /**
     * Remove a registered event handler.
     * @param plugin
     * @param clazz
     * @param handler
     */
    <Evt extends MinecraftEvent<?, Evt>> void unregisterHandler(Plugin plugin, Class<Evt> clazz, McConsumer<Evt> handler);
    
    /**
     * Remove a registered event handler.
     * @param plugin
     * @param listener
     */
    void unregisterHandlers(Plugin plugin, McListener listener);
    
}
