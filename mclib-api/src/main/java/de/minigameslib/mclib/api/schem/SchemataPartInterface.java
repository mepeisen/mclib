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

package de.minigameslib.mclib.api.schem;

import org.bukkit.Location;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.util.function.McRunnable;

/**
 * A schemata part being a single cuboid.
 * 
 * @author mepeisen
 */
public interface SchemataPartInterface
{
    
    /**
     * Returns the name of the part.
     * 
     * @return part name.
     */
    String getPartName();
    
    /**
     * Returns the x size of the part.
     * 
     * @return x size in blocks.
     */
    int getXSize();
    
    /**
     * Returns the y size of the part.
     * 
     * @return y size in blocks.
     */
    int getYSize();
    
    /**
     * Returns the z size of the part.
     * 
     * @return z size in blocks.
     */
    int getZSize();
    
    /**
     * Applies this schemata part to world (asynchronous).
     * 
     * @param location
     *            target location
     * @param success
     *            function to invoke on success
     * @param failure
     *            function to invoke on failures
     * @throws McException
     *             thrown if there are locations missing or if the schemata is not valid
     */
    void applyToWorld(Location location, McRunnable success, McRunnable failure) throws McException;
    
}
