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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

import java.io.Serializable;

import org.bukkit.inventory.ItemStack;
import org.junit.Test;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.gui.ClickGuiItem.GuiReplaceHandler;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;

/**
 * test case for {@link ClickGuiItem}.
 * 
 * @author mepeisen
 */
public class ClickGuiItemTest
{
    
    /**
     * Tests {@link ClickGuiItem#ClickGuiItem(ItemStack, LocalizedMessageInterface, de.minigameslib.mclib.api.gui.ClickGuiItem.GuiItemHandler, java.io.Serializable...)}
     */
    @Test
    public void constructorTest()
    {
        final ItemStack item = mock(ItemStack.class);
        final ItemStack clone = mock(ItemStack.class);
        when(item.clone()).thenReturn(clone);
        final LocalizedMessageInterface name = mock(LocalizedMessageInterface.class);
        final ClickGuiItem.GuiItemHandler handler = mock(ClickGuiItem.GuiItemHandler.class);
        
        final ClickGuiItem guiItem = new ClickGuiItem(item, name, handler);
        
        verify(item, times(1)).clone();
        
        assertSame(clone, guiItem.getItemStack());
        assertSame(name, guiItem.getDisplayName());
        assertArrayEquals(new Serializable[0], guiItem.getDisplayNameArgs());
        assertFalse(guiItem.isMoveable());
    }
    
    /**
     * Tests {@link ClickGuiItem#ClickGuiItem(ItemStack, boolean, GuiReplaceHandler)}.
     */
    @Test
    public void constructor2Test()
    {
        final ItemStack item = mock(ItemStack.class);
        final ItemStack clone = mock(ItemStack.class);
        when(item.clone()).thenReturn(clone);
        final GuiReplaceHandler handler = mock(GuiReplaceHandler.class);
        
        final ClickGuiItem guiItem = new ClickGuiItem(item, true, handler);
        
        verify(item, times(1)).clone();
        
        assertSame(clone, guiItem.getItemStack());
        assertNull(guiItem.getDisplayName());
        assertTrue(guiItem.isMoveable());
        
        guiItem.setMoveable(false);
        assertFalse(guiItem.isMoveable());
    }
    
    /**
     * Tests
     * {@link ClickGuiItem#replace(de.minigameslib.mclib.api.objects.McPlayerInterface, de.minigameslib.mclib.api.gui.GuiSessionInterface, de.minigameslib.mclib.api.gui.ClickGuiInterface, ItemStack)}.
     * @throws McException thrown on errors
     */
    @Test
    public void replaceTest() throws McException
    {
        final ItemStack item = mock(ItemStack.class);
        when(item.clone()).thenReturn(item);
        final GuiReplaceHandler handler = mock(GuiReplaceHandler.class);
        
        final ClickGuiItem guiItem = new ClickGuiItem(item, true, handler);
        final ItemStack stack = mock(ItemStack.class);
        final McPlayerInterface player = mock(McPlayerInterface.class);
        final GuiSessionInterface session = mock(GuiSessionInterface.class);
        final ClickGuiInterface gui = mock(ClickGuiInterface.class);
        
        guiItem.replace(player, session, gui, stack);
        verify(handler, times(1)).handle(player, session, gui, stack);
    }
    
    /**
     * Tests
     * {@link ClickGuiItem#replace(de.minigameslib.mclib.api.objects.McPlayerInterface, de.minigameslib.mclib.api.gui.GuiSessionInterface, de.minigameslib.mclib.api.gui.ClickGuiInterface, ItemStack)}.
     * @throws McException thrown on errors
     */
    @Test
    public void replaceNullTest() throws McException
    {
        final ItemStack item = mock(ItemStack.class);
        when(item.clone()).thenReturn(item);
        
        final ClickGuiItem guiItem = new ClickGuiItem(item, true, null);
        final ItemStack stack = mock(ItemStack.class);
        final McPlayerInterface player = mock(McPlayerInterface.class);
        final GuiSessionInterface session = mock(GuiSessionInterface.class);
        final ClickGuiInterface gui = mock(ClickGuiInterface.class);
        
        guiItem.replace(player, session, gui, stack);
    }
    
    /**
     * Tests {@link ClickGuiItem#ClickGuiItem(ItemStack, LocalizedMessageInterface, de.minigameslib.mclib.api.gui.ClickGuiItem.GuiItemHandler, java.io.Serializable...)}
     * @throws McException thrown on errors
     */
    @Test
    public void handlerTest() throws McException
    {
        final ItemStack item = mock(ItemStack.class);
        when(item.clone()).thenReturn(item);
        final LocalizedMessageInterface name = mock(LocalizedMessageInterface.class);
        final ClickGuiItem.GuiItemHandler handler = mock(ClickGuiItem.GuiItemHandler.class);
        
        final ClickGuiItem guiItem = new ClickGuiItem(item, name, handler);
        assertTrue(guiItem.handle(null, null, null));
        
        verify(handler, times(1)).handle(null, null, null);
    }
    
    /**
     * Tests {@link ClickGuiItem#ClickGuiItem(ItemStack, LocalizedMessageInterface, de.minigameslib.mclib.api.gui.ClickGuiItem.GuiItemHandler, java.io.Serializable...)}
     * @throws McException thrown on errors
     */
    @Test
    public void handlerNullTest() throws McException
    {
        final ItemStack item = mock(ItemStack.class);
        when(item.clone()).thenReturn(item);
        final LocalizedMessageInterface name = mock(LocalizedMessageInterface.class);
        
        final ClickGuiItem guiItem = new ClickGuiItem(item, name, null);
        assertTrue(guiItem.handle(null, null, null));
    }
    
    /**
     * Tests {@link ClickGuiItem#ClickGuiItem(ItemStack, LocalizedMessageInterface, de.minigameslib.mclib.api.gui.ClickGuiItem.GuiItemHandler, java.io.Serializable...)}
     * @throws McException thrown on errors
     */
    @Test
    public void handlerMovableTest() throws McException
    {
        final ItemStack item = mock(ItemStack.class);
        when(item.clone()).thenReturn(item);
        final LocalizedMessageInterface name = mock(LocalizedMessageInterface.class);
        final ClickGuiItem.GuiItemHandler handler = mock(ClickGuiItem.GuiItemHandler.class);
        
        final ClickGuiItem guiItem = new ClickGuiItem(item, name, handler);
        guiItem.setMoveable(true);
        assertFalse(guiItem.handle(null, null, null));
        
        verify(handler, times(1)).handle(null, null, null);
    }
    
}
