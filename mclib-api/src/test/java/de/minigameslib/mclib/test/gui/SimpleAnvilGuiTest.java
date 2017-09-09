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

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

import org.bukkit.inventory.ItemStack;
import org.junit.Test;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.gui.AnvilGuiId;
import de.minigameslib.mclib.api.gui.SimpleAnvilGui;
import de.minigameslib.mclib.api.util.function.McConsumer;

/**
 * Tests for {@link SimpleAnvilGui}.
 * 
 * @author mepeisen
 */
public class SimpleAnvilGuiTest
{
    
    /**
     * test me.
     */
    @Test
    public void testConstructor()
    {
        final ItemStack item = mock(ItemStack.class);
        final ItemStack clone = mock(ItemStack.class);
        when(item.clone()).thenReturn(clone);
        
        final AnvilGuiId id = mock(AnvilGuiId.class);
        final SimpleAnvilGui gui = new SimpleAnvilGui(id, item, null);
        
        assertSame(id, gui.getUniqueId());
        assertSame(clone, gui.getItem());
    }
    
    /**
     * test me.
     */
    @Test
    public void testConstructor2()
    {
        final ItemStack item = mock(ItemStack.class);
        final ItemStack clone = mock(ItemStack.class);
        when(item.clone()).thenReturn(clone);
        
        final AnvilGuiId id = mock(AnvilGuiId.class);
        final SimpleAnvilGui gui = new SimpleAnvilGui(id, item, null, null);
        
        assertSame(id, gui.getUniqueId());
        assertSame(clone, gui.getItem());
    }
    
    /**
     * test me.
     * @throws McException thrown on errors.
     */
    @Test
    public void testOnInput() throws McException
    {
        final ItemStack item = mock(ItemStack.class);
        final ItemStack clone = mock(ItemStack.class);
        when(item.clone()).thenReturn(clone);
        
        final AnvilGuiId id = mock(AnvilGuiId.class);
        @SuppressWarnings("unchecked")
        final McConsumer<String> input = mock(McConsumer.class);
        final SimpleAnvilGui gui = new SimpleAnvilGui(id, item, input);
        
        gui.onInput("FOO"); //$NON-NLS-1$
        
        verify(input, times(1)).accept("FOO"); //$NON-NLS-1$
    }
    
    /**
     * test me.
     * @throws McException thrown on errors.
     */
    @Test
    public void testOnInput2() throws McException
    {
        final ItemStack item = mock(ItemStack.class);
        final ItemStack clone = mock(ItemStack.class);
        when(item.clone()).thenReturn(clone);
        
        final AnvilGuiId id = mock(AnvilGuiId.class);
        @SuppressWarnings("unchecked")
        final McConsumer<String> input = mock(McConsumer.class);
        final SimpleAnvilGui gui = new SimpleAnvilGui(id, item, null, input);
        
        gui.onInput("FOO"); //$NON-NLS-1$
        
        verify(input, times(1)).accept("FOO"); //$NON-NLS-1$
    }
    
    /**
     * test me.
     * @throws McException thrown on errors.
     */
    @Test
    public void testOnCancel() throws McException
    {
        final ItemStack item = mock(ItemStack.class);
        final ItemStack clone = mock(ItemStack.class);
        when(item.clone()).thenReturn(clone);
        
        final AnvilGuiId id = mock(AnvilGuiId.class);
        final Runnable close = mock(Runnable.class);
        final SimpleAnvilGui gui = new SimpleAnvilGui(id, item, close, null);
        
        gui.onCancel();
        
        verify(close, times(1)).run();
    }
    
    /**
     * test me.
     * @throws McException thrown on errors.
     */
    @Test
    public void testOnCancelNull() throws McException
    {
        final ItemStack item = mock(ItemStack.class);
        final ItemStack clone = mock(ItemStack.class);
        when(item.clone()).thenReturn(clone);
        
        final AnvilGuiId id = mock(AnvilGuiId.class);
        final SimpleAnvilGui gui = new SimpleAnvilGui(id, item, null, null);
        
        gui.onCancel();
    }
    
}
