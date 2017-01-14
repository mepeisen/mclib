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

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.impl.yml.YmlFile;

/**
 * Base class for all minigame components.
 * 
 * @author mepeisen
 */
public abstract class AbstractComponent
{
    
    /** the underlying component registry owning this component. */
    private final ComponentRegistry registry;
    
    /** the current world chunks this component is located in. */
    private final Set<WorldChunk> currentChunks = new HashSet<>();
    
    /** the file config. */
    protected YmlFile config;
    
    /** the config file. */
    protected File configFile;
    
    /** deleted flag. */
    protected boolean deleted;
    
    /** the component owner. */
    protected ComponentOwner owner;
    
    /**
     * Constructor to create the component.
     * 
     * @param registry
     *            the owning registry.
     * @param config
     *            the file config.
     *            @param owner the owning component
     * @throws McException 
     */
    public AbstractComponent(ComponentRegistry registry, File config, ComponentOwner owner) throws McException
    {
        this.registry = registry;
        this.configFile = config;
        this.owner = owner;
        if (config != null)
        {
            try
            {
                if (config.exists())
                {
                    this.config = new YmlFile(config);
                }
                else
                {
                    this.config = new YmlFile();
                }
            }
            catch (IOException e)
            {
                throw new McException(CommonMessages.InternalError, e, e.getMessage());
            }
        }
    }
    
    /**
     * Sets/Changes the world chunks this component is located in.
     * @param chunks
     */
    protected void setWorldChunks(Set<WorldChunk> chunks)
    {
        final Set<WorldChunk> removed = new HashSet<>(this.currentChunks);
        removed.removeAll(chunks);
        final Set<WorldChunk> added = new HashSet<>(chunks);
        added.removeAll(this.currentChunks);
        if (removed.size() > 0)
        {
            this.registry.unregister(removed, this);
        }
        if (added.size() > 0)
        {
            this.registry.register(added, this);
        }
        this.currentChunks.clear();
        this.currentChunks.addAll(chunks);
    }
    
    /**
     * Deletes this component.
     * @throws McException
     */
    public void delete() throws McException
    {
        this.setWorldChunks(Collections.emptySet());
        this.deleted = true;
        if (this.configFile != null)
        {
            this.configFile.delete();
        }
        this.owner.onDelete(this);
    }
    
}
