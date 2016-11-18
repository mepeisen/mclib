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
 * Vector data.
 * 
 * @author mepeisen
 */
public class VectorData implements DataFragment
{
    
    /** x coordinate. */
    private double x;
    /** y coordinate. */
    private double y;
    /** z coordinate. */
    private double z;

    /**
     * @param x
     * @param y
     * @param z
     */
    public VectorData(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
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
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VectorData other = (VectorData) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x))
            return false;
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y))
            return false;
        if (Double.doubleToLongBits(this.z) != Double.doubleToLongBits(other.z))
            return false;
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

    /**
     * @return the x
     */
    public double getX()
    {
        return this.x;
    }

    /**
     * @return the y
     */
    public double getY()
    {
        return this.y;
    }

    /**
     * @return the z
     */
    public double getZ()
    {
        return this.z;
    }
    
}
