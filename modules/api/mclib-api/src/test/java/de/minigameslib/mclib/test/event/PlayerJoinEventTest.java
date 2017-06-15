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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import org.bukkit.event.player.PlayerJoinEvent;
import org.junit.Test;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.McPlayerJoinEvent;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.util.function.McOutgoingStubbing;
import de.minigameslib.mclib.api.util.function.McPredicate;

/**
 * Tests for {@link PlayerJoinEvent}.
 * 
 * @author mepeisen
 *
 */
public class PlayerJoinEventTest
{
    
    /**
     * Tests.
     */
    @Test
    public void testMe()
    {
        final PlayerJoinEvent bukkit = mock(PlayerJoinEvent.class);
        final McPlayerInterface player = mock(McPlayerInterface.class);
        final MyEvent event = new MyEvent(bukkit, player);
        when(player.encodeMessage(any(LocalizedMessageInterface.class))).thenReturn(new String[]{"FOO"}); //$NON-NLS-1$
        
        event.setJoinMessage(CommonMessages.InvokeIngame);
        
        verify(bukkit, times(1)).setJoinMessage("FOO"); //$NON-NLS-1$
    }
    
    /**
     * my event.
     */
    private static final class MyEvent implements McPlayerJoinEvent
    {
        
        /** bukkit event. */
        private PlayerJoinEvent evt;
        
        /** player. */
        private McPlayerInterface player;

        /**
         * Constructor.
         * @param evt bukkit event.
         * @param player player.
         */
        public MyEvent(PlayerJoinEvent evt, McPlayerInterface player)
        {
            this.evt = evt;
            this.player = player;
        }
        
        @Override
        public McPlayerInterface getPlayer()
        {
            return this.player;
        }

        @Override
        public PlayerJoinEvent getBukkitEvent()
        {
            return this.evt;
        }

        @Override
        public McOutgoingStubbing<McPlayerJoinEvent> when(McPredicate<McPlayerJoinEvent> test) throws McException
        {
            return null;
        }
        
    }
    
}
