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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import de.minigameslib.mclib.shared.api.com.ColorData;
import de.minigameslib.mclib.shared.api.com.ColorDataFragment;
import de.minigameslib.mclib.shared.api.com.ItemStackDataFragment;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;
import de.minigameslib.mclib.shared.api.com.PlayerData;
import de.minigameslib.mclib.shared.api.com.PlayerDataFragment;
import de.minigameslib.mclib.shared.api.com.VectorData;
import de.minigameslib.mclib.shared.api.com.VectorDataFragment;

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
        assertFalse(section.isInt("FOO.BAR")); //$NON-NLS-1$
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
    public void testGetDeepCharOnString()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(0, section.getCharacter("FOO.BAR")); //$NON-NLS-1$
        assertFalse(section.isCharacter("FOO.BAR")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepChar()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", 'A'); //$NON-NLS-1$
        section.set("FOO1.FOO2", 'A'); //$NON-NLS-1$
        section.set("FOO3", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals('A', section.getCharacter("FOO")); //$NON-NLS-1$
        assertEquals('A', section.getCharacter("FOO1.FOO2")); //$NON-NLS-1$
        assertTrue(section.isCharacter("FOO")); //$NON-NLS-1$
        assertTrue(section.isCharacter("FOO1.FOO2")); //$NON-NLS-1$
        assertEquals(0, section.getCharacter("FOO3")); //$NON-NLS-1$
        
        assertEquals('A', section.getCharacter("FOO", 'B')); //$NON-NLS-1$
        assertEquals('A', section.getCharacter("FOO1.FOO2", 'B')); //$NON-NLS-1$
        
        assertEquals('B', section.getCharacter("FOO1", 'B')); //$NON-NLS-1$
        assertEquals('B', section.getCharacter("FOO.FOO2", 'B')); //$NON-NLS-1$
        assertEquals('B', section.getCharacter("FOO1.FOO3", 'B')); //$NON-NLS-1$
        assertEquals('B', section.getCharacter("FOO3", 'B')); //$NON-NLS-1$
        assertFalse(section.isCharacter("FOO1")); //$NON-NLS-1$
        assertFalse(section.isCharacter("FOO.FOO2")); //$NON-NLS-1$
        assertFalse(section.isCharacter("FOO1.FOO3")); //$NON-NLS-1$
        assertFalse(section.isCharacter("FOO3")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepByteOnString()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(0, section.getByte("FOO.BAR")); //$NON-NLS-1$
        assertFalse(section.isByte("FOO.BAR")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepByte()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", (byte) 1); //$NON-NLS-1$
        section.set("FOO1.FOO2", (byte) 1); //$NON-NLS-1$
        section.set("FOO3", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(1, section.getByte("FOO")); //$NON-NLS-1$
        assertEquals(1, section.getByte("FOO1.FOO2")); //$NON-NLS-1$
        assertTrue(section.isByte("FOO")); //$NON-NLS-1$
        assertTrue(section.isByte("FOO1.FOO2")); //$NON-NLS-1$
        assertEquals(0, section.getByte("FOO3")); //$NON-NLS-1$
        
        assertEquals(1, section.getByte("FOO", (byte) 2)); //$NON-NLS-1$
        assertEquals(1, section.getByte("FOO1.FOO2", (byte) 2)); //$NON-NLS-1$
        
        assertEquals(2, section.getByte("FOO1", (byte) 2)); //$NON-NLS-1$
        assertEquals(2, section.getByte("FOO.FOO2", (byte) 2)); //$NON-NLS-1$
        assertEquals(2, section.getByte("FOO1.FOO3", (byte) 2)); //$NON-NLS-1$
        assertEquals(2, section.getByte("FOO3", (byte) 2)); //$NON-NLS-1$
        assertFalse(section.isByte("FOO1")); //$NON-NLS-1$
        assertFalse(section.isByte("FOO.FOO2")); //$NON-NLS-1$
        assertFalse(section.isByte("FOO1.FOO3")); //$NON-NLS-1$
        assertFalse(section.isByte("FOO3")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepDateTimeOnString()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertNull(section.getDateTime("FOO.BAR")); //$NON-NLS-1$
        assertFalse(section.isDateTime("FOO.BAR")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepDateTime()
    {
        final LocalDateTime obj1 = LocalDateTime.of(2016, 10, 5, 10, 30);
        final LocalDateTime obj2 = LocalDateTime.of(2016, 10, 5, 10, 40);
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", obj1); //$NON-NLS-1$
        section.set("FOO1.FOO2", obj1); //$NON-NLS-1$
        section.set("FOO3", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(obj1, section.getDateTime("FOO")); //$NON-NLS-1$
        assertEquals(obj1, section.getDateTime("FOO1.FOO2")); //$NON-NLS-1$
        assertTrue(section.isDateTime("FOO")); //$NON-NLS-1$
        assertTrue(section.isDateTime("FOO1.FOO2")); //$NON-NLS-1$
        assertNull(section.getDateTime("FOO3")); //$NON-NLS-1$
        
        assertEquals(obj1, section.getDateTime("FOO", obj2)); //$NON-NLS-1$
        assertEquals(obj1, section.getDateTime("FOO1.FOO2", obj2)); //$NON-NLS-1$
        
        assertEquals(obj2, section.getDateTime("FOO1", obj2)); //$NON-NLS-1$
        assertEquals(obj2, section.getDateTime("FOO.FOO2", obj2)); //$NON-NLS-1$
        assertEquals(obj2, section.getDateTime("FOO1.FOO3", obj2)); //$NON-NLS-1$
        assertEquals(obj2, section.getDateTime("FOO3", obj2)); //$NON-NLS-1$
        assertFalse(section.isDateTime("FOO1")); //$NON-NLS-1$
        assertFalse(section.isDateTime("FOO.FOO2")); //$NON-NLS-1$
        assertFalse(section.isDateTime("FOO1.FOO3")); //$NON-NLS-1$
        assertFalse(section.isDateTime("FOO3")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepDateOnString()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertNull(section.getDate("FOO.BAR")); //$NON-NLS-1$
        assertFalse(section.isDate("FOO.BAR")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepDate()
    {
        final LocalDate obj1 = LocalDate.of(2016, 10, 5);
        final LocalDate obj2 = LocalDate.of(2016, 10, 6);
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", obj1); //$NON-NLS-1$
        section.set("FOO1.FOO2", obj1); //$NON-NLS-1$
        section.set("FOO3", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(obj1, section.getDate("FOO")); //$NON-NLS-1$
        assertEquals(obj1, section.getDate("FOO1.FOO2")); //$NON-NLS-1$
        assertTrue(section.isDate("FOO")); //$NON-NLS-1$
        assertTrue(section.isDate("FOO1.FOO2")); //$NON-NLS-1$
        assertNull(section.getDate("FOO3")); //$NON-NLS-1$
        
        assertEquals(obj1, section.getDate("FOO", obj2)); //$NON-NLS-1$
        assertEquals(obj1, section.getDate("FOO1.FOO2", obj2)); //$NON-NLS-1$
        
        assertEquals(obj2, section.getDate("FOO1", obj2)); //$NON-NLS-1$
        assertEquals(obj2, section.getDate("FOO.FOO2", obj2)); //$NON-NLS-1$
        assertEquals(obj2, section.getDate("FOO1.FOO3", obj2)); //$NON-NLS-1$
        assertEquals(obj2, section.getDate("FOO3", obj2)); //$NON-NLS-1$
        assertFalse(section.isDate("FOO1")); //$NON-NLS-1$
        assertFalse(section.isDate("FOO.FOO2")); //$NON-NLS-1$
        assertFalse(section.isDate("FOO1.FOO3")); //$NON-NLS-1$
        assertFalse(section.isDate("FOO3")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepTimeOnString()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertNull(section.getTime("FOO.BAR")); //$NON-NLS-1$
        assertFalse(section.isTime("FOO.BAR")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepTime()
    {
        final LocalTime obj1 = LocalTime.of(10, 5);
        final LocalTime obj2 = LocalTime.of(10, 6);
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", obj1); //$NON-NLS-1$
        section.set("FOO1.FOO2", obj1); //$NON-NLS-1$
        section.set("FOO3", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(obj1, section.getTime("FOO")); //$NON-NLS-1$
        assertEquals(obj1, section.getTime("FOO1.FOO2")); //$NON-NLS-1$
        assertTrue(section.isTime("FOO")); //$NON-NLS-1$
        assertTrue(section.isTime("FOO1.FOO2")); //$NON-NLS-1$
        assertNull(section.getTime("FOO3")); //$NON-NLS-1$
        
        assertEquals(obj1, section.getTime("FOO", obj2)); //$NON-NLS-1$
        assertEquals(obj1, section.getTime("FOO1.FOO2", obj2)); //$NON-NLS-1$
        
        assertEquals(obj2, section.getTime("FOO1", obj2)); //$NON-NLS-1$
        assertEquals(obj2, section.getTime("FOO.FOO2", obj2)); //$NON-NLS-1$
        assertEquals(obj2, section.getTime("FOO1.FOO3", obj2)); //$NON-NLS-1$
        assertEquals(obj2, section.getTime("FOO3", obj2)); //$NON-NLS-1$
        assertFalse(section.isTime("FOO1")); //$NON-NLS-1$
        assertFalse(section.isTime("FOO.FOO2")); //$NON-NLS-1$
        assertFalse(section.isTime("FOO1.FOO3")); //$NON-NLS-1$
        assertFalse(section.isTime("FOO3")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepLongOnString()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(0, section.getLong("FOO.BAR")); //$NON-NLS-1$
        assertFalse(section.isLong("FOO.BAR")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepLong()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", 1l); //$NON-NLS-1$
        section.set("FOO1.FOO2", 1l); //$NON-NLS-1$
        section.set("FOO3", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(1, section.getLong("FOO")); //$NON-NLS-1$
        assertEquals(1, section.getLong("FOO1.FOO2")); //$NON-NLS-1$
        assertTrue(section.isLong("FOO")); //$NON-NLS-1$
        assertTrue(section.isLong("FOO1.FOO2")); //$NON-NLS-1$
        assertEquals(0, section.getLong("FOO3")); //$NON-NLS-1$
        
        assertEquals(1, section.getLong("FOO", 2l)); //$NON-NLS-1$
        assertEquals(1, section.getLong("FOO1.FOO2", 2l)); //$NON-NLS-1$
        
        assertEquals(2, section.getLong("FOO1", 2l)); //$NON-NLS-1$
        assertEquals(2, section.getLong("FOO.FOO2", 2l)); //$NON-NLS-1$
        assertEquals(2, section.getLong("FOO1.FOO3", 2l)); //$NON-NLS-1$
        assertEquals(2, section.getLong("FOO3", 2l)); //$NON-NLS-1$
        assertFalse(section.isLong("FOO1")); //$NON-NLS-1$
        assertFalse(section.isLong("FOO.FOO2")); //$NON-NLS-1$
        assertFalse(section.isLong("FOO1.FOO3")); //$NON-NLS-1$
        assertFalse(section.isLong("FOO3")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepShortOnString()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(0, section.getShort("FOO.BAR")); //$NON-NLS-1$
        assertFalse(section.isShort("FOO.BAR")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepShort()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", (short) 1); //$NON-NLS-1$
        section.set("FOO1.FOO2", (short) 1); //$NON-NLS-1$
        section.set("FOO3", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(1, section.getShort("FOO")); //$NON-NLS-1$
        assertEquals(1, section.getShort("FOO1.FOO2")); //$NON-NLS-1$
        assertTrue(section.isShort("FOO")); //$NON-NLS-1$
        assertTrue(section.isShort("FOO1.FOO2")); //$NON-NLS-1$
        assertEquals(0, section.getShort("FOO3")); //$NON-NLS-1$
        
        assertEquals(1, section.getShort("FOO", (short) 2)); //$NON-NLS-1$
        assertEquals(1, section.getShort("FOO1.FOO2", (short) 2)); //$NON-NLS-1$
        
        assertEquals(2, section.getShort("FOO1", (short) 2)); //$NON-NLS-1$
        assertEquals(2, section.getShort("FOO.FOO2", (short) 2)); //$NON-NLS-1$
        assertEquals(2, section.getShort("FOO1.FOO3", (short) 2)); //$NON-NLS-1$
        assertEquals(2, section.getShort("FOO3", (short) 2)); //$NON-NLS-1$
        assertFalse(section.isShort("FOO1")); //$NON-NLS-1$
        assertFalse(section.isShort("FOO.FOO2")); //$NON-NLS-1$
        assertFalse(section.isShort("FOO1.FOO3")); //$NON-NLS-1$
        assertFalse(section.isShort("FOO3")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepDoubleOnString()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(0d, section.getDouble("FOO.BAR"), 0); //$NON-NLS-1$
        assertFalse(section.isDouble("FOO.BAR")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepDouble()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", 1d); //$NON-NLS-1$
        section.set("FOO1.FOO2", 1d); //$NON-NLS-1$
        section.set("FOO3", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(1d, section.getDouble("FOO"), 0); //$NON-NLS-1$
        assertEquals(1d, section.getDouble("FOO1.FOO2"), 0); //$NON-NLS-1$
        assertTrue(section.isDouble("FOO")); //$NON-NLS-1$
        assertTrue(section.isDouble("FOO1.FOO2")); //$NON-NLS-1$
        assertEquals(0d, section.getDouble("FOO3"), 0); //$NON-NLS-1$
        
        assertEquals(1d, section.getDouble("FOO", 2d), 0); //$NON-NLS-1$
        assertEquals(1d, section.getDouble("FOO1.FOO2", 2d), 0); //$NON-NLS-1$
        
        assertEquals(2d, section.getDouble("FOO1", 2d), 0); //$NON-NLS-1$
        assertEquals(2d, section.getDouble("FOO.FOO2", 2d), 0); //$NON-NLS-1$
        assertEquals(2d, section.getDouble("FOO1.FOO3", 2d), 0); //$NON-NLS-1$
        assertEquals(2d, section.getDouble("FOO3", 2d), 0); //$NON-NLS-1$
        assertFalse(section.isDouble("FOO1")); //$NON-NLS-1$
        assertFalse(section.isDouble("FOO.FOO2")); //$NON-NLS-1$
        assertFalse(section.isDouble("FOO1.FOO3")); //$NON-NLS-1$
        assertFalse(section.isDouble("FOO3")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepFloatOnString()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(0f, section.getFloat("FOO.BAR"), 0); //$NON-NLS-1$
        assertFalse(section.isFloat("FOO.BAR")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepFloat()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", 1f); //$NON-NLS-1$
        section.set("FOO1.FOO2", 1f); //$NON-NLS-1$
        section.set("FOO3", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(1f, section.getFloat("FOO"), 0); //$NON-NLS-1$
        assertEquals(1f, section.getFloat("FOO1.FOO2"), 0); //$NON-NLS-1$
        assertTrue(section.isFloat("FOO")); //$NON-NLS-1$
        assertTrue(section.isFloat("FOO1.FOO2")); //$NON-NLS-1$
        assertEquals(0f, section.getFloat("FOO3"), 0); //$NON-NLS-1$
        
        assertEquals(1f, section.getFloat("FOO", 2f), 0); //$NON-NLS-1$
        assertEquals(1f, section.getFloat("FOO1.FOO2", 2f), 0); //$NON-NLS-1$
        
        assertEquals(2f, section.getFloat("FOO1", 2f), 0); //$NON-NLS-1$
        assertEquals(2f, section.getFloat("FOO.FOO2", 2f), 0); //$NON-NLS-1$
        assertEquals(2f, section.getFloat("FOO1.FOO3", 2f), 0); //$NON-NLS-1$
        assertEquals(2f, section.getFloat("FOO3", 2f), 0); //$NON-NLS-1$
        assertFalse(section.isFloat("FOO1")); //$NON-NLS-1$
        assertFalse(section.isFloat("FOO.FOO2")); //$NON-NLS-1$
        assertFalse(section.isFloat("FOO1.FOO3")); //$NON-NLS-1$
        assertFalse(section.isFloat("FOO3")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepBoolOnString()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(false, section.getBoolean("FOO.BAR")); //$NON-NLS-1$
        assertFalse(section.isBoolean("FOO.BAR")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepBool()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", true); //$NON-NLS-1$
        section.set("FOO1.FOO2", true); //$NON-NLS-1$
        section.set("FOO3", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(true, section.getBoolean("FOO")); //$NON-NLS-1$
        assertEquals(true, section.getBoolean("FOO1.FOO2")); //$NON-NLS-1$
        assertTrue(section.isBoolean("FOO")); //$NON-NLS-1$
        assertTrue(section.isBoolean("FOO1.FOO2")); //$NON-NLS-1$
        assertEquals(false, section.getBoolean("FOO3")); //$NON-NLS-1$
        
        assertEquals(true, section.getBoolean("FOO", false)); //$NON-NLS-1$
        assertEquals(true, section.getBoolean("FOO1.FOO2", false)); //$NON-NLS-1$
        
        assertEquals(false, section.getBoolean("FOO1", false)); //$NON-NLS-1$
        assertEquals(false, section.getBoolean("FOO.FOO2", false)); //$NON-NLS-1$
        assertEquals(false, section.getBoolean("FOO1.FOO3", false)); //$NON-NLS-1$
        assertEquals(false, section.getBoolean("FOO3", false)); //$NON-NLS-1$
        assertFalse(section.isBoolean("FOO1")); //$NON-NLS-1$
        assertFalse(section.isBoolean("FOO.FOO2")); //$NON-NLS-1$
        assertFalse(section.isBoolean("FOO1.FOO3")); //$NON-NLS-1$
        assertFalse(section.isBoolean("FOO3")); //$NON-NLS-1$
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
    public void testGetList()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO.A", 1); //$NON-NLS-1$
        section.set("FOO.B", 2); //$NON-NLS-1$
        section.set("FOO.C", 3); //$NON-NLS-1$
        final List<?> result = section.getPrimitiveList("FOO"); //$NON-NLS-1$
        assertEquals(3, result.size());
        assertTrue(result.contains(1));
        assertTrue(result.contains(2));
        assertTrue(result.contains(3));
        assertNull(section.getPrimitiveList("BAR")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetListWithDefaults()
    {
        final List<Integer> list = new ArrayList<>();
        list.add(4);
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO.A", 1); //$NON-NLS-1$
        section.set("FOO.B", 2); //$NON-NLS-1$
        section.set("FOO.C", 3); //$NON-NLS-1$
        final List<?> result = section.getPrimitiveList("FOO", list); //$NON-NLS-1$
        assertEquals(3, result.size());
        assertTrue(result.contains(1));
        assertTrue(result.contains(2));
        assertTrue(result.contains(3));
        assertSame(list, section.getPrimitiveList("BAR", list)); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testIsList()
    {
        final List<Integer> list = new ArrayList<>();
        list.add(4);
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO.A", 1); //$NON-NLS-1$
        section.set("FOO.B", 2); //$NON-NLS-1$
        section.set("FOO.C", 3); //$NON-NLS-1$
        assertTrue(section.isList("FOO")); //$NON-NLS-1$
        assertFalse(section.isList("BAR")); //$NON-NLS-1$
        assertFalse(section.isList("FOO.A")); //$NON-NLS-1$
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
    public void testGetPrimitiveMapList()
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
        assertEquals(maplist, section.getPrimitiveMapList("FOO")); //$NON-NLS-1$
        assertNull(section.getPrimitiveMapList("BAR")); //$NON-NLS-1$
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
    public void testGetPrimitiveListMap()
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
        assertEquals(listmap, section.getPrimitiveListMap("FOO")); //$NON-NLS-1$
        assertNull(section.getPrimitiveListMap("FOO2")); //$NON-NLS-1$
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
    public void testGetPrimitiveMap()
    {
        final Map<String, Integer> map1 = new HashMap<>();
        map1.put("A1", 1); //$NON-NLS-1$
        map1.put("A2", 2); //$NON-NLS-1$
        final MemoryDataSection section = new MemoryDataSection();
        section.setPrimitiveMap("FOO", map1); //$NON-NLS-1$
        assertEquals(map1, section.getPrimitiveMap("FOO")); //$NON-NLS-1$
        assertNull(section.getPrimitiveMap("FOO2")); //$NON-NLS-1$
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
    public void testGetFragmentMap()
    {
        final Map<String, PlayerData> map1 = new HashMap<>();
        final PlayerData player1 = new PlayerData(UUID.randomUUID(), "FooPlayer1"); //$NON-NLS-1$
        final PlayerData player2 = new PlayerData(UUID.randomUUID(), "FooPlayer2"); //$NON-NLS-1$
        map1.put("A1", player1); //$NON-NLS-1$
        map1.put("A2", player2); //$NON-NLS-1$
        final MemoryDataSection section = new MemoryDataSection();
        section.setFragmentMap("FOO", map1); //$NON-NLS-1$
        assertEquals(map1, section.getFragmentMap(PlayerDataFragment.class, "FOO")); //$NON-NLS-1$
        assertNull(section.getFragmentMap(PlayerDataFragment.class, "FOO2")); //$NON-NLS-1$
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
    public void testGetFragmentListMap()
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
        assertEquals(listmap, section.getFragmentListMap(PlayerDataFragment.class, "FOO")); //$NON-NLS-1$
        assertNull(section.getFragmentListMap(PlayerDataFragment.class, "FOO2")); //$NON-NLS-1$
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
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetFragmentMapList()
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
        assertEquals(maplist, section.getFragmentMapList(PlayerDataFragment.class, "FOO")); //$NON-NLS-1$
        assertNull(section.getFragmentMapList(PlayerDataFragment.class, "FOO2")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetStringList()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO.item0", "A"); //$NON-NLS-1$ //$NON-NLS-2$
        section.set("FOO.item1", "B"); //$NON-NLS-1$ //$NON-NLS-2$
        final List<String> result = section.getStringList("FOO"); //$NON-NLS-1$
        assertEquals(2, result.size());
        assertTrue(result.contains("A")); //$NON-NLS-1$
        assertTrue(result.contains("B")); //$NON-NLS-1$
        assertNull(section.getStringList("BAR")); //$NON-NLS-1$
        assertNull(section.getStringList("FOO.A")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test(expected = ClassCastException.class)
    public void testGetStringListInvalid()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO.item0", "A"); //$NON-NLS-1$ //$NON-NLS-2$
        section.set("FOO.item1", "B"); //$NON-NLS-1$ //$NON-NLS-2$
        section.set("FOO.item2", 1); //$NON-NLS-1$
        section.getStringList("FOO"); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetBooleanList()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO.item0", true); //$NON-NLS-1$
        section.set("FOO.item1", false); //$NON-NLS-1$
        final List<Boolean> result = section.getBooleanList("FOO"); //$NON-NLS-1$
        assertEquals(2, result.size());
        assertTrue(result.contains(true));
        assertTrue(result.contains(false));
        assertNull(section.getBooleanList("BAR")); //$NON-NLS-1$
        assertNull(section.getBooleanList("FOO.A")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test(expected = ClassCastException.class)
    public void testGetBooleanListInvalid()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO.item0", true); //$NON-NLS-1$
        section.set("FOO.item1", false); //$NON-NLS-1$
        section.set("FOO.item2", "A"); //$NON-NLS-1$ //$NON-NLS-2$
        section.getBooleanList("FOO"); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetByteList()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO.item0", (byte) 1); //$NON-NLS-1$
        section.set("FOO.item1", (byte) 2); //$NON-NLS-1$
        final List<Byte> result = section.getByteList("FOO"); //$NON-NLS-1$
        assertEquals(2, result.size());
        assertTrue(result.contains((byte) 1));
        assertTrue(result.contains((byte) 2));
        assertNull(section.getByteList("BAR")); //$NON-NLS-1$
        assertNull(section.getByteList("FOO.A")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test(expected = ClassCastException.class)
    public void testGetByteListInvalid()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO.item0", (byte) 1); //$NON-NLS-1$
        section.set("FOO.item1", (byte) 2); //$NON-NLS-1$
        section.set("FOO.item2", "A"); //$NON-NLS-1$ //$NON-NLS-2$
        section.getByteList("FOO"); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetShortList()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO.item0", (short) 1); //$NON-NLS-1$
        section.set("FOO.item1", (short) 2); //$NON-NLS-1$
        final List<Short> result = section.getShortList("FOO"); //$NON-NLS-1$
        assertEquals(2, result.size());
        assertTrue(result.contains((short) 1));
        assertTrue(result.contains((short) 2));
        assertNull(section.getShortList("BAR")); //$NON-NLS-1$
        assertNull(section.getShortList("FOO.A")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test(expected = ClassCastException.class)
    public void testGetShortListInvalid()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO.item0", (short) 1); //$NON-NLS-1$
        section.set("FOO.item1", (short) 2); //$NON-NLS-1$
        section.set("FOO.item2", "A"); //$NON-NLS-1$ //$NON-NLS-2$
        section.getShortList("FOO"); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetIntList()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO.item0", 1); //$NON-NLS-1$
        section.set("FOO.item1", 2); //$NON-NLS-1$
        final List<Integer> result = section.getIntegerList("FOO"); //$NON-NLS-1$
        assertEquals(2, result.size());
        assertTrue(result.contains(1));
        assertTrue(result.contains(2));
        assertNull(section.getIntegerList("BAR")); //$NON-NLS-1$
        assertNull(section.getIntegerList("FOO.A")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test(expected = ClassCastException.class)
    public void testGetIntListInvalid()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO.item0", 1); //$NON-NLS-1$
        section.set("FOO.item1", 2); //$NON-NLS-1$
        section.set("FOO.item2", "A"); //$NON-NLS-1$ //$NON-NLS-2$
        section.getIntegerList("FOO"); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetLongList()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO.item0", 1l); //$NON-NLS-1$
        section.set("FOO.item1", 2l); //$NON-NLS-1$
        final List<Long> result = section.getLongList("FOO"); //$NON-NLS-1$
        assertEquals(2, result.size());
        assertTrue(result.contains(1l));
        assertTrue(result.contains(2l));
        assertNull(section.getLongList("BAR")); //$NON-NLS-1$
        assertNull(section.getLongList("FOO.A")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test(expected = ClassCastException.class)
    public void testGetLongListInvalid()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO.item0", 1l); //$NON-NLS-1$
        section.set("FOO.item1", 2l); //$NON-NLS-1$
        section.set("FOO.item2", "A"); //$NON-NLS-1$ //$NON-NLS-2$
        section.getIntegerList("FOO"); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDoubleList()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO.item0", 1d); //$NON-NLS-1$
        section.set("FOO.item1", 2d); //$NON-NLS-1$
        final List<Double> result = section.getDoubleList("FOO"); //$NON-NLS-1$
        assertEquals(2, result.size());
        assertTrue(result.contains(1d));
        assertTrue(result.contains(2d));
        assertNull(section.getDoubleList("BAR")); //$NON-NLS-1$
        assertNull(section.getDoubleList("FOO.A")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test(expected = ClassCastException.class)
    public void testGetDoubleListInvalid()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO.item0", 1d); //$NON-NLS-1$
        section.set("FOO.item1", 2d); //$NON-NLS-1$
        section.set("FOO.item2", "A"); //$NON-NLS-1$ //$NON-NLS-2$
        section.getDoubleList("FOO"); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetFloatList()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO.item0", 1f); //$NON-NLS-1$
        section.set("FOO.item1", 2f); //$NON-NLS-1$
        final List<Float> result = section.getFloatList("FOO"); //$NON-NLS-1$
        assertEquals(2, result.size());
        assertTrue(result.contains(1f));
        assertTrue(result.contains(2f));
        assertNull(section.getFloatList("BAR")); //$NON-NLS-1$
        assertNull(section.getFloatList("FOO.A")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test(expected = ClassCastException.class)
    public void testGetFloatListInvalid()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO.item0", 1f); //$NON-NLS-1$
        section.set("FOO.item1", 2f); //$NON-NLS-1$
        section.set("FOO.item2", "A"); //$NON-NLS-1$ //$NON-NLS-2$
        section.getFloatList("FOO"); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetCharList()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO.item0", '1'); //$NON-NLS-1$
        section.set("FOO.item1", '2'); //$NON-NLS-1$
        final List<Character> result = section.getCharacterList("FOO"); //$NON-NLS-1$
        assertEquals(2, result.size());
        assertTrue(result.contains('1'));
        assertTrue(result.contains('2'));
        assertNull(section.getCharacterList("BAR")); //$NON-NLS-1$
        assertNull(section.getCharacterList("FOO.A")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test(expected = ClassCastException.class)
    public void testGetCharListInvalid()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO.item0", '1'); //$NON-NLS-1$
        section.set("FOO.item1", '2'); //$NON-NLS-1$
        section.set("FOO.item2", "A"); //$NON-NLS-1$ //$NON-NLS-2$
        section.getCharacterList("FOO"); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetVectorList()
    {
        final MemoryDataSection section = new MemoryDataSection();
        final VectorData data1 = new VectorData(1, 2, 3);
        final VectorData data2 = new VectorData(2, 3, 4);
        section.set("FOO.item0", data1); //$NON-NLS-1$
        section.set("FOO.item1", data2); //$NON-NLS-1$
        final List<VectorDataFragment> result = section.getVectorList("FOO"); //$NON-NLS-1$
        assertEquals(2, result.size());
        assertTrue(result.contains(data1));
        assertTrue(result.contains(data2));
        assertNull(section.getVectorList("BAR")); //$NON-NLS-1$
        assertNull(section.getVectorList("FOO.A")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test(expected = ClassCastException.class)
    public void testGetVectorListInvalid()
    {
        final MemoryDataSection section = new MemoryDataSection();
        final VectorData data1 = new VectorData(1, 2, 3);
        final VectorData data2 = new VectorData(2, 3, 4);
        section.set("FOO.item0", data1); //$NON-NLS-1$
        section.set("FOO.item1", data2); //$NON-NLS-1$
        section.set("FOO.item2", "A"); //$NON-NLS-1$ //$NON-NLS-2$
        section.getVectorList("FOO"); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test(expected = ClassCastException.class)
    public void testGetVectorListInvalid2()
    {
        final MemoryDataSection section = new MemoryDataSection();
        final VectorData data1 = new VectorData(1, 2, 3);
        final VectorData data2 = new VectorData(2, 3, 4);
        section.set("FOO.item0", data1); //$NON-NLS-1$
        section.set("FOO.item1", data2); //$NON-NLS-1$
        section.set("FOO.item2.BAR", "A"); //$NON-NLS-1$ //$NON-NLS-2$
        section.getVectorList("FOO"); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetPlayerList()
    {
        final MemoryDataSection section = new MemoryDataSection();
        final PlayerData data1 = new PlayerData(UUID.randomUUID(), "FooPlayer"); //$NON-NLS-1$
        final PlayerData data2 = new PlayerData(UUID.randomUUID(), "BarPlayer"); //$NON-NLS-1$
        section.set("FOO.item0", data1); //$NON-NLS-1$
        section.set("FOO.item1", data2); //$NON-NLS-1$
        final List<PlayerDataFragment> result = section.getPlayerList("FOO"); //$NON-NLS-1$
        assertEquals(2, result.size());
        assertTrue(result.contains(data1));
        assertTrue(result.contains(data2));
        assertNull(section.getPlayerList("BAR")); //$NON-NLS-1$
        assertNull(section.getPlayerList("FOO.A")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test(expected = ClassCastException.class)
    public void testGetPlayerListInvalid()
    {
        final MemoryDataSection section = new MemoryDataSection();
        final PlayerData data1 = new PlayerData(UUID.randomUUID(), "FooPlayer"); //$NON-NLS-1$
        final PlayerData data2 = new PlayerData(UUID.randomUUID(), "BarPlayer"); //$NON-NLS-1$
        section.set("FOO.item0", data1); //$NON-NLS-1$
        section.set("FOO.item1", data2); //$NON-NLS-1$
        section.set("FOO.item2", "A"); //$NON-NLS-1$ //$NON-NLS-2$
        section.getPlayerList("FOO"); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test(expected = ClassCastException.class)
    public void testGetPlayerListInvalid2()
    {
        final MemoryDataSection section = new MemoryDataSection();
        final PlayerData data1 = new PlayerData(UUID.randomUUID(), "FooPlayer"); //$NON-NLS-1$
        final PlayerData data2 = new PlayerData(UUID.randomUUID(), "BarPlayer"); //$NON-NLS-1$
        section.set("FOO.item0", data1); //$NON-NLS-1$
        section.set("FOO.item1", data2); //$NON-NLS-1$
        section.set("FOO.item2.BAR", "A"); //$NON-NLS-1$ //$NON-NLS-2$
        section.getPlayerList("FOO"); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetColorList()
    {
        final MemoryDataSection section = new MemoryDataSection();
        final ColorData data1 = new ColorData((byte)1, (byte)2, (byte)3);
        final ColorData data2 = new ColorData((byte)3, (byte)4, (byte)5);
        section.set("FOO.item0", data1); //$NON-NLS-1$
        section.set("FOO.item1", data2); //$NON-NLS-1$
        final List<ColorDataFragment> result = section.getColorList("FOO"); //$NON-NLS-1$
        assertEquals(2, result.size());
        assertTrue(result.contains(data1));
        assertTrue(result.contains(data2));
        assertNull(section.getColorList("BAR")); //$NON-NLS-1$
        assertNull(section.getColorList("FOO.A")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test(expected = ClassCastException.class)
    public void testGetColorListInvalid()
    {
        final MemoryDataSection section = new MemoryDataSection();
        final ColorData data1 = new ColorData((byte)1, (byte)2, (byte)3);
        final ColorData data2 = new ColorData((byte)3, (byte)4, (byte)5);
        section.set("FOO.item0", data1); //$NON-NLS-1$
        section.set("FOO.item1", data2); //$NON-NLS-1$
        section.set("FOO.item2", "A"); //$NON-NLS-1$ //$NON-NLS-2$
        section.getColorList("FOO"); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test(expected = ClassCastException.class)
    public void testGetColorListInvalid2()
    {
        final MemoryDataSection section = new MemoryDataSection();
        final ColorData data1 = new ColorData((byte)1, (byte)2, (byte)3);
        final ColorData data2 = new ColorData((byte)3, (byte)4, (byte)5);
        section.set("FOO.item0", data1); //$NON-NLS-1$
        section.set("FOO.item1", data2); //$NON-NLS-1$
        section.set("FOO.item2.BAR", "A"); //$NON-NLS-1$ //$NON-NLS-2$
        section.getColorList("FOO"); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetItemStackList()
    {
        // save current internal state
        boolean oldLock = Whitebox.getInternalState(MemoryDataSection.class, "fragmentOverrideLock"); //$NON-NLS-1$
        final Map<Class<?>, Class<?>> oldMap = new HashMap<>(Whitebox.getInternalState(MemoryDataSection.class, "fragmentImpls")); //$NON-NLS-1$
        try
        {
            // prepare
            Whitebox.setInternalState(MemoryDataSection.class, "fragmentOverrideLock", false); //$NON-NLS-1$
            MemoryDataSection.initFragmentImplementation(ItemStackDataFragment.class, ItemStackUtil.class);
            
            // test
            final MemoryDataSection section = new MemoryDataSection();
            final ItemStackUtil data1 = new ItemStackUtil(1);
            final ItemStackUtil data2 = new ItemStackUtil(2);
            section.set("FOO.item0", data1); //$NON-NLS-1$
            section.set("FOO.item1", data2); //$NON-NLS-1$
            final List<ItemStackDataFragment> result = section.getItemList("FOO"); //$NON-NLS-1$
            assertEquals(2, result.size());
            assertTrue(result.contains(data1));
            assertTrue(result.contains(data2));
            assertNull(section.getItemList("BAR")); //$NON-NLS-1$
            assertNull(section.getItemList("FOO.A")); //$NON-NLS-1$
        }
        finally
        {
            // restore internal state
            Whitebox.setInternalState(MemoryDataSection.class, "fragmentOverrideLock", oldLock); //$NON-NLS-1$
            final Map<Class<?>, Class<?>> map = Whitebox.getInternalState(MemoryDataSection.class, "fragmentImpls"); //$NON-NLS-1$
            map.clear();
            map.putAll(oldMap);
        }
    }
    
    /**
     * Simple test case.
     */
    @Test(expected = ClassCastException.class)
    public void testGetItemStackListInvalid()
    {
        // save current internal state
        boolean oldLock = Whitebox.getInternalState(MemoryDataSection.class, "fragmentOverrideLock"); //$NON-NLS-1$
        final Map<Class<?>, Class<?>> oldMap = new HashMap<>(Whitebox.getInternalState(MemoryDataSection.class, "fragmentImpls")); //$NON-NLS-1$
        try
        {
            // prepare
            Whitebox.setInternalState(MemoryDataSection.class, "fragmentOverrideLock", false); //$NON-NLS-1$
            MemoryDataSection.initFragmentImplementation(ItemStackDataFragment.class, ItemStackUtil.class);
            
            // test
            final MemoryDataSection section = new MemoryDataSection();
            final ItemStackUtil data1 = new ItemStackUtil(1);
            final ItemStackUtil data2 = new ItemStackUtil(2);
            section.set("FOO.item0", data1); //$NON-NLS-1$
            section.set("FOO.item1", data2); //$NON-NLS-1$
            section.set("FOO.item2", "A"); //$NON-NLS-1$ //$NON-NLS-2$
            section.getItemList("FOO"); //$NON-NLS-1$
        }
        finally
        {
            // restore internal state
            Whitebox.setInternalState(MemoryDataSection.class, "fragmentOverrideLock", oldLock); //$NON-NLS-1$
            final Map<Class<?>, Class<?>> map = Whitebox.getInternalState(MemoryDataSection.class, "fragmentImpls"); //$NON-NLS-1$
            map.clear();
            map.putAll(oldMap);
        }
    }
    
    /**
     * Simple test case.
     */
    @Test(expected = ClassCastException.class)
    public void testGetItemStackListInvalid2()
    {
        // save current internal state
        boolean oldLock = Whitebox.getInternalState(MemoryDataSection.class, "fragmentOverrideLock"); //$NON-NLS-1$
        final Map<Class<?>, Class<?>> oldMap = new HashMap<>(Whitebox.getInternalState(MemoryDataSection.class, "fragmentImpls")); //$NON-NLS-1$
        try
        {
            // prepare
            Whitebox.setInternalState(MemoryDataSection.class, "fragmentOverrideLock", false); //$NON-NLS-1$
            MemoryDataSection.initFragmentImplementation(ItemStackDataFragment.class, ItemStackUtil.class);
            
            // test
            final MemoryDataSection section = new MemoryDataSection();
            final ItemStackUtil data1 = new ItemStackUtil(1);
            final ItemStackUtil data2 = new ItemStackUtil(2);
            section.set("FOO.item0", data1); //$NON-NLS-1$
            section.set("FOO.item1", data2); //$NON-NLS-1$
            section.set("FOO.item2.BAR", "A"); //$NON-NLS-1$ //$NON-NLS-2$
            section.getItemList("FOO"); //$NON-NLS-1$
        }
        finally
        {
            // restore internal state
            Whitebox.setInternalState(MemoryDataSection.class, "fragmentOverrideLock", oldLock); //$NON-NLS-1$
            final Map<Class<?>, Class<?>> map = Whitebox.getInternalState(MemoryDataSection.class, "fragmentImpls"); //$NON-NLS-1$
            map.clear();
            map.putAll(oldMap);
        }
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepVectorOnString()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertNull(section.getVector("FOO.BAR")); //$NON-NLS-1$
        assertFalse(section.isVector("FOO.BAR")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepVector()
    {
        final MemoryDataSection section = new MemoryDataSection();
        final VectorData data1 = new VectorData(1, 2, 3);
        final VectorData data2 = new VectorData(2, 3, 4);
        section.set("FOO", data1); //$NON-NLS-1$
        section.set("FOO1.FOO2", data1); //$NON-NLS-1$
        section.set("FOO3", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(data1, section.getVector("FOO")); //$NON-NLS-1$
        assertEquals(data1, section.getVector("FOO1.FOO2")); //$NON-NLS-1$
        assertTrue(section.isVector("FOO")); //$NON-NLS-1$
        assertTrue(section.isVector("FOO1.FOO2")); //$NON-NLS-1$
        assertNull(section.getVector("FOO3")); //$NON-NLS-1$
        
        assertEquals(data1, section.getVector("FOO", data2)); //$NON-NLS-1$
        assertEquals(data1, section.getVector("FOO1.FOO2", data2)); //$NON-NLS-1$
        
        assertEquals(data2, section.getVector("FOO1", data2)); //$NON-NLS-1$
        assertEquals(data2, section.getVector("FOO.FOO2", data2)); //$NON-NLS-1$
        assertEquals(data2, section.getVector("FOO1.FOO3", data2)); //$NON-NLS-1$
        assertEquals(data2, section.getVector("FOO3", data2)); //$NON-NLS-1$
        assertFalse(section.isVector("FOO1")); //$NON-NLS-1$
        assertFalse(section.isVector("FOO.FOO2")); //$NON-NLS-1$
        assertFalse(section.isVector("FOO1.FOO3")); //$NON-NLS-1$
        assertFalse(section.isVector("FOO3")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepPlayerOnString()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertNull(section.getPlayer("FOO.BAR")); //$NON-NLS-1$
        assertFalse(section.isPlayer("FOO.BAR")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepPlayer()
    {
        final MemoryDataSection section = new MemoryDataSection();
        final PlayerData data1 = new PlayerData(UUID.randomUUID(), "Player1"); //$NON-NLS-1$
        final PlayerData data2 = new PlayerData(UUID.randomUUID(), "Player2"); //$NON-NLS-1$
        section.set("FOO", data1); //$NON-NLS-1$
        section.set("FOO1.FOO2", data1); //$NON-NLS-1$
        section.set("FOO3", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(data1, section.getPlayer("FOO")); //$NON-NLS-1$
        assertEquals(data1, section.getPlayer("FOO1.FOO2")); //$NON-NLS-1$
        assertTrue(section.isPlayer("FOO")); //$NON-NLS-1$
        assertTrue(section.isPlayer("FOO1.FOO2")); //$NON-NLS-1$
        assertNull(section.getPlayer("FOO3")); //$NON-NLS-1$
        
        assertEquals(data1, section.getPlayer("FOO", data2)); //$NON-NLS-1$
        assertEquals(data1, section.getPlayer("FOO1.FOO2", data2)); //$NON-NLS-1$
        
        assertEquals(data2, section.getPlayer("FOO1", data2)); //$NON-NLS-1$
        assertEquals(data2, section.getPlayer("FOO.FOO2", data2)); //$NON-NLS-1$
        assertEquals(data2, section.getPlayer("FOO1.FOO3", data2)); //$NON-NLS-1$
        assertEquals(data2, section.getPlayer("FOO3", data2)); //$NON-NLS-1$
        assertFalse(section.isPlayer("FOO1")); //$NON-NLS-1$
        assertFalse(section.isPlayer("FOO.FOO2")); //$NON-NLS-1$
        assertFalse(section.isPlayer("FOO1.FOO3")); //$NON-NLS-1$
        assertFalse(section.isPlayer("FOO3")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepColorOnString()
    {
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertNull(section.getColor("FOO.BAR")); //$NON-NLS-1$
        assertFalse(section.isColor("FOO.BAR")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepColor()
    {
        final MemoryDataSection section = new MemoryDataSection();
        final ColorData data1 = new ColorData((byte)1, (byte)2, (byte)3);
        final ColorData data2 = new ColorData((byte)4, (byte)5, (byte)6);
        section.set("FOO", data1); //$NON-NLS-1$
        section.set("FOO1.FOO2", data1); //$NON-NLS-1$
        section.set("FOO3", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(data1, section.getColor("FOO")); //$NON-NLS-1$
        assertEquals(data1, section.getColor("FOO1.FOO2")); //$NON-NLS-1$
        assertTrue(section.isColor("FOO")); //$NON-NLS-1$
        assertTrue(section.isColor("FOO1.FOO2")); //$NON-NLS-1$
        assertNull(section.getColor("FOO3")); //$NON-NLS-1$
        
        assertEquals(data1, section.getColor("FOO", data2)); //$NON-NLS-1$
        assertEquals(data1, section.getColor("FOO1.FOO2", data2)); //$NON-NLS-1$
        
        assertEquals(data2, section.getColor("FOO1", data2)); //$NON-NLS-1$
        assertEquals(data2, section.getColor("FOO.FOO2", data2)); //$NON-NLS-1$
        assertEquals(data2, section.getColor("FOO1.FOO3", data2)); //$NON-NLS-1$
        assertEquals(data2, section.getColor("FOO3", data2)); //$NON-NLS-1$
        assertFalse(section.isColor("FOO1")); //$NON-NLS-1$
        assertFalse(section.isColor("FOO.FOO2")); //$NON-NLS-1$
        assertFalse(section.isColor("FOO1.FOO3")); //$NON-NLS-1$
        assertFalse(section.isColor("FOO3")); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepItemStackOnString()
    {
        // save current internal state
        boolean oldLock = Whitebox.getInternalState(MemoryDataSection.class, "fragmentOverrideLock"); //$NON-NLS-1$
        final Map<Class<?>, Class<?>> oldMap = new HashMap<>(Whitebox.getInternalState(MemoryDataSection.class, "fragmentImpls")); //$NON-NLS-1$
        try
        {
            // prepare
            Whitebox.setInternalState(MemoryDataSection.class, "fragmentOverrideLock", false); //$NON-NLS-1$
            MemoryDataSection.initFragmentImplementation(ItemStackDataFragment.class, ItemStackUtil.class);
            
            // test
            final MemoryDataSection section = new MemoryDataSection();
            section.set("FOO", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
            assertNull(section.getItemStack("FOO.BAR")); //$NON-NLS-1$
            assertFalse(section.isItemStack("FOO.BAR")); //$NON-NLS-1$
        }
        finally
        {
            // restore internal state
            Whitebox.setInternalState(MemoryDataSection.class, "fragmentOverrideLock", oldLock); //$NON-NLS-1$
            final Map<Class<?>, Class<?>> map = Whitebox.getInternalState(MemoryDataSection.class, "fragmentImpls"); //$NON-NLS-1$
            map.clear();
            map.putAll(oldMap);
        }
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testGetDeepItemStack()
    {
        // save current internal state
        boolean oldLock = Whitebox.getInternalState(MemoryDataSection.class, "fragmentOverrideLock"); //$NON-NLS-1$
        final Map<Class<?>, Class<?>> oldMap = new HashMap<>(Whitebox.getInternalState(MemoryDataSection.class, "fragmentImpls")); //$NON-NLS-1$
        try
        {
            // prepare
            Whitebox.setInternalState(MemoryDataSection.class, "fragmentOverrideLock", false); //$NON-NLS-1$
            MemoryDataSection.initFragmentImplementation(ItemStackDataFragment.class, ItemStackUtil.class);
            
            // test
            final MemoryDataSection section = new MemoryDataSection();
            final ItemStackUtil data1 = new ItemStackUtil(1);
            final ItemStackUtil data2 = new ItemStackUtil(2);
            section.set("FOO", data1); //$NON-NLS-1$
            section.set("FOO1.FOO2", data1); //$NON-NLS-1$
            section.set("FOO3", "BAR"); //$NON-NLS-1$ //$NON-NLS-2$
            assertEquals(data1, section.getItemStack("FOO")); //$NON-NLS-1$
            assertEquals(data1, section.getItemStack("FOO1.FOO2")); //$NON-NLS-1$
            assertTrue(section.isItemStack("FOO")); //$NON-NLS-1$
            assertTrue(section.isItemStack("FOO1.FOO2")); //$NON-NLS-1$
            assertNull(section.getItemStack("FOO3")); //$NON-NLS-1$
            
            assertEquals(data1, section.getItemStack("FOO", data2)); //$NON-NLS-1$
            assertEquals(data1, section.getItemStack("FOO1.FOO2", data2)); //$NON-NLS-1$
            
            assertEquals(data2, section.getItemStack("FOO1", data2)); //$NON-NLS-1$
            assertEquals(data2, section.getItemStack("FOO.FOO2", data2)); //$NON-NLS-1$
            assertEquals(data2, section.getItemStack("FOO1.FOO3", data2)); //$NON-NLS-1$
            assertEquals(data2, section.getItemStack("FOO3", data2)); //$NON-NLS-1$
            assertFalse(section.isItemStack("FOO1")); //$NON-NLS-1$
            assertFalse(section.isItemStack("FOO.FOO2")); //$NON-NLS-1$
            assertFalse(section.isItemStack("FOO1.FOO3")); //$NON-NLS-1$
            assertFalse(section.isItemStack("FOO3")); //$NON-NLS-1$
        }
        finally
        {
            // restore internal state
            Whitebox.setInternalState(MemoryDataSection.class, "fragmentOverrideLock", oldLock); //$NON-NLS-1$
            final Map<Class<?>, Class<?>> map = Whitebox.getInternalState(MemoryDataSection.class, "fragmentImpls"); //$NON-NLS-1$
            map.clear();
            map.putAll(oldMap);
        }
    }
    
}
