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

import org.bukkit.Color;
import org.junit.Test;

import de.minigameslib.mclib.api.config.ConfigColorData;

/**
 * Tests {@link ConfigColorData}.
 * 
 * @author mepeisen
 *
 */
public class ConfigColorDataTest
{
    
    /**
     * test the constructors.
     */
    @Test
    public void testConstructors()
    {
        final ConfigColorData data1 = new ConfigColorData(255, 2, 3);
        final ConfigColorData data2 = new ConfigColorData((byte) -1, (byte) 2, (byte) 3);
        assertTrue(data1.equals(data2));
        
        assertEquals(255, data1.getRedAsInt());
        assertEquals(2, data1.getGreenAsInt());
        assertEquals(3, data1.getBlueAsInt());
        
        assertEquals((byte) -1, data1.getRed());
        assertEquals((byte) 2, data1.getGreen());
        assertEquals((byte) 3, data1.getBlue());
        
        assertEquals(255, data2.getRedAsInt());
        assertEquals(2, data2.getGreenAsInt());
        assertEquals(3, data2.getBlueAsInt());
        
        assertEquals((byte) -1, data2.getRed());
        assertEquals((byte) 2, data2.getGreen());
        assertEquals((byte) 3, data2.getBlue());
    }
    
    /**
     * test the conversion methods.
     */
    @Test
    public void testConversion()
    {
        final ConfigColorData data1 = ConfigColorData.fromBukkitColor(Color.AQUA);
        final ConfigColorData data2 = new ConfigColorData(0, 255, 255);
        assertTrue(data1.equals(data2));
        
        assertTrue(data2.toBukkitColor().equals(Color.AQUA));
    }
    
}
