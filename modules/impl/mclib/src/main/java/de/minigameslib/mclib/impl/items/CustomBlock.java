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

package de.minigameslib.mclib.impl.items;

import java.util.HashMap;
import java.util.Map;

import de.minigameslib.mclib.api.items.BlockId;
import de.minigameslib.mclib.api.items.BlockVariantId;

/**
 * A custom registered item.
 * 
 * @author mepeisen
 */
class CustomBlock implements Comparable<CustomBlock>
{
    
    /** the plugin name. */
    private final String pluginName;
    
    /** the enum name. */
    private final String enumName;
    
    /** the numeric block id. */
    private int numId;
    
    /** the block id. */
    private final BlockId blockId;
    
    /**
     * the block variants.
     */
    private final Map<Integer, BlockVariantId> variants = new HashMap<>();

    /**
     * @param pluginName
     * @param enumName
     * @param blockId
     */
    public CustomBlock(String pluginName, String enumName, BlockId blockId)
    {
        this.pluginName = pluginName;
        this.enumName = enumName;
        this.blockId = blockId;
        for (final BlockVariantId variant : blockId.variants())
        {
            this.variants.put(variant.ordinal(), variant);
        }
    }

    @Override
    public int compareTo(CustomBlock o)
    {
        int result = this.pluginName.compareTo(o.pluginName);
        if (result == 0)
        {
            result = this.enumName.compareTo(o.enumName);
        }
        return result;
    }

    /**
     * @return the pluginName
     */
    public String getPluginName()
    {
        return this.pluginName;
    }

    /**
     * @return the enumName
     */
    public String getEnumName()
    {
        return this.enumName;
    }

    /**
     * @return the numId
     */
    public int getNumId()
    {
        return this.numId;
    }

    /**
     * @param numId the numId to set
     */
    public void setNumId(int numId)
    {
        this.numId = numId;
    }

    /**
     * @return the blockId
     */
    public BlockId getBlockId()
    {
        return this.blockId;
    }

    /**
     * @param variantId
     * @return custom variant
     */
    public BlockVariantId getVariant(int variantId)
    {
        return this.variants.get(variantId);
    }
    
}
