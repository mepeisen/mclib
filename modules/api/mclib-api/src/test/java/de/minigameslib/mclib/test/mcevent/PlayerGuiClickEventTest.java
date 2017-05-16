/*
    Copyright 2016 by minigameslib.de
    All rights reserved.
    If you do not own a hand-Entityed commercial license from minigames.de
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
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.mcevent.PlayerGuiClickEvent;
import de.minigameslib.mclib.api.objects.McPlayerInterface;

/**
 * Test for {@link PlayerGuiClickEvent}.
 * 
 * @author mepeisen
 */
public class PlayerGuiClickEventTest
{
    
    /**
     * simple test.
     */
    @Test
    public void testMe()
    {
        final ClickGuiInterface gui = mock(ClickGuiInterface.class);
        final ClickGuiItem item = mock(ClickGuiItem.class);
        final McPlayerInterface player = mock(McPlayerInterface.class);
        final PlayerGuiClickEvent evt = new PlayerGuiClickEvent(gui, player, item);
        
        assertSame(evt, evt.getBukkitEvent());
        assertSame(evt.getHandlers(), PlayerGuiClickEvent.getHandlerList());
        final Iterator<McPlayerInterface> iter = evt.getPlayers().iterator();
        assertTrue(iter.hasNext());
        assertSame(player, iter.next());
        assertFalse(iter.hasNext());
        
        assertSame(gui, evt.getGui());
        assertSame(item, evt.getItem());
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
        final ClickGuiInterface gui = mock(ClickGuiInterface.class);
        final ClickGuiItem item = mock(ClickGuiItem.class);
        final McPlayerInterface player = mock(McPlayerInterface.class);
        final PlayerGuiClickEvent evt = new PlayerGuiClickEvent(gui, player, item);
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
        final ClickGuiInterface gui = mock(ClickGuiInterface.class);
        final ClickGuiItem item = mock(ClickGuiItem.class);
        final McPlayerInterface player = mock(McPlayerInterface.class);
        final PlayerGuiClickEvent evt = new PlayerGuiClickEvent(gui, player, item);
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
        final ClickGuiInterface gui = mock(ClickGuiInterface.class);
        final ClickGuiItem item = mock(ClickGuiItem.class);
        final McPlayerInterface player = mock(McPlayerInterface.class);
        final PlayerGuiClickEvent evt = new PlayerGuiClickEvent(gui, player, item);
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
        final ClickGuiInterface gui = mock(ClickGuiInterface.class);
        final ClickGuiItem item = mock(ClickGuiItem.class);
        final McPlayerInterface player = mock(McPlayerInterface.class);
        final PlayerGuiClickEvent evt = new PlayerGuiClickEvent(gui, player, item);
        evt.when(p -> false).thenThrow(CommonMessages.InvokeIngame);
    }
    
}
