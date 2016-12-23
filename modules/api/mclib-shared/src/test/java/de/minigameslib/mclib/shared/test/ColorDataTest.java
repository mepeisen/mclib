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

import de.minigameslib.mclib.shared.api.com.ColorData;

/**
 * Testing {@code ColorData}
 * 
 * @author mepeisen
 */
public class ColorDataTest
{
    
    /**
     * Simple test case.
     */
    @Test
    public void testEmptyConstructor()
    {
        final ColorData data1 = new ColorData();
        final ColorData data2 = new ColorData();
        assertEquals(data1, data2);
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testConstructor()
    {
        final ColorData data1 = new ColorData((byte)1, (byte)2, (byte)3);
        final ColorData data2 = new ColorData((byte)1, (byte)2, (byte)3);
        assertEquals(data1, data2);
        assertEquals(1, data1.getRed());
        assertEquals(2, data1.getGreen());
        assertEquals(3, data1.getBlue());
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testEquals()
    {
        final ColorData data1 = new ColorData((byte)1, (byte)2, (byte)3);
        final ColorData data2 = new ColorData((byte)1, (byte)2, (byte)3);
        assertNotEquals(data1, null);
        assertNotEquals(data2, "FOO"); //$NON-NLS-1$
        assertNotEquals(data1, new ColorData((byte)2, (byte)2, (byte)3));
        assertNotEquals(data1, new ColorData((byte)1, (byte)1, (byte)3));
        assertNotEquals(data1, new ColorData((byte)1, (byte)2, (byte)4));
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testHashCode()
    {
        final Map<ColorData, String> map = new HashMap<>();
        map.put(new ColorData((byte)1, (byte)2, (byte)3), "FOO"); //$NON-NLS-1$
        map.put(new ColorData((byte)1, (byte)2, (byte)4), "BAR"); //$NON-NLS-1$
        assertEquals("FOO", map.get(new ColorData((byte)1, (byte)2, (byte)3))); //$NON-NLS-1$
        assertEquals("BAR", map.get(new ColorData((byte)1, (byte)2, (byte)4))); //$NON-NLS-1$
    }
    
}
