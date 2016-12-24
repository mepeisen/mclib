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

import de.minigameslib.mclib.shared.api.com.ServerBlockLocationData;

/**
 * Testing {@code ServerBlockLocationData}
 * 
 * @author mepeisen
 */
public class ServerBlockLocationDataTest
{
    
    /**
     * Simple test case.
     */
    @Test
    public void testEmptyConstructor()
    {
        final ServerBlockLocationData data1 = new ServerBlockLocationData();
        final ServerBlockLocationData data2 = new ServerBlockLocationData();
        assertEquals(data1, data2);
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testConstructor()
    {
        final ServerBlockLocationData data1 = new ServerBlockLocationData(1, 2, 3, "world", "server"); //$NON-NLS-1$ //$NON-NLS-2$
        final ServerBlockLocationData data2 = new ServerBlockLocationData(1, 2, 3, "world", "server"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(data1, data2);
        assertEquals(1, data1.getX());
        assertEquals(2, data1.getY());
        assertEquals(3, data1.getZ());
        assertEquals("world", data1.getWorld()); //$NON-NLS-1$
        assertEquals("server", data1.getName()); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testEquals()
    {
        final ServerBlockLocationData data1 = new ServerBlockLocationData(1, 2, 3, "world", "server"); //$NON-NLS-1$ //$NON-NLS-2$
        final ServerBlockLocationData data2 = new ServerBlockLocationData(1, 2, 3, "world", "server"); //$NON-NLS-1$ //$NON-NLS-2$
        assertNotEquals(data1, null);
        assertNotEquals(data2, "FOO"); //$NON-NLS-1$
        assertNotEquals(data1, new ServerBlockLocationData(2, 2, 3, "world", "server")); //$NON-NLS-1$ //$NON-NLS-2$
        assertNotEquals(data1, new ServerBlockLocationData(1, 1, 3, "world", "server")); //$NON-NLS-1$ //$NON-NLS-2$
        assertNotEquals(data1, new ServerBlockLocationData(1, 2, 4, "world", "server")); //$NON-NLS-1$ //$NON-NLS-2$
        assertNotEquals(data1, new ServerBlockLocationData(1, 2, 3, "world2", "server")); //$NON-NLS-1$ //$NON-NLS-2$
        assertNotEquals(data1, new ServerBlockLocationData(1, 2, 3, null, "server")); //$NON-NLS-1$
        assertNotEquals(data1, new ServerBlockLocationData(1, 2, 3, "world", null)); //$NON-NLS-1$
        assertEquals(new ServerBlockLocationData(1, 2, 3, null, null), new ServerBlockLocationData(1, 2, 3, null, null));
        assertEquals(new ServerBlockLocationData(1, 2, 3, "world", null), new ServerBlockLocationData(1, 2, 3, "world", null)); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(new ServerBlockLocationData(1, 2, 3, null, "server"), new ServerBlockLocationData(1, 2, 3, null, "server")); //$NON-NLS-1$ //$NON-NLS-2$
        assertNotEquals(new ServerBlockLocationData(1, 2, 3, null, "server"), new ServerBlockLocationData(1, 2, 3, "world", "server")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertNotEquals(new ServerBlockLocationData(1, 2, 3, "world", null), new ServerBlockLocationData(1, 2, 3, "world", "server")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertNotEquals(new ServerBlockLocationData(1, 2, 3, "world", "server"), new ServerBlockLocationData(1, 2, 3, null, "server")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertNotEquals(new ServerBlockLocationData(1, 2, 3, "world", "server"), new ServerBlockLocationData(1, 2, 3, "world", null)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testHashCode()
    {
        final Map<ServerBlockLocationData, String> map = new HashMap<>();
        map.put(new ServerBlockLocationData(1, 2, 3, "world", "server"), "FOO"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        map.put(new ServerBlockLocationData(1, 2, 3, "world2", "server2"), "BAR"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        map.put(new ServerBlockLocationData(1, 2, 3, null, null), "BAR2"); //$NON-NLS-1$
        assertEquals("FOO", map.get(new ServerBlockLocationData(1, 2, 3, "world", "server"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertEquals("BAR", map.get(new ServerBlockLocationData(1, 2, 3, "world2", "server2"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertEquals("BAR2", map.get(new ServerBlockLocationData(1, 2, 3, null, null))); //$NON-NLS-1$
    }
    
}
