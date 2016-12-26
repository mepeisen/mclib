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

package de.minigameslib.mclib.api.config;

import org.bukkit.util.Vector;

import de.minigameslib.mclib.shared.api.com.VectorData;

/**
 * Vector object for storing and reloading from config.
 * 
 * @author mepeisen
 */
public class ConfigVectorData extends VectorData
{
    
    /**
     * @param x
     * @param y
     * @param z
     */
    public ConfigVectorData(double x, double y, double z)
    {
        super(x, y, z);
    }

    /**
     * Constructor
     */
    public ConfigVectorData()
    {
        super();
    }

    /**
     * Converts this vector to a bukkit vector.
     * @return bukkit vector.
     */
    public Vector toBukkitVector()
    {
        return new Vector(this.getX(), this.getY(), this.getZ());
    }
    
    /**
     * Converts bukkit vector to config vector.
     * @param bukkitVector
     * @return config vector
     */
    public static ConfigVectorData fromBukkitVector(Vector bukkitVector)
    {
        return new ConfigVectorData(bukkitVector.getX(), bukkitVector.getY(), bukkitVector.getZ());
    }
    
}
