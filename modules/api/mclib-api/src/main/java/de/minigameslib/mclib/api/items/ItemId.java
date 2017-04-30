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

import java.util.ArrayList;
import java.util.List;

import de.minigameslib.mclib.api.enums.McUniqueEnumInterface;

/**
 * Interface to be used for enumerations. Declaring items to be used.
 * 
 * @author mepeisen
 */
public interface ItemId extends McUniqueEnumInterface
{
    
    /**
     * Returns the mod item flag.
     * @return mod item flag
     */
    default boolean isModded()
    {
        try
        {
            final ItemData data = this.getClass().getDeclaredField(this.name()).getAnnotation(ItemData.class);
            return data.modEnabled();
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the resource paths to the textures.
     * @return resource paths to textures
     */
    default String[] getTextures()
    {
        try
        {
            final ItemData data = this.getClass().getDeclaredField(this.name()).getAnnotation(ItemData.class);
            return data.textures();
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the resource paths to the textures; used by custom armor.
     * @return resource paths to textures
     */
    default String[] getModelTextures()
    {
        try
        {
            final ItemData data = this.getClass().getDeclaredField(this.name()).getAnnotation(ItemData.class);
            return data.modelTextures();
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the crafting recipes.
     * @return crafting recipes
     */
    default CraftingRecipes recipes()
    {
        try
        {
            final CraftingRecipes data = this.getClass().getDeclaredField(this.name()).getAnnotation(CraftingRecipes.class);
            return data;
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * the custom item model json.
     * @return custom item model json
     */
    default String getModelJson()
    {
        try
        {
            final ItemData data = this.getClass().getDeclaredField(this.name()).getAnnotation(ItemData.class);
            return data.modelJson();
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * The furnace recipe.
     * @return the furnace recipe
     */
    default FurnaceRecipeInterface furnaceRecipe()
    {
        try
        {
            final FurnaceRecipe data = this.getClass().getDeclaredField(this.name()).getAnnotation(FurnaceRecipe.class);
            return data == null ? null : data.value().newInstance();
        }
        catch (NoSuchFieldException | InstantiationException | IllegalAccessException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the provider for getting the item name.
     * @return provider for getting the name.
     */
    default NameProvider nameProvider()
    {
        try
        {
            final ItemData data = this.getClass().getDeclaredField(this.name()).getAnnotation(ItemData.class);
            return data.name().newInstance();
        }
        catch (NoSuchFieldException | InstantiationException | IllegalAccessException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the stack size.
     * @return stack size
     */
    default int stackSize()
    {
        try
        {
            final ItemStackSize data = this.getClass().getDeclaredField(this.name()).getAnnotation(ItemStackSize.class);
            return data == null ? 1 : data.value();
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * The armor data.
     * @return item armor data
     */
    default ItemArmor armor()
    {
        try
        {
            final ItemArmor data = this.getClass().getDeclaredField(this.name()).getAnnotation(ItemArmor.class);
            return data;
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * The sword data.
     * @return item sword data
     */
    default ItemSword sword()
    {
        try
        {
            final ItemSword data = this.getClass().getDeclaredField(this.name()).getAnnotation(ItemSword.class);
            return data;
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * The shovel data.
     * @return item shovel data
     */
    default ItemShovel shovel()
    {
        try
        {
            final ItemShovel data = this.getClass().getDeclaredField(this.name()).getAnnotation(ItemShovel.class);
            return data;
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * The pickaxe data.
     * @return item sword data
     */
    default ItemPickaxe pickaxe()
    {
        try
        {
            final ItemPickaxe data = this.getClass().getDeclaredField(this.name()).getAnnotation(ItemPickaxe.class);
            return data;
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * The hoe data.
     * @return item hoe data
     */
    default ItemHoe hoe()
    {
        try
        {
            final ItemHoe data = this.getClass().getDeclaredField(this.name()).getAnnotation(ItemHoe.class);
            return data;
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * The axe data.
     * @return item axe data
     */
    default ItemAxe axe()
    {
        try
        {
            final ItemAxe data = this.getClass().getDeclaredField(this.name()).getAnnotation(ItemAxe.class);
            return data;
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * class of items.
     */
    enum ItemClass
    {
        /** armor. */
        Armor,
        /** sword/weapon. */
        Sword,
        /** hoe. */
        Hoe,
        /** axe. */
        Axe,
        /** pickaxe. */
        Pickaxe,
        /** shovel. */
        Shovel
    }
    
    /**
     * Returns the item classes of this item.
     * @return item classes
     */
    default ItemClass[] getItemClasses()
    {
        final List<ItemClass> result = new ArrayList<>();
        if (this.armor() != null)
        {
            result.add(ItemClass.Armor);
        }
        if (this.sword() != null)
        {
            result.add(ItemClass.Sword);
        }
        if (this.hoe() != null)
        {
            result.add(ItemClass.Hoe);
        }
        if (this.axe() != null)
        {
            result.add(ItemClass.Axe);
        }
        if (this.pickaxe() != null)
        {
            result.add(ItemClass.Pickaxe);
        }
        if (this.shovel() != null)
        {
            result.add(ItemClass.Shovel);
        }
        return result.toArray(new ItemClass[result.size()]);
    }
    
}
