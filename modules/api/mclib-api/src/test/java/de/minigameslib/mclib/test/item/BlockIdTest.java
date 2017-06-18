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

package de.minigameslib.mclib.test.item;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.junit.Test;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.items.BlockData;
import de.minigameslib.mclib.api.items.BlockDropRule;
import de.minigameslib.mclib.api.items.BlockDropRuleInterface.DropSelf;
import de.minigameslib.mclib.api.items.BlockId;
import de.minigameslib.mclib.api.items.BlockInventoryMeta;
import de.minigameslib.mclib.api.items.BlockInventoryMeta.BlockInventoryInterface;
import de.minigameslib.mclib.api.items.BlockMeta;
import de.minigameslib.mclib.api.items.BlockVariantData;
import de.minigameslib.mclib.api.items.BlockVariantId;
import de.minigameslib.mclib.api.items.CraftingRecipes;
import de.minigameslib.mclib.api.items.FurnaceRecipe;
import de.minigameslib.mclib.api.items.FurnaceRecipeInterface;
import de.minigameslib.mclib.api.items.InventoryId;
import de.minigameslib.mclib.api.items.ItemId;
import de.minigameslib.mclib.api.items.ItemStackSize;
import de.minigameslib.mclib.api.items.NameProvider;
import de.minigameslib.mclib.api.items.NullNameProvider;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;

/**
 * Tests for {@link BlockId}.
 * 
 * @author mepeisen
 *
 */
public class BlockIdTest
{
    
    /**
     * test me.
     */
    @Test
    public void testTextures()
    {
        assertArrayEquals(new String[] { "texture1" }, TestBlocks.Common.getTextures()); //$NON-NLS-1$
        assertNull(TestBlocks.Empty.getTextures());
    }
    
    /**
     * test me.
     */
    @Test(expected = IllegalStateException.class)
    public void testTexturesInvalid()
    {
        new InvalidBlock().getTextures();
    }
    
    /**
     * test me.
     */
    @Test
    public void testRecipes()
    {
        assertNotNull(TestBlocks.Common.recipes());
        assertNull(TestBlocks.Empty.recipes());
    }
    
    /**
     * test me.
     */
    @Test(expected = IllegalStateException.class)
    public void testRecipesInvalid()
    {
        new InvalidBlock().recipes();
    }
    
    /**
     * test me.
     */
    @Test
    public void testModelJson()
    {
        assertEquals("json", TestBlocks.Common.getModelJson()); //$NON-NLS-1$
        assertNull(TestBlocks.Empty.getModelJson());
    }
    
    /**
     * test me.
     */
    @Test(expected = IllegalStateException.class)
    public void testModelJsonInvalid()
    {
        new InvalidBlock().getModelJson();
    }
    
    /**
     * test me.
     */
    @Test
    public void testVariants()
    {
        assertArrayEquals(new BlockVariantId[]{BlockData.CustomVariantId.DEFAULT}, TestBlocks.Empty.variants());
        assertArrayEquals(new BlockVariantId[]{TestVariants.Common}, TestBlocks.Common.variants());
    }
    
    /**
     * test me.
     */
    @Test(expected = IllegalStateException.class)
    public void testVariantsInvalid()
    {
        new InvalidBlock().variants();
    }
    
    /**
     * test me.
     */
    @Test
    public void testFurnaceRecipe()
    {
        assertTrue(TestBlocks.Common.furnaceRecipe() instanceof FurnRec);
        assertNull(TestBlocks.Empty.furnaceRecipe());
    }
    
    /**
     * test me.
     */
    @Test(expected = IllegalStateException.class)
    public void testFurnaceRecipeInvalid()
    {
        new InvalidBlock().furnaceRecipe();
    }
    
    /**
     * test me.
     */
    @Test
    public void testName()
    {
        assertTrue(TestBlocks.Common.nameProvider() instanceof MyName);
        assertTrue(TestBlocks.Empty.nameProvider() instanceof NullNameProvider);
    }
    
    /**
     * test me.
     */
    @Test(expected = IllegalStateException.class)
    public void testNameInvalid()
    {
        new InvalidBlock().nameProvider();
    }
    
    /**
     * test me.
     */
    @Test
    public void testStackSize()
    {
        assertEquals(32, TestBlocks.Common.stackSize());
        assertEquals(64, TestBlocks.Empty.stackSize());
    }
    
    /**
     * test me.
     */
    @Test(expected = IllegalStateException.class)
    public void testStackSizeInvalid()
    {
        new InvalidBlock().stackSize();
    }
    
    /**
     * test me.
     */
    @Test
    public void testDropRule()
    {
        assertTrue(TestBlocks.Common.dropRule() instanceof DropSelf);
        assertNull(TestBlocks.Empty.dropRule());
    }
    
    /**
     * test me.
     */
    @Test(expected = IllegalStateException.class)
    public void testDropRuleInvalid()
    {
        new InvalidBlock().dropRule();
    }
    
    /**
     * test me.
     */
    @Test
    public void testMeta()
    {
        assertNotNull(TestBlocks.Common.meta());
        assertNull(TestBlocks.Empty.meta());
    }
    
    /**
     * test me.
     */
    @Test(expected = IllegalStateException.class)
    public void testMetaInvalid()
    {
        new InvalidBlock().meta();
    }
    
    /**
     * test me.
     */
    @Test
    public void testInventory()
    {
        assertNotNull(TestBlocks.Common.inventory());
        assertNull(TestBlocks.Empty.inventory());
    }
    
    /**
     * test me.
     */
    @Test(expected = IllegalStateException.class)
    public void testInventoryInvalid()
    {
        new InvalidBlock().inventory();
    }
    
    /**
     * test items.
     */
    private enum TestBlocks implements BlockId
    {
        
        /**
         * modded.
         */
        @BlockData(name = MyName.class, variants = TestVariants.class)
        @BlockVariantData(
            modelJson = "json",
            textures = "texture1")
        @CraftingRecipes
        @FurnaceRecipe(value = FurnRec.class)
        @ItemStackSize(32)
        @BlockDropRule(DropSelf.class)
        @BlockMeta(hardness = 12f, resistance = 24f)
        @BlockInventoryMeta(size = 9, blockInventory = Inv.class)
        Common,
        
        /**
         * modded.
         */
        @BlockData
        Empty,
        
    }
    
    /** test. */
    private enum TestVariants implements BlockVariantId
    {
        /** test. */
        Common,
    }
    
    /**
     * inventory.
     */
    public static final class Inv implements BlockInventoryInterface
    {

        @Override
        public void createInventory(BlockId block, BlockVariantId variant, Location location, McPlayerInterface player, int initialSize, boolean fixed, boolean shared) throws McException
        {
            // empty
        }

        @Override
        public InventoryId getInventory(BlockId block, BlockVariantId variant, Location location, McPlayerInterface player) throws McException
        {
            return null;
        }

        @Override
        public void onBreak(BlockId block, BlockVariantId variant, Location location)
        {
            // empty
        }
        
    }
    
    /**
     * helper class.
     */
    public static final class MyName implements NameProvider
    {
        
        @Override
        public LocalizedMessageInterface getName()
        {
            return null;
        }
        
    }
    
    /**
     * helper class.
     */
    public static final class FurnRec implements FurnaceRecipeInterface
    {
        
        @Override
        public ItemStack getReceipe(ItemId item, BlockId block, BlockVariantId variant)
        {
            return null;
        }
        
        @Override
        public float getExperience(ItemId item, BlockId block, BlockVariantId variant)
        {
            return 0;
        }
        
    }
    
    /**
     * test items.
     */
    private static final class InvalidBlock implements BlockId
    {
        
        /**
         * Constructor.
         */
        public InvalidBlock()
        {
            // empty
        }
        
        @Override
        public String name()
        {
            return "FOO"; //$NON-NLS-1$
        }
        
    }
    
}
