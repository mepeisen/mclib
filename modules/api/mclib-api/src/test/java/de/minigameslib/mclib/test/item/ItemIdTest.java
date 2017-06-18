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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.bukkit.inventory.ItemStack;
import org.junit.Test;

import de.minigameslib.mclib.api.items.BlockId;
import de.minigameslib.mclib.api.items.BlockVariantId;
import de.minigameslib.mclib.api.items.CraftingRecipes;
import de.minigameslib.mclib.api.items.DefaultAxeDigRule;
import de.minigameslib.mclib.api.items.DefaultHoeDigRule;
import de.minigameslib.mclib.api.items.DefaultPickaxeDigRule;
import de.minigameslib.mclib.api.items.DefaultShovelDigRule;
import de.minigameslib.mclib.api.items.DefaultSwordDigRule;
import de.minigameslib.mclib.api.items.DefaultToolDmg;
import de.minigameslib.mclib.api.items.DefaultWeaponDmg;
import de.minigameslib.mclib.api.items.FurnaceRecipe;
import de.minigameslib.mclib.api.items.FurnaceRecipeInterface;
import de.minigameslib.mclib.api.items.ItemArmor;
import de.minigameslib.mclib.api.items.ItemArmor.ArmorSlot;
import de.minigameslib.mclib.api.items.ItemAxe;
import de.minigameslib.mclib.api.items.ItemData;
import de.minigameslib.mclib.api.items.ItemHoe;
import de.minigameslib.mclib.api.items.ItemId;
import de.minigameslib.mclib.api.items.ItemPickaxe;
import de.minigameslib.mclib.api.items.ItemShovel;
import de.minigameslib.mclib.api.items.ItemStackSize;
import de.minigameslib.mclib.api.items.ItemSword;
import de.minigameslib.mclib.api.items.NameProvider;
import de.minigameslib.mclib.api.items.NullNameProvider;
import de.minigameslib.mclib.api.items.SelfRepair;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.spigottest.CommonTestUtil;

/**
 * Tests for {@link ItemId}.
 * 
 * @author mepeisen
 *
 */
public class ItemIdTest
{
    
    /**
     * test me.
     */
    @Test
    public void testItemClass()
    {
        CommonTestUtil.testEnumClass(ItemId.ItemClass.class);

        assertArrayEquals(new ItemId.ItemClass[]{}, TestItems.Modded.getItemClasses());
        assertArrayEquals(new ItemId.ItemClass[]{ItemId.ItemClass.Armor}, TestItems.BronzeHelmet.getItemClasses());
        assertArrayEquals(new ItemId.ItemClass[]{ItemId.ItemClass.Sword}, TestItems.BronzeSword.getItemClasses());
        assertArrayEquals(new ItemId.ItemClass[]{ItemId.ItemClass.Shovel}, TestItems.BronzeShovel.getItemClasses());
        assertArrayEquals(new ItemId.ItemClass[]{ItemId.ItemClass.Axe}, TestItems.BronzeAxe.getItemClasses());
        assertArrayEquals(new ItemId.ItemClass[]{ItemId.ItemClass.Pickaxe}, TestItems.BronzePickaxe.getItemClasses());
        assertArrayEquals(new ItemId.ItemClass[]{ItemId.ItemClass.Hoe}, TestItems.BronzeHoe.getItemClasses());
    }
    
    /**
     * test me.
     */
    @Test
    public void testModded()
    {
        assertTrue(TestItems.Modded.isModded());
        assertFalse(TestItems.NotModded.isModded());
    }
    
    /**
     * test me.
     */
    @Test(expected = IllegalStateException.class)
    public void testModdedInvalid()
    {
        new InvalidItem().isModded();
    }
    
    /**
     * test me.
     */
    @Test
    public void testTextures()
    {
        assertArrayEquals(new String[] { "texture1" }, TestItems.Modded.getTextures()); //$NON-NLS-1$
    }
    
    /**
     * test me.
     */
    @Test(expected = IllegalStateException.class)
    public void testTexturesInvalid()
    {
        new InvalidItem().getTextures();
    }
    
    /**
     * test me.
     */
    @Test
    public void testModelTextures()
    {
        assertArrayEquals(new String[] { "texture2" }, TestItems.Modded.getModelTextures()); //$NON-NLS-1$
    }
    
    /**
     * test me.
     */
    @Test(expected = IllegalStateException.class)
    public void testModelTexturesInvalid()
    {
        new InvalidItem().getModelTextures();
    }
    
    /**
     * test me.
     */
    @Test
    public void testRecipes()
    {
        assertNotNull(TestItems.Modded.recipes());
        assertNull(TestItems.NotModded.recipes());
    }
    
    /**
     * test me.
     */
    @Test(expected = IllegalStateException.class)
    public void testRecipesInvalid()
    {
        new InvalidItem().recipes();
    }
    
    /**
     * test me.
     */
    @Test
    public void testModelJson()
    {
        assertEquals("json", TestItems.Modded.getModelJson()); //$NON-NLS-1$
    }
    
    /**
     * test me.
     */
    @Test(expected = IllegalStateException.class)
    public void testModelJsonInvalid()
    {
        new InvalidItem().getModelJson();
    }
    
    /**
     * test me.
     */
    @Test
    public void testFurnaceRecipe()
    {
        assertTrue(TestItems.Modded.furnaceRecipe() instanceof FurnRec);
        assertNull(TestItems.NotModded.furnaceRecipe());
    }
    
    /**
     * test me.
     */
    @Test(expected = IllegalStateException.class)
    public void testFurnaceRecipeInvalid()
    {
        new InvalidItem().furnaceRecipe();
    }
    
    /**
     * test me.
     */
    @Test
    public void testName()
    {
        assertTrue(TestItems.Modded.nameProvider() instanceof MyName);
        assertTrue(TestItems.NotModded.nameProvider() instanceof NullNameProvider);
    }
    
    /**
     * test me.
     */
    @Test(expected = IllegalStateException.class)
    public void testNameInvalid()
    {
        new InvalidItem().nameProvider();
    }
    
    /**
     * test me.
     */
    @Test
    public void testStackSize()
    {
        assertEquals(64, TestItems.Modded.stackSize());
        assertEquals(1, TestItems.NotModded.stackSize());
    }
    
    /**
     * test me.
     */
    @Test(expected = IllegalStateException.class)
    public void testStackSizeInvalid()
    {
        new InvalidItem().stackSize();
    }
    
    /**
     * test me.
     */
    @Test
    public void testArmor()
    {
        assertNull(TestItems.Modded.armor());
        assertNotNull(TestItems.BronzeHelmet.armor());
    }
    
    /**
     * test me.
     */
    @Test(expected = IllegalStateException.class)
    public void testArmorInvalid()
    {
        new InvalidItem().armor();
    }
    
    /**
     * test me.
     */
    @Test
    public void testSword()
    {
        assertNull(TestItems.Modded.sword());
        assertNotNull(TestItems.BronzeSword.sword());
    }
    
    /**
     * test me.
     */
    @Test(expected = IllegalStateException.class)
    public void testSwordInvalid()
    {
        new InvalidItem().sword();
    }
    
    /**
     * test me.
     */
    @Test
    public void testShovel()
    {
        assertNull(TestItems.Modded.shovel());
        assertNotNull(TestItems.BronzeShovel.shovel());
    }
    
    /**
     * test me.
     */
    @Test(expected = IllegalStateException.class)
    public void testShovelInvalid()
    {
        new InvalidItem().shovel();
    }
    
    /**
     * test me.
     */
    @Test
    public void testHoe()
    {
        assertNull(TestItems.Modded.hoe());
        assertNotNull(TestItems.BronzeHoe.hoe());
    }
    
    /**
     * test me.
     */
    @Test(expected = IllegalStateException.class)
    public void testHowInvalid()
    {
        new InvalidItem().hoe();
    }
    
    /**
     * test me.
     */
    @Test
    public void testAxe()
    {
        assertNull(TestItems.Modded.axe());
        assertNotNull(TestItems.BronzeAxe.axe());
    }
    
    /**
     * test me.
     */
    @Test(expected = IllegalStateException.class)
    public void testAxeInvalid()
    {
        new InvalidItem().axe();
    }
    
    /**
     * test me.
     */
    @Test
    public void testPickaxe()
    {
        assertNull(TestItems.Modded.pickaxe());
        assertNotNull(TestItems.BronzePickaxe.pickaxe());
    }
    
    /**
     * test me.
     */
    @Test(expected = IllegalStateException.class)
    public void testPickaxeInvalid()
    {
        new InvalidItem().pickaxe();
    }
    
    /**
     * test items.
     */
    private enum TestItems implements ItemId
    {
        
        /**
         * modded.
         */
        @ItemData(
            modEnabled = true,
            modelJson = "json",
            textures = "texture1",
            modelTextures = "texture2",
            name = MyName.class)
        @CraftingRecipes
        @FurnaceRecipe(value = FurnRec.class)
        @ItemStackSize(64)
        Modded,
        
        /**
         * not modded.
         */
        @ItemData(modEnabled = false, modelJson = "json", textures = "texture1")
        NotModded,
        
        /**
         * A sword made of bronze.
         */
        @ItemData(modEnabled = true, textures = "eu/xworlds/ironmine/textures/items/bronze_sword.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
        @ItemStackSize(1)
        @ItemSword(
            damage = 4.5f,
            damageVsEntity = 1.5f,
            speed = -2.4000000953674316D,
            getItemEnchantability = 5,
            durability = 190,
            digRule = DefaultSwordDigRule.class,
            dmgRule = DefaultWeaponDmg.class,
            repairRule = SelfRepair.class)
        BronzeSword,
        /**
         * A shovel made of bronze.
         */
        @ItemData(modEnabled = true, textures = "eu/xworlds/ironmine/textures/items/bronze_shovel.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
        @ItemStackSize(1)
        @ItemShovel(
            durability = 190,
            digRule = DefaultShovelDigRule.class,
            dmgRule = DefaultToolDmg.class,
            repairRule = SelfRepair.class,
            damage = 3.0f,
            speed = -3.0D,
            getItemEnchantability = 5)
        BronzeShovel,
        /**
         * An axe made of bronze.
         */
        @ItemData(modEnabled = true, textures = "eu/xworlds/ironmine/textures/items/bronze_axe.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
        @ItemStackSize(1)
        @ItemAxe(
            durability = 190,
            digRule = DefaultAxeDigRule.class,
            dmgRule = DefaultToolDmg.class,
            repairRule = SelfRepair.class,
            damage = 8.0f,
            speed = -3.2D,
            getItemEnchantability = 5)
        BronzeAxe,
        /**
         * A hoe made of bronze.
         */
        @ItemData(modEnabled = true, textures = "eu/xworlds/ironmine/textures/items/bronze_hoe.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
        @ItemStackSize(1)
        @ItemHoe(
            durability = 190,
            digRule = DefaultHoeDigRule.class,
            dmgRule = DefaultToolDmg.class,
            repairRule = SelfRepair.class,
            damage = 0.0f,
            speed = -1.5D,
            getItemEnchantability = 5)
        BronzeHoe,
        /**
         * A pickaxe made of bronze.
         */
        @ItemData(modEnabled = true, textures = "eu/xworlds/ironmine/textures/items/bronze_pickaxe.png", modelJson = "{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"%1$s\"}}")
        @ItemStackSize(1)
        @ItemPickaxe(
            durability = 190,
            digRule = DefaultPickaxeDigRule.class,
            dmgRule = DefaultToolDmg.class,
            repairRule = SelfRepair.class,
            damage = 4.5f,
            speed = -2.8D,
            getItemEnchantability = 5)
        BronzePickaxe,
        
        /**
         * A helmet made of bronze.
         */
        @ItemData(
            modEnabled = true,
            textures = "eu/xworlds/ironmine/textures/items/bronze_helmet.png",
            modelTextures = "eu/xworlds/ironmine/textures/models/bronze/layer_1.png",
            modelJson = "{\"parent\": \"item/generated\",\"textures\": {\"layer0\": \"%1$s\"}}")
        @ItemStackSize(1)
        @ItemArmor(
            durability = 7 * 13,
            dmgReduceAmount = 1,
            getItemEnchantability = 9,
            toughness = 0.0f,
            slot = ArmorSlot.Helmet,
            repairRule = SelfRepair.class)
        BronzeHelmet,
        
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
    private static final class InvalidItem implements ItemId
    {
        
        /**
         * Constructor.
         */
        public InvalidItem()
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
