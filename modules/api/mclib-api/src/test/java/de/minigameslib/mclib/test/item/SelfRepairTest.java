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

package de.minigameslib.mclib.test.item;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;

import de.minigameslib.mclib.api.items.SelfRepair;

/**
 * Tests for {@link SelfRepair}.
 * 
 * @author mepeisen
 *
 */
public class SelfRepairTest
{
    
    /**
     * test me.
     */
    @Test
    public void testMe()
    {
        final SelfRepair repair = new SelfRepair();
        final ItemStack stack1 = new ItemStack(Material.IRON_SWORD);
        final ItemStack stack2 = new ItemStack(Material.IRON_SWORD);
        final ItemStack stack3 = new ItemStack(Material.GOLD_SWORD);
        final ItemStack stack4 = new ItemStack(Material.GOLD_SWORD);
        
        assertTrue(repair.getIsRepairable(stack1, stack2));
        assertTrue(repair.getIsRepairable(stack3, stack4));
        
        assertFalse(repair.getIsRepairable(stack1, stack3));
        assertFalse(repair.getIsRepairable(stack3, stack1));
    }
    
}
