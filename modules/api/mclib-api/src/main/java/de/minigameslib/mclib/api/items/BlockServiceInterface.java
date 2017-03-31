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

import java.io.Serializable;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;

/**
 * A service to register custom blocks.
 * 
 * @author mepeisen
 */
public interface BlockServiceInterface
{
    
    /**
     * Returns the item services instance.
     * 
     * @return item services instance.
     */
    static BlockServiceInterface instance()
    {
        return BlockServiceCache.get();
    }
    
    /**
     * Creates a new item stack for given block id
     * @param id the block
     * @param variant the block variant
     * @return item stack
     */
    ItemStack createItem(BlockId id, BlockVariantId variant);
    
    /**
     * Creates a new item stack for given block id
     * @param id the block
     * @param variant the block variant
     * @param name the display name (already localized)
     * @return item stack
     */
    ItemStack createItem(BlockId id, BlockVariantId variant, String name);
    
    /**
     * Creates a new item stack for given block id
     * @param id the block
     * @param variant the block variant
     * @param name the display name (already localized)
     * @param nameArgs name arguments
     * @return item stack
     */
    ItemStack createItem(BlockId id, BlockVariantId variant, LocalizedMessageInterface name, Serializable... nameArgs);
    
    /**
     * Creates a new item stack for given block id
     * @param player the player used for localization
     * @param id the block
     * @param variant the block variant
     * @return item stack
     */
    ItemStack createItem(McPlayerInterface player, BlockId id, BlockVariantId variant);
    
    /**
     * Creates a new item stack for given block id
     * @param player the player used for localization
     * @param id the block
     * @param variant the block variant
     * @param name the display name (already localized)
     * @param nameArgs name arguments
     * @return item stack
     */
    ItemStack createItem(McPlayerInterface player, BlockId id, BlockVariantId variant, LocalizedMessageInterface name, Serializable... nameArgs);
    
    /**
     * Returns block id from item stack
     * @param stack
     * @return block id or {@code null} if stack does not represent custom blocks
     */
    BlockId getBlockId(ItemStack stack);
    
    /**
     * Returns block id from bukkit blocks
     * @param block
     * @return block id or {@code null} if bukkit block does not represent custom blocks
     */
    BlockId getBlockId(Block block);
    
    /**
     * Returns block id from item stack
     * @param stack
     * @return block id or {@code null} if stack does not represent custom blocks
     */
    BlockVariantId getBlockVariantId(ItemStack stack);
    
    /**
     * Returns block id from bukkit blocks
     * @param block
     * @return block id or {@code null} if bukkit block does not represent custom blocks
     */
    BlockVariantId getBlockVariantId(Block block);
    
    /**
     * Sets custom block data
     * @param block the target bukkit block
     * @param id the block id
     * @param variant block variant
     */
    void setBlockData(Block block, BlockId id, BlockVariantId variant);
    
    /**
     * Creates a minable vein at given location
     * @param random
     * @param location
     * @param block
     * @param variant
     * @param size
     */
    void createMinable(Random random, Location location, BlockId block, BlockVariantId variant, int size);
    
}
