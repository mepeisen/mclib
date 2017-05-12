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

import de.minigameslib.mclib.pshared.ButtonData;
import de.minigameslib.mclib.pshared.DisplayErrorData;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * Test case for {@link DisplayErrorData}.
 * 
 * @author mepeisen
 *
 */
public class DisplayErrorDataTest
{
    
    /**
     * Simple test case for reading/storing data.
     */
    @Test
    public void testMe()
    {
        final DisplayErrorData data = new DisplayErrorData();
        data.setId("winid"); //$NON-NLS-1$
        data.setMessage("message"); //$NON-NLS-1$
        data.setTitle("title"); //$NON-NLS-1$
        final ButtonData button = new ButtonData();
        button.setLabel("label"); //$NON-NLS-1$
        data.setOkButton(button);
        assertEquals("winid", data.getId()); //$NON-NLS-1$
        assertEquals("message", data.getMessage()); //$NON-NLS-1$
        assertEquals("title", data.getTitle()); //$NON-NLS-1$
        assertEquals("label", data.getOkButton().getLabel()); //$NON-NLS-1$

        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", data); //$NON-NLS-1$
        
        final DisplayErrorData data2 = section.getFragment(DisplayErrorData.class, "FOO"); //$NON-NLS-1$
        assertEquals("winid", data2.getId()); //$NON-NLS-1$
        assertEquals("message", data2.getMessage()); //$NON-NLS-1$
        assertEquals("title", data2.getTitle()); //$NON-NLS-1$
        assertEquals("label", data2.getOkButton().getLabel()); //$NON-NLS-1$
    }
    
}
