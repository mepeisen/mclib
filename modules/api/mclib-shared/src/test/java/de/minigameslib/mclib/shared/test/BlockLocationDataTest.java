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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.minigameslib.mclib.shared.api.com.BlockLocationData;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * Testing {@code BlockLocationData}.
 * 
 * @author mepeisen
 */
public class BlockLocationDataTest
{
    
    /**
     * Simple test case.
     */
    @Test
    public void testEmptyConstructor()
    {
        final BlockLocationData data1 = new BlockLocationData();
        final BlockLocationData data2 = new BlockLocationData();
        assertEquals(data1, data2);
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testConstructor()
    {
        final BlockLocationData data1 = new BlockLocationData(1, 2, 3, "world"); //$NON-NLS-1$
        final BlockLocationData data2 = new BlockLocationData(1, 2, 3, "world"); //$NON-NLS-1$
        assertEquals(data1, data2);
        assertEquals(1, data1.getX());
        assertEquals(2, data1.getY());
        assertEquals(3, data1.getZ());
        assertEquals("world", data1.getWorld()); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testEquals()
    {
        final BlockLocationData data1 = new BlockLocationData(1, 2, 3, "world"); //$NON-NLS-1$
        final BlockLocationData data2 = new BlockLocationData(1, 2, 3, "world"); //$NON-NLS-1$
        assertNotEquals(data1, null);
        assertNotEquals(data2, "FOO"); //$NON-NLS-1$
        assertNotEquals(data1, new BlockLocationData(2, 2, 3, "world")); //$NON-NLS-1$
        assertNotEquals(data1, new BlockLocationData(1, 1, 3, "world")); //$NON-NLS-1$
        assertNotEquals(data1, new BlockLocationData(1, 2, 4, "world")); //$NON-NLS-1$
        assertNotEquals(data1, new BlockLocationData(1, 2, 3, "world2")); //$NON-NLS-1$
        assertNotEquals(data1, new BlockLocationData(1, 2, 3, null));
        assertEquals(new BlockLocationData(1, 2, 3, null), new BlockLocationData(1, 2, 3, null));
        assertNotEquals(new BlockLocationData(1, 2, 3, null), new BlockLocationData(1, 2, 3, "world")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testHashCode()
    {
        final Map<BlockLocationData, String> map = new HashMap<>();
        map.put(new BlockLocationData(1, 2, 3, "world"), "FOO"); //$NON-NLS-1$ //$NON-NLS-2$
        map.put(new BlockLocationData(1, 2, 3, "world2"), "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        map.put(new BlockLocationData(1, 2, 3, null), "BAR2"); //$NON-NLS-1$
        assertEquals("FOO", map.get(new BlockLocationData(1, 2, 3, "world"))); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("BAR", map.get(new BlockLocationData(1, 2, 3, "world2"))); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("BAR2", map.get(new BlockLocationData(1, 2, 3, null))); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testValid()
    {
        final DataSection section = new MemoryDataSection();
        section.set("foo.x", 1); //$NON-NLS-1$
        section.set("foo.y", 2); //$NON-NLS-1$
        section.set("foo.z", 3); //$NON-NLS-1$
        section.set("foo.world", "world"); //$NON-NLS-1$ //$NON-NLS-2$
        assertTrue(section.isFragment(BlockLocationData.class, "foo")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testInvalid1()
    {
        final DataSection section = new MemoryDataSection();
        section.set("foo.y", 2); //$NON-NLS-1$
        section.set("foo.z", 3); //$NON-NLS-1$
        section.set("foo.world", "world"); //$NON-NLS-1$ //$NON-NLS-2$
        assertFalse(section.isFragment(BlockLocationData.class, "foo")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testInvalid2()
    {
        final DataSection section = new MemoryDataSection();
        section.set("foo.x", 1); //$NON-NLS-1$
        section.set("foo.z", 3); //$NON-NLS-1$
        section.set("foo.world", "world"); //$NON-NLS-1$ //$NON-NLS-2$
        assertFalse(section.isFragment(BlockLocationData.class, "foo")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testInvalid3()
    {
        final DataSection section = new MemoryDataSection();
        section.set("foo.x", 1); //$NON-NLS-1$
        section.set("foo.y", 2); //$NON-NLS-1$
        section.set("foo.world", "world"); //$NON-NLS-1$ //$NON-NLS-2$
        assertFalse(section.isFragment(BlockLocationData.class, "foo")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testInvalid4()
    {
        final DataSection section = new MemoryDataSection();
        section.set("foo.x", 1); //$NON-NLS-1$
        section.set("foo.y", 2); //$NON-NLS-1$
        section.set("foo.z", 3); //$NON-NLS-1$
        assertFalse(section.isFragment(BlockLocationData.class, "foo")); //$NON-NLS-1$
    }
    
}
