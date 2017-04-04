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

import de.minigameslib.mclib.api.enums.McUniqueEnumInterface;

/**
 * Interface to be used for enumerations. Declaring block vatriants to be used.
 * 
 * @author mepeisen
 */
public interface BlockVariantId extends McUniqueEnumInterface
{
    
    /**
     * Returns the resource paths to the textures
     * @return resource paths to textures
     */
    default String[] getTextures()
    {
        try
        {
            final BlockVariantData data = this.getClass().getDeclaredField(this.name()).getAnnotation(BlockVariantData.class);
            return data.textures();
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the crafting recipes
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
     * the custom item model json
     * @return custom item model json
     */
    default String getModelJson()
    {
        try
        {
            final BlockVariantData data = this.getClass().getDeclaredField(this.name()).getAnnotation(BlockVariantData.class);
            return data.modelJson();
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * The furnace recipe
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
     * Returns the numeric id (variant id)
     * @return numeric id
     */
    int ordinal();
    
    /**
     * Returns the provider for getting the item name.
     * @return provider for getting the name.
     */
    default NameProvider nameProvider()
    {
        try
        {
            final BlockVariantData data = this.getClass().getDeclaredField(this.name()).getAnnotation(BlockVariantData.class);
            return data.name().newInstance();
        }
        catch (NoSuchFieldException | InstantiationException | IllegalAccessException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
}
