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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.shared.api.com.DataSection;

/**
 * Base class for all single location components.
 * 
 * @author mepeisen
 */
public abstract class AbstractLocationComponent extends AbstractComponent
{
    
    /** the location. */
    protected Location location;
    
    /**
     * Constructor to create the component.
     * 
     * @param registry
     *            the owning registry.
     * @param location
     *            the component location
     * @param config
     *            the config file
     * @param owner
     *            the component owner 
     * @throws McException 
     */
    public AbstractLocationComponent(ComponentRegistry registry, Location location, File config, ComponentOwner owner) throws McException
    {
        super(registry, config, owner);
        this.location = location == null ? null : location;
        this.changeLocs();
    }
    
    /**
     * reads the file config.
     * 
     * @throws McException
     */
    public void readConfig() throws McException
    {
        if (this.config != null)
        {
            final DataSection core = this.config.getSection("core"); //$NON-NLS-1$
            if (core != null)
            {
                final int x = core.getInt("location.x"); //$NON-NLS-1$
                final int y = core.getInt("location.y"); //$NON-NLS-1$
                final int z = core.getInt("location.z"); //$NON-NLS-1$
                final World world = Bukkit.getWorld(core.getString("location.world")); //$NON-NLS-1$
                this.setLocation(new Location(world, x, y, z));
                this.readData(core);
            }
        }
    }
    
    /**
     * Saves the file config.
     * 
     * @throws McException
     */
    public void saveConfig() throws McException
    {
        if (this.config != null)
        {
            final DataSection core = this.config.createSection("core"); //$NON-NLS-1$
            core.set("location.x", this.location.getBlockX()); //$NON-NLS-1$
            core.set("location.y", this.location.getBlockY()); //$NON-NLS-1$
            core.set("location.z", this.location.getBlockZ()); //$NON-NLS-1$
            core.set("location.world", this.location.getWorld().getName()); //$NON-NLS-1$
            this.saveData(core);
            try
            {
                this.config.saveFile(this.configFile);
            }
            catch (IOException e)
            {
                throw new McException(CommonMessages.InternalError, e, e.getMessage());
            }
        }
    }
    
    /**
     * Stores core data into config.
     * 
     * @param coreSection
     */
    protected abstract void saveData(DataSection coreSection);
    
    /**
     * Reads core data from config.
     * 
     * @param coreSection
     */
    protected abstract void readData(DataSection coreSection);
    
    /**
     * Changes the locations depending on the given cuboid.
     */
    private void changeLocs()
    {
        if (this.location == null)
        {
            this.setWorldChunks(Collections.emptySet());
        }
        else
        {
            final Set<WorldChunk> chunks = new HashSet<>();
            chunks.add(new WorldChunk(this.location));
            this.setWorldChunks(chunks);
        }
    }
    
    /**
     * Returns the location.
     * 
     * @return location of this component.
     */
    public Location getLocation()
    {
        return this.location;
    }
    
    /**
     * Sets the location
     * 
     * @param loc
     *            location of the component.
     * @throws McException
     */
    public void setLocation(Location loc) throws McException
    {
        // TODO notify sgui
        if (this.deleted)
        {
            throw new McException(CommonMessages.AlreadyDeletedError);
        }
        this.location = loc == null ? null : loc;
        this.saveConfig();
        this.changeLocs();
    }
    
}
