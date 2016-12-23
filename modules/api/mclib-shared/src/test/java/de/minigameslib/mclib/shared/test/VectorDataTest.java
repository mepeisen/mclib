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

import de.minigameslib.mclib.shared.api.com.VectorData;

/**
 * Testing {@code VectorData}
 * 
 * @author mepeisen
 */
public class VectorDataTest
{
    
    /**
     * Simple test case.
     */
    @Test
    public void testEmptyConstructor()
    {
        final VectorData data1 = new VectorData();
        final VectorData data2 = new VectorData();
        assertEquals(data1, data2);
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testConstructor()
    {
        final VectorData data1 = new VectorData(1, 2, 3);
        final VectorData data2 = new VectorData(1, 2, 3);
        assertEquals(data1, data2);
        assertEquals(1, data1.getX(), 0);
        assertEquals(2, data1.getY(), 0);
        assertEquals(3, data1.getZ(), 0);
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testEquals()
    {
        final VectorData data1 = new VectorData(1, 2, 3);
        final VectorData data2 = new VectorData(1, 2, 3);
        assertNotEquals(data1, null);
        assertNotEquals(data2, "FOO"); //$NON-NLS-1$
        assertNotEquals(data1, new VectorData(2, 2, 3));
        assertNotEquals(data1, new VectorData(1, 1, 3));
        assertNotEquals(data1, new VectorData(1, 2, 4));
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testHashCode()
    {
        final Map<VectorData, String> map = new HashMap<>();
        map.put(new VectorData(1, 2, 3), "FOO"); //$NON-NLS-1$
        map.put(new VectorData(1, 2, 4), "BAR"); //$NON-NLS-1$
        assertEquals("FOO", map.get(new VectorData(1, 2, 3))); //$NON-NLS-1$
        assertEquals("BAR", map.get(new VectorData(1, 2, 4))); //$NON-NLS-1$
    }
    
}
