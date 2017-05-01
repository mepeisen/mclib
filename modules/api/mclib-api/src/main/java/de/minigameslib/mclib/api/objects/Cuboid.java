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

package de.minigameslib.mclib.api.objects;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import de.minigameslib.mclib.api.config.ConfigurationValueInterface;
import de.minigameslib.mclib.shared.api.com.DataFragment;
import de.minigameslib.mclib.shared.api.com.DataSection;

/**
 * Modified by:
 *
 * @author instancelabs
 *
 *         Original version by:
 * @author Pandemoneus - https://github.com/Pandemoneus
 */
public final class Cuboid implements DataFragment
{
    /** high points. */
    private Location highPoints;
    /** low points. */
    private Location lowPoints;
    
    /**
     * Constructor for a null Cuboid; used by {@link ConfigurationValueInterface#getObject()}.
     */
    public Cuboid()
    {
        this.highPoints = null;
        this.lowPoints = null;
    }
    
    /**
     * Constructs a new cuboid.
     * 
     * @param startLoc
     *            the first point
     * @param endLoc
     *            the second point
     */
    public Cuboid(final Location startLoc, final Location endLoc)
    {
        
        if (startLoc != null && endLoc != null)
        {
            final int lowx = Math.min(startLoc.getBlockX(), endLoc.getBlockX());
            final int lowy = Math.min(startLoc.getBlockY(), endLoc.getBlockY());
            final int lowz = Math.min(startLoc.getBlockZ(), endLoc.getBlockZ());
            
            final int highx = Math.max(startLoc.getBlockX(), endLoc.getBlockX());
            final int highy = Math.max(startLoc.getBlockY(), endLoc.getBlockY());
            final int highz = Math.max(startLoc.getBlockZ(), endLoc.getBlockZ());
            
            this.highPoints = new Location(startLoc.getWorld(), highx, highy, highz);
            this.lowPoints = new Location(startLoc.getWorld(), lowx, lowy, lowz);
        }
        else
        {
            this.highPoints = null;
            this.lowPoints = null;
        }
        
    }
    
    /**
     * Returns a new cuboid with given low locations.
     * 
     * @param lowPoints
     *            the new low location
     * @return new cuboid
     */
    public Cuboid setLowLoc(Location lowPoints)
    {
        return new Cuboid(lowPoints, this.highPoints == null ? lowPoints : this.highPoints);
    }
    
    /**
     * Returns a new cuboid with given high location.
     * 
     * @param highPoints
     *            the new high location
     * @return new cuboid
     */
    public Cuboid setHighLoc(Location highPoints)
    {
        return new Cuboid(this.lowPoints == null ? highPoints : this.lowPoints, highPoints);
    }
    
    /**
     * Determines whether the passed area is within this area.
     * 
     * @param area
     *            the area to check
     * @return true if the area is within this area, otherwise false
     */
    public boolean isAreaWithinArea(final Cuboid area)
    {
        return this.containsLoc(area.highPoints) && this.containsLoc(area.lowPoints);
    }
    
    /**
     * Determines whether the this cuboid contains the passed location.
     * 
     * @param loc
     *            the location to check
     * @return true if the location is within this cuboid, otherwise false
     */
    public boolean containsLoc(final Location loc)
    {
        if (this.highPoints == null || this.lowPoints == null)
        {
            return false;
        }
        if (loc == null || !loc.getWorld().equals(this.highPoints.getWorld()))
        {
            return false;
        }
        
        return this.lowPoints.getBlockX() <= loc.getBlockX() && this.highPoints.getBlockX() >= loc.getBlockX() && this.lowPoints.getBlockZ() <= loc.getBlockZ()
            && this.highPoints.getBlockZ() >= loc.getBlockZ() && this.lowPoints.getBlockY() <= loc.getBlockY() && this.highPoints.getBlockY() >= loc.getBlockY();
    }
    
    /**
     * Determines whether the this cuboid contains the passed location without y coord.
     * 
     * @param loc
     *            the location to check
     * @return true if the location is within this cuboid without y coord, otherwise false
     */
    public boolean containsLocWithoutY(final Location loc)
    {
        if (this.highPoints == null || this.lowPoints == null)
        {
            return false;
        }
        if (loc == null || !loc.getWorld().equals(this.highPoints.getWorld()))
        {
            return false;
        }
        
        return this.lowPoints.getBlockX() <= loc.getBlockX() && this.highPoints.getBlockX() >= loc.getBlockX() && this.lowPoints.getBlockZ() <= loc.getBlockZ()
            && this.highPoints.getBlockZ() >= loc.getBlockZ();
    }
    
    /**
     * Determines whether the this cuboid contains the passed location without y coord and by including the 2 blocks beyond the location.
     * 
     * @param loc
     *            the location to check
     * @return true if the location is within this cuboid without y coord, otherwise false
     */
    public boolean containsLocWithoutYd(final Location loc)
    {
        if (this.highPoints == null || this.lowPoints == null)
        {
            return false;
        }
        if (loc == null || !loc.getWorld().equals(this.highPoints.getWorld()))
        {
            return false;
        }
        
        return this.lowPoints.getBlockX() <= loc.getBlockX() + 2 && this.highPoints.getBlockX() >= loc.getBlockX() - 2 && this.lowPoints.getBlockZ() <= loc.getBlockZ() + 2
            && this.highPoints.getBlockZ() >= loc.getBlockZ() - 2;
    }
    
    /**
     * Returns the volume of this cuboid.
     * 
     * @return the volume of this cuboid
     */
    public long getSize()
    {
        return Math.abs(this.getXSize() * this.getYSize() * this.getZSize());
    }
    
    /**
     * Checks if this cuboid is a child of given cuboid.
     * 
     * <p>
     * This will return {@code false} if the zones are matching (equals returns true).
     * </p>
     * 
     * @param possibleParent
     *            possible parent to be checked
     * @return {@code true} if {@code possibleParent} is the parent of this cuboid
     */
    public boolean isChild(Cuboid possibleParent)
    {
        if (equals(this.getWorld(), possibleParent.getWorld()) && this.lowPoints != null && possibleParent.lowPoints != null && this.highPoints != null && possibleParent.highPoints != null)
        {
            return child0(possibleParent) && !equals0(possibleParent);
        }
        return false;
    }
    
    /**
     * Checks if this cuboid is a parent of given cuboid.
     * 
     * <p>
     * This will return {@code false} if the zones are matching (equals returns true).
     * </p>
     * 
     * @param possibleChild
     *            possible child to be checked
     * @return {@code true} if {@code possibleChild} is the child of this cuboid
     */
    public boolean isParent(Cuboid possibleChild)
    {
        if (equals(this.getWorld(), possibleChild.getWorld()) && this.lowPoints != null && possibleChild.lowPoints != null && this.highPoints != null && possibleChild.highPoints != null)
        {
            return parent0(possibleChild) && !equals0(possibleChild);
        }
        return false;
    }
    
    /**
     * Checks if this cuboid is sharing sdome locations with given cuboid but is neither parent nor child.
     * 
     * @param cuboid
     *            cuboid to be checked
     * @return {@code true} if {@code cuboid} shares location with this cuboid
     */
    public boolean isOverlapping(Cuboid cuboid)
    {
        if (equals(this.getWorld(), cuboid.getWorld()) && this.lowPoints != null && cuboid.lowPoints != null && this.highPoints != null && cuboid.highPoints != null)
        {
            return !equals0(cuboid) && !child0(cuboid) && !parent0(cuboid) && shared0(cuboid);
        }
        return false;
    }
    
    /**
     * Determines a random location inside the cuboid and returns it.
     * 
     * @return a random location within the cuboid
     */
    public Location getRandomLocation()
    {
        final World world = this.getWorld();
        final Random randomGenerator = new Random();
        
        Location result;
        
        if (!this.lowPoints.equals(this.highPoints))
        {
            final double randomX = this.lowPoints.getBlockX() + randomGenerator.nextInt(this.getXSize());
            final double randomY = this.lowPoints.getBlockY() + randomGenerator.nextInt(this.getYSize());
            final double randomZ = this.lowPoints.getBlockZ() + randomGenerator.nextInt(this.getZSize());
            
            result = new Location(world, randomX, randomY, randomZ);
        }
        else
        {
            result = this.highPoints.clone();
        }
        
        return result;
    }
    
    /**
     * Determines a random location inside the cuboid that is suitable for mob spawning and returns it.
     * 
     * @return a random location inside the cuboid that is suitable for mob spawning
     */
    public Location getRandomLocationForMobs()
    {
        final Location temp = this.getRandomLocation();
        
        return temp.add(0.5, 0.5, 0.5);
    }
    
    /**
     * Returns the x span of this cuboid.
     * 
     * @return the x span of this cuboid
     */
    public int getXSize()
    {
        return (this.highPoints.getBlockX() - this.lowPoints.getBlockX()) + 1;
    }
    
    /**
     * Returns the y span of this cuboid.
     * 
     * @return the y span of this cuboid
     */
    public int getYSize()
    {
        return (this.highPoints.getBlockY() - this.lowPoints.getBlockY()) + 1;
    }
    
    /**
     * Returns the z span of this cuboid.
     * 
     * @return the z span of this cuboid
     */
    public int getZSize()
    {
        return (this.highPoints.getBlockZ() - this.lowPoints.getBlockZ()) + 1;
    }
    
    /**
     * Returns the higher location of this cuboid.
     * 
     * @return the higher location of this cuboid
     */
    public Location getHighLoc()
    {
        return this.highPoints;
    }
    
    /**
     * Returns the lower location of this cuboid.
     * 
     * @return the lower location of this cuboid
     */
    public Location getLowLoc()
    {
        return this.lowPoints;
    }
    
    /**
     * Returns the world this cuboid is in.
     * 
     * @return the world this cuboid is in
     */
    public World getWorld()
    {
        return this.highPoints == null ? null : this.highPoints.getWorld();
    }
    
    /**
     * Saves the cuboid to a Map.
     * 
     * @return the cuboid in a Map
     */
    private Map<String, Object> save()
    {
        final Map<String, Object> root = new LinkedHashMap<>();
        
        root.put("World", this.highPoints.getWorld().getName()); //$NON-NLS-1$
        root.put("X1", this.lowPoints.getBlockX()); //$NON-NLS-1$
        root.put("Y1", this.lowPoints.getBlockY()); //$NON-NLS-1$
        root.put("Z1", this.lowPoints.getBlockZ()); //$NON-NLS-1$
        root.put("X2", this.highPoints.getBlockX()); //$NON-NLS-1$
        root.put("Y2", this.highPoints.getBlockY()); //$NON-NLS-1$
        root.put("Z2", this.highPoints.getBlockZ()); //$NON-NLS-1$
        
        return root;
    }
    
    /**
     * Loads the cuboid from a Map.
     * 
     * @param root
     *            the Map
     * @throws IllegalArgumentException
     *             thrown if map is {@code null} or contains invalid values
     */
    private void load(final Map<String, Object> root) throws IllegalArgumentException
    {
        try
        {
            final World world = Bukkit.getServer().getWorld((String) root.get("World")); //$NON-NLS-1$
            final int x1 = (Integer) root.get("X1"); //$NON-NLS-1$
            final int y1 = (Integer) root.get("Y1"); //$NON-NLS-1$
            final int z1 = (Integer) root.get("Z1"); //$NON-NLS-1$
            final int x2 = (Integer) root.get("X2"); //$NON-NLS-1$
            final int y2 = (Integer) root.get("Y2"); //$NON-NLS-1$
            final int z2 = (Integer) root.get("Z2"); //$NON-NLS-1$
            
            this.lowPoints = new Location(world, x1, y1, z1);
            this.highPoints = new Location(world, x2, y2, z2);
        }
        catch (NullPointerException | ClassCastException ex)
        {
            throw new IllegalArgumentException("Invalid root map!", ex); //$NON-NLS-1$
        }
    }
    
    @Override
    public void read(DataSection section)
    {
        this.load(section.getValues(false));
    }
    
    @Override
    public void write(DataSection section)
    {
        for (final Map.Entry<String, Object> entry : this.save().entrySet())
        {
            section.set(entry.getKey(), entry.getValue());
        }
    }
    
    @Override
    public boolean test(DataSection section)
    {
        return section.isString("World") //$NON-NLS-1$
            && section.isInt("X1") //$NON-NLS-1$
            && section.isInt("X2") //$NON-NLS-1$
            && section.isInt("Y1") //$NON-NLS-1$
            && section.isInt("Y2") //$NON-NLS-1$
            && section.isInt("Z1") //$NON-NLS-1$
            && section.isInt("Z2"); //$NON-NLS-1$
    }
    
    @Override
    public String toString()
    {
        return this.lowPoints == null ? "(null) to (null)" //$NON-NLS-1$
            : new StringBuilder("(").append(this.lowPoints.getBlockX()).append(", ").append(this.lowPoints.getBlockY()).append(", ").append(this.lowPoints.getBlockZ()).append(") to (") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                .append(this.highPoints.getBlockX()).append(", ").append(this.highPoints.getBlockY()).append(", ").append(this.highPoints.getBlockZ()).append(")").toString(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    
    /**
     * Returns a raw representation that is easy to read for Java.
     * 
     * @return a raw representation of this cuboid
     */
    public String toRaw()
    {
        if (this.lowPoints == null)
        {
            return "null"; //$NON-NLS-1$
        }
        return new StringBuilder(this.getWorld() == null ? "null" : this.getWorld().getName()).append(",").append(this.lowPoints.getBlockX()).append(",").append(this.lowPoints.getBlockY()).append(",") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            .append(this.lowPoints.getBlockZ())
            .append(",").append(this.highPoints.getBlockX()).append(",").append(this.highPoints.getBlockY()).append(",").append(this.highPoints.getBlockZ()).toString(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.highPoints == null || this.highPoints.getWorld() == null) ? 0 : this.highPoints.getWorld().getName().hashCode());
        result = prime * result + ((this.lowPoints == null) ? 0 : this.lowPoints.getBlockX());
        result = prime * result + ((this.lowPoints == null) ? 0 : this.lowPoints.getBlockY());
        result = prime * result + ((this.lowPoints == null) ? 0 : this.lowPoints.getBlockZ());
        result = prime * result + ((this.highPoints == null) ? 0 : this.highPoints.getBlockX());
        result = prime * result + ((this.highPoints == null) ? 0 : this.highPoints.getBlockY());
        result = prime * result + ((this.highPoints == null) ? 0 : this.highPoints.getBlockZ());
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
        Cuboid other = (Cuboid) obj;
        
        if (!equals(this.getWorld(), other.getWorld()))
        {
            return false;
        }
        
        if (!equals(this.lowPoints, other.lowPoints))
        {
            return false;
        }
        
        if (!equals(this.highPoints, other.highPoints))
        {
            return false;
        }
        
        return true;
    }
    
    /**
     * Checks for the locations having the same block coordinates (null-safe).
     * 
     * @param a
     *            first location
     * @param b
     *            second location
     * @return {@code true} if the locations have same block locations
     */
    private boolean equals(Location a, Location b)
    {
        if (a == null)
        {
            if (b != null)
            {
                return false;
            }
        }
        else if (b == null)
        {
            return false;
        }
        else
        {
            return a.getBlockX() == b.getBlockX() && a.getBlockY() == b.getBlockY() && a.getBlockZ() == b.getBlockZ();
        }
        return true;
    }
    
    /**
     * Checks for the worlds are the same (null-safe).
     * 
     * @param a
     *            first world
     * @param b
     *            second world
     * @return {@code true} if the worlds are the same
     */
    private boolean equals(World a, World b)
    {
        if (a == null)
        {
            if (b != null)
            {
                return false;
            }
        }
        else if (b == null)
        {
            return false;
        }
        else
        {
            final String worldA = a.getName();
            final String worldB = b.getName();
            if (worldA == null)
            {
                if (worldB != null)
                {
                    return false;
                }
            }
            else if (worldB == null)
            {
                return false;
            }
            else
            {
                return worldA.equals(worldB);
            }
        }
        return true;
    }
    
    /**
     * checks equality of locations.
     * 
     * @param cuboid
     *            cuboid to be checked
     * @return {@code true} if locations are equal
     */
    private boolean equals0(Cuboid cuboid)
    {
        return this.lowPoints.getBlockX() == cuboid.lowPoints.getBlockX() && this.lowPoints.getBlockY() == cuboid.lowPoints.getBlockY() && this.lowPoints.getBlockZ() == cuboid.lowPoints.getBlockZ()
            && this.highPoints.getBlockX() == cuboid.highPoints.getBlockX() && this.highPoints.getBlockY() == cuboid.highPoints.getBlockY()
            && this.highPoints.getBlockZ() == cuboid.highPoints.getBlockZ();
    }
    
    /**
     * checks parent/child.
     * 
     * @param possibleParent
     *            cuboid to be checked
     * @return {@code true} if given cuboid is a parent
     */
    private boolean child0(Cuboid possibleParent)
    {
        return this.lowPoints.getBlockX() >= possibleParent.lowPoints.getBlockX() && this.lowPoints.getBlockY() >= possibleParent.lowPoints.getBlockY()
            && this.lowPoints.getBlockZ() >= possibleParent.lowPoints.getBlockZ()
            && this.highPoints.getBlockX() <= possibleParent.highPoints.getBlockX() && this.highPoints.getBlockY() <= possibleParent.highPoints.getBlockY()
            && this.highPoints.getBlockZ() <= possibleParent.highPoints.getBlockZ();
    }
    
    /**
     * checks parent/child.
     * 
     * @param possibleChild
     *            cuboid to be checked
     * @return {@code true} if given cuboid is a child
     */
    private boolean parent0(Cuboid possibleChild)
    {
        return this.lowPoints.getBlockX() <= possibleChild.lowPoints.getBlockX() && this.lowPoints.getBlockY() <= possibleChild.lowPoints.getBlockY()
            && this.lowPoints.getBlockZ() <= possibleChild.lowPoints.getBlockZ()
            && this.highPoints.getBlockX() >= possibleChild.highPoints.getBlockX() && this.highPoints.getBlockY() >= possibleChild.highPoints.getBlockY()
            && this.highPoints.getBlockZ() >= possibleChild.highPoints.getBlockZ();
    }
    
    /**
     * checks shared locations.
     * 
     * @param cuboid
     *            cuboid to be checked
     * @return {@code true} if given cuboid shares locations
     */
    private boolean shared0(Cuboid cuboid)
    {
        return this.containsLoc(cuboid.lowPoints) || this.containsLoc(cuboid.highPoints) || cuboid.containsLoc(this.lowPoints) || cuboid.containsLoc(this.highPoints);
    }
    
}
