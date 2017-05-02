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

package de.minigameslib.mclib.test.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.bukkit.util.Vector;
import org.junit.Test;

import de.minigameslib.mclib.api.config.ConfigVectorData;

/**
 * Tests {@link ConfigVectorData}.
 * 
 * @author mepeisen
 *
 */
public class ConfigVectorDataTest
{
    
    /**
     * test the constructors.
     */
    @Test
    public void testConstructors()
    {
        final ConfigVectorData data1 = new ConfigVectorData(1.0d, 2.5d, 3.7d);
        assertEquals(1.0d, data1.getX(), 0);
        assertEquals(2.5d, data1.getY(), 0);
        assertEquals(3.7d, data1.getZ(), 0);
    }
    
    /**
     * test the conversion methods.
     */
    @Test
    public void testConversion()
    {
        final ConfigVectorData data1 = ConfigVectorData.fromBukkitVector(new Vector(1.0d, 2.5d, 3.7d));
        final ConfigVectorData data2 = new ConfigVectorData(1.0d, 2.5d, 3.7d);
        assertEquals(1.0d, data1.getX(), 0);
        assertEquals(2.5d, data1.getY(), 0);
        assertEquals(3.7d, data1.getZ(), 0);
        
        assertTrue(data2.toBukkitVector().equals(new Vector(1.0d, 2.5d, 3.7d)));
    }
    
}
