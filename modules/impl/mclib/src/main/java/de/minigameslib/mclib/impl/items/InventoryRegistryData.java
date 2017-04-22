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

import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.PersistentField;

/**
 * Registry entry of inventories.
 * 
 * @author mepeisen
 */
public class InventoryRegistryData extends AnnotatedDataFragment
{
    
    /**
     * the plugin name
     */
    @PersistentField
    protected String pluginName;
    
    /**
     * the uuid
     */
    @PersistentField
    protected String uuid;
    
    /**
     * the enum name
     */
    @PersistentField
    protected String enumName;

    /**
     * Constructor for reading from file
     */
    public InventoryRegistryData()
    {
        super();
    }

    /**
     * Constructor for new data
     * @param pluginName
     * @param uuid
     * @param enumName
     */
    public InventoryRegistryData(String pluginName, String uuid, String enumName)
    {
        this.pluginName = pluginName;
        this.uuid = uuid;
        this.enumName = enumName;
    }

    /**
     * @return the pluginName
     */
    public String getPluginName()
    {
        return this.pluginName;
    }

    /**
     * @param pluginName the pluginName to set
     */
    public void setPluginName(String pluginName)
    {
        this.pluginName = pluginName;
    }

    /**
     * @return the uuid
     */
    public String getUuid()
    {
        return this.uuid;
    }

    /**
     * @param uuid the uuid to set
     */
    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    /**
     * @return the enumName
     */
    public String getEnumName()
    {
        return this.enumName;
    }

    /**
     * @param enumName the enumName to set
     */
    public void setEnumName(String enumName)
    {
        this.enumName = enumName;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.enumName == null) ? 0 : this.enumName.hashCode());
        result = prime * result + ((this.pluginName == null) ? 0 : this.pluginName.hashCode());
        result = prime * result + ((this.uuid == null) ? 0 : this.uuid.hashCode());
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
        InventoryRegistryData other = (InventoryRegistryData) obj;
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
        if (this.uuid == null)
        {
            if (other.uuid != null)
                return false;
        }
        else if (!this.uuid.equals(other.uuid))
            return false;
        return true;
    }
    
}
