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

package de.minigameslib.mclib.api.items;

import org.bukkit.block.Hopper;
import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.DataFragment;

/**
 * A rule for influencing hoppers.
 * 
 * @author mepeisen
 *
 */
public interface BlockHopperRuleInterface extends DataFragment
{
    
    /**
     * Checks if item is valid for given slot.
     * 
     * @param index
     *            slot index
     * @param stack
     *            stack size
     * @param hopper
     *            Hopper the underlying hopper
     * @return true if item is valid, false if invalid and super for invocing original method.
     */
    BooleanResult isItemValidForSlot(int index, ItemStack stack, Hopper hopper);
    
    /**
     * Update tick (player placed an item manually).
     * 
     * @param hopper
     *            Hopper the underlying hopper
     * 
     * @return void result
     */
    VoidResult update(Hopper hopper);
    
    /**
     * Special enum for void results.
     */
    public enum VoidResult
    {
        /** return from method. */
        Return,
        /** call super method. */
        CallSuper
    }
    
    /**
     * Special enum for boolean results.
     */
    public enum BooleanResult
    {
        /** represents true. */
        True,
        /** represents false. */
        False,
        /** forces invocation of super method (original tile tntiy hopper method). */
        CallSuper
    }
    
    /**
     * original behavior.
     */
    class Original extends AnnotatedDataFragment implements BlockHopperRuleInterface
    {
        
        @Override
        public BooleanResult isItemValidForSlot(int index, ItemStack stack, Hopper hopper)
        {
            return BooleanResult.CallSuper;
        }
        
        @Override
        public VoidResult update(Hopper hopper)
        {
            return VoidResult.CallSuper;
        }
        
    }
    
}
