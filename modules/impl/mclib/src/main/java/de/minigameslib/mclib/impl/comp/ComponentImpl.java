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

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.ComponentHandlerInterface;
import de.minigameslib.mclib.api.objects.ComponentIdInterface;
import de.minigameslib.mclib.api.objects.ComponentInterface;

/**
 * @author mepeisen
 *
 */
public class ComponentImpl extends AbstractLocationComponent implements ComponentInterface
{
    
    /** component id. */
    private final ComponentId id;
    
    /** component handler. */
    private final ComponentHandlerInterface handler;
    
    /**
     * @param registry
     * @param location
     * @param id
     * @param handler
     * @param config
     * @param owner 
     */
    public ComponentImpl(ComponentRegistry registry, Location location, ComponentId id, ComponentHandlerInterface handler, File config, ComponentOwner owner)
    {
        super(registry, location, config, owner);
        this.id = id;
        this.handler = handler;
    }

    @Override
    public ComponentIdInterface getComponentId()
    {
        return this.id;
    }

    @Override
    protected void saveData(ConfigurationSection coreSection)
    {
        this.id.writeToConfig(coreSection.createSection("id")); //$NON-NLS-1$
        this.handler.writeToConfig(coreSection.createSection("handler")); //$NON-NLS-1$
    }

    @Override
    public void delete() throws McException
    {
        if (this.deleted)
        {
            // TODO error
        }
        this.handler.canDelete();
        super.delete();
        this.handler.onDelete();
    }
    
}