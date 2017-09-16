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

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.config.ConfigItemStackData;
import de.minigameslib.mclib.api.items.BlockDropperRuleInterface;
import de.minigameslib.mclib.api.items.BlockHopperRuleInterface;
import de.minigameslib.mclib.api.items.ItemArmor.ArmorSlot;
import de.minigameslib.mclib.nms.api.ChunkDataImpl.TileEntityData;
import de.minigameslib.mclib.shared.api.com.DataSection;

/**
 * A helper for item stacks.
 * 
 * @author mepeisen
 */
public interface ItemHelperInterface
{
    
    /**
     * Adds custom data to given item stack.
     * 
     * @param stack
     *            item stack.
     * @param plugin
     *            plugin name
     * @param key
     *            key for identifying custom data
     * @param value
     *            string value
     */
    void addCustomData(ItemStack stack, String plugin, String key, String value);
    
    /**
     * Receives custom data from given item stack.
     * 
     * @param stack
     *            item stack
     * @param plugin
     *            plugin name
     * @param key
     *            key for identifying custom data
     * @return item data
     */
    String getCustomData(ItemStack stack, String plugin, String key);
    
    /**
     * Returns the variant for given block.
     * 
     * @param block
     *            bukkit block to check for variants.
     * @return numeric variant
     */
    int getVariant(Block block);
    
    /**
     * Returns the variant for given item stack.
     * 
     * @param stack
     *            bukkit item stack to check for variants.
     * @return variant
     */
    int getVariant(ItemStack stack);
    
    /**
     * Initializes the custom blocks.
     */
    void initBlocks();
    
    /**
     * Initializes the custom items.
     */
    void initItems();
    
    /**
     * Sets the block custom type and variant.
     * 
     * @param block
     *            target bukkit block
     * @param type
     *            new type (numeric)
     * @param variant
     *            new variant (numeric)
     */
    void setBlockVariant(Block block, int type, int variant);
    
    /**
     * Creates a custom item stack for given block type and variant.
     * 
     * @param type
     *            block type (numeric)
     * @param variant
     *            block variant (numeric)
     * @param displayName
     *            the display name to be used.
     * @return item stack
     */
    ItemStack createItemStackForBlock(int type, int variant, String displayName);
    
    /**
     * Sets the display name; safe method for all stacks, either modded or classic vanilla.
     * 
     * @param stack
     *            bukkit item stack.
     * @param displayName
     *            the new display name.
     */
    void setDisplayName(ItemStack stack, String displayName);
    
    /**
     * Sets description; safe method for all stacks, either modded or classic vanilla.
     * 
     * @param stack
     *            bukkit item stack
     * @param description
     *            the new description
     */
    void setDescription(ItemStack stack, String[] description);
    
    /**
     * Returns the display name; safe method for all stacks, either modded or classic vanilla.
     * 
     * @param stack
     *            item stack
     * @return display name
     */
    String getDisplayName(ItemStack stack);
    
    /**
     * Returns the description; safe method for all stacks, either modded or classic vanilla.
     * 
     * @param stack
     *            item stack
     * @return description
     */
    String[] getDescription(ItemStack stack);
    
    /**
     * Creates a minable with custom blocks.
     * 
     * @param random
     *            the world random
     * @param location
     *            the starting location
     * @param blockId
     *            the numeric block id
     * @param meta
     *            the numeric block variant
     * @param size
     *            the size of the minable
     */
    void createMinable(Random random, Location location, int blockId, int meta, int size);
    
    /**
     * Adds given item to inventory.
     * 
     * @param inventory
     *            target inventory
     * @param stack
     *            item stack to be added
     * @return left over items; {@code null} if item could be added completly
     */
    ItemStack addToInventory(Inventory inventory, ItemStack stack);
    
    /**
     * Sets block meta values.
     * 
     * @param blockId
     *            numeric block id
     * @param hardness
     *            block hardness
     * @param resistence
     *            block resistence
     * @param dropRule
     *            the drop rule
     */
    void setBlockMeta(int blockId, float hardness, float resistence, NmsDropRuleInterface dropRule);
    
    /**
     * Installs a furnace receipe.
     * 
     * @param blockId
     *            numeric block id (source)
     * @param variant
     *            numeric variant (source)
     * @param stack
     *            item stack (result)
     * @param experience
     *            experience to drop
     */
    void installFurnaceRecipe(int blockId, int variant, ItemStack stack, float experience);
    
    /**
     * Installs a furnace receipe.
     * 
     * @param material
     *            bukkit material (source)
     * @param itemStackDurability
     *            item stack durability (source)
     * @param receipe
     *            item stack (result)
     * @param experience
     *            experience to drop
     */
    void installFurnaceRecipe(Material material, short itemStackDurability, ItemStack receipe, float experience);
    
    /**
     * Installs a furnace receipe.
     * 
     * @param itemId
     *            numeric item id (source)
     * @param receipe
     *            item stack (result)
     * @param experience
     *            experience to drop
     */
    void installFurnaceRecipe(int itemId, ItemStack receipe, float experience);
    
    /**
     * Initializes the given item material and hack the item class.
     * 
     * @param material
     *            bukkit material.
     */
    void initNmsItem(Material material);
    
    /**
     * Sets maximum stack size.
     * 
     * @param material
     *            bukkit material
     * @param itemStackDurability
     *            durability
     * @param stackSize
     *            size
     */
    void setStackSize(Material material, short itemStackDurability, int stackSize);
    
    /**
     * Sets maximum stack size.
     * 
     * @param blockOrItemId
     *            numeric block or item id for modded blocks/items
     * @param stackSize
     *            size
     */
    void setStackSize(int blockOrItemId, int stackSize);
    
    /**
     * Installs a shaped receipe.
     * 
     * @param item
     *            new item (target)
     * @param amount
     *            amount of new item
     * @param shape
     *            the shape as string array
     * @param shapedItems
     *            the mapping from character to source item
     */
    void installShapedRecipe(ItemStack item, int amount, String[] shape, Map<Character, ItemStack> shapedItems);
    
    /**
     * Installs a shaped receipe.
     * 
     * @param blockId
     *            numeric block id (target)
     * @param variant
     *            numeric block variant (target)
     * @param amount
     *            amount of new item
     * @param shape
     *            the shape as string array
     * @param shapedItems
     *            the mapping from character to source item
     */
    void installShapedRecipe(int blockId, int variant, int amount, String[] shape, Map<Character, ItemStack> shapedItems);
    
    /**
     * Installs a shapeless receipe.
     * 
     * @param item
     *            new item (target)
     * @param amount
     *            amount of new item
     * @param shapelessItems
     *            the shapeless items (source)
     */
    void installShapelessRecipe(ItemStack item, int amount, ItemStack[] shapelessItems);
    
    /**
     * Installs a shapeless receipe.
     * 
     * @param blockId
     *            numeric block id (target)
     * @param variant
     *            numeric block variant (target)
     * @param amount
     *            amount of new item
     * @param shapelessItems
     *            the shapeless items (source)
     */
    void installShapelessRecipe(int blockId, int variant, int amount, ItemStack[] shapelessItems);
    
    /**
     * Creates a new item stack for given numeric item id.
     * 
     * @param numId
     *            numeric item id.
     * @param name
     *            display name
     * @return item stack
     */
    ItemStack createItemStackForItem(int numId, String name);
    
    /**
     * Initializes an amor item.
     * 
     * @param numId
     *            numeric item id (modded item)
     * @param dmgReduceAmount
     *            amount for damage reduce
     * @param durability
     *            durability
     * @param itemEnchantability
     *            item enchantability
     * @param toughness
     *            toughness
     * @param slot
     *            target slot this item is suitable for
     * @param nmsItemRule
     *            the item rules.
     */
    void initArmor(int numId, int dmgReduceAmount, int durability, int itemEnchantability, float toughness, ArmorSlot slot, NmsItemRuleInterface nmsItemRule);
    
    /**
     * Initializes an amor item.
     * 
     * @param material
     *            bukkit material
     * @param itemStackDurability
     *            the item stack durability
     * @param dmgReduceAmount
     *            amount for damage reduce
     * @param itemEnchantability
     *            item enchantability
     * @param toughness
     *            toughness
     * @param slot
     *            target slot this item is suitable for
     * @param nmsItemRule
     *            the item rules.
     */
    void initArmor(Material material, short itemStackDurability, int dmgReduceAmount, int itemEnchantability, float toughness, ArmorSlot slot, NmsItemRuleInterface nmsItemRule);
    
    /**
     * Initializes an axe item.
     * 
     * @param numId
     *            numeric item id (modded item)
     * @param durability
     *            durability
     * @param damage
     *            damage value
     * @param itemEnchantability
     *            item enchantability
     * @param speed
     *            item speed
     * @param nmsItemRule
     *            the item rules
     */
    void initAxe(int numId, int durability, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule);
    
    /**
     * Initializes an axe item.
     * 
     * @param material
     *            bukkit material
     * @param itemStackDurability
     *            the item stack durability
     * @param damage
     *            damage value
     * @param itemEnchantability
     *            item enchantability
     * @param speed
     *            item speed
     * @param nmsItemRule
     *            the item rules
     */
    void initAxe(Material material, short itemStackDurability, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule);
    
    /**
     * Initializes a pickaxe item.
     * 
     * @param numId
     *            numeric item id (modded item)
     * @param durability
     *            durability
     * @param damage
     *            damage value
     * @param itemEnchantability
     *            item enchantability
     * @param speed
     *            item speed
     * @param nmsItemRule
     *            the item rules
     */
    void initPickaxe(int numId, int durability, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule);
    
    /**
     * Initializes a pickaxe item.
     * 
     * @param material
     *            bukkit material
     * @param itemStackDurability
     *            the item stack durability
     * @param damage
     *            damage value
     * @param itemEnchantability
     *            item enchantability
     * @param speed
     *            item speed
     * @param nmsItemRule
     *            the item rules
     */
    void initPickaxe(Material material, short itemStackDurability, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule);
    
    /**
     * Initializes a hoe item.
     * 
     * @param numId
     *            numeric item id (modded item)
     * @param durability
     *            durability
     * @param damage
     *            damage value
     * @param itemEnchantability
     *            item enchantability
     * @param speed
     *            item speed
     * @param nmsItemRule
     *            the item rules
     */
    void initHoe(int numId, int durability, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule);
    
    /**
     * Initializes a hoe item.
     * 
     * @param material
     *            bukkit material
     * @param itemStackDurability
     *            the item stack durability
     * @param damage
     *            damage value
     * @param itemEnchantability
     *            item enchantability
     * @param speed
     *            item speed
     * @param nmsItemRule
     *            the item rules
     */
    void initHoe(Material material, short itemStackDurability, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule);
    
    /**
     * Initializes a shovel item.
     * 
     * @param numId
     *            numeric item id (modded item)
     * @param durability
     *            durability
     * @param damage
     *            damage value
     * @param itemEnchantability
     *            item enchantability
     * @param speed
     *            item speed
     * @param nmsItemRule
     *            the item rules
     */
    void initShovel(int numId, int durability, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule);
    
    /**
     * Initializes a shovel item.
     * 
     * @param material
     *            bukkit material
     * @param itemStackDurability
     *            the item stack durability
     * @param damage
     *            damage value
     * @param itemEnchantability
     *            item enchantability
     * @param speed
     *            item speed
     * @param nmsItemRule
     *            the item rules
     */
    void initShovel(Material material, short itemStackDurability, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule);
    
    /**
     * Initializes a sword item.
     * 
     * @param numId
     *            numeric item id (modded item)
     * @param durability
     *            durability
     * @param damageVsEntity
     *            damage value versus entities
     * @param damage
     *            damage value
     * @param itemEnchantability
     *            item enchantability
     * @param speed
     *            item speed
     * @param nmsItemRule
     *            the item rules
     */
    void initSword(int numId, int durability, float damageVsEntity, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule);
    
    /**
     * Initializes a sword item.
     * 
     * @param material
     *            bukkit material
     * @param itemStackDurability
     *            the item stack durability
     * @param damageVsEntity
     *            damage value versus entities
     * @param damage
     *            damage value
     * @param itemEnchantability
     *            item enchantability
     * @param speed
     *            item speed
     * @param nmsItemRule
     *            the item rules
     */
    void initSword(Material material, short itemStackDurability, float damageVsEntity, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule);
    
    // access to classic vanilly materials
    
    /**
     * Checks for plant.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is PLANT
     */
    boolean isPlant(Material material);
    
    /**
     * Checks for plant.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is PLANT
     */
    boolean isPlant(int block);
    
    /**
     * Checks for gourd.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is GOURD
     */
    boolean isGourd(Material material);
    
    /**
     * Checks for gourd.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is GOURD
     */
    boolean isGourd(int block);
    
    /**
     * Checks for coral.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is coral
     */
    boolean isCoral(Material material);
    
    /**
     * Checks for coral.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is coral
     */
    boolean isCoral(int block);
    
    /**
     * Checks for grass.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is GRASS
     */
    boolean isGrass(Material material);
    
    /**
     * Checks for grass.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is GRASS
     */
    boolean isGrass(int block);
    
    /**
     * Checks for wood.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is WOOD
     */
    boolean isWood(Material material);
    
    /**
     * Checks for wood.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is WOOD
     */
    boolean isWood(int block);
    
    /**
     * Checks for rock.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is ROCK (forge) / STONE (spigot)
     */
    boolean isRock(Material material);
    
    /**
     * Checks for rock.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is ROCK (forge) / STONE (spigot)
     */
    boolean isRock(int block);
    
    /**
     * Checks for iron/ ore.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is IRON (forge)/ ORE (spigot)
     */
    boolean isOre(Material material);
    
    /**
     * Checks for iron/ ore.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is IRON (forge)/ ORE (spigot)
     */
    boolean isOre(int block);
    
    /**
     * Checks for anvil/ heavy.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is ANVIL (forge)/ HEAVY (spigot)
     */
    boolean isHeavy(Material material);
    
    /**
     * Checks for anvil/ heavy.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is ANVIL (forge)/ HEAVY (spigot)
     */
    boolean isHeavy(int block);
    
    /**
     * Checks for water.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is WATER
     */
    boolean isWater(Material material);
    
    /**
     * Checks for water.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is WATER
     */
    boolean isWater(int block);
    
    /**
     * Checks for lava.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is LAVA
     */
    boolean isLava(Material material);
    
    /**
     * Checks for lava.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is LAVA
     */
    boolean isLava(int block);
    
    /**
     * Checks for leaves.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is LEAVES
     */
    boolean isLeaves(Material material);
    
    /**
     * Checks for leaves.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is LEAVES
     */
    boolean isLeaves(int block);
    
    /**
     * Checks for vine.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is VINE
     */
    boolean isVine(Material material);
    
    /**
     * Checks for vine.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is VINE
     */
    boolean isVine(int block);
    
    /**
     * Checks for sponge.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is SPONGE
     */
    boolean isSponge(Material material);
    
    /**
     * Checks for sponge.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is SPONGE
     */
    boolean isSponge(int block);
    
    /**
     * Checks for cloth.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is CLOTH
     */
    boolean isCloth(Material material);
    
    /**
     * Checks for cloth.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is CLOTH
     */
    boolean isCloth(int block);
    
    /**
     * Checks for fire.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is FIRE
     */
    boolean isFire(Material material);
    
    /**
     * Checks for fire.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is FIRE
     */
    boolean isFire(int block);
    
    /**
     * Checks for sand.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is SAND
     */
    boolean isSand(Material material);
    
    /**
     * Checks for sand.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is SAND
     */
    boolean isSand(int block);
    
    /**
     * Checks for circuits.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is CIRCUITS
     */
    boolean isCircuits(Material material);
    
    /**
     * Checks for circuits.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is CIRCUITS
     */
    boolean isCircuits(int block);
    
    /**
     * Checks for carpet.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is CARPET
     */
    boolean isCarpet(Material material);
    
    /**
     * Checks for carpet.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is CARPET
     */
    boolean isCarpet(int block);
    
    /**
     * Checks for glass.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is GLASS
     */
    boolean isGlass(Material material);
    
    /**
     * Checks for glass.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is GLASS
     */
    boolean isGlass(int block);
    
    /**
     * Checks for redstone light.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is REDSTONE_LIGHT
     */
    boolean isRedstoneLight(Material material);
    
    /**
     * Checks for redstone light.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is REDSTONE_LIGHT
     */
    boolean isRedstoneLight(int block);
    
    /**
     * Checks for tnt.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is TNT
     */
    boolean isTnt(Material material);
    
    /**
     * Checks for tnt.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is TNT
     */
    boolean isTnt(int block);
    
    /**
     * Checks for ice.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is ICE
     */
    boolean isIce(Material material);
    
    /**
     * Checks for ice.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is ICE
     */
    boolean isIce(int block);
    
    /**
     * Checks for packed ice.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is PACKED_ICE
     */
    boolean isPackedIce(Material material);
    
    /**
     * Checks for packed ice.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is PACKED_ICE
     */
    boolean isPackedIce(int block);
    
    /**
     * Checks for snow.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is SNOW
     */
    boolean isSnow(Material material);
    
    /**
     * Checks for snow.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is SNOW
     */
    boolean isSnow(int block);
    
    /**
     * Checks for crafted snow.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is CRAFTED_SNOW
     */
    boolean isCraftedSnow(Material material);
    
    /**
     * Checks for crafted snow.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is CRAFTED_SNOW
     */
    boolean isCraftedSnow(int block);
    
    /**
     * Checks for cactus.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is CACTUS
     */
    boolean isCactus(Material material);
    
    /**
     * Checks for cactus.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is CACTUS
     */
    boolean isCactus(int block);
    
    /**
     * Checks for clay.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is CLAY
     */
    boolean isClay(Material material);
    
    /**
     * Checks for clay.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is CLAY
     */
    boolean isClay(int block);
    
    /**
     * Checks for dragon egg.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is DRAGON_EGG
     */
    boolean isDragonEgg(Material material);
    
    /**
     * Checks for dragon egg.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is DRAGON_EGG
     */
    boolean isDragonEgg(int block);
    
    /**
     * Checks for portal.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is PORTAL
     */
    boolean isPortal(Material material);
    
    /**
     * Checks for portal.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is PORTAL
     */
    boolean isPortal(int block);
    
    /**
     * Checks for cake.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is CAKE
     */
    boolean isCake(Material material);
    
    /**
     * Checks for cake.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is CAKE
     */
    boolean isCake(int block);
    
    /**
     * Checks for web.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is WEB
     */
    boolean isWeb(Material material);
    
    /**
     * Checks for web.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is WEB
     */
    boolean isWeb(int block);
    
    /**
     * Checks for piston.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is PISTON
     */
    boolean isPiston(Material material);
    
    /**
     * Checks for piston.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is PISTON
     */
    boolean isPiston(int block);
    
    /**
     * Checks for Barrier.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is BARRIER
     */
    boolean isBarrier(Material material);
    
    /**
     * Checks for barrier.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is BARRIER
     */
    boolean isBarrier(int block);
    
    /**
     * Checks for structure void.
     * 
     * @param material
     *            bukkit material
     * 
     * @return {@code true} if given material is STRUCTURE_VOID
     */
    boolean isStructureVoid(Material material);
    
    /**
     * Checks for structure void.
     * 
     * @param block
     *            bukkit block
     * 
     * @return {@code true} if given material is STRCTURE_VOID
     */
    boolean isStructureVoid(int block);
    
    /**
     * Initializes inventory.
     * 
     * @param blockId
     *            numeric block id
     * @param variant
     *            numeric block variant
     * @param nmsInventoryHandler
     *            nms inventory handler
     */
    void initInventory(int blockId, int variant, NmsInventoryHandlerInterface nmsInventoryHandler);
    
    /**
     * Converts bukkit item stack to saveable config data.
     * 
     * @param stack
     *            bukkit item stack
     * @return config item stack
     */
    ConfigItemStackData toConfigData(ItemStack stack);
    
    /**
     * Converts data section to saveable config data.
     * 
     * @param section
     *            data section to read from
     * @return config item stack
     */
    ConfigItemStackData fromConfigData(DataSection section);
    
    /**
     * Returns a data snapshot of given chunk.
     * 
     * @param chunk
     *            chunk to be stored
     * @return data snapshot
     */
    ChunkDataImpl getChunkSnapshot(Chunk chunk);
    
    /**
     * Returns the block mappings; the mappings are equal within the whole server.
     * 
     * @return block mappings (block name to numeric id)
     */
    Map<String, Integer> getBlockMappings();
    
    /**
     * Load tile entities from given tile entity data.
     * 
     * @param loc
     *            starting location.
     * @param data
     *            tile entity data.
     */
    void loadTileEntities(Location loc, List<TileEntityData> data);
    
    /**
     * Sets blocks along z axis.
     * 
     * @param location
     *            starting location.
     * @param blockMappings
     *            numeric block mappings
     * @param blocks
     *            block data
     */
    void setBlocks(Location location, Map<Integer, Integer> blockMappings, int[] blocks);
    
    /**
     * Sets the hopper rule class for implementing custom hoppers.
     * 
     * @param numBlockId
     *            numeric blockid
     * @param hopper
     *            the hopper rule class.
     */
    void setHopperRule(int numBlockId, Class<? extends BlockHopperRuleInterface> hopper);

    /**
     * Sets the dropper rule class for implementing custom droppers.
     * 
     * @param numBlockId
     *            numeric blockid
     * @param dropper
     *            the dropper rule class.
     */
    void setDropperRule(int numBlockId, Class<? extends BlockDropperRuleInterface> dropper);
    
}
