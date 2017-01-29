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

package de.minigameslib.mclib.impl.comp;

import java.util.UUID;

import de.minigameslib.mclib.api.objects.ZoneIdInterface;
import de.minigameslib.mclib.shared.api.com.DataSection;

/**
 * @author mepeisen
 *
 */
public class ZoneId implements ZoneIdInterface
{
    
    /** the plugin name. */
    private String pluginName;
    
    /** the uuid. */
    private UUID uuid;

    /** component type name. */
    private String type;
    
    /**
     * Constructor.
     */
    public ZoneId()
    {
        // empty
    }
    
    /**
     * @param pluginName
     * @param type 
     * @param uuid
     */
    public ZoneId(String pluginName, String type, UUID uuid)
    {
        this.pluginName = pluginName;
        this.uuid = uuid;
        this.type = type;
    }

    @Override
    public void read(DataSection section)
    {
        this.pluginName = section.getString("plugin"); //$NON-NLS-1$
        this.type = section.getString("type"); //$NON-NLS-1$
        this.uuid = UUID.fromString(section.getString("uuid")); //$NON-NLS-1$
    }
    
    @Override
    public void write(DataSection section)
    {
        section.set("plugin", this.pluginName); //$NON-NLS-1$
        section.set("type", this.type); //$NON-NLS-1$
        section.set("uuid", this.uuid.toString()); //$NON-NLS-1$
    }

    @Override
    public boolean test(DataSection section)
    {
        return section.isString("plugin") //$NON-NLS-1$
                && section.isString("type") //$NON-NLS-1$
                && section.isString("uuid") //$NON-NLS-1$
                && isUUID(section.getString("uuid")); //$NON-NLS-1$
    }

    /**
     * @param str
     * @return true if this is a uuid
     */
    private boolean isUUID(String str)
    {
        try
        {
            if (str == null) return false;
            UUID.fromString(str);
            return true;
        }
        catch (@SuppressWarnings("unused") IllegalArgumentException ex)
        {
            return false;
        }
    }

    /**
     * @return the pluginName
     */
    public String getPluginName()
    {
        return this.pluginName;
    }

    /**
     * @return the uuid
     */
    public UUID getUuid()
    {
        return this.uuid;
    }

    /**
     * @return the type
     */
    public String getType()
    {
        return this.type;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.pluginName == null) ? 0 : this.pluginName.hashCode());
        result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
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
        ZoneId other = (ZoneId) obj;
        if (this.pluginName == null)
        {
            if (other.pluginName != null)
                return false;
        }
        else if (!this.pluginName.equals(other.pluginName))
            return false;
        if (this.type == null)
        {
            if (other.type != null)
                return false;
        }
        else if (!this.type.equals(other.type))
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

    @Override
    public String toString()
    {
        return "zone-" + this.pluginName + "-" + this.type + "-" + this.uuid; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    
}
