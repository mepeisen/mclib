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

package de.minigameslib.mclib.test.schem;

import static org.junit.Assert.assertSame;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.lang.reflect.Constructor;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.ServicesManager;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.reflect.Whitebox;

import de.minigameslib.mclib.api.schem.SchemataServiceInterface;

/**
 * Tests SchemataServiceCache.
 * 
 * @author mepeisen
 *
 */
public class SchemataServiceCacheTest
{
    
    /**
     * test constructor for code coverage.
     * 
     * @throws Exception
     *             thrown on problems
     */
    @Test
    public void testConstructor() throws Exception
    {
        final Constructor<?> ctor = Class.forName("de.minigameslib.mclib.api.schem.SchemataServiceCache").getDeclaredConstructors()[0]; //$NON-NLS-1$
        ctor.setAccessible(true);
        ctor.newInstance();
    }
    
    /**
     * test the services cache.
     * 
     * @throws ClassNotFoundException
     *             thrown on problems
     */
    @Test
    public void testCache() throws ClassNotFoundException
    {
        Whitebox.setInternalState(Class.forName("de.minigameslib.mclib.api.schem.SchemataServiceCache"), "SERVICES", (Object) null); //$NON-NLS-1$ //$NON-NLS-2$
        
        final ServicesManager manager = mock(ServicesManager.class);
        final Server server = mock(Server.class);
        when(server.getServicesManager()).thenReturn(manager);
        Whitebox.setInternalState(Bukkit.class, "server", server); //$NON-NLS-1$
        
        when(manager.load(SchemataServiceInterface.class)).thenAnswer(new Answer<SchemataServiceInterface>() {
            @Override
            public SchemataServiceInterface answer(InvocationOnMock invocation) throws Throwable
            {
                return mock(SchemataServiceInterface.class);
            }
        });
        
        // should return the same objects even if the underlying bukkit impl will return some other
        // object (Answer returns new instance every invocation)
        final SchemataServiceInterface csi1 = SchemataServiceInterface.instance();
        final SchemataServiceInterface csi2 = SchemataServiceInterface.instance();
        
        assertSame(csi1, csi2);
    }
    
}
