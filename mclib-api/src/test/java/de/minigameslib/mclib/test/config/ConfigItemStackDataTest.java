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

package de.minigameslib.mclib.test.config;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import de.minigameslib.mclib.api.config.ConfigItemStackData;
import de.minigameslib.mclib.api.items.ItemServiceInterface;

/**
 * Tests {@link ConfigItemStackData}.
 * 
 * @author mepeisen
 *
 */
public class ConfigItemStackDataTest
{
    
    /**
     * test that fromBukkit passes to item stack service.
     * @throws ClassNotFoundException thrown on problems
     */
    @Test
    public void testToBukkit() throws ClassNotFoundException
    {
        final ItemServiceInterface isi = mock(ItemServiceInterface.class);
        Whitebox.setInternalState(Class.forName("de.minigameslib.mclib.api.items.ItemServiceCache"), "SERVICES", isi); //$NON-NLS-1$ //$NON-NLS-2$
        final ConfigItemStackData cis = mock(ConfigItemStackData.class);
        when(isi.toConfigData(any(ItemStack.class))).thenReturn(cis);
        
        assertSame(cis, ConfigItemStackData.fromBukkit(new ItemStack(Material.APPLE)));
    }
    
}
