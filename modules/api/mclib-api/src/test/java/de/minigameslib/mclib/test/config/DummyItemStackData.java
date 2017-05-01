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

package de.minigameslib.mclib.test.config;

import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.config.ConfigItemStackData;
import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.PersistentField;

/**
 * A dummy helper for item stacks.
 * 
 * @author mepeisen
 */
public class DummyItemStackData extends AnnotatedDataFragment implements ConfigItemStackData
{
    
    /** the material. */
    @PersistentField
    public int material;

    /**
     * 
     */
    public DummyItemStackData()
    {
        super();
    }

    /**
     * Constructor
     * @param itemStack
     */
    @SuppressWarnings("deprecation")
    public DummyItemStackData(ItemStack itemStack)
    {
        this.material = itemStack.getType().getId();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.material;
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
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
        DummyItemStackData other = (DummyItemStackData) obj;
        if (this.material != other.material)
        {
            return false;
        }
        return true;
    }

    @Override
    public ItemStack toBukkit()
    {
        // empty
        return null;
    }
    
}
