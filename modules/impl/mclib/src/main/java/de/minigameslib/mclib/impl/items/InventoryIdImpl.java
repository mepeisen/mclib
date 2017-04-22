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

import java.util.UUID;

import de.minigameslib.mclib.api.items.InventoryId;
import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.PersistentField;

/**
 * @author mepeisen
 */
public class InventoryIdImpl extends AnnotatedDataFragment implements InventoryId
{
    
    /**
     * inventory unique id.
     */
    @PersistentField
    protected String uuid;

    /**
     * Constructor for rading from config
     */
    public InventoryIdImpl()
    {
        // empty
    }

    /**
     * @param uuid
     */
    public InventoryIdImpl(UUID uuid)
    {
        this.uuid = uuid.toString();
    }

    /**
     * @return the uuid
     */
    public UUID getUuid()
    {
        return UUID.fromString(this.uuid);
    }

    /**
     * @param uuid the uuid to set
     */
    public void setUuid(UUID uuid)
    {
        this.uuid = uuid.toString();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
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
        InventoryIdImpl other = (InventoryIdImpl) obj;
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
        return "inv-" + this.uuid; //$NON-NLS-1$
    }
    
}
