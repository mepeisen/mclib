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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.event.McListener;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.mcevent.ComponentDeleteEvent;
import de.minigameslib.mclib.api.mcevent.ComponentDeletedEvent;
import de.minigameslib.mclib.api.mcevent.ComponentRelocateEvent;
import de.minigameslib.mclib.api.mcevent.ComponentRelocatedEvent;
import de.minigameslib.mclib.api.objects.ComponentHandlerInterface;
import de.minigameslib.mclib.api.objects.ComponentIdInterface;
import de.minigameslib.mclib.api.objects.ComponentInterface;
import de.minigameslib.mclib.api.objects.ComponentTypeId;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.nms.api.EventBus;
import de.minigameslib.mclib.nms.api.EventSystemInterface;
import de.minigameslib.mclib.nms.api.MgEventListener;
import de.minigameslib.mclib.shared.api.com.DataSection;

/**
 * Component implementation.
 * 
 * @author mepeisen
 *
 */
public class ComponentImpl extends AbstractLocationComponent implements ComponentInterface, MgEventListener
{
    
    /** component id. */
    private final ComponentId               id;
    
    /** component handler. */
    private final ComponentHandlerInterface handler;
    
    /** an event bus to handle events. */
    private final EventBus                  eventBus = Bukkit.getServicesManager().load(EventSystemInterface.class).createEventBus();
    
    /**
     * Constructor.
     * 
     * @param plugin
     *            the owning plugin.
     * @param registry
     *            the registry.
     * @param location
     *            the components location
     * @param id
     *            the id
     * @param handler
     *            the handler managing the component
     * @param config
     *            the config file
     * @param owner
     *            thw owning service
     * @throws McException
     *             thrown on config errors
     */
    public ComponentImpl(Plugin plugin, ComponentRegistry registry, Location location, ComponentId id, ComponentHandlerInterface handler, File config, ComponentOwner owner) throws McException
    {
        super(registry, location, config, owner);
        this.id = id;
        this.handler = handler;
        if (this.handler instanceof McListener)
        {
            this.eventBus.registerHandlers(plugin, (McListener) this.handler);
        }
    }
    
    @Override
    public ComponentIdInterface getComponentId()
    {
        return this.id;
    }
    
    @Override
    public void readData(DataSection coreSection)
    {
        // TODO should we re-read the id?
        // the id is already read from registry.yml
        this.handler.read(coreSection.getSection("handler")); //$NON-NLS-1$
    }
    
    @Override
    protected void saveData(DataSection coreSection)
    {
        this.id.write(coreSection.createSection("id")); //$NON-NLS-1$
        this.handler.write(coreSection.createSection("handler")); //$NON-NLS-1$
    }
    
    @Override
    public void setLocation(Location loc) throws McException
    {
        final Location old = this.location;
        McLibInterface.instance().runInCopiedContext(() ->
        {
            McLibInterface.instance().setContext(ComponentInterface.class, this);
            this.handler.canChangeLocation(loc);
        });
        final ComponentRelocateEvent relocateEvent = new ComponentRelocateEvent(this, old, loc);
        Bukkit.getPluginManager().callEvent(relocateEvent);
        if (relocateEvent.isCancelled())
        {
            throw new McException(relocateEvent.getVetoReason(), relocateEvent.getVetoReasonArgs());
        }
        
        McLibInterface.instance().runInCopiedContext(() ->
        {
            McLibInterface.instance().setContext(ComponentInterface.class, this);
            super.setLocation(loc);
            this.handler.onLocationChange(loc);
        });
        
        final ComponentRelocatedEvent relocatedEvent = new ComponentRelocatedEvent(this, old, loc);
        Bukkit.getPluginManager().callEvent(relocatedEvent);
    }
    
    @Override
    public void delete() throws McException
    {
        if (this.deleted)
        {
            throw new McException(CommonMessages.AlreadyDeletedError);
        }
        McLibInterface.instance().runInCopiedContext(() ->
        {
            McLibInterface.instance().setContext(ComponentInterface.class, this);
            this.handler.canDelete();
        });
        
        final ComponentDeleteEvent deleteEvent = new ComponentDeleteEvent(this);
        Bukkit.getPluginManager().callEvent(deleteEvent);
        if (deleteEvent.isCancelled())
        {
            throw new McException(deleteEvent.getVetoReason(), deleteEvent.getVetoReasonArgs());
        }
        
        McLibInterface.instance().runInCopiedContext(() ->
        {
            McLibInterface.instance().setContext(ComponentInterface.class, this);
            super.delete();
            this.handler.onDelete();
        });
        this.eventBus.clear();
        
        final ComponentDeletedEvent deletedEvent = new ComponentDeletedEvent(this);
        Bukkit.getPluginManager().callEvent(deletedEvent);
    }
    
    /**
     * Clears all event registrations.
     */
    public void clearEventRegistrations()
    {
        this.eventBus.clear();
    }
    
    @Override
    public ComponentHandlerInterface getHandler()
    {
        return this.handler;
    }
    
    @Override
    public <EVT extends MinecraftEvent<?, EVT>> void registerHandler(Plugin plugin, Class<EVT> clazz, McConsumer<EVT> h)
    {
        this.eventBus.registerHandler(plugin, clazz, h);
    }
    
    @Override
    public void registerHandlers(Plugin plugin, McListener listener)
    {
        this.eventBus.registerHandlers(plugin, listener);
    }
    
    @Override
    public <EVT extends MinecraftEvent<?, EVT>> void unregisterHandler(Plugin plugin, Class<EVT> clazz, McConsumer<EVT> h)
    {
        this.eventBus.unregisterHandler(plugin, clazz, h);
    }
    
    @Override
    public void unregisterHandlers(Plugin plugin, McListener listener)
    {
        this.eventBus.unregisterHandlers(plugin, listener);
    }
    
    @Override
    public <T extends Event, EVT extends MinecraftEvent<T, EVT>> void handle(Class<EVT> eventClass, EVT event)
    {
        this.eventBus.handle(eventClass, event);
    }
    
    /**
     * Plugin disable.
     * 
     * @param plugin
     *            plugin that was disabled.
     */
    public void onDisable(Plugin plugin)
    {
        this.eventBus.onDisable(plugin);
    }
    
    @Override
    public ComponentTypeId getTypeId()
    {
        return ObjectServiceInterface.instance().getType(this.getComponentId());
    }
    
}
