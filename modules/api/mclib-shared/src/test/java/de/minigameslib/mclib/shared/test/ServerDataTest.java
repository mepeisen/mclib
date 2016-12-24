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

import de.minigameslib.mclib.shared.api.com.ServerData;

/**
 * Testing {@code ServerData}
 * 
 * @author mepeisen
 */
public class ServerDataTest
{
    
    /**
     * Simple test case.
     */
    @Test
    public void testEmptyConstructor()
    {
        final ServerData data1 = new ServerData();
        final ServerData data2 = new ServerData();
        assertEquals(data1, data2);
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testConstructor()
    {
        final ServerData data1 = new ServerData("world"); //$NON-NLS-1$
        final ServerData data2 = new ServerData("world"); //$NON-NLS-1$
        assertEquals(data1, data2);
        assertEquals("world", data1.getName()); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testEquals()
    {
        final ServerData data1 = new ServerData("world"); //$NON-NLS-1$
        final ServerData data2 = new ServerData("world"); //$NON-NLS-1$
        assertNotEquals(data1, null);
        assertNotEquals(data2, "FOO"); //$NON-NLS-1$
        assertNotEquals(data1, new ServerData("world2")); //$NON-NLS-1$
        assertNotEquals(data1, new ServerData(null));
        assertEquals(new ServerData(null), new ServerData(null));
        assertNotEquals(new ServerData(null), data1);
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testHashCode()
    {
        final Map<ServerData, String> map = new HashMap<>();
        map.put(new ServerData("world"), "FOO"); //$NON-NLS-1$ //$NON-NLS-2$
        map.put(new ServerData("world2"), "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        map.put(new ServerData(null), "BAR2"); //$NON-NLS-1$
        assertEquals("FOO", map.get(new ServerData("world"))); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("BAR", map.get(new ServerData("world2"))); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("BAR2", map.get(new ServerData(null))); //$NON-NLS-1$
    }
    
}