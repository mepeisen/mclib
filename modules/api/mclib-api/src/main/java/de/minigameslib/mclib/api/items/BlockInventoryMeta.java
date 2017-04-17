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

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.bukkit.Location;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.McPlayerInterface;

/**
 * @author mepeisen
 *
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface BlockInventoryMeta
{
    
    /**
     * the inventory size in slots
     * @return number of slots
     */
    int size();
    
    /**
     * {@code true} for fixed size inventories
     * @return {@code true} if inventory size cannot be changed 
     */
    boolean fixed() default true;
    
    /**
     * {@code true} for shared inventories
     * @return {@code true} if all players share the inventory
     */
    boolean shared() default true;
    
    /**
     * block inventory handler
     * @return block inventory handler
     */
    Class<? extends BlockInventoryInterface> blockInventory();
    
    /**
     * Block inventory handler for managing inventories
     */
    public interface BlockInventoryInterface
    {
        
        /**
         * Returns an inventory to be used for this block
         * @param block
         * @param variant
         * @param location
         * @param player
         * @param initialSize 
         * @param fixed 
         * @param shared 
         * @throws McException thrown if player is not allowed to create an inventory at this position
         */
        void createInventory(BlockId block, BlockVariantId variant, Location location, McPlayerInterface player, int initialSize, boolean fixed, boolean shared) throws McException;
        
        /**
         * Returns an inventory to be used for opening.
         * This method will be invoked after creation but without opening the inventory to initialize the inventory for player placing the block.
         * @param block
         * @param variant
         * @param location
         * @param player
         * @return {@code null} for not opening any inventory (silently drop event)
         * @throws McException thrown if player is not allowed to open an inventory
         */
        InventoryId getInventory(BlockId block, BlockVariantId variant, Location location, McPlayerInterface player) throws McException;

        /**
         * break handler
         * @param block 
         * @param variant 
         * @param location
         */
        void onBreak(BlockId block, BlockVariantId variant, Location location);
        
    }
    
}
