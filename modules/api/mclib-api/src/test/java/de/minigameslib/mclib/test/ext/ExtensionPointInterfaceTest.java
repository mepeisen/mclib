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

package de.minigameslib.mclib.test.ext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import de.minigameslib.mclib.api.ext.ExtensionInterface;
import de.minigameslib.mclib.api.ext.ExtensionPointInterface;
import de.minigameslib.mclib.api.ext.ExtensionServiceInterface;

/**
 * Tests {@link ExtensionPointInterface}.
 * 
 * @author mepeisen
 *
 */
public class ExtensionPointInterfaceTest
{
    
    /**
     * test the delegation to extension service.
     * 
     * @throws ClassNotFoundException
     *             thrown on problems
     */
    @Test
    public void testGetExtensions() throws ClassNotFoundException
    {
        final ExtensionServiceInterface esi = mock(ExtensionServiceInterface.class);
        final ExtensionInterface ex1 = mock(ExtensionInterface.class);
        final ExtensionInterface ex2 = mock(ExtensionInterface.class);
        final List<ExtensionInterface> list = Arrays.asList(ex1, ex2);
        when(esi.getExtensions(POINT)).thenReturn(list);
        Whitebox.setInternalState(Class.forName("de.minigameslib.mclib.api.ext.ExtensionServiceCache"), "SERVICES", esi); //$NON-NLS-1$ //$NON-NLS-2$
        
        final Iterator<ExtensionInterface> iter = POINT.getExtensions().iterator();
        assertTrue(iter.hasNext());
        assertSame(ex1, iter.next());
        assertTrue(iter.hasNext());
        assertSame(ex2, iter.next());
        assertFalse(iter.hasNext());
    }
    
    /**
     * dummy extension point.
     */
    private static final ExtensionPointInterface<ExtensionInterface> POINT = new ExtensionPointInterface<ExtensionInterface>() {
        
        @Override
        public String name()
        {
            return "POINT"; //$NON-NLS-1$
        }
        
        @Override
        public Class<ExtensionInterface> extensionClass()
        {
            return ExtensionInterface.class;
        }
    };
    
}
