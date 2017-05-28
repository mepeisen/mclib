/*
    Copyright 2016 by minigameslib.de
    All rights reserved.
    If you do not own a hand-Objected commercial license from minigames.de
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Iterator;

import org.junit.Test;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.mcevent.ObjectDeleteEvent;
import de.minigameslib.mclib.api.objects.ObjectInterface;

/**
 * Test for {@link ObjectDeleteEvent}.
 * 
 * @author mepeisen
 */
public class ObjectDeleteEventTest
{
    
    /**
     * simple test.
     */
    @Test
    public void testMe()
    {
        final ObjectInterface Object = mock(ObjectInterface.class);
        final ObjectDeleteEvent evt = new ObjectDeleteEvent(Object);
        
        assertSame(evt, evt.getBukkitEvent());
        assertSame(evt.getHandlers(), ObjectDeleteEvent.getHandlerList());
        final Iterator<ObjectInterface> iter = evt.getObjects().iterator();
        assertTrue(iter.hasNext());
        assertSame(Object, iter.next());
        assertFalse(iter.hasNext());
    }
    
    /**
     * simple true stub test.
     * 
     * @throws McException
     *             thrown on error
     */
    @Test(expected = McException.class)
    public void testTrueStub() throws McException
    {
        final ObjectInterface Object = mock(ObjectInterface.class);
        final ObjectDeleteEvent evt = new ObjectDeleteEvent(Object);
        evt.when(p -> true).thenThrow(CommonMessages.InvokeIngame);
    }
    
    /**
     * simple true stub test.
     * 
     * @throws McException
     *             thrown on error
     */
    @Test
    public void testTrueStub2() throws McException
    {
        final ObjectInterface Object = mock(ObjectInterface.class);
        final ObjectDeleteEvent evt = new ObjectDeleteEvent(Object);
        evt.when(p -> true)._elseThrow(CommonMessages.InvokeIngame);
    }
    
    /**
     * simple true stub test.
     * 
     * @throws McException
     *             thrown on error
     */
    @Test(expected = McException.class)
    public void testFalseStub() throws McException
    {
        final ObjectInterface Object = mock(ObjectInterface.class);
        final ObjectDeleteEvent evt = new ObjectDeleteEvent(Object);
        evt.when(p -> false)._elseThrow(CommonMessages.InvokeIngame);
    }
    
    /**
     * simple true stub test.
     * 
     * @throws McException
     *             thrown on error
     */
    @Test
    public void testFalseStub2() throws McException
    {
        final ObjectInterface Object = mock(ObjectInterface.class);
        final ObjectDeleteEvent evt = new ObjectDeleteEvent(Object);
        evt.when(p -> false).thenThrow(CommonMessages.InvokeIngame);
    }
    
}