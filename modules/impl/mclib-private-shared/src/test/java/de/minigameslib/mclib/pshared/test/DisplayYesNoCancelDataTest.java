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
import de.minigameslib.mclib.pshared.DisplayYesNoCancelData;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * Test case for {@link DisplayYesNoCancelData}.
 * 
 * @author mepeisen
 *
 */
public class DisplayYesNoCancelDataTest
{
    
    /**
     * Simple test case for reading/storing data.
     */
    @Test
    public void testMe()
    {
        final DisplayYesNoCancelData data = new DisplayYesNoCancelData();
        data.setId("winid"); //$NON-NLS-1$
        data.setMessage("message"); //$NON-NLS-1$
        data.setTitle("title"); //$NON-NLS-1$
        final ButtonData yes = new ButtonData();
        yes.setLabel("yes"); //$NON-NLS-1$
        data.setYesButton(yes);
        final ButtonData no = new ButtonData();
        no.setLabel("no"); //$NON-NLS-1$
        data.setNoButton(no);
        final ButtonData cancel = new ButtonData();
        cancel.setLabel("cancel"); //$NON-NLS-1$
        data.setCancelButton(cancel);
        assertEquals("winid", data.getId()); //$NON-NLS-1$
        assertEquals("message", data.getMessage()); //$NON-NLS-1$
        assertEquals("title", data.getTitle()); //$NON-NLS-1$
        assertEquals("yes", data.getYesButton().getLabel()); //$NON-NLS-1$
        assertEquals("no", data.getNoButton().getLabel()); //$NON-NLS-1$
        assertEquals("cancel", data.getCancelButton().getLabel()); //$NON-NLS-1$

        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", data); //$NON-NLS-1$
        
        final DisplayYesNoCancelData data2 = section.getFragment(DisplayYesNoCancelData.class, "FOO"); //$NON-NLS-1$
        assertEquals("winid", data2.getId()); //$NON-NLS-1$
        assertEquals("message", data2.getMessage()); //$NON-NLS-1$
        assertEquals("title", data2.getTitle()); //$NON-NLS-1$
        assertEquals("yes", data2.getYesButton().getLabel()); //$NON-NLS-1$
        assertEquals("no", data2.getNoButton().getLabel()); //$NON-NLS-1$
        assertEquals("cancel", data2.getCancelButton().getLabel()); //$NON-NLS-1$
    }
    
}
