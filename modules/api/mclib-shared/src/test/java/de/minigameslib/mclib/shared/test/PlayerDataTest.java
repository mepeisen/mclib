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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import de.minigameslib.mclib.shared.api.com.PlayerData;

/**
 * Testing {@code PlayerData}
 * 
 * @author mepeisen
 */
public class PlayerDataTest
{
    
    /**
     * Simple test case.
     */
    @Test
    public void testEmptyConstructor()
    {
        final PlayerData data1 = new PlayerData();
        final PlayerData data2 = new PlayerData();
        assertEquals(data1, data2);
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testConstructor()
    {
        final PlayerData data1 = new PlayerData(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"), "Player1"); //$NON-NLS-1$ //$NON-NLS-2$
        final PlayerData data2 = new PlayerData(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"), "Player1"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(data1, data2);
        assertEquals(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"), data1.getPlayerUUID()); //$NON-NLS-1$
        assertEquals("Player1", data1.getDisplayName()); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testEquals()
    {
        final PlayerData data1 = new PlayerData(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"), "Player1"); //$NON-NLS-1$ //$NON-NLS-2$
        final PlayerData data2 = new PlayerData(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"), "Player1"); //$NON-NLS-1$ //$NON-NLS-2$
        assertNotEquals(data1, null);
        assertNotEquals(data2, "FOO"); //$NON-NLS-1$
        assertNotEquals(data1, new PlayerData(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"), "Player2")); //$NON-NLS-1$ //$NON-NLS-2$
        assertNotEquals(data1, new PlayerData(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00e"), "Player1")); //$NON-NLS-1$ //$NON-NLS-2$
        assertNotEquals(data1, new PlayerData(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"), null)); //$NON-NLS-1$
        assertNotEquals(data1, new PlayerData(null, "Player1")); //$NON-NLS-1$
        assertNotEquals(new PlayerData(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"), null), data1); //$NON-NLS-1$
        assertNotEquals(new PlayerData(null, "Player1"), data1); //$NON-NLS-1$
    }
    
    /**
     * Simple test case.
     */
    @Test
    public void testHashCode()
    {
        final Map<PlayerData, String> map = new HashMap<>();
        map.put(new PlayerData(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"), "Player1"), "FOO"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        map.put(new PlayerData(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef003"), "Player2"), "BAR"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        map.put(new PlayerData(null, null), "BAR2"); //$NON-NLS-1$
        assertEquals("FOO", map.get(new PlayerData(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d"), "Player1"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertEquals("BAR", map.get(new PlayerData(UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef003"), "Player2"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        assertEquals("BAR2", map.get(new PlayerData(null, null))); //$NON-NLS-1$
    }
    
}
