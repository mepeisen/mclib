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

package de.minigameslib.mclib.test.objects;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.junit.Test;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McStorage;
import de.minigameslib.mclib.api.event.McListener;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.gui.AnvilGuiInterface;
import de.minigameslib.mclib.api.gui.ClickGuiInterface;
import de.minigameslib.mclib.api.gui.GuiSessionInterface;
import de.minigameslib.mclib.api.gui.RawMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.api.objects.ZoneTypeId;
import de.minigameslib.mclib.api.perms.PermissionsInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.api.util.function.McOutgoingStubbing;
import de.minigameslib.mclib.api.util.function.McPredicate;
import de.minigameslib.mclib.shared.api.com.CommunicationEndpointId;
import de.minigameslib.mclib.shared.api.com.DataSection;


/**
 * test case for {@link McPlayerInterface}
 * 
 * @author mepeisen
 */
public class McPlayerInterfaceTest
{
    
    /**
     * Tests {@link McPlayerInterface#isInZone()}
     * 
     * @throws McException
     *             thrown on errors
     */
    @Test
    public void isInZoneTestTrue() throws McException
    {
        final ZoneInterface arena = mock(ZoneInterface.class);
        final McPlayerInterface player = new DummyPlayer(null, arena);
        
        assertTrue(player.isInZone());
    }
    
    /**
     * Tests {@link McPlayerInterface#isInZone()}
     * 
     * @throws McException
     *             thrown on errors
     */
    @Test
    public void isInArenaTestFalse() throws McException
    {
        final McPlayerInterface player = new DummyPlayer(null, null);
        
        assertFalse(player.isInZone());
    }
    
    // *****
    
    /**
     * Tests {@link McPlayerInterface#isOnline()}
     * 
     * @throws McException
     *             thrown on errors
     */
    @Test
    public void isOnlineTestTrue() throws McException
    {
        final Player bukkit = mock(Player.class);
        final McPlayerInterface player = new DummyPlayer(bukkit, null);
        
        assertTrue(player.isOnline());
    }
    
    /**
     * Tests {@link McPlayerInterface#isOnline()}
     * 
     * @throws McException
     *             thrown on errors
     */
    @Test
    public void isOnlineTestFalse() throws McException
    {
        final McPlayerInterface player = new DummyPlayer(null, null);
        
        assertFalse(player.isOnline());
    }
    
    /**
     * Dummy player for testing
     */
    private class DummyPlayer implements McPlayerInterface
    {

        /** the bukkit player. */
        private Player player;
        /** zone. */
        private ZoneInterface zone;

        /**
         * @param player
         * @param zone 
         */
        public DummyPlayer(Player player, ZoneInterface zone)
        {
            this.player = player;
            this.zone = zone;
        }

        @Override
        public Player getBukkitPlayer()
        {
            return this.player;
        }

        @Override
        public String getName()
        {
            return null;
        }

        @Override
        public OfflinePlayer getOfflinePlayer()
        {
            return null;
        }

        @Override
        public UUID getPlayerUUID()
        {
            return null;
        }

        @Override
        public void sendMessage(LocalizedMessageInterface msg, Serializable... args)
        {
            // empty
        }

        @Override
        public Locale getPreferredLocale()
        {
            return null;
        }

        @Override
        public void setPreferredLocale(Locale locale) throws McException
        {
            // empty
        }

        @Override
        public boolean checkPermission(PermissionsInterface perm)
        {
            return false;
        }

        @Override
        public McStorage getContextStorage()
        {
            return null;
        }

        @Override
        public McStorage getSessionStorage()
        {
            return null;
        }

        @Override
        public McStorage getPersistentStorage()
        {
            return null;
        }

        @Override
        public GuiSessionInterface getGuiSession()
        {
            return null;
        }

        @Override
        public GuiSessionInterface openClickGui(ClickGuiInterface gui) throws McException
        {
            return null;
        }

        @Override
        public GuiSessionInterface openAnvilGui(AnvilGuiInterface gui) throws McException
        {
            return null;
        }

        @Override
        public ZoneInterface getZone()
        {
            return this.zone;
        }

        @Override
        public McOutgoingStubbing<McPlayerInterface> when(McPredicate<McPlayerInterface> test) throws McException
        {
            return null;
        }

        @Override
        public String[] encodeMessage(LocalizedMessageInterface msg, Serializable... args)
        {
            return null;
        }

        @Override
        public boolean hasSmartGui()
        {
            return false;
        }

        @Override
        public GuiSessionInterface openSmartGui() throws McException
        {
            return null;
        }

        @Override
        public void sendToClient(CommunicationEndpointId endpoint, DataSection... data)
        {
            // empty
        }

        @Override
        public String getDisplayName()
        {
            return null;
        }

        @Override
        public void read(DataSection section)
        {
            // empty
        }

        @Override
        public void write(DataSection section)
        {
            // empty
        }

        @Override
        public boolean test(DataSection section)
        {
            return false;
        }

        @Override
        public void sendRaw(RawMessageInterface raw) throws McException
        {
            // empty
        }

        @Override
        public boolean isInsideZone(ZoneInterface z)
        {
            return false;
        }

        @Override
        public boolean isInsideRandomZone(ZoneInterface... z)
        {
            return false;
        }

        @Override
        public boolean isInsideAllZones(ZoneInterface... z)
        {
            return false;
        }

        @Override
        public ZoneInterface getZone(ZoneTypeId... type)
        {
            return null;
        }

        @Override
        public boolean isInsideRandomZone(ZoneTypeId... type)
        {
            return false;
        }

        @Override
        public Collection<ZoneInterface> getZones()
        {
            return Collections.emptyList();
        }

        @Override
        public Collection<ZoneInterface> getZones(ZoneTypeId... type)
        {
            return Collections.emptyList();
        }

        @Override
        public <Evt extends MinecraftEvent<?, Evt>> void registerHandler(Plugin plugin, Class<Evt> clazz, McConsumer<Evt> handler)
        {
            // empty
        }

        @Override
        public void registerHandlers(Plugin plugin, McListener listener)
        {
            // empty
        }

        @Override
        public <Evt extends MinecraftEvent<?, Evt>> void unregisterHandler(Plugin plugin, Class<Evt> clazz, McConsumer<Evt> handler)
        {
            // empty
        }

        @Override
        public void unregisterHandlers(Plugin plugin, McListener listener)
        {
            // empty
        }
        
    }
    
}
