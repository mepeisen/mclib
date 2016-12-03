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

import org.bukkit.configuration.ConfigurationSection;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.Cuboid;
import de.minigameslib.mclib.api.objects.ZoneHandlerInterface;
import de.minigameslib.mclib.api.objects.ZoneIdInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;

/**
 * @author mepeisen
 *
 */
public class ZoneImpl extends AbstractCuboidComponent implements ZoneInterface
{
    
    /** zone id. */
    private final ZoneId id;
    
    /** zone handler. */
    private final ZoneHandlerInterface handler;
    
    /**
     * @param registry
     * @param cuboid
     * @param id 
     * @param handler 
     * @param config 
     * @param owner 
     */
    public ZoneImpl(ComponentRegistry registry, Cuboid cuboid, ZoneId id, ZoneHandlerInterface handler, File config, ComponentOwner owner)
    {
        super(registry, cuboid, config, owner);
        this.id = id;
        this.handler = handler;
    }

    @Override
    public ZoneIdInterface getZoneId()
    {
        return this.id;
    }
    
    @Override
    public void delete() throws McException
    {
        if (this.deleted)
        {
            throw new McException(CommonMessages.AlreadyDeleted);
        }
        this.handler.canDelete();
        super.delete();
        this.handler.onDelete();
    }

    @Override
    protected void saveData(ConfigurationSection coreSection)
    {
        this.id.writeToConfig(coreSection.createSection("id")); //$NON-NLS-1$
        this.handler.writeToConfig(coreSection.createSection("handler")); //$NON-NLS-1$
    }
    
    @Override
    public void readData(ConfigurationSection coreSection)
    {
        // TODO should we re-read the id?
        // the id is already read from registry.yml
        this.handler.readFromConfig(coreSection.getConfigurationSection("handler")); //$NON-NLS-1$
    }

    /**
     * Returns the handler
     * @return handler
     */
    public ZoneHandlerInterface getHandler()
    {
        return this.handler;
    }
    
}
