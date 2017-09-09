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

import org.bukkit.event.Event;
import org.junit.Test;

import de.minigameslib.mclib.api.McContext;
import de.minigameslib.mclib.api.cmd.CommandInterface;
import de.minigameslib.mclib.api.objects.ComponentInterface;
import de.minigameslib.mclib.api.objects.EntityInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectInterface;
import de.minigameslib.mclib.api.objects.SignInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;


/**
 * test case for {@link McContext}.
 * 
 * @author mepeisen
 */
public class McContextTest
{
    
    /**
     * Tests {@link McContext#getCurrentZone()}.
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
     * Tests {@link McContext#getCurrentCommand()}.
     */
    @Test
    public void getCurrentCommandTest()
    {
        final CommandInterface command = mock(CommandInterface.class);
        final McContextImpl ctx = mock(McContextImpl.class);
        when(ctx.getCurrentCommand()).thenCallRealMethod();
        when(ctx.getContext(CommandInterface.class)).thenReturn(command);
        
        assertSame(command, ctx.getCurrentCommand());
    }
    
    /**
     * Tests {@link McContext#getCurrentSign()}.
     */
    @Test
    public void getCurrentSignTest()
    {
        final SignInterface sign = mock(SignInterface.class);
        final McContextImpl ctx = mock(McContextImpl.class);
        when(ctx.getCurrentSign()).thenCallRealMethod();
        when(ctx.getContext(SignInterface.class)).thenReturn(sign);
        
        assertSame(sign, ctx.getCurrentSign());
    }
    
    /**
     * Tests {@link McContext#getCurrentObject()}.
     */
    @Test
    public void getCurrentObjectTest()
    {
        final ObjectInterface object = mock(ObjectInterface.class);
        final McContextImpl ctx = mock(McContextImpl.class);
        when(ctx.getCurrentObject()).thenCallRealMethod();
        when(ctx.getContext(ObjectInterface.class)).thenReturn(object);
        
        assertSame(object, ctx.getCurrentObject());
    }
    
    /**
     * Tests {@link McContext#getCurrentEntity()}.
     */
    @Test
    public void getCurrentEntityTest()
    {
        final EntityInterface entity = mock(EntityInterface.class);
        final McContextImpl ctx = mock(McContextImpl.class);
        when(ctx.getCurrentEntity()).thenCallRealMethod();
        when(ctx.getContext(EntityInterface.class)).thenReturn(entity);
        
        assertSame(entity, ctx.getCurrentEntity());
    }
    
    /**
     * Tests {@link McContext#getCurrentEvent()}.
     */
    @Test
    public void getCurrentEventTest()
    {
        final Event event = mock(Event.class);
        final McContextImpl ctx = mock(McContextImpl.class);
        when(ctx.getCurrentEvent()).thenCallRealMethod();
        when(ctx.getContext(Event.class)).thenReturn(event);
        
        assertSame(event, ctx.getCurrentEvent());
    }
    
    /**
     * Tests {@link McContext#getCurrentComponent()}.
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
     * Tests {@link McContext#getCurrentPlayer()}.
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
