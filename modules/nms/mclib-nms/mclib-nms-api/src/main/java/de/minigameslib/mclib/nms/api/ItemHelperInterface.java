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

import java.util.Map;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
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
    
    /**
     * Adds given item to inventory
     * @param inventory
     * @param stack
     * @return left over items; {@code null} if item could be added completly
     */
    ItemStack addToInventory(Inventory inventory, ItemStack stack);
    
    /**
     * Sets block meta values
     * @param blockId
     * @param hardness
     * @param resistence
     * @param dropRule
     */
    void setBlockMeta(int blockId, float hardness, float resistence, NmsDropRuleInterface dropRule);

    /**
     * @param blockId
     * @param variant
     * @param stack
     * @param experience
     */
    void installFurnaceRecipe(int blockId, int variant, ItemStack stack, float experience);

    /**
     * @param material
     * @param itemStackDurability
     * @param receipe
     * @param experience
     */
    void installFurnaceRecipe(Material material, short itemStackDurability, ItemStack receipe, float experience);

    /**
     * Initializes the given item material and hack the item class
     * @param material
     */
    void initNmsItem(Material material);

    /**
     * @param material
     * @param itemStackDurability
     * @param stackSize
     */
    void setStackSize(Material material, short itemStackDurability, int stackSize);

    /**
     * @param blockId
     * @param stackSize
     */
    void setStackSize(int blockId, int stackSize);

    /**
     * @param item
     * @param amount
     * @param shape
     * @param shapedItems
     */
    void installShapedRecipe(ItemStack item, int amount, String[] shape, Map<Character, ItemStack> shapedItems);

    /**
     * @param blockId
     * @param variant
     * @param amount
     * @param shape
     * @param shapedItems
     */
    void installShapedRecipe(int blockId, int variant, int amount, String[] shape, Map<Character, ItemStack> shapedItems);

    /**
     * @param item
     * @param amount
     * @param shapelessItems
     */
    void installShapelessRecipe(ItemStack item, int amount, ItemStack[] shapelessItems);

    /**
     * @param blockId
     * @param variant
     * @param amount
     * @param shapelessItems
     */
    void installShapelessRecipe(int blockId, int variant, int amount, ItemStack[] shapelessItems);
    
}
