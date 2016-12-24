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
public class LocationData implements LocationDataFragment
{
    
    /**
     * the x coordinate
     */
    private double x;
    
    /**
     * the y coordinate
     */
    private double y;
    
    /**
     * the z coordinate
     */
    private double z;
    
    /**
     * The yaw
     */
    private float yaw;
    
    /**
     * The pitch
     */
    private float pitch;
    
    /**
     * the world name.
     */
    private String world;

    /**
     * Constructor
     */
    public LocationData()
    {
        // empty
    }

    /**
     * @param x
     * @param y
     * @param z
     * @param yaw
     * @param pitch
     * @param world
     */
    public LocationData(double x, double y, double z, float yaw, float pitch, String world)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = world;
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

    @Override
    public float getYaw()
    {
        return this.yaw;
    }

    @Override
    public float getPitch()
    {
        return this.pitch;
    }

    @Override
    public String getWorld()
    {
        return this.world;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(this.pitch);
        result = prime * result + ((this.world == null) ? 0 : this.world.hashCode());
        long temp;
        temp = Double.doubleToLongBits(this.x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(this.y);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + Float.floatToIntBits(this.yaw);
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
        LocationData other = (LocationData) obj;
        if (Float.floatToIntBits(this.pitch) != Float.floatToIntBits(other.pitch))
            return false;
        if (this.world == null)
        {
            if (other.world != null)
                return false;
        }
        else if (!this.world.equals(other.world))
            return false;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x))
            return false;
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y))
            return false;
        if (Float.floatToIntBits(this.yaw) != Float.floatToIntBits(other.yaw))
            return false;
        if (Double.doubleToLongBits(this.z) != Double.doubleToLongBits(other.z))
            return false;
        return true;
    }

    @Override
    public void read(DataSection section)
    {
        this.x = section.getDouble("x"); //$NON-NLS-1$
        this.y = section.getDouble("y"); //$NON-NLS-1$
        this.z = section.getDouble("z"); //$NON-NLS-1$
        this.pitch = section.getFloat("pitch"); //$NON-NLS-1$
        this.yaw = section.getFloat("yaw"); //$NON-NLS-1$
        this.world = section.getString("world"); //$NON-NLS-1$
    }

    @Override
    public void write(DataSection section)
    {
        section.set("x",  this.x); //$NON-NLS-1$
        section.set("y",  this.y); //$NON-NLS-1$
        section.set("z",  this.z); //$NON-NLS-1$
        section.set("pitch",  this.pitch); //$NON-NLS-1$
        section.set("yaw",  this.yaw); //$NON-NLS-1$
        section.set("world",  this.world); //$NON-NLS-1$
    }

    @Override
    public boolean test(DataSection section)
    {
        return section.isDouble("x") //$NON-NLS-1$
                && section.isDouble("y") //$NON-NLS-1$
                && section.isDouble("z") //$NON-NLS-1$
                && section.isFloat("pitch") //$NON-NLS-1$
                && section.isFloat("yaw") //$NON-NLS-1$
                && section.isString("world"); //$NON-NLS-1$
    }
    
}