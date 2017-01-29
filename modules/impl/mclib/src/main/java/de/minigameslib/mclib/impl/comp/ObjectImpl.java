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
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.McListener;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.mcevent.ObjectDeleteEvent;
import de.minigameslib.mclib.api.mcevent.ObjectDeletedEvent;
import de.minigameslib.mclib.api.objects.ObjectHandlerInterface;
import de.minigameslib.mclib.api.objects.ObjectIdInterface;
import de.minigameslib.mclib.api.objects.ObjectInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.objects.ObjectTypeId;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.impl.EventBus;
import de.minigameslib.mclib.nms.api.MgEventListener;
import de.minigameslib.mclib.shared.api.com.DataSection;

/**
 * @author mepeisen
 *
 */
public class ObjectImpl extends AbstractComponent implements ObjectInterface, MgEventListener
{
    
    /** the object id. */
    private final ObjectId id;
    
    /** the object handler. */
    private final ObjectHandlerInterface handler;
    
    /** an event bus to handle events. */
    private final EventBus                      eventBus         = new EventBus();
    
    /**
     * @param plugin 
     * @param id 
     * @param handler 
     * @param config 
     * @param owner 
     * @throws McException 
     */
    public ObjectImpl(Plugin plugin, ObjectId id, ObjectHandlerInterface handler, File config, ComponentOwner owner) throws McException
    {
        super(null, config, owner);
        this.id = id;
        this.handler = handler;
        if (this.handler instanceof McListener)
        {
            this.eventBus.registerHandlers(plugin, (McListener) this.handler);
        }
    }

    @Override
    public ObjectIdInterface getObjectId()
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
        
        final ObjectDeleteEvent deleteEvent = new ObjectDeleteEvent(this);
        Bukkit.getPluginManager().callEvent(deleteEvent);
        if (deleteEvent.isCancelled())
        {
            throw new McException(deleteEvent.getVetoReason(), deleteEvent.getVetoReasonArgs());
        }
        
        super.delete();
        this.handler.onDelete();
        this.eventBus.clear();

        final ObjectDeletedEvent deletedEvent = new ObjectDeletedEvent(this);
        Bukkit.getPluginManager().callEvent(deletedEvent);
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
                this.handler.read(core.getSection("handler")); //$NON-NLS-1$
            }
        }
    }
    
    @Override
    public void saveConfig() throws McException
    {
        if (this.config != null)
        {
            final DataSection core = this.config.createSection("core"); //$NON-NLS-1$
            this.handler.write(core.createSection("handler")); //$NON-NLS-1$
        }
    }

    @Override
    public ObjectHandlerInterface getHandler()
    {
        return this.handler;
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

    @Override
    public ObjectTypeId getTypeId()
    {
        return ObjectServiceInterface.instance().getType(this.getObjectId());
    }
    
}
