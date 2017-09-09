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
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.event.McListener;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.mcevent.ObjectDeleteEvent;
import de.minigameslib.mclib.api.mcevent.ObjectDeletedEvent;
import de.minigameslib.mclib.api.objects.ObjectHandlerInterface;
import de.minigameslib.mclib.api.objects.ObjectInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.objects.ObjectTypeId;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.nms.api.EventBus;
import de.minigameslib.mclib.nms.api.EventSystemInterface;
import de.minigameslib.mclib.nms.api.MgEventListener;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * Implementation of sbatract objects.
 * 
 * @author mepeisen
 *
 */
public class ObjectImpl extends AbstractComponent implements ObjectInterface, MgEventListener
{
    
    /** the object id. */
    private final ObjectId               id;
    
    /** the object handler. */
    private final ObjectHandlerInterface handler;
    
    /** an event bus to handle events. */
    private final EventBus               eventBus = Bukkit.getServicesManager().load(EventSystemInterface.class).createEventBus();
    
    /**
     * Constructor.
     * 
     * @param plugin
     *            plugin owning this object
     * @param id
     *            id of this object
     * @param handler
     *            handler managing the object
     * @param config
     *            configuration file
     * @param owner
     *            owning service
     * @throws McException
     *             thrown on config errors
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
    public ObjectId getObjectId()
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
        McLibInterface.instance().runInCopiedContext(() ->
        {
            McLibInterface.instance().setContext(ObjectInterface.class, this);
            this.handler.canDelete();
        });
        
        final ObjectDeleteEvent deleteEvent = new ObjectDeleteEvent(this);
        Bukkit.getPluginManager().callEvent(deleteEvent);
        if (deleteEvent.isCancelled())
        {
            throw new McException(deleteEvent.getVetoReason(), deleteEvent.getVetoReasonArgs());
        }
        
        McLibInterface.instance().runInCopiedContext(() ->
        {
            McLibInterface.instance().setContext(ObjectInterface.class, this);
            super.delete();
            this.handler.onDelete();
        });
        this.eventBus.clear();
        
        final ObjectDeletedEvent deletedEvent = new ObjectDeletedEvent(this);
        Bukkit.getPluginManager().callEvent(deletedEvent);
    }
    
    /**
     * reads the file config.
     * 
     * @throws McException
     *             thrown on config errors
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
    
    @Override
    public ObjectHandlerInterface getHandler()
    {
        return this.handler;
    }
    
    /**
     * Clears all event registrations.
     */
    public void clearEventRegistrations()
    {
        this.eventBus.clear();
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
     * @param plugin Plugin that was disabled.
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
    
    /**
     * Copies configuration for storing in schemata.
     * 
     * @return data section holding configuration needed for restoring the zone.
     */
    public DataSection copyAndSaveConfig()
    {
        final DataSection result = new MemoryDataSection();
        result.set("otype", "object"); //$NON-NLS-1$ //$NON-NLS-2$
        result.set("tplugin", this.getTypeId().getPluginName()); //$NON-NLS-1$
        result.set("tname", this.getTypeId().name()); //$NON-NLS-1$
        if (this.config != null)
        {
            for (final Map.Entry<String, Object> entry : this.config.getValues(true).entrySet())
            {
                result.set("file." + entry.getKey(), entry.getValue()); //$NON-NLS-1$
            }
        }
        return result;
    }
    
}
