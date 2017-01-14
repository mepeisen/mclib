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

import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.McListener;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.objects.Cuboid;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface.CuboidMode;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.impl.EventBus;
import de.minigameslib.mclib.nms.api.MgEventListener;
import de.minigameslib.mclib.api.objects.ZoneHandlerInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.api.objects.ZoneTypeId;
import de.minigameslib.mclib.shared.api.com.DataSection;

/**
 * @author mepeisen
 */
public class ZoneImpl extends AbstractCuboidComponent implements ZoneInterface, MgEventListener
{
    
    /** zone id. */
    private final ZoneId id;
    
    /** zone handler. */
    private final ZoneHandlerInterface handler;
    
    /** an event bus to handle events. */
    private final EventBus                      eventBus         = new EventBus();
    
    /**
     * @param plugin 
     * @param registry
     * @param cuboid
     * @param id 
     * @param handler 
     * @param config 
     * @param owner 
     * @throws McException 
     */
    public ZoneImpl(Plugin plugin, ComponentRegistry registry, Cuboid cuboid, ZoneId id, ZoneHandlerInterface handler, File config, ComponentOwner owner) throws McException
    {
        super(registry, cuboid, config, owner);
        this.id = id;
        this.handler = handler;
        if (this.handler instanceof McListener)
        {
            this.eventBus.registerHandlers(plugin, (McListener) this.handler);
        }
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

    /**
     * Clears all event registrations
     */
    public void clearEventRegistrations()
    {
        this.eventBus.clear();
    }
    
    @Override
    public <Evt extends MinecraftEvent<?, Evt>> void registerHandler(Plugin plugin, Class<Evt> clazz, McConsumer<Evt> h)
    {
        this.eventBus.registerHandler(plugin, clazz, h);
    }
    
    @Override
    public void registerHandlers(Plugin plugin, McListener listener)
    {
        this.eventBus.registerHandlers(plugin, listener);
    }
    
    @Override
    public <Evt extends MinecraftEvent<?, Evt>> void unregisterHandler(Plugin plugin, Class<Evt> clazz, McConsumer<Evt> h)
    {
        this.eventBus.unregisterHandler(plugin, clazz, h);
    }
    
    @Override
    public void unregisterHandlers(Plugin plugin, McListener listener)
    {
        this.eventBus.unregisterHandlers(plugin, listener);
    }
    
    @Override
    public <T extends Event, Evt extends MinecraftEvent<T, Evt>> void handle(Class<T> eventClass, Evt event)
    {
        this.eventBus.handle(eventClass, event);
    }
    
    /**
     * Plugin disable
     * @param plugin
     */
    public void onDisable(Plugin plugin)
    {
        this.eventBus.onDisable(plugin);
    }
    
}
