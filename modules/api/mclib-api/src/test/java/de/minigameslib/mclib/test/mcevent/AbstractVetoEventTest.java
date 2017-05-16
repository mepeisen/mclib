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

package de.minigameslib.mclib.test.mcevent;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.bukkit.event.HandlerList;
import org.junit.Test;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.mcevent.AbstractVetoEvent;

/**
 * Test case for {@link AbstractVetoEvent}.
 * 
 * @author mepeisen
 *
 */
public class AbstractVetoEventTest
{
    
    /**
     * test me.
     */
    @Test
    public void testSetCancelled()
    {
        final SomeEvent event = new SomeEvent();
        
        assertFalse(event.isCancelled());
        
        event.setCancelled(true);
        assertTrue(event.isCancelled());
        assertNull(event.getVetoReason());
        assertNull(event.getVetoReasonArgs());
    }
    
    /**
     * test me.
     */
    @Test
    public void testSetCancelled2()
    {
        final SomeEvent event = new SomeEvent();
        
        assertFalse(event.isCancelled());
        
        event.setCancelled(CommonMessages.CommandDisabled, "Foo", "bar"); //$NON-NLS-1$ //$NON-NLS-2$
        assertTrue(event.isCancelled());
        assertEquals(CommonMessages.CommandDisabled, event.getVetoReason());
        assertArrayEquals(new String[]{"Foo", "bar"}, event.getVetoReasonArgs()); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * some event.
     */
    public static final class SomeEvent extends AbstractVetoEvent
    {

        @Override
        public HandlerList getHandlers()
        {
            return null;
        }
        
    }
    
}
