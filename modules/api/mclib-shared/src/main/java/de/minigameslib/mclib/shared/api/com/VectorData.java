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

import java.util.Arrays;
import java.util.HashSet;

/**
 * Vector data.
 * 
 * @author mepeisen
 */
public class VectorData implements VectorDataFragment
{
    
    // CHECKSTYLE:OFF
    /** x coordinate. */
    private double x;
    /** y coordinate. */
    private double y;
    /** z coordinate. */
    private double z;
    // CHECKSTYLE:ON
    
    /**
     * Constructor to create vector from new data.
     * 
     * @param x
     *            the x coordinate
     * @param y
     *            the y coordinate
     * @param z
     *            the z coordinate
     */
    public VectorData(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * Constructor to read from file.
     */
    public VectorData()
    {
        // empty
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(this.x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.y);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.z);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        VectorData other = (VectorData) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x))
        {
            return false;
        }
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y))
        {
            return false;
        }
        if (Double.doubleToLongBits(this.z) != Double.doubleToLongBits(other.z))
        {
            return false;
        }
        return true;
    }
    
    @Override
    public void read(DataSection section)
    {
        this.x = section.getDouble("x", 0.0d); //$NON-NLS-1$
        this.y = section.getDouble("y", 0.0d); //$NON-NLS-1$
        this.z = section.getDouble("z", 0.0d); //$NON-NLS-1$
    }
    
    @Override
    public void write(DataSection section)
    {
        section.set("x", this.x); //$NON-NLS-1$
        section.set("y", this.y); //$NON-NLS-1$
        section.set("z", this.z); //$NON-NLS-1$
    }
    
    @Override
    public boolean test(DataSection section)
    {
        boolean result = true;
        result &= section.isDouble("x"); //$NON-NLS-1$
        result &= section.isDouble("y"); //$NON-NLS-1$
        result &= section.isDouble("z"); //$NON-NLS-1$
        result &= section.getKeys(true).equals(new HashSet<>(Arrays.asList("x", "y", "z"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        return result;
    }
    
    @Override
    public double getX()
    {
        return this.x;
    }
    
    @Override
    public double getY()
    {
        return this.y;
    }
    
    @Override
    public double getZ()
    {
        return this.z;
    }
    
}
