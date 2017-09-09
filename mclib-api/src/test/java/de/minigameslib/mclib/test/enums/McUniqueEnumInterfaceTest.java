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

package de.minigameslib.mclib.test.enums;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import org.bukkit.plugin.Plugin;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import de.minigameslib.mclib.api.enums.EnumServiceInterface;
import de.minigameslib.mclib.api.enums.McUniqueEnumInterface;
import de.minigameslib.mclib.shared.api.com.EnumerationValue;

/**
 * Tests {@link McUniqueEnumInterface}.
 * 
 * @author mepeisen
 *
 */
public class McUniqueEnumInterfaceTest
{
    
    /**
     * test the delegation to enum service.
     * 
     * @throws ClassNotFoundException
     *             thrown on problems
     */
    @Test
    public void testGetExtensions() throws ClassNotFoundException
    {
        final EnumServiceInterface esi = mock(EnumServiceInterface.class);
        final Plugin plugin = mock(Plugin.class);
        when(plugin.getName()).thenReturn("FOO"); //$NON-NLS-1$
        when(esi.getPlugin(any(EnumerationValue.class))).thenReturn(plugin);
        Whitebox.setInternalState(Class.forName("de.minigameslib.mclib.api.enums.EnumServiceCache"), "SERVICES", esi); //$NON-NLS-1$ //$NON-NLS-2$
        
        assertSame(plugin, MyEnum.FOO.getPlugin());
        assertEquals("FOO", MyEnum.FOO.getPluginName()); //$NON-NLS-1$
    }
    
    /**
     * dummy enum.
     */
    private enum MyEnum implements McUniqueEnumInterface
    {
        /** dummy enum value. */
        FOO
    }
    
}
