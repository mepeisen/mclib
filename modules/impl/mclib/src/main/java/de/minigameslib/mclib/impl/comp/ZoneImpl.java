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
import java.util.Collection;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.Cuboid;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface.CuboidMode;
import de.minigameslib.mclib.api.objects.ZoneHandlerInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.api.objects.ZoneTypeId;
import de.minigameslib.mclib.shared.api.com.DataSection;

/**
 * @author mepeisen
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
     * @throws McException 
     */
    public ZoneImpl(ComponentRegistry registry, Cuboid cuboid, ZoneId id, ZoneHandlerInterface handler, File config, ComponentOwner owner) throws McException
    {
        super(registry, cuboid, config, owner);
        this.id = id;
        this.handler = handler;
    }

    @Override
    public ZoneId getZoneId()
    {
        return this.id;
    }
    
    @Override
    public void delete() throws McException
    {
        if (this.deleted)
        {
            throw new McException(CommonMessages.AlreadyDeletedError);
        }
        this.handler.canDelete();
        super.delete();
        this.handler.onDelete();
    }

    @Override
    protected void saveData(DataSection coreSection)
    {
        this.id.write(coreSection.createSection("id")); //$NON-NLS-1$
        this.handler.write(coreSection.createSection("handler")); //$NON-NLS-1$
    }
    
    @Override
    public void readData(DataSection coreSection)
    {
        // TODO should we re-read the id?
        // the id is already read from registry.yml
        this.handler.read(coreSection.getSection("handler")); //$NON-NLS-1$
    }

    /**
     * Returns the handler
     * @return handler
     */
    public ZoneHandlerInterface getHandler()
    {
        return this.handler;
    }

    @Override
    public Collection<ZoneInterface> getChildZones()
    {
        return ObjectServiceInterface.instance().findZones(this.cuboid, CuboidMode.FindChildren);
    }

    @Override
    public Collection<ZoneInterface> getChildZones(ZoneTypeId... type)
    {
        return ObjectServiceInterface.instance().findZones(this.cuboid, CuboidMode.FindChildren, type);
    }

    @Override
    public Collection<ZoneInterface> getParentZones()
    {
        return ObjectServiceInterface.instance().findZones(this.cuboid, CuboidMode.FindParents);
    }

    @Override
    public Collection<ZoneInterface> getParentZones(ZoneTypeId... type)
    {
        return ObjectServiceInterface.instance().findZones(this.cuboid, CuboidMode.FindParents, type);
    }

    @Override
    public Collection<ZoneInterface> getMatchingZones()
    {
        return ObjectServiceInterface.instance().findZones(this.cuboid, CuboidMode.FindMatching);
    }

    @Override
    public Collection<ZoneInterface> getMatchingZones(ZoneTypeId... type)
    {
        return ObjectServiceInterface.instance().findZones(this.cuboid, CuboidMode.FindMatching, type);
    }

    @Override
    public Collection<ZoneInterface> getOverlappingZones()
    {
        return ObjectServiceInterface.instance().findZones(this.cuboid, CuboidMode.FindOverlapping);
    }

    @Override
    public Collection<ZoneInterface> getOverlappingZones(ZoneTypeId... type)
    {
        return ObjectServiceInterface.instance().findZones(this.cuboid, CuboidMode.FindOverlapping, type);
    }
    
}
