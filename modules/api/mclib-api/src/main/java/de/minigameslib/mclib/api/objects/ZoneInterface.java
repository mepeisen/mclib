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

import de.minigameslib.mclib.api.McException;

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
     * @return zone id.
     */
    ZoneIdInterface getZoneId();

    /**
     * Returns the cuboid.
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
     */
    void saveConfig() throws McException;
    
}
