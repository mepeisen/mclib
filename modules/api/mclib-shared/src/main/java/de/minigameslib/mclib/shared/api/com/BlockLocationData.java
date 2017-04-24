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

package de.minigameslib.mclib.shared.api.com;

/**
 * World location fragment.
 * 
 * @author mepeisen
 */
public class BlockLocationData implements BlockLocationDataFragment
{

    //CHECKSTYLE:OFF
    /**
     * the x coordinate.
     */
    private int x;

    /**
     * the y coordinate.
     */
    private int y;

    /**
     * the z coordinate.
     */
    private int z;
    //CHECKSTYLE:ON
    
    /**
     * Name of the world.
     */
    private String world;

    /**
     * Constructor for reading from file.
     */
    public BlockLocationData()
    {
        // empty
    }

    /**
     * Constructor for new object.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     * @param world world name
     */
    public BlockLocationData(int x, int y, int z, String world)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    @Override
    public int getX()
    {
        return this.x;
    }

    @Override
    public int getY()
    {
        return this.y;
    }

    @Override
    public int getZ()
    {
        return this.z;
    }

    @Override
    public String getWorld()
    {
        return this.world;
    }

    @Override
    public void read(DataSection section)
    {
        this.x = section.getInt("x"); //$NON-NLS-1$
        this.y = section.getInt("y"); //$NON-NLS-1$
        this.z = section.getInt("z"); //$NON-NLS-1$
        this.world = section.getString("world"); //$NON-NLS-1$
    }

    @Override
    public void write(DataSection section)
    {
        section.set("x",  this.x); //$NON-NLS-1$
        section.set("y",  this.y); //$NON-NLS-1$
        section.set("z",  this.z); //$NON-NLS-1$
        section.set("world",  this.world); //$NON-NLS-1$
    }

    @Override
    public boolean test(DataSection section)
    {
        return section.isInt("x") //$NON-NLS-1$
                && section.isInt("y") //$NON-NLS-1$
                && section.isInt("z") //$NON-NLS-1$
                && section.isString("world"); //$NON-NLS-1$
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.world == null) ? 0 : this.world.hashCode());
        result = prime * result + this.x;
        result = prime * result + this.y;
        result = prime * result + this.z;
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
        BlockLocationData other = (BlockLocationData) obj;
        if (this.world == null)
        {
            if (other.world != null)
            {
                return false;
            }
        }
        else if (!this.world.equals(other.world))
        {
            return false;
        }
        if (this.x != other.x)
        {
            return false;
        }
        if (this.y != other.y)
        {
            return false;
        }
        if (this.z != other.z)
        {
            return false;
        }
        return true;
    }
    
}
