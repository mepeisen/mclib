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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

import de.minigameslib.mclib.shared.api.com.MemoryDataSection;
import de.minigameslib.mclib.shared.api.com.PlayerData;

/**
 * Testing MemoryDataSection
 * 
 * @author mepeisen
 */
public class MemoryDataSectionTest
{
    
    /**
     * Simple test case.
     */
    @Test
    public void testSimple()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO",  "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("BAR", section.get("FOO")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("BAR", section.getString("FOO")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("BAR", section.getValues(false).get("FOO")); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testCreateSection()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.createSection("BAZ").set("FOO",  "BAR"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertEquals("BAR", section.get("BAZ.FOO")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("BAR", section.getString("BAZ.FOO")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("BAR", section.getSection("BAZ").get("FOO")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertEquals("BAR", section.getSection("BAZ").getString("FOO")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        final Map<String, Object> values = section.getValues(true);
        assertEquals("BAR", values.get("BAZ.FOO")); //$NON-NLS-1$ //$NON-NLS-2$
        assertFalse(values.containsKey("BAZ")); //$NON-NLS-1$
        assertTrue(section.getValues(false).isEmpty());
        assertTrue(section.contains("BAZ")); //$NON-NLS-1$
        assertTrue(section.contains("BAZ.FOO")); //$NON-NLS-1$
        assertFalse(section.contains("FOO")); //$NON-NLS-1$
        assertFalse(section.contains("FOO.BAZ")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testCreateSectionDeep()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.createSection("BAZ1.BAZ2").set("FOO", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertEquals("BAR", section.get("BAZ1.BAZ2.FOO")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("BAR", section.getString("BAZ1.BAZ2.FOO")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("BAR", section.getSection("BAZ1").getSection("BAZ2").get("FOO")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        assertEquals("BAR", section.getSection("BAZ1").getSection("BAZ2").getString("FOO")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        final Map<String, Object> values = section.getValues(true);
        assertEquals("BAR", values.get("BAZ1.BAZ2.FOO")); //$NON-NLS-1$ //$NON-NLS-2$
        assertFalse(values.containsKey("BAZ1")); //$NON-NLS-1$
        assertFalse(values.containsKey("BAZ1.BAZ2")); //$NON-NLS-1$
        assertTrue(section.getValues(false).isEmpty());
        assertTrue(section.contains("BAZ1")); //$NON-NLS-1$
        assertTrue(section.contains("BAZ1.BAZ2")); //$NON-NLS-1$
        assertTrue(section.contains("BAZ1.BAZ2.FOO")); //$NON-NLS-1$
        assertFalse(section.contains("FOO")); //$NON-NLS-1$
        assertFalse(section.contains("FOO.BAZ")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testCreateSectionOverride()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("BAZ1", "FOO"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("FOO", section.get("BAZ1")); //$NON-NLS-1$ //$NON-NLS-2$
        assertNull(section.get("BAZ1.BAZ2.FOO")); //$NON-NLS-1$
        section.createSection("BAZ1.BAZ2").set("FOO", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertEquals("BAR", section.get("BAZ1.BAZ2.FOO")); //$NON-NLS-1$ //$NON-NLS-2$
        
        section.set("BAZ1.BAZ2", "FOO"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("FOO", section.get("BAZ1.BAZ2")); //$NON-NLS-1$ //$NON-NLS-2$
        assertNull(section.get("BAZ1.BAZ2.FOO")); //$NON-NLS-1$
        section.createSection("BAZ1.BAZ2").set("FOO", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertEquals("BAR", section.get("BAZ1.BAZ2.FOO")); //$NON-NLS-1$ //$NON-NLS-2$
        
        section.set("BAZ1.BAZ2", "FOO"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("FOO", section.get("BAZ1.BAZ2")); //$NON-NLS-1$ //$NON-NLS-2$
        assertNull(section.get("BAZ1.BAZ2.BAZ3.FOO")); //$NON-NLS-1$
        section.createSection("BAZ1.BAZ2.BAZ3").set("FOO", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertEquals("BAR", section.get("BAZ1.BAZ2.BAZ3.FOO")); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testCreateSectionWithDefaults()
    {
        final MemoryDataSection section = new MemoryDataSection();
        final Map<String, Integer> defaults = new HashMap<>();
        defaults.put("A",  1); //$NON-NLS-1$
        defaults.put("B",  2); //$NON-NLS-1$
        defaults.put("C",  3); //$NON-NLS-1$
        section.createSection("FOO", defaults); //$NON-NLS-1$
        assertEquals(1, section.get("FOO.A"));  //$NON-NLS-1$
        assertEquals(2, section.get("FOO.B"));  //$NON-NLS-1$
        assertEquals(3, section.get("FOO.C"));  //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetSectionOnPrimitive()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", 1); //$NON-NLS-1$
        assertNull(section.getSection("FOO")); //$NON-NLS-1$
        assertFalse(section.isSection("FOO")); //$NON-NLS-1$
        assertNull(section.getSection("FOO.BAR")); //$NON-NLS-1$
        assertFalse(section.isSection("FOO.BAR")); //$NON-NLS-1$
        section.set("BAR.BAZ", 1); //$NON-NLS-1$
        assertNull(section.getSection("BAR.BAZ")); //$NON-NLS-1$
        assertFalse(section.isSection("BAR.BAZ")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepStringOnPrimitive()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", 1); //$NON-NLS-1$
        assertNull(section.getString("FOO.BAR")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepString()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        section.set("FOO1.FOO2", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        section.set("FOO3", 1); //$NON-NLS-1$
        assertEquals("BAR", section.getString("FOO")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("BAR", section.getString("FOO1.FOO2")); //$NON-NLS-1$ //$NON-NLS-2$
        assertTrue(section.isString("FOO")); //$NON-NLS-1$
        assertTrue(section.isString("FOO1.FOO2")); //$NON-NLS-1$
        assertNull(section.getString("FOO3")); //$NON-NLS-1$
        
        assertEquals("BAR", section.getString("FOO", "BAZ")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertEquals("BAR", section.getString("FOO1.FOO2", "BAZ")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        
        assertEquals("BAZ", section.getString("FOO1", "BAZ")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertEquals("BAZ", section.getString("FOO.FOO2", "BAZ")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertEquals("BAZ", section.getString("FOO1.FOO3", "BAZ")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertEquals("BAZ", section.getString("FOO3", "BAZ")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertFalse(section.isString("FOO1")); //$NON-NLS-1$
        assertFalse(section.isString("FOO.FOO2")); //$NON-NLS-1$
        assertFalse(section.isString("FOO1.FOO3")); //$NON-NLS-1$
        assertFalse(section.isString("FOO3")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepIntOnString()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(0, section.getInt("FOO.BAR")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepInt()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", 1); //$NON-NLS-1$
        section.set("FOO1.FOO2", 1); //$NON-NLS-1$
        section.set("FOO3", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(1, section.getInt("FOO")); //$NON-NLS-1$
        assertEquals(1, section.getInt("FOO1.FOO2")); //$NON-NLS-1$
        assertTrue(section.isInt("FOO")); //$NON-NLS-1$
        assertTrue(section.isInt("FOO1.FOO2")); //$NON-NLS-1$
        assertEquals(0, section.getInt("FOO3")); //$NON-NLS-1$
        
        assertEquals(1, section.getInt("FOO", 2)); //$NON-NLS-1$
        assertEquals(1, section.getInt("FOO1.FOO2", 2)); //$NON-NLS-1$
        
        assertEquals(2, section.getInt("FOO1", 2)); //$NON-NLS-1$
        assertEquals(2, section.getInt("FOO.FOO2", 2)); //$NON-NLS-1$
        assertEquals(2, section.getInt("FOO1.FOO3", 2)); //$NON-NLS-1$
        assertEquals(2, section.getInt("FOO3", 2)); //$NON-NLS-1$
        assertFalse(section.isInt("FOO1")); //$NON-NLS-1$
        assertFalse(section.isInt("FOO.FOO2")); //$NON-NLS-1$
        assertFalse(section.isInt("FOO1.FOO3")); //$NON-NLS-1$
        assertFalse(section.isInt("FOO3")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testIsSection()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", 1); //$NON-NLS-1$
        assertFalse(section.isSection("FOO")); //$NON-NLS-1$
        assertFalse(section.isSection("FOO.BAR")); //$NON-NLS-1$
        section.set("BAR.BAZ", 1); //$NON-NLS-1$
        assertFalse(section.isSection("BAR.BAZ")); //$NON-NLS-1$
        section.createSection("FOO.BAR"); //$NON-NLS-1$
        assertTrue(section.isSection("FOO")); //$NON-NLS-1$
        assertTrue(section.isSection("FOO")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testSetDeep()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("BAZ.FOO",  "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("BAR", section.get("BAZ.FOO")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("BAR", section.getString("BAZ.FOO")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("BAR", section.getSection("BAZ").get("FOO")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertEquals("BAR", section.getSection("BAZ").getString("FOO")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertEquals("BAR", section.getValues(true).get("BAZ.FOO")); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepKeys()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("BAZ.FOO",  "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        final Set<String> keys = section.getKeys(false);
        assertEquals(1, keys.size());
        assertTrue(keys.contains("BAZ")); //$NON-NLS-1$
        final Set<String> deep = section.getKeys(true);
        assertEquals(2, deep.size());
        assertTrue(deep.contains("BAZ")); //$NON-NLS-1$
        assertTrue(deep.contains("BAZ.FOO")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetCurrentPath()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("BAZ.FOO.FOO2",  "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertNull(section.getCurrentPath());
        assertEquals("BAZ", section.getSection("BAZ").getCurrentPath()); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("BAZ.FOO", section.getSection("BAZ").getSection("FOO").getCurrentPath()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertNull(section.getSection("BAZ.FOO.FOO3")); //$NON-NLS-1$
        assertEquals("BAZ.FOO.FOO3", section.getSection("BAZ").getSection("FOO").createSection("FOO3").getCurrentPath()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        assertNotNull(section.getSection("BAZ.FOO.FOO3")); //$NON-NLS-1$
        assertNull(section.getName());
        assertEquals("BAZ", section.getSection("BAZ").getName()); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("FOO", section.getSection("BAZ").getSection("FOO").getName()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertEquals("FOO3", section.getSection("BAZ").getSection("FOO").getSection("FOO3").getName()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        assertNull(section.getParent());
        assertSame(section, section.getSection("BAZ").getParent()); //$NON-NLS-1$
        assertSame(section.getSection("BAZ"), section.getSection("BAZ").getSection("FOO").getParent()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertSame(section.getSection("BAZ.FOO"), section.getSection("BAZ").getSection("FOO").getSection("FOO3").getParent()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        
        assertSame(section, section.getRoot());
        assertSame(section, section.getSection("BAZ").getRoot()); //$NON-NLS-1$
        assertSame(section, section.getSection("BAZ.FOO").getRoot()); //$NON-NLS-1$
        assertSame(section, section.getSection("BAZ.FOO.FOO3").getRoot()); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test(expected = NullPointerException.class)
    public void testSetInvalid1()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set(null, "FOO"); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetInvalid2()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", UUID.randomUUID()); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testSet()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("BAZ2",  "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        section.set("BAZ.FOO",  "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("BAR", section.get("BAZ2")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("BAR", section.get("BAZ.FOO")); //$NON-NLS-1$ //$NON-NLS-2$
        
        final PlayerData player = new PlayerData(UUID.randomUUID(), "FooPlayer"); //$NON-NLS-1$
        section.set("player", player); //$NON-NLS-1$
        section.set("foo.player", player); //$NON-NLS-1$
        assertEquals(player.getPlayerUuid().toString(), section.get("player.uuid")); //$NON-NLS-1$
        assertEquals(player.getPlayerUuid().toString(), section.get("foo.player.uuid")); //$NON-NLS-1$
        assertEquals("FooPlayer", section.get("player.name")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("FooPlayer", section.get("foo.player.name")); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testSetNull()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("BAZ2",  "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        section.set("BAZ.FOO",  "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        section.set("BAZZER.FOO", null); //$NON-NLS-1$
        assertEquals("BAR", section.get("BAZ2")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("BAR", section.get("BAZ.FOO")); //$NON-NLS-1$ //$NON-NLS-2$
        section.set("BAZ2.FOO", null); //$NON-NLS-1$
        assertEquals("BAR", section.get("BAZ2")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("BAR", section.get("BAZ.FOO")); //$NON-NLS-1$ //$NON-NLS-2$
        section.set("BAZ.BAR", null); //$NON-NLS-1$
        assertEquals("BAR", section.get("BAZ2")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("BAR", section.get("BAZ.FOO")); //$NON-NLS-1$ //$NON-NLS-2$
        section.set("BAZ2", null); //$NON-NLS-1$
        assertNull(section.get("BAZ2")); //$NON-NLS-1$
        assertEquals("BAR", section.get("BAZ.FOO")); //$NON-NLS-1$ //$NON-NLS-2$
        section.set("BAZ.FOO", null); //$NON-NLS-1$
        assertNull(section.get("BAZ2")); //$NON-NLS-1$
        assertNull(section.get("BAZ.FOO")); //$NON-NLS-1$
        
        final PlayerData player = new PlayerData(UUID.randomUUID(), "FooPlayer"); //$NON-NLS-1$
        section.set("player", player); //$NON-NLS-1$
        section.set("foo.player", player); //$NON-NLS-1$
        assertEquals(player.getPlayerUuid().toString(), section.get("player.uuid")); //$NON-NLS-1$
        assertEquals(player.getPlayerUuid().toString(), section.get("foo.player.uuid")); //$NON-NLS-1$
        assertEquals("FooPlayer", section.get("player.name")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("FooPlayer", section.get("foo.player.name")); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGet()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("BAZ2",  "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        section.set("BAZ.FOO",  "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("BAR", section.get("BAZ2")); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals("BAR", section.get("BAZ.FOO")); //$NON-NLS-1$ //$NON-NLS-2$
        
        assertNull(section.get("BAZ3")); //$NON-NLS-1$
        assertNull(section.get("BAZ3.FOO")); //$NON-NLS-1$
        assertNull(section.get("BAZ.FOO3")); //$NON-NLS-1$
        
        assertEquals("BAR", section.get("BAZ2", "BAR3")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertEquals("BAR", section.get("BAZ.FOO", "BAR3")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        
        assertEquals("BAR3", section.get("BAZ3", "BAR3")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertEquals("BAR3", section.get("BAZ3.FOO", "BAR3")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertEquals("BAR3", section.get("BAZ.FOO3", "BAR3")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testSetPrimitiveList()
    {
        final List<Integer> intlist = Arrays.asList(1, 2, 3, 4);
        final MemoryDataSection section = new MemoryDataSection();
        section.setPrimitiveList("FOO", intlist); //$NON-NLS-1$
        assertEquals(1, section.getInt("FOO.item0")); //$NON-NLS-1$
        assertEquals(2, section.getInt("FOO.item1")); //$NON-NLS-1$
        assertEquals(3, section.getInt("FOO.item2")); //$NON-NLS-1$
        assertEquals(4, section.getInt("FOO.item3")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testSetPrimitiveMapList()
    {
        final List<Map<String, Integer>> maplist = new ArrayList<>();
        final Map<String, Integer> map1 = new HashMap<>();
        map1.put("A1", 1); //$NON-NLS-1$
        map1.put("A2", 2); //$NON-NLS-1$
        maplist.add(map1);
        final Map<String, Integer> map2 = new HashMap<>();
        map2.put("B1", 3); //$NON-NLS-1$
        map2.put("B2", 4); //$NON-NLS-1$
        maplist.add(map2);
        final MemoryDataSection section = new MemoryDataSection();
        section.setPrimitiveMapList("FOO", maplist); //$NON-NLS-1$
        assertEquals(1, section.getInt("FOO.map0.A1")); //$NON-NLS-1$
        assertEquals(2, section.getInt("FOO.map0.A2")); //$NON-NLS-1$
        assertEquals(3, section.getInt("FOO.map1.B1")); //$NON-NLS-1$
        assertEquals(4, section.getInt("FOO.map1.B2")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testSetPrimitiveListMap()
    {
        final Map<String, List<Integer>> listmap = new HashMap<>();
        final List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        listmap.put("A", list1); //$NON-NLS-1$
        final List<Integer> list2 = new ArrayList<>();
        list2.add(3);
        list2.add(4);
        listmap.put("B", list2); //$NON-NLS-1$
        final MemoryDataSection section = new MemoryDataSection();
        section.setPrimitiveListMap("FOO", listmap); //$NON-NLS-1$
        assertEquals(1, section.getInt("FOO.A.item0")); //$NON-NLS-1$
        assertEquals(2, section.getInt("FOO.A.item1")); //$NON-NLS-1$
        assertEquals(3, section.getInt("FOO.B.item0")); //$NON-NLS-1$
        assertEquals(4, section.getInt("FOO.B.item1")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testSetPrimitiveMap()
    {
        final Map<String, Integer> map1 = new HashMap<>();
        map1.put("A1", 1); //$NON-NLS-1$
        map1.put("A2", 2); //$NON-NLS-1$
        final MemoryDataSection section = new MemoryDataSection();
        section.setPrimitiveMap("FOO", map1); //$NON-NLS-1$
        assertEquals(1, section.getInt("FOO.A1")); //$NON-NLS-1$
        assertEquals(2, section.getInt("FOO.A2")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testSetFragmentMap()
    {
        final Map<String, PlayerData> map1 = new HashMap<>();
        final PlayerData player1 = new PlayerData(UUID.randomUUID(), "FooPlayer1"); //$NON-NLS-1$
        final PlayerData player2 = new PlayerData(UUID.randomUUID(), "FooPlayer2"); //$NON-NLS-1$
        map1.put("A1", player1); //$NON-NLS-1$
        map1.put("A2", player2); //$NON-NLS-1$
        final MemoryDataSection section = new MemoryDataSection();
        section.setFragmentMap("FOO", map1); //$NON-NLS-1$
        assertEquals(player1.getPlayerUuid().toString(), section.getString("FOO.A1.uuid")); //$NON-NLS-1$
        assertEquals(player2.getPlayerUuid().toString(), section.getString("FOO.A2.uuid")); //$NON-NLS-1$
        assertEquals(player1.getPlayerName(), section.getString("FOO.A1.name")); //$NON-NLS-1$
        assertEquals(player2.getPlayerName(), section.getString("FOO.A2.name")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testSetFragmentListMap()
    {
        final Map<String, List<PlayerData>> listmap = new HashMap<>();
        final List<PlayerData> list1 = new ArrayList<>();
        final PlayerData player1 = new PlayerData(UUID.randomUUID(), "FooPlayer1"); //$NON-NLS-1$
        final PlayerData player2 = new PlayerData(UUID.randomUUID(), "FooPlayer2"); //$NON-NLS-1$
        list1.add(player1);
        list1.add(player2);
        listmap.put("A", list1); //$NON-NLS-1$
        final List<PlayerData> list2 = new ArrayList<>();
        final PlayerData player3 = new PlayerData(UUID.randomUUID(), "FooPlayer1"); //$NON-NLS-1$
        final PlayerData player4 = new PlayerData(UUID.randomUUID(), "FooPlayer2"); //$NON-NLS-1$
        list2.add(player3);
        list2.add(player4);
        listmap.put("B", list2); //$NON-NLS-1$
        final MemoryDataSection section = new MemoryDataSection();
        section.setFragmentListMap("FOO", listmap); //$NON-NLS-1$
        assertEquals(player1.getPlayerUuid().toString(), section.getString("FOO.A.item0.uuid")); //$NON-NLS-1$
        assertEquals(player2.getPlayerUuid().toString(), section.getString("FOO.A.item1.uuid")); //$NON-NLS-1$
        assertEquals(player3.getPlayerUuid().toString(), section.getString("FOO.B.item0.uuid")); //$NON-NLS-1$
        assertEquals(player4.getPlayerUuid().toString(), section.getString("FOO.B.item1.uuid")); //$NON-NLS-1$
        assertEquals(player1.getPlayerName(), section.getString("FOO.A.item0.name")); //$NON-NLS-1$
        assertEquals(player2.getPlayerName(), section.getString("FOO.A.item1.name")); //$NON-NLS-1$
        assertEquals(player3.getPlayerName(), section.getString("FOO.B.item0.name")); //$NON-NLS-1$
        assertEquals(player4.getPlayerName(), section.getString("FOO.B.item1.name")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testSetFragmentMapList()
    {
        final List<Map<String, PlayerData>> maplist = new ArrayList<>();
        final Map<String, PlayerData> map1 = new HashMap<>();
        final PlayerData player1 = new PlayerData(UUID.randomUUID(), "FooPlayer1"); //$NON-NLS-1$
        final PlayerData player2 = new PlayerData(UUID.randomUUID(), "FooPlayer2"); //$NON-NLS-1$
        map1.put("A1", player1); //$NON-NLS-1$
        map1.put("A2", player2); //$NON-NLS-1$
        maplist.add(map1);
        final Map<String, PlayerData> map2 = new HashMap<>();
        final PlayerData player3 = new PlayerData(UUID.randomUUID(), "FooPlayer1"); //$NON-NLS-1$
        final PlayerData player4 = new PlayerData(UUID.randomUUID(), "FooPlayer2"); //$NON-NLS-1$
        map2.put("B1", player3); //$NON-NLS-1$
        map2.put("B2", player4); //$NON-NLS-1$
        maplist.add(map2);
        final MemoryDataSection section = new MemoryDataSection();
        section.setFragmentMapList("FOO", maplist); //$NON-NLS-1$
        assertEquals(player1.getPlayerUuid().toString(), section.getString("FOO.map0.A1.uuid")); //$NON-NLS-1$
        assertEquals(player2.getPlayerUuid().toString(), section.getString("FOO.map0.A2.uuid")); //$NON-NLS-1$
        assertEquals(player3.getPlayerUuid().toString(), section.getString("FOO.map1.B1.uuid")); //$NON-NLS-1$
        assertEquals(player4.getPlayerUuid().toString(), section.getString("FOO.map1.B2.uuid")); //$NON-NLS-1$
        assertEquals(player1.getPlayerName(), section.getString("FOO.map0.A1.name")); //$NON-NLS-1$
        assertEquals(player2.getPlayerName(), section.getString("FOO.map0.A2.name")); //$NON-NLS-1$
        assertEquals(player3.getPlayerName(), section.getString("FOO.map1.B1.name")); //$NON-NLS-1$
        assertEquals(player4.getPlayerName(), section.getString("FOO.map1.B2.name")); //$NON-NLS-1$
    }
    
}
