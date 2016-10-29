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

package de.minigameslib.mclib.test;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import org.junit.Test;

import de.minigameslib.mclib.api.McContext;
import de.minigameslib.mclib.api.objects.ComponentInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;


/**
 * test case for {@link McContext}
 * 
 * @author mepeisen
 */
public class McContextTest
{
    
    /**
     * Tests {@link McContext#getCurrentZone()}
     */
    @Test
    public void getCurrentZoneTest()
    {
        final ZoneInterface arena = mock(ZoneInterface.class);
        final McContextImpl ctx = mock(McContextImpl.class);
        when(ctx.getCurrentZone()).thenCallRealMethod();
        when(ctx.getContext(ZoneInterface.class)).thenReturn(arena);
        
        assertSame(arena, ctx.getCurrentZone());
    }
    
    /**
     * Tests {@link McContext#getCurrentComponent()}
     */
    @Test
    public void getCurrentComponentTest()
    {
        final ComponentInterface comp = mock(ComponentInterface.class);
        final McContextImpl ctx = mock(McContextImpl.class);
        when(ctx.getCurrentComponent()).thenCallRealMethod();
        when(ctx.getContext(ComponentInterface.class)).thenReturn(comp);
        
        assertSame(comp, ctx.getCurrentComponent());
    }
    
    /**
     * Tests {@link McContext#getCurrentPlayer()}
     */
    @Test
    public void getCurrentPlayerTest()
    {
        final McPlayerInterface player = mock(McPlayerInterface.class);
        final McContextImpl ctx = mock(McContextImpl.class);
        when(ctx.getCurrentPlayer()).thenCallRealMethod();
        when(ctx.getContext(McPlayerInterface.class)).thenReturn(player);
        
        assertSame(player, ctx.getCurrentPlayer());
    }
    
    /** helper. */
    private abstract class McContextImpl implements McContext
    {
        // empty
    }
    
}
