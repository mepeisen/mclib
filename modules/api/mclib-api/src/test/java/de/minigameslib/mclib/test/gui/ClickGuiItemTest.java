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
import de.minigameslib.mclib.api.gui.ClickGuiItem;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;

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
        when(item.clone()).thenReturn(item);
        final LocalizedMessageInterface name = mock(LocalizedMessageInterface.class);
        final ClickGuiItem.GuiItemHandler handler = mock(ClickGuiItem.GuiItemHandler.class);
        
        final ClickGuiItem guiItem = new ClickGuiItem(item, name, handler);
        
        verify(item, times(1)).clone();
        
        assertSame(item, guiItem.getItemStack());
        assertSame(name, guiItem.getDisplayName());
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
        guiItem.handle(null, null, null);
        
        verify(handler, times(1)).handle(null, null, null);
    }
    
}
