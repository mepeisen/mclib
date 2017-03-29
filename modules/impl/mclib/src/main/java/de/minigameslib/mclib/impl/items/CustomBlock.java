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
import de.minigameslib.mclib.api.items.NameProvider;
import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.PersistentField;

/**
 * A custom registered item.
 * 
 * @author mepeisen
 */
public class CustomBlock extends AnnotatedDataFragment
{
    
    /** the plugin name. */
    @PersistentField
    protected String pluginName;
    
    /** the enum name. */
    @PersistentField
    protected String enumName;
    
    /** the numeric block id. */
    @PersistentField
    protected int numId;
    
    /** the block id. */
    private BlockId blockId;
    
    /**
     * the block variants.
     */
    private final Map<Integer, BlockVariantId> variants = new HashMap<>();
    
    /** the name provider. */
    private NameProvider nameProvider;

    /**
     */
    public CustomBlock()
    {
        // empty
    }

    /**
     * @param pluginName
     * @param enumName
     */
    public CustomBlock(String pluginName, String enumName)
    {
        this.pluginName = pluginName;
        this.enumName = enumName;
    }

    /**
     * @param pluginName
     * @param enumName
     * @param blockId
     */
    public CustomBlock(String pluginName, String enumName, BlockId blockId)
    {
        this.pluginName = pluginName;
        this.enumName = enumName;
        this.setBlockId(blockId);
    }

    /**
     * @param blockId the blockId to set
     */
    public void setBlockId(BlockId blockId)
    {
        this.blockId = blockId;
        for (final BlockVariantId variant : blockId.variants())
        {
            this.variants.put(variant.ordinal(), variant);
        }
        this.nameProvider = blockId.nameProvider();
    }

    /**
     * @return the nameProvider
     */
    public NameProvider getNameProvider()
    {
        return this.nameProvider;
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

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.enumName == null) ? 0 : this.enumName.hashCode());
        result = prime * result + ((this.pluginName == null) ? 0 : this.pluginName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CustomBlock other = (CustomBlock) obj;
        if (this.enumName == null)
        {
            if (other.enumName != null)
                return false;
        }
        else if (!this.enumName.equals(other.enumName))
            return false;
        if (this.pluginName == null)
        {
            if (other.pluginName != null)
                return false;
        }
        else if (!this.pluginName.equals(other.pluginName))
            return false;
        return true;
    }
    
}
