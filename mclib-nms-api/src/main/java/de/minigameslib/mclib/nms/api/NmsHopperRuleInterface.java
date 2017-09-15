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

package de.minigameslib.mclib.nms.api;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Hopper rules to be passed to nms classes.
 * 
 * @author mepeisen
 *
 */
public interface NmsHopperRuleInterface
{
    
    /**
     * Checks if item is valid for given slot.
     * 
     * @param index
     *            slot index
     * @param stack
     *            stack size
     * @return true if item is valid, false if invalid and super for invocing original method.
     */
    BooleanResult isItemValidForSlot(int index, ItemStack stack);
    
    /**
     * Update tick (player placed an item manually).
     * 
     * @return void result
     */
    VoidResult update();
    
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
     * Returns the harvest speed modified.
     * 
     * @param stack
     *            item stack that is used for harvesting.
     * @param blockId
     *            block that is harvested
     * @param variantId
     *            block variant that is harvested
     * @return harvest
     */
    float getHarvestSpeed(ItemStack stack, int blockId, int variantId);
    
    /**
     * Returns the damage by harvesting a block.
     * 
     * @param stack
     *            item stack that is used for harvesting.
     * @param blockId
     *            block that is harvested
     * @param variantId
     *            block variant that is harvested
     * @param location
     *            target location
     * @param player
     *            target player
     * @return harvest block modifier
     */
    int getDamageByBlock(ItemStack stack, int blockId, int variantId, Location location, Player player);
    
    /**
     * Checks if block can be harvested.
     * 
     * @param material
     *            bukkit material to check
     * @return true for harvest block
     */
    boolean canHarvest(Material material);
    
    /**
     * Checks if block can be harvested.
     * 
     * @param block
     *            block id
     * @param variant
     *            block variant id
     * @return true for harvest block
     */
    boolean canHarvest(int block, int variant);
    
    /**
     * Returns the amount of damage for damaging an entity.
     * 
     * @param stack
     *            item stack that was used
     * @param target
     *            target entity
     * @param player
     *            target player
     * @return damage for entity attack
     */
    int getDamageByEntity(ItemStack stack, Entity target, Player player);
    
    /**
     * Checks if item is repairable.
     * 
     * @param toRepair
     *            item that will be repaired
     * @param repair
     *            repair item to be consumed
     * @return {@code true} if repairable
     */
    boolean getIsRepairable(ItemStack toRepair, ItemStack repair);
    
}
