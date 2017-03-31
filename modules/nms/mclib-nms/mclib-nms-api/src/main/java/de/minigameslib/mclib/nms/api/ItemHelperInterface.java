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

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

/**
 * A helper for item stacks
 * 
 * @author mepeisen
 */
public interface ItemHelperInterface
{
    
    /**
     * Adds custom data to given item stack
     * @param stack
     * @param plugin
     * @param key
     * @param value
     */
    void addCustomData(ItemStack stack, String plugin, String key, String value);
    
    /**
     * Receives custom data from given item stack
     * @param stack
     * @param plugin
     * @param key
     * @return item data
     */
    String getCustomData(ItemStack stack, String plugin, String key);
    
    /**
     * Returns the variant for given block
     * @param block
     * @return numeric variant
     */
    int getVariant(Block block);
    
    /**
     * Returns the variant for given item stack
     * @param stack
     * @return variant
     */
    int getVariant(ItemStack stack);
    
    /**
     * Initializes the custom blocks
     */
    void initBlocks();
    
    /**
     * Sets the block custom type and variant
     * @param block
     * @param type
     * @param variant
     */
    void setBlockVariant(Block block, int type, int variant);
    
    /**
     * Creates a custom item stack for given block type and variant
     * @param type
     * @param variant
     * @param displayName 
     * @return item stack
     */
    ItemStack createItemStackForBlock(int type, int variant, String displayName);
    
    /**
     * Sets the display name
     * @param stack
     * @param displayName
     */
    void setDisplayName(ItemStack stack, String displayName);
    
    /**
     * Sets description
     * @param stack
     * @param description
     */
    void setDescription(ItemStack stack, String[] description);
    
    /**
     * Returns the display name
     * @param stack
     * @return display name
     */
    String getDisplayName(ItemStack stack);
    
    /**
     * Returns the description
     * @param stack
     * @return description
     */
    String[] getDescription(ItemStack stack);

    /**
     * @param random
     * @param location
     * @param blockId
     * @param meta
     * @param size
     */
    void createMinable(Random random, Location location, int blockId, int meta, int size);
    
}
