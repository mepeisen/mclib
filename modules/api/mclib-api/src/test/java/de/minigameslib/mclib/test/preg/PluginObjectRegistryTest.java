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

package de.minigameslib.mclib.test.preg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.Set;

import org.bukkit.plugin.Plugin;
import org.junit.Test;

import de.minigameslib.mclib.api.preg.PluginObjectRegistry;

/**
 * Tests for {@link PluginObjectRegistry}.
 * 
 * @author mepeisen
 *
 */
public class PluginObjectRegistryTest
{
    
    /**
     * test me.
     */
    @Test
    public void testMe()
    {
        final Plugin plugin1 = mock(Plugin.class);
        when(plugin1.getName()).thenReturn("plugin1"); //$NON-NLS-1$
        final Plugin plugin2 = mock(Plugin.class);
        when(plugin2.getName()).thenReturn("plugin2"); //$NON-NLS-1$
        
        final TestRegistry registry = new TestRegistry();
        registry.register(plugin1, "KEY1", "VAL1a"); //$NON-NLS-1$ //$NON-NLS-2$
        registry.register(plugin1, "KEY1", "VAL1b"); //$NON-NLS-1$ //$NON-NLS-2$
        registry.register(plugin1, "KEY2", "VAL2a"); //$NON-NLS-1$ //$NON-NLS-2$
        registry.register(plugin2, "KEY1", "VAL1c"); //$NON-NLS-1$ //$NON-NLS-2$
        registry.register(plugin2, "KEY2", "VAL2b"); //$NON-NLS-1$ //$NON-NLS-2$
        registry.register(plugin2, "KEY2", "VAL2c"); //$NON-NLS-1$ //$NON-NLS-2$
        
        Set<String> content = registry.get("KEY1"); //$NON-NLS-1$
        assertEquals(3, content.size());
        assertTrue(content.contains("VAL1a")); //$NON-NLS-1$
        assertTrue(content.contains("VAL1b")); //$NON-NLS-1$
        assertTrue(content.contains("VAL1c")); //$NON-NLS-1$
        content = registry.get("KEY2"); //$NON-NLS-1$
        assertEquals(3, content.size());
        assertTrue(content.contains("VAL2a")); //$NON-NLS-1$
        assertTrue(content.contains("VAL2b")); //$NON-NLS-1$
        assertTrue(content.contains("VAL2c")); //$NON-NLS-1$
        
        registry.unregister(plugin1, "KEY1", "VAL1a"); //$NON-NLS-1$ //$NON-NLS-2$
        content = registry.get("KEY1"); //$NON-NLS-1$
        assertEquals(2, content.size());
        assertTrue(content.contains("VAL1b")); //$NON-NLS-1$
        assertTrue(content.contains("VAL1c")); //$NON-NLS-1$
        content = registry.get("KEY2"); //$NON-NLS-1$
        assertEquals(3, content.size());
        assertTrue(content.contains("VAL2a")); //$NON-NLS-1$
        assertTrue(content.contains("VAL2b")); //$NON-NLS-1$
        assertTrue(content.contains("VAL2c")); //$NON-NLS-1$
        
        registry.unregisterAll(plugin2);
        assertEquals(1, content.size());
        content = registry.get("KEY1"); //$NON-NLS-1$
        assertTrue(content.contains("VAL1b")); //$NON-NLS-1$
        assertEquals(1, content.size());
        content = registry.get("KEY2"); //$NON-NLS-1$
        assertTrue(content.contains("VAL2a")); //$NON-NLS-1$
        
        registry.unregisterAll(plugin1);
        content = registry.get("KEY1"); //$NON-NLS-1$
        assertTrue(content.isEmpty());
        content = registry.get("KEY2"); //$NON-NLS-1$
        assertTrue(content.isEmpty());
        
        registry.unregisterAll(plugin1);
        registry.unregisterAll(plugin2);
        content = registry.get("KEY1"); //$NON-NLS-1$
        assertTrue(content.isEmpty());
        content = registry.get("KEY2"); //$NON-NLS-1$
        assertTrue(content.isEmpty());
        
        registry.register(plugin1, "KEY1", "VAL1a"); //$NON-NLS-1$ //$NON-NLS-2$
        registry.register(plugin1, "KEY1", "VAL1a"); //$NON-NLS-1$ //$NON-NLS-2$
        registry.unregister(plugin1, "KEY1", "VAL1a"); //$NON-NLS-1$ //$NON-NLS-2$
        content = registry.get("KEY1"); //$NON-NLS-1$
        assertTrue(content.isEmpty());
        registry.unregister(plugin1, "KEY2", "VAL2a"); //$NON-NLS-1$ //$NON-NLS-2$
        content = registry.get("KEY2"); //$NON-NLS-1$
        assertTrue(content.isEmpty());
    }
    
    /**
     * Test registry.
     */
    private static final class TestRegistry extends PluginObjectRegistry<String, String>
    {
        /**
         * Constructor.
         */
        public TestRegistry()
        {
            // empty
        }
    }
    
}
