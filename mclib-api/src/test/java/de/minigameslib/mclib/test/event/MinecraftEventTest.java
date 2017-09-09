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

package de.minigameslib.mclib.test.event;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;
import static org.powermock.api.mockito.PowerMockito.mock;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.junit.Test;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.objects.ComponentInterface;
import de.minigameslib.mclib.api.objects.EntityInterface;
import de.minigameslib.mclib.api.objects.HologramInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectInterface;
import de.minigameslib.mclib.api.objects.SignInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.api.util.function.McOutgoingStubbing;
import de.minigameslib.mclib.api.util.function.McPredicate;

/**
 * Tests for {@link MinecraftEvent}.
 * 
 * @author mepeisen
 *
 */
public class MinecraftEventTest
{
    
    /**
     * test me.
     */
    @Test
    public void testNulls()
    {
        final Evt1 evt = new Evt1();
        assertNull(evt.getHologram());
        assertNull(evt.getObject());
        assertNull(evt.getZone());
        assertNull(evt.getPlayer());
        assertNull(evt.getSign());
        assertNull(evt.getEntity());
        assertNull(evt.getComponent());

        assertFalse(evt.getHolograms().iterator().hasNext());
        assertFalse(evt.getObjects().iterator().hasNext());
        assertFalse(evt.getZones().iterator().hasNext());
        assertFalse(evt.getPlayers().iterator().hasNext());
        assertFalse(evt.getSigns().iterator().hasNext());
        assertFalse(evt.getEntities().iterator().hasNext());
        assertFalse(evt.getComponents().iterator().hasNext());
    }
    
    /**
     * test me.
     */
    @Test
    public void testCollections()
    {
        final Evt1 evt = mock(Evt1.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
        when(evt.getHologram()).thenReturn(mock(HologramInterface.class));
        when(evt.getObject()).thenReturn(mock(ObjectInterface.class));
        when(evt.getZone()).thenReturn(mock(ZoneInterface.class));
        when(evt.getPlayer()).thenReturn(mock(McPlayerInterface.class));
        when(evt.getSign()).thenReturn(mock(SignInterface.class));
        when(evt.getEntity()).thenReturn(mock(EntityInterface.class));
        when(evt.getComponent()).thenReturn(mock(ComponentInterface.class));

        assertTrue(evt.getHolograms().iterator().hasNext());
        assertTrue(evt.getObjects().iterator().hasNext());
        assertTrue(evt.getZones().iterator().hasNext());
        assertTrue(evt.getPlayers().iterator().hasNext());
        assertTrue(evt.getSigns().iterator().hasNext());
        assertTrue(evt.getEntities().iterator().hasNext());
        assertTrue(evt.getComponents().iterator().hasNext());
    }
    
    /**
     * test helper.
     */
    private static class Evt1 extends Event implements MinecraftEvent<Evt1, Evt1>
    {

        /**
         * Constructor.
         */
        public Evt1()
        {
            // empty
        }

        @Override
        public Evt1 getBukkitEvent()
        {
            return this;
        }

        @Override
        public McOutgoingStubbing<Evt1> when(McPredicate<Evt1> test) throws McException
        {
            return null;
        }

        @Override
        public HandlerList getHandlers()
        {
            return null;
        }
        
    }
    
}
