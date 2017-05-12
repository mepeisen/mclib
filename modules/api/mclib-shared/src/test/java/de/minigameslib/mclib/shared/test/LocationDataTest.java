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

package de.minigameslib.mclib.shared.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.minigameslib.mclib.shared.api.com.LocationData;

/**
 * Testing {@code LocationData}.
 * 
 * @author mepeisen
 */
public class LocationDataTest
{
    
    /**
     * Simple test case.
     */
    @Test
    public void testEmptyConstructor()
    {
        final LocationData data1 = new LocationData();
        final LocationData data2 = new LocationData();
        assertEquals(data1, data2);
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testConstructor()
    {
        final LocationData data1 = new LocationData(1, 2, 3, 4, 5, "world"); //$NON-NLS-1$
        final LocationData data2 = new LocationData(1, 2, 3, 4, 5, "world"); //$NON-NLS-1$
        assertEquals(data1, data2);
        assertEquals(1, data1.getX(), 0);
        assertEquals(2, data1.getY(), 0);
        assertEquals(3, data1.getZ(), 0);
        assertEquals(4, data1.getYaw(), 0);
        assertEquals(5, data1.getPitch(), 0);
        assertEquals("world", data1.getWorld()); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testEquals()
    {
        final LocationData data1 = new LocationData(1, 2, 3, 4, 5, "world"); //$NON-NLS-1$
        final LocationData data2 = new LocationData(1, 2, 3, 4, 5, "world"); //$NON-NLS-1$
        assertNotEquals(data1, null);
        assertNotEquals(data2, "FOO"); //$NON-NLS-1$
        assertNotEquals(data1, new LocationData(2, 2, 3, 4, 5, "world")); //$NON-NLS-1$
        assertNotEquals(data1, new LocationData(1, 1, 3, 4, 5, "world")); //$NON-NLS-1$
        assertNotEquals(data1, new LocationData(1, 2, 4, 4, 5, "world")); //$NON-NLS-1$
        assertNotEquals(data1, new LocationData(1, 2, 3, 4, 5, "world2")); //$NON-NLS-1$
        assertNotEquals(data1, new LocationData(1, 2, 3, 5, 5, "world")); //$NON-NLS-1$
        assertNotEquals(data1, new LocationData(1, 2, 3, 4, 6, "world")); //$NON-NLS-1$
        assertNotEquals(data1, new LocationData(1, 2, 3, 4, 5, null));
        assertEquals(new LocationData(1, 2, 3, 4, 5, null), new LocationData(1, 2, 3, 4, 5, null));
        assertNotEquals(new LocationData(1, 2, 3, 4, 5, null), new LocationData(1, 2, 3, 4, 5, "world")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testHashCode()
    {
        final Map<LocationData, String> map = new HashMap<>();
        map.put(new LocationData(1, 2, 3, 4, 5, "world"), "FOO"); //$NON-NLS-1$ //$NON-NLS-2$
        map.put(new LocationData(1, 2, 3, 4, 5, "world2"), "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        map.put(new LocationData(1, 2, 3, 4, 5, null), "BAR2"); //$NON-NLS-1$
        assertEquals("FOO", map.get(new LocationData(1, 2, 3, 4, 5, "world"))); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("BAR", map.get(new LocationData(1, 2, 3, 4, 5, "world2"))); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("BAR2", map.get(new LocationData(1, 2, 3, 4, 5, null))); //$NON-NLS-1$
    }
    
}
