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
public class ServerBlockLocationData implements ServerBlockLocationDataFragment
{

    /**
     * the server name.
     */
    private String name;

    /**
     * the x coordinate
     */
    private int x;

    /**
     * the y coordinate
     */
    private int y;

    /**
     * the z coordinate
     */
    private int z;
    
    /**
     * Name of the world.
     */
    private String world;

    /**
     * 
     */
    public ServerBlockLocationData()
    {
        // empty
    }

    /**
     * @param x
     * @param y
     * @param z
     * @param world
     * @param serverName
     */
    public ServerBlockLocationData(int x, int y, int z, String world, String serverName)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.name = serverName;
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
    public String getName()
    {
        return this.name;
    }

    @Override
    public void read(DataSection section)
    {
        this.x = section.getInt("x"); //$NON-NLS-1$
        this.y = section.getInt("y"); //$NON-NLS-1$
        this.z = section.getInt("z"); //$NON-NLS-1$
        this.world = section.getString("world"); //$NON-NLS-1$
        this.name = section.getString("server"); //$NON-NLS-1$
    }

    @Override
    public void write(DataSection section)
    {
        section.set("x",  this.x); //$NON-NLS-1$
        section.set("y",  this.y); //$NON-NLS-1$
        section.set("z",  this.z); //$NON-NLS-1$
        section.set("world",  this.world); //$NON-NLS-1$
        section.set("server",  this.name); //$NON-NLS-1$
    }

    @Override
    public boolean test(DataSection section)
    {
        return section.isInt("x") //$NON-NLS-1$
                && section.isInt("y") //$NON-NLS-1$
                && section.isInt("z") //$NON-NLS-1$
                && section.isString("world") //$NON-NLS-1$
                && section.isString("server"); //$NON-NLS-1$
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
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
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
        ServerBlockLocationData other = (ServerBlockLocationData) obj;
        if (this.world == null)
        {
            if (other.world != null)
                return false;
        }
        else if (!this.world.equals(other.world))
            return false;
        if (this.x != other.x)
            return false;
        if (this.y != other.y)
            return false;
        if (this.z != other.z)
            return false;
        if (this.name == null)
        {
            if (other.name != null)
                return false;
        }
        else if (!this.name.equals(other.name))
            return false;
        return true;
    }
    
}
