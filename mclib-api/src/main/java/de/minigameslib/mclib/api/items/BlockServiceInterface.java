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
import org.bukkit.Material;
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
     * Creates a new item stack for given block id.
     * 
     * @param id
     *            the block
     * @param variant
     *            the block variant
     * @return item stack
     */
    ItemStack createItem(BlockId id, BlockVariantId variant);
    
    /**
     * Creates a new item stack for given block id.
     * 
     * @param id
     *            the block
     * @param variant
     *            the block variant
     * @param name
     *            the display name (already localized)
     * @return item stack
     */
    ItemStack createItem(BlockId id, BlockVariantId variant, String name);
    
    /**
     * Creates a new item stack for given block id.
     * 
     * @param id
     *            the block
     * @param variant
     *            the block variant
     * @param name
     *            the display name (already localized)
     * @param nameArgs
     *            name arguments
     * @return item stack
     */
    ItemStack createItem(BlockId id, BlockVariantId variant, LocalizedMessageInterface name, Serializable... nameArgs);
    
    /**
     * Creates a new item stack for given block id.
     * 
     * @param player
     *            the player used for localization
     * @param id
     *            the block
     * @param variant
     *            the block variant
     * @return item stack
     */
    ItemStack createItem(McPlayerInterface player, BlockId id, BlockVariantId variant);
    
    /**
     * Creates a new item stack for given block id.
     * 
     * @param player
     *            the player used for localization
     * @param id
     *            the block
     * @param variant
     *            the block variant
     * @param name
     *            the display name (already localized)
     * @param nameArgs
     *            name arguments
     * @return item stack
     */
    ItemStack createItem(McPlayerInterface player, BlockId id, BlockVariantId variant, LocalizedMessageInterface name, Serializable... nameArgs);
    
    /**
     * Returns block id from item stack.
     * 
     * @param stack
     *            item stack
     * @return block id or {@code null} if stack does not represent custom blocks
     */
    BlockId getBlockId(ItemStack stack);
    
    /**
     * Returns block id from bukkit blocks.
     * 
     * @param block
     *            bukkit block
     * @return block id or {@code null} if bukkit block does not represent custom blocks
     */
    BlockId getBlockId(Block block);
    
    /**
     * Returns block id from item stack.
     * 
     * @param stack
     *            item stack
     * @return block id or {@code null} if stack does not represent custom blocks
     */
    BlockVariantId getBlockVariantId(ItemStack stack);
    
    /**
     * Returns block id from bukkit blocks.
     * 
     * @param block
     *            bukkit block
     * @return block id or {@code null} if bukkit block does not represent custom blocks
     */
    BlockVariantId getBlockVariantId(Block block);
    
    /**
     * Sets custom block data.
     * 
     * @param block
     *            the target bukkit block
     * @param id
     *            the block id
     * @param variant
     *            block variant
     */
    void setBlockData(Block block, BlockId id, BlockVariantId variant);
    
    /**
     * Creates a minable vein at given location.
     * 
     * @param random
     *            world random.
     * @param location
     *            start location
     * @param block
     *            block id
     * @param variant
     *            variant id
     * @param size
     *            vine size
     */
    void createMinable(Random random, Location location, BlockId block, BlockVariantId variant, int size);
    
    // access to classic vanilly materials
    
    /**
     * Checks for plant.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is PLANT
     */
    boolean isPlant(Material material);
    
    /**
     * Checks for plant.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is PLANT
     */
    boolean isPlant(BlockId block);
    
    /**
     * Checks for gourd.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is GOURD
     */
    boolean isGourd(Material material);
    
    /**
     * Checks for gourd.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is GOURD
     */
    boolean isGourd(BlockId block);
    
    /**
     * Checks for coral.
     * 
     * @param material
     *            bukkit material.
     * @return {@code true} if given material is coral
     */
    boolean isCoral(Material material);
    
    /**
     * Checks for coral.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is coral
     */
    boolean isCoral(BlockId block);
    
    /**
     * Checks for grass.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is GRASS
     */
    boolean isGrass(Material material);
    
    /**
     * Checks for grass.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is GRASS
     */
    boolean isGrass(BlockId block);
    
    /**
     * Checks for wood.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is WOOD
     */
    boolean isWood(Material material);
    
    /**
     * Checks for wood.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is WOOD
     */
    boolean isWood(BlockId block);
    
    /**
     * Checks for rock.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is ROCK (forge) / STONE (spigot)
     */
    boolean isRock(Material material);
    
    /**
     * Checks for rock.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is ROCK (forge) / STONE (spigot)
     */
    boolean isRock(BlockId block);
    
    /**
     * Checks for iron/ ore.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is IRON (forge)/ ORE (spigot)
     */
    boolean isOre(Material material);
    
    /**
     * Checks for iron/ ore.
     * 
     * @param block
     *            bukkit block.
     * @return {@code true} if given material is IRON (forge)/ ORE (spigot)
     */
    boolean isOre(BlockId block);
    
    /**
     * Checks for anvil/ heavy.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is ANVIL (forge)/ HEAVY (spigot)
     */
    boolean isHeavy(Material material);
    
    /**
     * Checks for anvil/ heavy.
     * 
     * @param block
     *            bukkit block.
     * @return {@code true} if given material is ANVIL (forge)/ HEAVY (spigot)
     */
    boolean isHeavy(BlockId block);
    
    /**
     * Checks for water.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is WATER
     */
    boolean isWater(Material material);
    
    /**
     * Checks for water.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is WATER
     */
    boolean isWater(BlockId block);
    
    /**
     * Checks for lava.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is LAVA
     */
    boolean isLava(Material material);
    
    /**
     * Checks for lava.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is LAVA
     */
    boolean isLava(BlockId block);
    
    /**
     * Checks for leaves.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is LEAVES
     */
    boolean isLeaves(Material material);
    
    /**
     * Checks for leaves.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is LEAVES
     */
    boolean isLeaves(BlockId block);
    
    /**
     * Checks for vine.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is VINE
     */
    boolean isVine(Material material);
    
    /**
     * Checks for vine.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is VINE
     */
    boolean isVine(BlockId block);
    
    /**
     * Checks for sponge.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is SPONGE
     */
    boolean isSponge(Material material);
    
    /**
     * Checks for sponge.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is SPONGE
     */
    boolean isSponge(BlockId block);
    
    /**
     * Checks for cloth.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is CLOTH
     */
    boolean isCloth(Material material);
    
    /**
     * Checks for cloth.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is CLOTH
     */
    boolean isCloth(BlockId block);
    
    /**
     * Checks for fire.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is FIRE
     */
    boolean isFire(Material material);
    
    /**
     * Checks for fire.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is FIRE
     */
    boolean isFire(BlockId block);
    
    /**
     * Checks for sand.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is SAND
     */
    boolean isSand(Material material);
    
    /**
     * Checks for sand.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is SAND
     */
    boolean isSand(BlockId block);
    
    /**
     * Checks for circuits.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is CIRCUITS
     */
    boolean isCircuits(Material material);
    
    /**
     * Checks for circuits.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is CIRCUITS
     */
    boolean isCircuits(BlockId block);
    
    /**
     * Checks for carpet.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is CARPET
     */
    boolean isCarpet(Material material);
    
    /**
     * Checks for carpet.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is CARPET
     */
    boolean isCarpet(BlockId block);
    
    /**
     * Checks for glass.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is GLASS
     */
    boolean isGlass(Material material);
    
    /**
     * Checks for glass.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is GLASS
     */
    boolean isGlass(BlockId block);
    
    /**
     * Checks for redstone light.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is REDSTONE_LIGHT
     */
    boolean isRedstoneLight(Material material);
    
    /**
     * Checks for redstone light.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is REDSTONE_LIGHT
     */
    boolean isRedstoneLight(BlockId block);
    
    /**
     * Checks for tnt.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is TNT
     */
    boolean isTnt(Material material);
    
    /**
     * Checks for tnt.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is TNT
     */
    boolean isTnt(BlockId block);
    
    /**
     * Checks for ice.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is ICE
     */
    boolean isIce(Material material);
    
    /**
     * Checks for ice.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is ICE
     */
    boolean isIce(BlockId block);
    
    /**
     * Checks for packed ice.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is PACKED_ICE
     */
    boolean isPackedIce(Material material);
    
    /**
     * Checks for packed ice.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is PACKED_ICE
     */
    boolean isPackedIce(BlockId block);
    
    /**
     * Checks for snow.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is SNOW
     */
    boolean isSnow(Material material);
    
    /**
     * Checks for snow.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is SNOW
     */
    boolean isSnow(BlockId block);
    
    /**
     * Checks for crafted snow.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is CRAFTED_SNOW
     */
    boolean isCraftedSnow(Material material);
    
    /**
     * Checks for crafted snow.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is CRAFTED_SNOW
     */
    boolean isCraftedSnow(BlockId block);
    
    /**
     * Checks for cactus.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is CACTUS
     */
    boolean isCactus(Material material);
    
    /**
     * Checks for cactus.
     * 
     * @param block
     *            bukkit block.
     * @return {@code true} if given material is CACTUS
     */
    boolean isCactus(BlockId block);
    
    /**
     * Checks for clay.
     * 
     * @param material
     *            bukkit material.
     * @return {@code true} if given material is CLAY
     */
    boolean isClay(Material material);
    
    /**
     * Checks for clay.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is CLAY
     */
    boolean isClay(BlockId block);
    
    /**
     * Checks for dragon egg.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is DRAGON_EGG
     */
    boolean isDragonEgg(Material material);
    
    /**
     * Checks for dragon egg.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is DRAGON_EGG
     */
    boolean isDragonEgg(BlockId block);
    
    /**
     * Checks for portal.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is PORTAL
     */
    boolean isPortal(Material material);
    
    /**
     * Checks for portal.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is PORTAL
     */
    boolean isPortal(BlockId block);
    
    /**
     * Checks for cake.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is CAKE
     */
    boolean isCake(Material material);
    
    /**
     * Checks for cake.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is CAKE
     */
    boolean isCake(BlockId block);
    
    /**
     * Checks for web.
     * 
     * @param material
     *            bukkit material.
     * @return {@code true} if given material is WEB
     */
    boolean isWeb(Material material);
    
    /**
     * Checks for web.
     * 
     * @param block
     *            bukkit block.
     * @return {@code true} if given material is WEB
     */
    boolean isWeb(BlockId block);
    
    /**
     * Checks for piston.
     * 
     * @param material
     *            bukkit material.
     * @return {@code true} if given material is PISTON
     */
    boolean isPiston(Material material);
    
    /**
     * Checks for piston.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is PISTON
     */
    boolean isPiston(BlockId block);
    
    /**
     * Checks for Barrier.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is BARRIER
     */
    boolean isBarrier(Material material);
    
    /**
     * Checks for barrier.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is BARRIER
     */
    boolean isBarrier(BlockId block);
    
    /**
     * Checks for structure void.
     * 
     * @param material
     *            bukkit material
     * @return {@code true} if given material is STRUCTURE_VOID
     */
    boolean isStructureVoid(Material material);
    
    /**
     * Checks for structure void.
     * 
     * @param block
     *            bukkit block
     * @return {@code true} if given material is STRCTURE_VOID
     */
    boolean isStructureVoid(BlockId block);
    
}
