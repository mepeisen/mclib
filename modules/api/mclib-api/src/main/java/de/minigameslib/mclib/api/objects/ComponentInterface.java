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
 * A component within arenas.
 * 
 * @author mepeisen
 */
public interface ComponentInterface
{
    
    /**
     * Returns the unique id of this component.
     * 
     * @return component id.
     */
    ComponentIdInterface getComponentId();
    
    /**
     * Returns the component location.
     * 
     * @return component location.
     */
    Location getLocation();
    
    /**
     * Changes the component location
     * 
     * @param location
     * @throws McException
     *             thrown if the location cannot be changed.
     */
    void setLocation(Location location) throws McException;
    
    /**
     * Deletes this component.
     * 
     * @throws McException
     *             thrown if the component cannot be deleted.
     */
    void delete() throws McException;
    
    /**
     * Saves the configuration after handler changed data.
     * 
     * @throws McException
     */
    void saveConfig() throws McException;
    
}
