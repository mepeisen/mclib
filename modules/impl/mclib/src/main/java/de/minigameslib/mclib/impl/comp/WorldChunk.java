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

import org.bukkit.Chunk;
import org.bukkit.Location;

/**
 * Helper class for minecraft chunks of different servers and worlds.
 * 
 * @author mepeisen
 *
 */
public class WorldChunk
{
    
    /**
     * the absolute server name; {@code null} for current/local server.
     */
    private final String serverName;
    
    /** the world name. */
    private final String worldName;
    
    // CHECKSTYLE:OFF
    /** x coordinate of the chunk. */
    private final int    x;
    
    /** z coordinate of the chunk. */
    private final int    z;
    // CHECKSTYLE:ON
    
    /** pre calculated hash. */
    private final int    hash;
    
    /**
     * Constructor to create the world chunk.
     * 
     * @param serverName
     *            server name
     * @param worldName
     *            world name
     * @param x
     *            chunk x coord
     * @param z
     *            chunk y coord
     */
    public WorldChunk(String serverName, String worldName, int x, int z)
    {
        this.serverName = serverName;
        this.worldName = worldName;
        this.x = x;
        this.z = z;
        
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.serverName == null) ? 0 : this.serverName.hashCode());
        result = prime * result + ((this.worldName == null) ? 0 : this.worldName.hashCode());
        result = prime * result + this.x;
        result = prime * result + this.z;
        this.hash = result;
    }
    
    /**
     * Constructor to create the world chunk.
     * 
     * @param chunk
     *            world chunk.
     */
    public WorldChunk(Chunk chunk)
    {
        this(null, chunk.getWorld().getName(), chunk.getX(), chunk.getZ());
    }
    
    /**
     * Constructor to create the world chunk.
     * 
     * @param location
     *            world location.
     */
    public WorldChunk(Location location)
    {
        this(location.getChunk());
    }
    
    @Override
    public int hashCode()
    {
        return this.hash;
    }
    
    /**
     * Returns the server name.
     * 
     * @return the serverName
     */
    public String getServerName()
    {
        return this.serverName;
    }
    
    /**
     * Returns the world name.
     * 
     * @return the worldName
     */
    public String getWorldName()
    {
        return this.worldName;
    }
    
    /**
     * Returns the chunk x coord.
     * 
     * @return the x
     */
    public int getX()
    {
        return this.x;
    }
    
    /**
     * Returns the chunk y coord.
     * 
     * @return the z
     */
    public int getZ()
    {
        return this.z;
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
        WorldChunk other = (WorldChunk) obj;
        if (this.hash != other.hash)
        {
            return false;
        }
        if (this.serverName == null)
        {
            if (other.serverName != null)
            {
                return false;
            }
        }
        else if (!this.serverName.equals(other.serverName))
        {
            return false;
        }
        if (this.worldName == null)
        {
            if (other.worldName != null)
            {
                return false;
            }
        }
        else if (!this.worldName.equals(other.worldName))
        {
            return false;
        }
        if (this.x != other.x)
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
