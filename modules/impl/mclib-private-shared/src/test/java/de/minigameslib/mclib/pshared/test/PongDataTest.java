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

import org.junit.Test;

import de.minigameslib.mclib.pshared.PongData;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * Test case for {@link PongData}
 * 
 * @author mepeisen
 *
 */
public class PongDataTest
{
    
    /**
     * Simple test case for reading/storing data
     */
    @Test
    public void testMe()
    {
        final PongData data = new PongData();
        data.getClientExtensions().put("winid", 16); //$NON-NLS-1$
        assertEquals(1, data.getClientExtensions().size());
        assertEquals(16, data.getClientExtensions().get("winid").intValue()); //$NON-NLS-1$

        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", data); //$NON-NLS-1$
        
        final PongData data2 = section.getFragment(PongData.class, "FOO"); //$NON-NLS-1$
        assertEquals(1, data2.getClientExtensions().size());
        assertEquals("winid", data2.getClientExtensions().get(0)); //$NON-NLS-1$
    }
    
}
