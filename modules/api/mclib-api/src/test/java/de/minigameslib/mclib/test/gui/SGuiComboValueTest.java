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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.Serializable;

import org.junit.Test;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.gui.SGuiComboValue;

/**
 * Tests for {@link SGuiComboValue}.
 * 
 * @author mepeisen
 */
public class SGuiComboValueTest
{
    
    /**
     * test me.
     */
    @Test
    public void testMe()
    {
        final SGuiComboValue combo = new SGuiComboValue("KEY", CommonMessages.BrokenObjectType, "BAR", "BAZ"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        
        assertEquals("KEY", combo.getKey()); //$NON-NLS-1$
        assertEquals(CommonMessages.BrokenObjectType, combo.getValue());
        assertArrayEquals(new Serializable[]{"BAR", "BAZ"}, combo.getArgs()); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
}