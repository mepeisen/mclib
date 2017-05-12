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

package de.minigameslib.mclib.shared.test;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import de.minigameslib.mclib.shared.api.com.CommunicationEndpointId;
import de.minigameslib.mclib.shared.api.com.CommunicationEndpointId.CommunicationServiceInterface;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * Tests for {@link CommunicationEndpointId}.
 * 
 * @author mepeisen
 *
 */
public class CommunicationEndpointIdTest
{
    
    /**
     * Tests the delegation to service.
     * 
     * @throws Exception
     *             thrown for errors
     */
    @Test
    public void testMe() throws Exception
    {
        final CommunicationServiceInterface service = mock(CommunicationServiceInterface.class);
        final Class<?> clazz = Class.forName("de.minigameslib.mclib.shared.api.com.CommunicationEndpointId$CommunicationServiceCache"); //$NON-NLS-1$
        Whitebox.invokeMethod(clazz, "init", service); //$NON-NLS-1$
        
        // simulate duplicate initialization (ignored for security reasons)
        final CommunicationServiceInterface service2 = mock(CommunicationServiceInterface.class);
        Whitebox.invokeMethod(clazz, "init", service2); //$NON-NLS-1$
        
        // only for code coverage
        clazz.newInstance();
        
        // underlying test
        final DataSection data1 = new MemoryDataSection();
        final DataSection data2 = new MemoryDataSection();
        DummyEndpoints.Dummy.send(data1, data2);
        
        verify(service, atLeastOnce()).send(DummyEndpoints.Dummy, data1, data2);
        verify(service2, never()).send(DummyEndpoints.Dummy, data1, data2);
    }
    
    /**
     * dummy endpoints for testing.
     * 
     * @author mepeisen
     *
     */
    private enum DummyEndpoints implements CommunicationEndpointId
    {
        /** dummy endpoint. */
        Dummy
    }
    
}
