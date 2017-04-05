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

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Item rule for dig/ harvest
 * @author mepeisen
 *
 */
public interface ItemDigInterface
{
    
    /**
     * Returns the harvest speed modified
     * @param stack
     * @param material
     * @return harvest
     */
    float getHarvestSpeed(ItemStack stack, Material material);
    
    /**
     * Returns the harvest speed modified
     * @param stack
     * @param block
     * @param variant
     * @return harvest
     */
    float getHarvestSpeed(ItemStack stack, BlockId block, BlockVariantId variant);
    
    /**
     * Returns the damage by harvesting a block
     * @param stack
     * @param block
     * @param player
     * @return harvest block modifier
     */
    int getDamageByBlock(ItemStack stack, Block block, Player player);
    
    /**
     * Checks if block can be harvested
     * @param material
     * @return true for harvest block
     */
    boolean canHarvest(Material material);
    
    /**
     * Checks if block can be harvested
     * @param block
     * @param variant
     * @return true for harvest block
     */
    boolean canHarvest(BlockId block, BlockVariantId variant);
}