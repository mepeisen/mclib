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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.minigameslib.mclib.pshared.ButtonData;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * Test case for {@link ButtonData}
 * 
 * @author mepeisen
 *
 */
public class ButtonDataTest
{
    
    /**
     * Simple test case for reading/storing data
     */
    @Test
    public void testMe()
    {
        final ButtonData data = new ButtonData();
        data.setActionId("actionid"); //$NON-NLS-1$
        data.setCloseAction(true);
        data.setHasActionListener(true);
        data.setLabel("label"); //$NON-NLS-1$
        assertEquals("actionid", data.getActionId()); //$NON-NLS-1$
        assertTrue(data.isCloseAction());
        assertTrue(data.isHasActionListener());
        assertEquals("label", data.getLabel()); //$NON-NLS-1$

        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", data); //$NON-NLS-1$
        
        final ButtonData data2 = section.getFragment(ButtonData.class, "FOO"); //$NON-NLS-1$
        assertEquals("actionid", data2.getActionId()); //$NON-NLS-1$
        assertTrue(data2.isCloseAction());
        assertTrue(data2.isHasActionListener());
        assertEquals("label", data2.getLabel()); //$NON-NLS-1$
    }
    
}
