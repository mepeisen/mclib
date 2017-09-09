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

package de.minigameslib.mclib.test.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import de.minigameslib.mclib.api.gui.ClickGuiId;
import de.minigameslib.mclib.api.gui.ClickGuiPageInterface;
import de.minigameslib.mclib.api.gui.SimpleClickGui;

/**
 * Tests for {@link SimpleClickGui}.
 * 
 * @author mepeisen
 */
public class SimpleClickGuiTest
{
    
    /**
     * test me.
     */
    @Test
    public void testMe()
    {
        final ClickGuiId id = mock(ClickGuiId.class);
        final ClickGuiPageInterface page = mock(ClickGuiPageInterface.class);
        final int lineCount = 4;
        final SimpleClickGui gui = new SimpleClickGui(id, page, lineCount);
        
        assertSame(id, gui.getUniqueId());
        assertSame(page, gui.getInitialPage());
        assertEquals(lineCount, gui.getLineCount());
    }
    
}
