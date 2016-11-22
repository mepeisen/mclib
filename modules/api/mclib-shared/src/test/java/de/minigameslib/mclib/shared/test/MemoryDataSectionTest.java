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

import org.junit.Test;

import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

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
        assertEquals("BAR", section.getValues(true).get("BAZ.FOO")); //$NON-NLS-1$ //$NON-NLS-2$
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
    
}
