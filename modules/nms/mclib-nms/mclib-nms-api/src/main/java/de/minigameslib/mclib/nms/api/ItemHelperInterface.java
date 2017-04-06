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

import de.minigameslib.mclib.api.items.ItemArmor.ArmorSlot;

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
     * Initializes the custom items
     */
    void initItems();
    
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
     * @param itemId
     * @param receipe
     * @param experience
     */
    void installFurnaceRecipe(int itemId, ItemStack receipe, float experience);

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
     * @param blockOrItemId
     * @param stackSize
     */
    void setStackSize(int blockOrItemId, int stackSize);

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

    /**
     * @param numId
     * @param name
     * @return item stack
     */
    ItemStack createItemStackForItem(int numId, String name);

    /**
     * @param numId
     * @param dmgReduceAmount
     * @param durability
     * @param itemEnchantability
     * @param toughness
     * @param slot
     * @param nmsItemRule
     */
    void initArmor(int numId, int dmgReduceAmount, int durability, int itemEnchantability, float toughness, ArmorSlot slot, NmsItemRuleInterface nmsItemRule);

    /**
     * @param material
     * @param itemStackDurability
     * @param dmgReduceAmount
     * @param durability
     * @param itemEnchantability
     * @param toughness
     * @param slot
     * @param nmsItemRule
     */
    void initArmor(Material material, short itemStackDurability, int dmgReduceAmount, int durability, int itemEnchantability, float toughness, ArmorSlot slot, NmsItemRuleInterface nmsItemRule);

    /**
     * @param numId
     * @param durability 
     * @param damage
     * @param itemEnchantability
     * @param speed
     * @param nmsItemRule
     */
    void initAxe(int numId, int durability, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule);

    /**
     * @param material
     * @param itemStackDurability
     * @param durability 
     * @param damage
     * @param itemEnchantability
     * @param speed
     * @param nmsItemRule
     */
    void initAxe(Material material, short itemStackDurability, int durability, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule);

    /**
     * @param numId
     * @param durability 
     * @param damage
     * @param itemEnchantability
     * @param speed
     * @param nmsItemRule
     */
    void initPickaxe(int numId, int durability, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule);

    /**
     * @param material
     * @param itemStackDurability
     * @param durability 
     * @param damage
     * @param itemEnchantability
     * @param speed
     * @param nmsItemRule
     */
    void initPickaxe(Material material, short itemStackDurability, int durability, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule);

    /**
     * @param numId
     * @param durability 
     * @param damage
     * @param itemEnchantability
     * @param speed
     * @param nmsItemRule
     */
    void initHoe(int numId, int durability, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule);

    /**
     * @param material
     * @param itemStackDurability
     * @param durability 
     * @param damage
     * @param itemEnchantability
     * @param speed
     * @param nmsItemRule
     */
    void initHoe(Material material, short itemStackDurability, int durability, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule);

    /**
     * @param numId
     * @param durability 
     * @param damage
     * @param itemEnchantability
     * @param speed
     * @param nmsItemRule
     */
    void initShovel(int numId, int durability, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule);

    /**
     * @param material
     * @param itemStackDurability
     * @param durability 
     * @param damage
     * @param itemEnchantability
     * @param speed
     * @param nmsItemRule
     */
    void initShovel(Material material, short itemStackDurability, int durability, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule);

    /**
     * @param numId
     * @param durability 
     * @param damageVsEntity
     * @param damage
     * @param itemEnchantability
     * @param speed
     * @param nmsItemRule
     */
    void initSword(int numId, int durability, float damageVsEntity, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule);

    /**
     * @param material
     * @param itemStackDurability
     * @param durability 
     * @param damageVsEntity
     * @param damage
     * @param itemEnchantability
     * @param speed
     * @param nmsItemRule
     */
    void initSword(Material material, short itemStackDurability, int durability, float damageVsEntity, double damage, int itemEnchantability, double speed, NmsItemRuleInterface nmsItemRule);
    
    // access to classic vanilly materials
    
    /**
     * Checks for plant
     * @param material
     * @return {@code true} if given material is PLANT
     */
    boolean isPlant(Material material);
    
    /**
     * Checks for plant
     * @param block
     * @return {@code true} if given material is PLANT
     */
    boolean isPlant(int block);
    
    /**
     * Checks for gourd
     * @param material
     * @return {@code true} if given material is GOURD
     */
    boolean isGourd(Material material);
    
    /**
     * Checks for gourd
     * @param block
     * @return {@code true} if given material is GOURD
     */
    boolean isGourd(int block);
    
    /**
     * Checks for coral
     * @param material
     * @return {@code true} if given material is coral
     */
    boolean isCoral(Material material);
    
    /**
     * Checks for coral
     * @param block
     * @return {@code true} if given material is coral
     */
    boolean isCoral(int block);
    
    /**
     * Checks for grass
     * @param material
     * @return {@code true} if given material is GRASS
     */
    boolean isGrass(Material material);
    
    /**
     * Checks for grass
     * @param block
     * @return {@code true} if given material is GRASS
     */
    boolean isGrass(int block);
    
    /**
     * Checks for wood
     * @param material
     * @return {@code true} if given material is WOOD
     */
    boolean isWood(Material material);
    
    /**
     * Checks for wood
     * @param block
     * @return {@code true} if given material is WOOD
     */
    boolean isWood(int block);
    
    /**
     * Checks for rock
     * @param material
     * @return {@code true} if given material is ROCK (forge) / STONE (spigot)
     */
    boolean isRock(Material material);
    
    /**
     * Checks for rock
     * @param block
     * @return {@code true} if given material is ROCK (forge) / STONE (spigot)
     */
    boolean isRock(int block);
    
    /**
     * Checks for iron/ ore
     * @param material
     * @return {@code true} if given material is IRON (forge)/ ORE (spigot)
     */
    boolean isOre(Material material);
    
    /**
     * Checks for iron/ ore
     * @param block
     * @return {@code true} if given material is IRON (forge)/ ORE (spigot)
     */
    boolean isOre(int block);
    
    /**
     * Checks for anvil/ heavy
     * @param material
     * @return {@code true} if given material is ANVIL (forge)/ HEAVY (spigot)
     */
    boolean isHeavy(Material material);
    
    /**
     * Checks for anvil/ heavy
     * @param block
     * @return {@code true} if given material is ANVIL (forge)/ HEAVY (spigot)
     */
    boolean isHeavy(int block);
    
    /**
     * Checks for water
     * @param material
     * @return {@code true} if given material is WATER
     */
    boolean isWater(Material material);
    
    /**
     * Checks for water
     * @param block
     * @return {@code true} if given material is WATER
     */
    boolean isWater(int block);
    
    /**
     * Checks for lava
     * @param material
     * @return {@code true} if given material is LAVA
     */
    boolean isLava(Material material);
    
    /**
     * Checks for lava
     * @param block
     * @return {@code true} if given material is LAVA
     */
    boolean isLava(int block);
    
    /**
     * Checks for leaves
     * @param material
     * @return {@code true} if given material is LEAVES
     */
    boolean isLeaves(Material material);
    
    /**
     * Checks for leaves
     * @param block
     * @return {@code true} if given material is LEAVES
     */
    boolean isLeaves(int block);
    
    /**
     * Checks for vine
     * @param material
     * @return {@code true} if given material is VINE
     */
    boolean isVine(Material material);
    
    /**
     * Checks for vine
     * @param block
     * @return {@code true} if given material is VINE
     */
    boolean isVine(int block);
    
    /**
     * Checks for sponge
     * @param material
     * @return {@code true} if given material is SPONGE
     */
    boolean isSponge(Material material);
    
    /**
     * Checks for sponge
     * @param block
     * @return {@code true} if given material is SPONGE
     */
    boolean isSponge(int block);
    
    /**
     * Checks for cloth
     * @param material
     * @return {@code true} if given material is CLOTH
     */
    boolean isCloth(Material material);
    
    /**
     * Checks for cloth
     * @param block
     * @return {@code true} if given material is CLOTH
     */
    boolean isCloth(int block);
    
    /**
     * Checks for fire
     * @param material
     * @return {@code true} if given material is FIRE
     */
    boolean isFire(Material material);
    
    /**
     * Checks for fire
     * @param block
     * @return {@code true} if given material is FIRE
     */
    boolean isFire(int block);
    
    /**
     * Checks for sand
     * @param material
     * @return {@code true} if given material is SAND
     */
    boolean isSand(Material material);
    
    /**
     * Checks for sand
     * @param block
     * @return {@code true} if given material is SAND
     */
    boolean isSand(int block);
    
    /**
     * Checks for circuits
     * @param material
     * @return {@code true} if given material is CIRCUITS
     */
    boolean isCircuits(Material material);
    
    /**
     * Checks for circuits
     * @param block
     * @return {@code true} if given material is CIRCUITS
     */
    boolean isCircuits(int block);
    
    /**
     * Checks for carpet
     * @param material
     * @return {@code true} if given material is CARPET
     */
    boolean isCarpet(Material material);
    
    /**
     * Checks for carpet
     * @param block
     * @return {@code true} if given material is CARPET
     */
    boolean isCarpet(int block);
    
    /**
     * Checks for glass
     * @param material
     * @return {@code true} if given material is GLASS
     */
    boolean isGlass(Material material);
    
    /**
     * Checks for glass
     * @param block
     * @return {@code true} if given material is GLASS
     */
    boolean isGlass(int block);
    
    /**
     * Checks for redstone light
     * @param material
     * @return {@code true} if given material is REDSTONE_LIGHT
     */
    boolean isRedstoneLight(Material material);
    
    /**
     * Checks for redstone light
     * @param block
     * @return {@code true} if given material is REDSTONE_LIGHT
     */
    boolean isRedstoneLight(int block);
    
    /**
     * Checks for tnt
     * @param material
     * @return {@code true} if given material is TNT
     */
    boolean isTnt(Material material);
    
    /**
     * Checks for tnt
     * @param block
     * @return {@code true} if given material is TNT
     */
    boolean isTnt(int block);
    
    /**
     * Checks for ice
     * @param material
     * @return {@code true} if given material is ICE
     */
    boolean isIce(Material material);
    
    /**
     * Checks for ice
     * @param block
     * @return {@code true} if given material is ICE
     */
    boolean isIce(int block);
    
    /**
     * Checks for packed ice
     * @param material
     * @return {@code true} if given material is PACKED_ICE
     */
    boolean isPackedIce(Material material);
    
    /**
     * Checks for packed ice
     * @param block
     * @return {@code true} if given material is PACKED_ICE
     */
    boolean isPackedIce(int block);
    
    /**
     * Checks for snow
     * @param material
     * @return {@code true} if given material is SNOW
     */
    boolean isSnow(Material material);
    
    /**
     * Checks for snow
     * @param block
     * @return {@code true} if given material is SNOW
     */
    boolean isSnow(int block);
    
    /**
     * Checks for crafted snow
     * @param material
     * @return {@code true} if given material is CRAFTED_SNOW
     */
    boolean isCraftedSnow(Material material);
    
    /**
     * Checks for crafted snow
     * @param block
     * @return {@code true} if given material is CRAFTED_SNOW
     */
    boolean isCraftedSnow(int block);
    
    /**
     * Checks for cactus
     * @param material
     * @return {@code true} if given material is CACTUS
     */
    boolean isCactus(Material material);
    
    /**
     * Checks for cactus
     * @param block
     * @return {@code true} if given material is CACTUS
     */
    boolean isCactus(int block);
    
    /**
     * Checks for clay
     * @param material
     * @return {@code true} if given material is CLAY
     */
    boolean isClay(Material material);
    
    /**
     * Checks for clay
     * @param block
     * @return {@code true} if given material is CLAY
     */
    boolean isClay(int block);
    
    /**
     * Checks for dragon egg
     * @param material
     * @return {@code true} if given material is DRAGON_EGG
     */
    boolean isDragonEgg(Material material);
    
    /**
     * Checks for dragon egg
     * @param block
     * @return {@code true} if given material is DRAGON_EGG
     */
    boolean isDragonEgg(int block);
    
    /**
     * Checks for portal
     * @param material
     * @return {@code true} if given material is PORTAL
     */
    boolean isPortal(Material material);
    
    /**
     * Checks for portal
     * @param block
     * @return {@code true} if given material is PORTAL
     */
    boolean isPortal(int block);
    
    /**
     * Checks for cake
     * @param material
     * @return {@code true} if given material is CAKE
     */
    boolean isCake(Material material);
    
    /**
     * Checks for cake
     * @param block
     * @return {@code true} if given material is CAKE
     */
    boolean isCake(int block);
    
    /**
     * Checks for web
     * @param material
     * @return {@code true} if given material is WEB
     */
    boolean isWeb(Material material);
    
    /**
     * Checks for web
     * @param block
     * @return {@code true} if given material is WEB
     */
    boolean isWeb(int block);
    
    /**
     * Checks for piston
     * @param material
     * @return {@code true} if given material is PISTON
     */
    boolean isPiston(Material material);
    
    /**
     * Checks for piston
     * @param block
     * @return {@code true} if given material is PISTON
     */
    boolean isPiston(int block);
    
    /**
     * Checks for Barrier
     * @param material
     * @return {@code true} if given material is BARRIER
     */
    boolean isBarrier(Material material);
    
    /**
     * Checks for barrier
     * @param block
     * @return {@code true} if given material is BARRIER
     */
    boolean isBarrier(int block);
    
    /**
     * Checks for structure void
     * @param material
     * @return {@code true} if given material is STRUCTURE_VOID
     */
    boolean isStructureVoid(Material material);
    
    /**
     * Checks for structure void
     * @param block
     * @return {@code true} if given material is STRCTURE_VOID
     */
    boolean isStructureVoid(int block);
    
}
