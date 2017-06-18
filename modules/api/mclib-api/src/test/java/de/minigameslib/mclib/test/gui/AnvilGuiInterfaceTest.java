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

package de.minigameslib.mclib.test.gui;

import static org.junit.Assert.assertNull;

import org.bukkit.inventory.ItemStack;
import org.junit.Test;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.gui.AnvilGuiId;
import de.minigameslib.mclib.api.gui.AnvilGuiInterface;

/**
 * Tests for {@link AnvilGuiInterface}.
 * 
 * @author mepeisen
 */
public class AnvilGuiInterfaceTest
{
    
    /**
     * test me.
     */
    @Test
    public void testMe()
    {
        assertNull(new Anvil().getOutputItem());
    }
    
    /**
     * test helper.
     * @author mepeisen
     *
     */
    private static final class Anvil implements AnvilGuiInterface
    {

        /**
         * Constructor.
         */
        public Anvil()
        {
            // empty
        }

        @Override
        public AnvilGuiId getUniqueId()
        {
            return null;
        }

        @Override
        public ItemStack getItem()
        {
            return null;
        }

        @Override
        public void onInput(String input) throws McException
        {
            // empty
        }

        @Override
        public void onCancel()
        {
            // empty
        }
        
    }
    
}
