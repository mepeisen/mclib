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

import de.minigameslib.mclib.api.items.ItemId;
import de.minigameslib.mclib.api.items.NameProvider;
import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.PersistentField;

/**
 * A custom registered item.
 * 
 * @author mepeisen
 */
public class CustomItem extends AnnotatedDataFragment
{
    
    /** the plugin name. */
    @PersistentField
    protected String          pluginName;
    
    /** the enum name. */
    @PersistentField
    protected String          enumName;
    
    /** the type. */
    @PersistentField
    protected CustomItemTypes customType;
    
    /** the item durability. */
    @PersistentField
    protected short           durability;
    
    /**
     * the numeric item id for modded items.
     */
    @PersistentField
    protected int             numId;
    
    /** the durability. */
    private Durability        customDurability;
    
    /** the item id. */
    private ItemId            itemId;
    
    /** the name provider. */
    private NameProvider      nameProvider;
    
    /**
     * Constructor to read from config.
     */
    public CustomItem()
    {
        // empty
    }
    
    /**
     * Constructor.
     * 
     * @param pluginName
     *            plugin name
     * @param enumName
     *            enum value name
     */
    public CustomItem(String pluginName, String enumName)
    {
        this.pluginName = pluginName;
        this.enumName = enumName;
    }
    
    /**
     * Constructor.
     * 
     * @param pluginName
     *            plugin name
     * @param enumName
     *            enumeration value name.
     * @param itemId
     *            item id to use
     */
    public CustomItem(String pluginName, String enumName, ItemId itemId)
    {
        this.pluginName = pluginName;
        this.enumName = enumName;
        this.setItemId(itemId);
    }
    
    /**
     * Returns the numeric item id for modded items.
     * 
     * @return the numId
     */
    public int getNumId()
    {
        return this.numId;
    }
    
    /**
     * Sets the numeric item id.
     * 
     * @param numId
     *            the numId to set
     */
    public void setNumId(int numId)
    {
        this.numId = numId;
    }
    
    /**
     * Returns the name provider.
     * 
     * @return the nameProvider
     */
    public NameProvider getNameProvider()
    {
        return this.nameProvider;
    }
    
    /**
     * Returns the short durability value.
     * 
     * @return the durability
     */
    public short getDurability()
    {
        return this.durability;
    }
    
    /**
     * Sets the short durability value.
     * 
     * @param durability
     *            the durability to set
     */
    public void setDurability(short durability)
    {
        this.durability = durability;
    }
    
    /**
     * Sets item id.
     * 
     * @param itemId
     *            the itemId to set
     */
    public void setItemId(ItemId itemId)
    {
        this.itemId = itemId;
        this.nameProvider = itemId == null ? null : itemId.nameProvider();
    }
    
    /**
     * Returns the custom item type.
     * 
     * @return the customType
     */
    public CustomItemTypes getCustomType()
    {
        return this.customType;
    }
    
    /**
     * Sets the custom item type.
     * 
     * @param customType
     *            the customType to set
     */
    public void setCustomType(CustomItemTypes customType)
    {
        this.customType = customType;
    }
    
    /**
     * Returns the custom durability.
     * 
     * @return the customDurability
     */
    public Durability getCustomDurability()
    {
        return this.customDurability;
    }
    
    /**
     * Sets the custom durability.
     * 
     * @param customDurability
     *            the customDurability to set
     */
    public void setCustomDurability(Durability customDurability)
    {
        this.customDurability = customDurability;
        this.durability = customDurability.getItemStackDurability();
    }
    
    /**
     * Returns the plugin name.
     * 
     * @return the pluginName
     */
    public String getPluginName()
    {
        return this.pluginName;
    }
    
    /**
     * Returns the enumeration value name.
     * 
     * @return the enumName
     */
    public String getEnumName()
    {
        return this.enumName;
    }
    
    /**
     * Returns the item id.
     * 
     * @return the itemId
     */
    public ItemId getItemId()
    {
        return this.itemId;
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
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        CustomItem other = (CustomItem) obj;
        if (this.enumName == null)
        {
            if (other.enumName != null)
            {
                return false;
            }
        }
        else if (!this.enumName.equals(other.enumName))
        {
            return false;
        }
        if (this.pluginName == null)
        {
            if (other.pluginName != null)
            {
                return false;
            }
        }
        else if (!this.pluginName.equals(other.pluginName))
        {
            return false;
        }
        return true;
    }
    
}
