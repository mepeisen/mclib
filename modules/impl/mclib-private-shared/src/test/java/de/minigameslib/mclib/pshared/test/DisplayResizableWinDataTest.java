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
import de.minigameslib.mclib.pshared.DisplayResizableWinData;
import de.minigameslib.mclib.pshared.WidgetData;
import de.minigameslib.mclib.pshared.WidgetData.Label;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * Test case for {@link DisplayResizableWinData}
 * 
 * @author mepeisen
 *
 */
public class DisplayResizableWinDataTest
{
    
    /**
     * Simple test case for reading/storing data
     */
    @Test
    public void testMe()
    {
        final DisplayResizableWinData data = new DisplayResizableWinData();
        data.setClosable(true);
        data.setId("id"); //$NON-NLS-1$
        data.setNumColumns(2);
        data.setTitle("title"); //$NON-NLS-1$
        final ButtonData button = new ButtonData();
        button.setLabel("label"); //$NON-NLS-1$
        data.getButtons().add(button);
        final WidgetData widget = new WidgetData();
        final Label label = new Label();
        label.setSpan(2);
        label.setText("label-text"); //$NON-NLS-1$
        widget.setLabel(label);
        data.getWidgets().add(widget);
        assertTrue(data.isClosable());
        assertEquals("id", data.getId()); //$NON-NLS-1$
        assertEquals(2, data.getNumColumns());
        assertEquals("title", data.getTitle()); //$NON-NLS-1$
        assertEquals(1, data.getButtons().size());
        assertEquals("label", data.getButtons().get(0).getLabel()); //$NON-NLS-1$
        assertEquals(1, data.getWidgets().size());
        assertEquals("label-text", data.getWidgets().get(0).getLabel().getText()); //$NON-NLS-1$
        assertEquals(2, data.getWidgets().get(0).getLabel().getSpan());

        final MemoryDataSection section = new MemoryDataSection();
        section.set("FOO", data); //$NON-NLS-1$
        
        final DisplayResizableWinData data2 = section.getFragment(DisplayResizableWinData.class, "FOO"); //$NON-NLS-1$
        assertTrue(data2.isClosable());
        assertEquals("id", data2.getId()); //$NON-NLS-1$
        assertEquals(2, data2.getNumColumns());
        assertEquals("title", data2.getTitle()); //$NON-NLS-1$
        assertEquals(1, data2.getButtons().size());
        assertEquals("label", data2.getButtons().get(0).getLabel()); //$NON-NLS-1$
        assertEquals(1, data2.getWidgets().size());
        assertEquals("label-text", data2.getWidgets().get(0).getLabel().getText()); //$NON-NLS-1$
        assertEquals(2, data2.getWidgets().get(0).getLabel().getSpan());
    }
    
}
