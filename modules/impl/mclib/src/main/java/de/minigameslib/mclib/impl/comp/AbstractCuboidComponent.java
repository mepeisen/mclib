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

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.Cuboid;

/**
 * Base class for all cuboid components.
 * 
 * @author mepeisen
 */
public abstract class AbstractCuboidComponent extends AbstractComponent
{
    
    /** the cuboid. */
    protected Cuboid cuboid;

    /**
     * Constructor to create the component.
     * 
     * @param registry
     *            the owning registry.
     * @param cuboid
     *            the component bounds
     * @param config
     *            the config file
     * @param owner
     *            the component owner
     */
    public AbstractCuboidComponent(ComponentRegistry registry, Cuboid cuboid, File config, ComponentOwner owner)
    {
        super(registry, config, owner);
        this.cuboid = cuboid == null ? null : cuboid;
        this.changeLocs();
    }
    
    /**
     * Saves the file config.
     * 
     * @throws McException
     */
    public void saveConfig() throws McException
    {
        final ConfigurationSection core = this.config.createSection("core"); //$NON-NLS-1$
        this.cuboid.writeToConfig(core.createSection("location")); //$NON-NLS-1$
        this.saveData(core);
        try
        {
            this.config.save(this.configFile);
        }
        catch (IOException e)
        {
            // TODO Report exception
            e.printStackTrace();
        }
    }
    
    /**
     * Stores core data into config.
     * 
     * @param coreSection
     */
    protected abstract void saveData(ConfigurationSection coreSection);
    
    /**
     * Changes the locations depending on the given cuboid.
     */
    private void changeLocs()
    {
        if (this.cuboid == null || this.cuboid.getLowLoc() == null)
        {
            this.setWorldChunks(Collections.emptySet());
        }
        else
        {
            final Set<WorldChunk> chunks = new HashSet<>();
            final WorldChunk lowChunk = new WorldChunk(this.cuboid.getLowLoc().clone().add(new Vector(-2, -2, -2)));
            final WorldChunk highChunk = new WorldChunk(this.cuboid.getHighLoc().clone().add(new Vector(2, 2, 2)));
            for (int x = lowChunk.getX(); x <= highChunk.getX(); x++)
            {
                for (int z = highChunk.getZ(); z <= highChunk.getZ(); z++)
                {
                    final WorldChunk chunk = new WorldChunk(lowChunk.getServerName(), lowChunk.getWorldName(), x, z);
                    chunks.add(chunk);
                }
            }
            this.setWorldChunks(chunks);
        }
    }

    /**
     * Returns the cuboid.
     * @return cuboid of this component.
     */
    public Cuboid getCuboid()
    {
        return this.cuboid;
    }
    
    /**
     * Sets the cuboid
     * @param cub cuboid of the component.
     * @throws McException 
     */
    public void setCuboid(Cuboid cub) throws McException
    {
        if (this.deleted)
        {
            // TODO throw exception
        }
        this.saveConfig();
        this.cuboid = cub == null ? null : cub;
        this.changeLocs();
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
        return this.cuboid == null ? false : this.cuboid.containsLoc(loc);
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
        return this.cuboid == null ? false : this.cuboid.containsLocWithoutY(loc);
    }
    
    /**
     * Determines whether the this cuboid contains the passed location without y coord and by including the 2 blocks beyond the location.
     * 
     * @param loc
     *            the location to check
     * @return true if the location is within this cuboid without y coord, otherwise false
     */
    public boolean containsLocWithoutYD(final Location loc)
    {
        return this.cuboid == null ? false : this.cuboid.containsLocWithoutYD(loc);
    }
    
}
