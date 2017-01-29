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

/**
 * A custom registered item.
 * 
 * @author mepeisen
 */
class CustomItem implements Comparable<CustomItem>
{
    
    /** the plugin name. */
    private final String pluginName;
    
    /** the enum name. */
    private final String enumName;
    
    /** the type. */
    private CustomItemTypes customType;
    
    /** the durability. */
    private Durability customDurability;
    
    /** the item id. */
    private final ItemId itemId;

    /**
     * @param pluginName
     * @param enumName
     * @param itemId
     */
    public CustomItem(String pluginName, String enumName, ItemId itemId)
    {
        this.pluginName = pluginName;
        this.enumName = enumName;
        this.itemId = itemId;
    }

    @Override
    public int compareTo(CustomItem o)
    {
        int result = this.pluginName.compareTo(o.pluginName);
        if (result == 0)
        {
            result = this.enumName.compareTo(o.enumName);
        }
        return result;
    }

    /**
     * @return the customType
     */
    public CustomItemTypes getCustomType()
    {
        return this.customType;
    }

    /**
     * @param customType the customType to set
     */
    public void setCustomType(CustomItemTypes customType)
    {
        this.customType = customType;
    }

    /**
     * @return the customDurability
     */
    public Durability getCustomDurability()
    {
        return this.customDurability;
    }

    /**
     * @param customDurability the customDurability to set
     */
    public void setCustomDurability(Durability customDurability)
    {
        this.customDurability = customDurability;
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
     * @return the itemId
     */
    public ItemId getItemId()
    {
        return this.itemId;
    }
    
}
