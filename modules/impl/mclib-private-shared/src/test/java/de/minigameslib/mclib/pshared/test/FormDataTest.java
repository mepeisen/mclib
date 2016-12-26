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

import de.minigameslib.mclib.pshared.FormData;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * Test case for {@link FormData}
 * 
 * @author mepeisen
 *
 */
public class FormDataTest
{
    
    /**
     * Simple test case for reading/storing data
     */
    @Test
    public void testMe()
    {
        final FormData data = new FormData();
        data.setKey("KEY"); //$NON-NLS-1$
        data.setValue("VALUE"); //$NON-NLS-1$
        assertEquals("KEY", data.getKey()); //$NON-NLS-1$
        assertEquals("VALUE", data.getValue()); //$NON-NLS-1$

        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", data); //$NON-NLS-1$
        
        final FormData data2 = section.getFragment(FormData.class, "FOO"); //$NON-NLS-1$
        assertEquals("KEY", data2.getKey()); //$NON-NLS-1$
        assertEquals("VALUE", data2.getValue()); //$NON-NLS-1$
    }
    
}