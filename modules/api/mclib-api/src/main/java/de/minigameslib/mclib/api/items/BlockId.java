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
 * Interface to be used for enumerations. Declaring blocks to be used.
 * 
 * @author mepeisen
 */
public interface BlockId extends McUniqueEnumInterface
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
            return data == null ? null : data.textures();
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
            return data == null ? null : data.modelJson();
        }
        catch (NoSuchFieldException ex)
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
            final BlockData data = this.getClass().getDeclaredField(this.name()).getAnnotation(BlockData.class);
            return data.name().newInstance();
        }
        catch (NoSuchFieldException | InstantiationException | IllegalAccessException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * Returns the configured variants.
     * @return configured variants.
     */
    @SuppressWarnings("unchecked")
    default BlockVariantId[] variants()
    {
        try
        {
            final BlockData data = this.getClass().getDeclaredField(this.name()).getAnnotation(BlockData.class);
            final Class<? extends BlockVariantId> clazz = data.variants();
            final List<BlockVariantId> result = new ArrayList<>();
            for (final Object obj : ((Class<? extends Enum<?>>) clazz).getEnumConstants())
            {
                result.add((BlockVariantId) obj);
            }
            return result.toArray(new BlockVariantId[result.size()]);
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * The drop rule
     * @return the drop rule
     */
    default BlockDropRuleInterface dropRule()
    {
        try
        {
            final BlockDropRule data = this.getClass().getDeclaredField(this.name()).getAnnotation(BlockDropRule.class);
            return data == null ? null : data.value().newInstance();
        }
        catch (NoSuchFieldException | InstantiationException | IllegalAccessException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
    /**
     * The meta data
     * @return block meta data
     */
    default BlockMeta meta()
    {
        try
        {
            final BlockMeta data = this.getClass().getDeclaredField(this.name()).getAnnotation(BlockMeta.class);
            return data;
        }
        catch (NoSuchFieldException ex)
        {
            throw new IllegalStateException(ex);
        }
    }
    
}
