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
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.event.McListener;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.mcevent.EntityDeleteEvent;
import de.minigameslib.mclib.api.mcevent.EntityDeletedEvent;
import de.minigameslib.mclib.api.objects.EntityHandlerInterface;
import de.minigameslib.mclib.api.objects.EntityInterface;
import de.minigameslib.mclib.api.objects.EntityTypeId;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.nms.api.EventBus;
import de.minigameslib.mclib.nms.api.EventSystemInterface;
import de.minigameslib.mclib.nms.api.MgEventListener;
import de.minigameslib.mclib.shared.api.com.DataSection;
import de.minigameslib.mclib.shared.api.com.MemoryDataSection;

/**
 * Implementation of entities.
 * 
 * @author mepeisen
 *
 */
public class EntityImpl extends AbstractComponent implements EntityInterface, MgEventListener
{
    
    // TODO fetch entity die events
    
    /** the bukkit entity. */
    private Entity                       entity;
    
    /** the event id. */
    private final EntityId               id;
    
    /** the event handler. */
    private final EntityHandlerInterface handler;
    
    /** an event bus to handle events. */
    private final EventBus               eventBus = Bukkit.getServicesManager().load(EventSystemInterface.class).createEventBus();
    
    /**
     * the entity uuid.
     */
    private UUID                         entityUuid;
    
    /**
     * Dyncmic entity type.
     */
    private DynamicEntityType            dynamic;
    
    /**
     * Constructor.
     * 
     * @param plugin
     *            the owning plugin.
     * @param entity
     *            the entity.
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
    public EntityImpl(Plugin plugin, Entity entity, EntityId id, EntityHandlerInterface handler, File config, ComponentOwner owner) throws McException
    {
        super(null, config, owner);
        this.entity = entity;
        this.id = id;
        this.handler = handler;
        if (this.handler instanceof McListener)
        {
            this.eventBus.registerHandlers(plugin, (McListener) this.handler);
        }
        if (this.entity != null)
        {
            this.entityUuid = this.entity.getUniqueId();
        }
    }
    
    @Override
    public EntityId getEntityId()
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
            McLibInterface.instance().setContext(EntityInterface.class, this);
            this.handler.canDelete();
        });
        
        final EntityDeleteEvent deleteEvent = new EntityDeleteEvent(this);
        Bukkit.getPluginManager().callEvent(deleteEvent);
        if (deleteEvent.isCancelled())
        {
            throw new McException(deleteEvent.getVetoReason(), deleteEvent.getVetoReasonArgs());
        }
        
        McLibInterface.instance().runInCopiedContext(() ->
        {
            McLibInterface.instance().setContext(EntityInterface.class, this);
            super.delete();
            this.handler.onDelete();
        });
        this.eventBus.clear();
        
        if (this.dynamic != null)
        {
            this.dynamic.onDelete(getBukkitEntity());
        }
        
        final EntityDeletedEvent deletedEvent = new EntityDeletedEvent(this);
        Bukkit.getPluginManager().callEvent(deletedEvent);
    }
    
    /**
     * reads the file config.
     * 
     * @throws McException
     *             thrown on config errors.
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
            if (this.config.isString("entityUuid")) //$NON-NLS-1$
            {
                this.entityUuid = UUID.fromString(this.config.getString("entityUuid")); //$NON-NLS-1$
            }
            if (this.config.isString("dynamictype")) //$NON-NLS-1$
            {
                this.dynamic = this.config.getEnum(DynamicEntityType.class, "dynamictype"); //$NON-NLS-1$
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
            if (this.entityUuid != null)
            {
                core.set("entityUuid", this.entityUuid.toString()); //$NON-NLS-1$
            }
            if (this.dynamic != null)
            {
                this.config.set("dynamictype", this.dynamic); //$NON-NLS-1$
                this.dynamic.onStore(this.getDynamicConfig(), this.entity);
            }
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
    public EntityHandlerInterface getHandler()
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
     * @param plugin
     *            Plugin that was disabled
     */
    public void onDisable(Plugin plugin)
    {
        this.eventBus.onDisable(plugin);
    }
    
    @Override
    public EntityTypeId getTypeId()
    {
        return ObjectServiceInterface.instance().getType(this.getEntityId());
    }
    
    @Override
    public Entity getBukkitEntity()
    {
        return this.entity;
    }
    
    /**
     * Sets the bukkit entity.
     * 
     * @param entity2
     *            bukkit entity
     */
    public void setEntity(Entity entity2)
    {
        this.entity = entity2;
        if (this.entity != null)
        {
            this.entityUuid = this.entity.getUniqueId();
        }
    }
    
    /**
     * @return entity uuid.
     */
    public UUID getEntityUuid()
    {
        return this.entityUuid;
    }
    
    /**
     * Sets the dynamic type for auto-creation of entites during server restart.
     * 
     * @param t
     *            dynamic type
     */
    public void setDynamicType(DynamicEntityType t)
    {
        this.dynamic = t;
    }
    
    /**
     * Returns the dynamic type of this entity.
     * 
     * @return the dynamic entity type
     */
    public DynamicEntityType getDynamicType()
    {
        return this.dynamic;
    }
    
    /**
     * Returns the config for dynamic entites.
     * 
     * @return the dynamiic section data config
     */
    public DataSection getDynamicConfig()
    {
        return this.config.createSection("dynamic"); //$NON-NLS-1$
    }
    
    /**
     * Copies configuration for storing in schemata.
     * 
     * @param lowloc
     *            starting position of schemata part
     * @return data section holding configuration needed for restoring the zone.
     */
    public DataSection copyAndSaveConfig(Location lowloc)
    {
        final DataSection result = new MemoryDataSection();
        result.set("otype", "entity"); //$NON-NLS-1$ //$NON-NLS-2$
        result.set("tplugin", this.getTypeId().getPluginName()); //$NON-NLS-1$
        result.set("tname", this.getTypeId().name()); //$NON-NLS-1$
        result.set("x", this.getBukkitEntity().getLocation().getBlockX() - lowloc.getBlockX()); //$NON-NLS-1$
        result.set("y", this.getBukkitEntity().getLocation().getBlockY() - lowloc.getBlockY()); //$NON-NLS-1$
        result.set("z", this.getBukkitEntity().getLocation().getBlockZ() - lowloc.getBlockZ()); //$NON-NLS-1$
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
