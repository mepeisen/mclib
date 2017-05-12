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

package de.minigameslib.mclib.pshared.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.minigameslib.mclib.pshared.PingData;
import de.minigameslib.mclib.pshared.PingData.ItemClass;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;
import de.minigameslib.mclib.spigottest.CommonTestUtil;

/**
 * Test case for {@link PingData}.
 * 
 * @author mepeisen
 *
 */
public class PingDataTest
{
    
    /**
     * Simple test case for reading/storing data.
     */
    @Test
    public void testMe()
    {
        final PingData data = new PingData();
        data.setApi(42);
        data.setLogin(true);
        
        data.addMeta(99, 1.0f, 2.0f).getOpaqueness().put("1",  "2"); //$NON-NLS-1$ //$NON-NLS-2$
        data.addMeta(100, 500, 3.0d, 4.5d, ItemClass.Axe);
        
        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", data); //$NON-NLS-1$
        final PingData data2 = section.getFragment(PingData.class, "FOO"); //$NON-NLS-1$
        assertNotNull(data2);
        
        assertEquals(42, data.getApi());
        assertTrue(data2.isLogin());
        
        assertEquals(1, data2.getBlocks().size());
        assertEquals(99, data2.getBlocks().get(0).getId());
        assertEquals(1.0f, data2.getBlocks().get(0).getHardness(), 0);
        assertEquals(2.0f, data2.getBlocks().get(0).getResistance(), 0);
        assertEquals(1, data2.getBlocks().get(0).getOpaqueness().size());
        assertEquals("2", data2.getBlocks().get(0).getOpaqueness().get("1")); //$NON-NLS-1$ //$NON-NLS-2$
        
        assertEquals(1, data2.getItems().size());
        assertEquals(100, data2.getItems().get(0).getId());
        assertEquals(500, data2.getItems().get(0).getDurability());
        assertEquals(3.0d, data2.getItems().get(0).getSpeed(), 0);
        assertEquals(4.5d, data2.getItems().get(0).getDamage(), 0);
        assertEquals(ItemClass.Axe, data2.getItems().get(0).getCls());
    }
    
    /**
     * Tests item class enum for code coverage.
     */
    @Test
    public void testItemClass()
    {
        CommonTestUtil.testEnumClass(ItemClass.class);
    }
    
}
