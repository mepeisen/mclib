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

package de.minigameslib.mclib.shared.test;

import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.ItemStackDataFragment;

/**
 * Some dummy item stack class
 * 
 * @author mepeisen
 */
public class ItemStackUtil implements ItemStackDataFragment
{
    
    /** material id. */
    private int material;

    /**
     * @param material
     */
    public ItemStackUtil(int material)
    {
        this.material = material;
    }

    /**
     * 
     */
    public ItemStackUtil()
    {
        // empty
    }

    @Override
    public void read(DataSection section)
    {
        this.material = section.getInt("material"); //$NON-NLS-1$
    }

    @Override
    public void write(DataSection section)
    {
        section.set("material", this.material); //$NON-NLS-1$
    }

    @Override
    public boolean test(DataSection section)
    {
        return section.isInt("material"); //$NON-NLS-1$
    }

    /**
     * @return the material
     */
    public int getMaterial()
    {
        return this.material;
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
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ItemStackUtil other = (ItemStackUtil) obj;
        if (this.material != other.material)
            return false;
        return true;
    }
    
}
