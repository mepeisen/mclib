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

package de.minigameslib.mclib.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.enums.EnumServiceInterface;
import de.minigameslib.mclib.api.mcevent.ComponentCreateEvent;
import de.minigameslib.mclib.api.mcevent.ComponentCreatedEvent;
import de.minigameslib.mclib.api.mcevent.EntityCreateEvent;
import de.minigameslib.mclib.api.mcevent.EntityCreatedEvent;
import de.minigameslib.mclib.api.mcevent.ObjectCreateEvent;
import de.minigameslib.mclib.api.mcevent.ObjectCreatedEvent;
import de.minigameslib.mclib.api.mcevent.SignCreateEvent;
import de.minigameslib.mclib.api.mcevent.SignCreatedEvent;
import de.minigameslib.mclib.api.mcevent.ZoneCreateEvent;
import de.minigameslib.mclib.api.mcevent.ZoneCreatedEvent;
import de.minigameslib.mclib.api.objects.ComponentHandlerInterface;
import de.minigameslib.mclib.api.objects.ComponentIdInterface;
import de.minigameslib.mclib.api.objects.ComponentInterface;
import de.minigameslib.mclib.api.objects.ComponentTypeId;
import de.minigameslib.mclib.api.objects.Cuboid;
import de.minigameslib.mclib.api.objects.EntityHandlerInterface;
import de.minigameslib.mclib.api.objects.EntityIdInterface;
import de.minigameslib.mclib.api.objects.EntityInterface;
import de.minigameslib.mclib.api.objects.EntityTypeId;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.NpcServiceInterface;
import de.minigameslib.mclib.api.objects.ObjectHandlerInterface;
import de.minigameslib.mclib.api.objects.ObjectIdInterface;
import de.minigameslib.mclib.api.objects.ObjectInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.objects.ObjectTypeId;
import de.minigameslib.mclib.api.objects.SignHandlerInterface;
import de.minigameslib.mclib.api.objects.SignIdInterface;
import de.minigameslib.mclib.api.objects.SignInterface;
import de.minigameslib.mclib.api.objects.SignTypeId;
import de.minigameslib.mclib.api.objects.ZoneHandlerInterface;
import de.minigameslib.mclib.api.objects.ZoneIdInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.api.objects.ZoneTypeId;
import de.minigameslib.mclib.api.util.function.McRunnable;
import de.minigameslib.mclib.impl.comp.AbstractComponent;
import de.minigameslib.mclib.impl.comp.ComponentId;
import de.minigameslib.mclib.impl.comp.ComponentImpl;
import de.minigameslib.mclib.impl.comp.ComponentOwner;
import de.minigameslib.mclib.impl.comp.ComponentRegistry;
import de.minigameslib.mclib.impl.comp.EntityId;
import de.minigameslib.mclib.impl.comp.EntityImpl;
import de.minigameslib.mclib.impl.comp.ObjectId;
import de.minigameslib.mclib.impl.comp.ObjectImpl;
import de.minigameslib.mclib.impl.comp.SignId;
import de.minigameslib.mclib.impl.comp.SignImpl;
import de.minigameslib.mclib.impl.comp.WorldChunk;
import de.minigameslib.mclib.impl.comp.ZoneId;
import de.minigameslib.mclib.impl.comp.ZoneImpl;
import de.minigameslib.mclib.impl.comp.builder.HumanBuilder;
import de.minigameslib.mclib.impl.comp.builder.VillagerBuilder;
import de.minigameslib.mclib.shared.api.com.EnumerationValue;

/**
 * A helper class managing plugin objects.
 * 
 * @author mepeisen
 *
 */
class ObjectsManager implements ComponentOwner, ObjectServiceInterface, NpcServiceInterface
{
    
    /** target data folder. */
    private final File                                                                                                           dataFolder;
    
    /** data container */
    private final ObjectsContainer<ComponentIdInterface, ComponentId, ComponentImpl, ComponentTypeId, ComponentHandlerInterface> components              = new ObjectsContainer<>();
    
    /** data container */
    private final ObjectsContainer<ObjectIdInterface, ObjectId, ObjectImpl, ObjectTypeId, ObjectHandlerInterface>                objects                 = new ObjectsContainer<>();
    
    /** data container */
    private final ObjectsContainer<SignIdInterface, SignId, SignImpl, SignTypeId, SignHandlerInterface>                          signs                   = new ObjectsContainer<>();
    
    /** data container */
    private final ObjectsContainer<EntityIdInterface, EntityId, EntityImpl, EntityTypeId, EntityHandlerInterface>                entities                = new ObjectsContainer<>();
    
    /** data container */
    private final ObjectsContainer<ZoneIdInterface, ZoneId, ZoneImpl, ZoneTypeId, ZoneHandlerInterface>                          zones                   = new ObjectsContainer<>();
    
    /** component types per plugin name. */
    private final Map<String, Map<String, ComponentTypeId>>                                                                      componentTypesByPlugin  = new HashMap<>();
    
    /** entity types per plugin name. */
    private final Map<String, Map<String, EntityTypeId>>                                                                         entityTypesByPlugin     = new HashMap<>();
    
    /** sign types per plugin name. */
    private final Map<String, Map<String, SignTypeId>>                                                                           signTypesByPlugin       = new HashMap<>();
    
    /** zone types per plugin name. */
    private final Map<String, Map<String, ZoneTypeId>>                                                                           zoneTypesByPlugin       = new HashMap<>();
    
    /** object types per plugin name. */
    private final Map<String, Map<String, ObjectTypeId>>                                                                         objectTypesByPlugin     = new HashMap<>();
    
    /** registered handlers. */
    private final Map<ComponentTypeId, Class<? extends ComponentHandlerInterface>>                                               componentHandlerClasses = new HashMap<>();
    
    /** registered handlers. */
    private final Map<EntityTypeId, Class<? extends EntityHandlerInterface>>                                                     entityHandlerClasses    = new HashMap<>();
    
    /** registered handlers. */
    private final Map<SignTypeId, Class<? extends SignHandlerInterface>>                                                         signHandlerClasses      = new HashMap<>();
    
    /** registered handlers. */
    private final Map<ZoneTypeId, Class<? extends ZoneHandlerInterface>>                                                         zoneHandlerClasses      = new HashMap<>();
    
    /** registered handlers. */
    private final Map<ObjectTypeId, Class<? extends ObjectHandlerInterface>>                                                     objectHandlerClasses    = new HashMap<>();
    
    /** the component registry handling location specific components (components, zones, signs) */
    private final ComponentRegistry                                                                                              registry                = new ComponentRegistry();
    
    /** the components folder. */
    private final File                                                                                                           componentsFolder;
    
    /** the signs folder. */
    private final File                                                                                                           signsFolder;
    
    /** the entites folder. */
    private final File                                                                                                           entitiesFolder;
    
    /** the zones folder. */
    private final File                                                                                                           zonesFolder;
    
    /** the objects folder. */
    private final File                                                                                                           objectsFolder;
    
    /** the loaded plugin sets. To detect duplicate loading. */
    private final Set<String>                                                                                                    loadedPlugins           = new HashSet<>();
    
    /** the players registry. */
    private final PlayerRegistry                                                                                                 players;
    
    /**
     * entities by uuid.
     */
    private final Map<UUID, Set<EntityId>>                                                                                       entitiesByUuid          = new HashMap<>();
    
    /**
     * Constructor.
     * 
     * @param dataFolder
     * @param players
     * @throws McException
     */
    public ObjectsManager(File dataFolder, PlayerRegistry players) throws McException
    {
        this.players = players;
        this.dataFolder = dataFolder;
        this.componentsFolder = new File(this.dataFolder, "components"); //$NON-NLS-1$
        this.signsFolder = new File(this.dataFolder, "signs"); //$NON-NLS-1$
        this.entitiesFolder = new File(this.dataFolder, "entities"); //$NON-NLS-1$
        this.zonesFolder = new File(this.dataFolder, "zones"); //$NON-NLS-1$
        this.objectsFolder = new File(this.dataFolder, "objects"); //$NON-NLS-1$
        
        if (!this.componentsFolder.exists())
            this.componentsFolder.mkdirs();
        if (!this.signsFolder.exists())
            this.signsFolder.mkdirs();
        if (!this.entitiesFolder.exists())
            this.entitiesFolder.mkdirs();
        if (!this.zonesFolder.exists())
            this.zonesFolder.mkdirs();
        if (!this.objectsFolder.exists())
            this.objectsFolder.mkdirs();
        
        this.components.loadRegistry(ComponentId::new, this.componentsFolder);
        this.zones.loadRegistry(ZoneId::new, this.zonesFolder);
        this.entities.loadRegistry(EntityId::new, this.entitiesFolder);
        this.signs.loadRegistry(SignId::new, this.signsFolder);
        this.objects.loadRegistry(ObjectId::new, this.objectsFolder);
    }
    
    @Override
    public <T extends ComponentHandlerInterface> void register(ComponentTypeId type, Class<T> handler) throws McException
    {
        this.componentTypesByPlugin.computeIfAbsent(safeGetPluginName(type.name(), type), k -> new HashMap<>()).put(type.name(), type);
        this.componentHandlerClasses.put(type, handler);
    }
    
    @Override
    public <T extends ObjectHandlerInterface> void register(ObjectTypeId type, Class<T> handler) throws McException
    {
        this.objectTypesByPlugin.computeIfAbsent(safeGetPluginName(type.name(), type), k -> new HashMap<>()).put(type.name(), type);
        this.objectHandlerClasses.put(type, handler);
    }
    
    @Override
    public <T extends EntityHandlerInterface> void register(EntityTypeId type, Class<T> handler) throws McException
    {
        this.entityTypesByPlugin.computeIfAbsent(safeGetPluginName(type.name(), type), k -> new HashMap<>()).put(type.name(), type);
        this.entityHandlerClasses.put(type, handler);
    }
    
    @Override
    public <T extends SignHandlerInterface> void register(SignTypeId type, Class<T> handler) throws McException
    {
        this.signTypesByPlugin.computeIfAbsent(safeGetPluginName(type.name(), type), k -> new HashMap<>()).put(type.name(), type);
        this.signHandlerClasses.put(type, handler);
    }
    
    @Override
    public <T extends ZoneHandlerInterface> void register(ZoneTypeId type, Class<T> handler) throws McException
    {
        this.zoneTypesByPlugin.computeIfAbsent(safeGetPluginName(type.name(), type), k -> new HashMap<>()).put(type.name(), type);
        this.zoneHandlerClasses.put(type, handler);
    }
    
    @Override
    public ResumeReport resumeObjects(Plugin plugin)
    {
        final String pluginName = plugin.getName();
        if (this.loadedPlugins.contains(pluginName))
        {
            final McException dupException = new McException(CommonMessages.PluginLoadedTwice, pluginName);
            final Set<ObjectIdInterface> brokenObjects = this.objects.containsPluginName(pluginName) ? new HashSet<>(this.objects.getByPlugin(pluginName)) : Collections.emptySet();
            final Set<ZoneIdInterface> brokenZones = this.zones.containsPluginName(pluginName) ? new HashSet<>(this.zones.getByPlugin(pluginName)) : Collections.emptySet();
            final Set<ComponentIdInterface> brokenComponents = this.components.containsPluginName(pluginName) ? new HashSet<>(this.components.getByPlugin(pluginName)) : Collections.emptySet();
            final Set<EntityIdInterface> brokenEntities = this.entities.containsPluginName(pluginName) ? new HashSet<>(this.entities.getByPlugin(pluginName)) : Collections.emptySet();
            final Set<SignIdInterface> brokenSigns = this.signs.containsPluginName(pluginName) ? new HashSet<>(this.signs.getByPlugin(pluginName)) : Collections.emptySet();
            return new ResumeReport() {
                
                @Override
                public boolean isOk()
                {
                    return false;
                }
                
                @Override
                public McException getException(ZoneIdInterface id)
                {
                    return brokenZones.contains(id) ? dupException : null;
                }
                
                @Override
                public McException getException(SignIdInterface id)
                {
                    return brokenSigns.contains(id) ? dupException : null;
                }
                
                @Override
                public McException getException(EntityIdInterface id)
                {
                    return brokenEntities.contains(id) ? dupException : null;
                }
                
                @Override
                public McException getException(ComponentIdInterface id)
                {
                    return brokenComponents.contains(id) ? dupException : null;
                }
                
                @Override
                public Iterable<ZoneIdInterface> getBrokenZones()
                {
                    return brokenZones;
                }
                
                @Override
                public Iterable<SignIdInterface> getBrokenSigns()
                {
                    return brokenSigns;
                }
                
                @Override
                public Iterable<EntityIdInterface> getBrokenEntities()
                {
                    return brokenEntities;
                }
                
                @Override
                public Iterable<ComponentIdInterface> getBrokenComponents()
                {
                    return brokenComponents;
                }
                
                @Override
                public Iterable<ObjectIdInterface> getBrokenObjects()
                {
                    return brokenObjects;
                }
                
                @Override
                public McException getException(ObjectIdInterface id)
                {
                    return brokenObjects.contains(id) ? dupException : null;
                }
            };
        }
        
        this.loadedPlugins.add(pluginName);
        final Map<ZoneIdInterface, McException> brokenZones = new HashMap<>();
        final Map<SignIdInterface, McException> brokenSigns = new HashMap<>();
        final Map<EntityIdInterface, McException> brokenEntities = new HashMap<>();
        final Map<ComponentIdInterface, McException> brokenComponents = new HashMap<>();
        final Map<ObjectIdInterface, McException> brokenObjects = new HashMap<>();
        
        this.objects.resumeObjects(pluginName, this.objectTypesByPlugin, ObjectId::getType, this::safeCreateHandler, (id, handler) -> {
            final ObjectImpl impl = new ObjectImpl(plugin, id, handler, new File(this.objectsFolder, id.getUuid().toString() + ".yml"), this); //$NON-NLS-1$
            impl.readConfig();
            
            this.runInContext(ObjectInterface.class, impl, () -> {
                handler.onResume(impl);
            });
            return impl;
        }, brokenObjects);
        
        this.zones.resumeObjects(pluginName, this.zoneTypesByPlugin, ZoneId::getType, this::safeCreateHandler, (id, handler) -> {
            final ZoneImpl impl = new ZoneImpl(plugin, this.registry, null, id, handler, new File(this.zonesFolder, id.getUuid().toString() + ".yml"), this); //$NON-NLS-1$
            impl.readConfig();
            
            this.runInContext(ZoneInterface.class, impl, () -> {
                handler.onResume(impl);
            });
            return impl;
        }, brokenZones);
        
        this.signs.resumeObjects(pluginName, this.signTypesByPlugin, SignId::getType, this::safeCreateHandler, (id, handler) -> {
            final SignImpl impl = new SignImpl(plugin, this.registry, null, id, handler, new File(this.signsFolder, id.getUuid().toString() + ".yml"), this); //$NON-NLS-1$
            impl.readConfig();
            // research sign
            final Block block = impl.getLocation().getBlock();
            if (block instanceof Sign)
            {
                impl.setSign((Sign) block);
                
                this.runInContext(SignInterface.class, impl, () -> {
                    handler.onResume(impl);
                });
                return impl;
            }
            throw new McException(CommonMessages.SignNotFoundError);
        }, brokenSigns);
        
        this.entities.resumeObjects(pluginName, this.entityTypesByPlugin, EntityId::getType, this::safeCreateHandler, (id, handler) -> {
            final EntityImpl impl = new EntityImpl(plugin, null, id, handler, new File(this.entitiesFolder, id.getUuid().toString() + ".yml"), this); //$NON-NLS-1$
            impl.readConfig();
            // research entity
            Entity entity = null;
            if (impl.getDynamicType() == null)
            {
                final UUID entityUuid = impl.getEntityUuid();
                outer: for (final World world : Bukkit.getWorlds())
                {
                    for (final Entity ent : world.getEntities())
                    {
                        if (ent.getUniqueId().equals(entityUuid))
                        {
                            entity = ent;
                            break outer;
                        }
                    }
                }
            }
            else
            {
                entity = impl.getDynamicType().onResume(impl.getDynamicConfig());
            }
            if (entity != null)
            {
                impl.setEntity(entity);
                
                this.runInContext(EntityInterface.class, impl, () -> {
                    handler.onResume(impl);
                });
                return impl;
            }
            throw new McException(CommonMessages.EntityNotFoundError);
        }, brokenEntities);
        
        this.components.resumeObjects(pluginName, this.componentTypesByPlugin, ComponentId::getType, this::safeCreateHandler, (id, handler) -> {
            final ComponentImpl impl = new ComponentImpl(plugin, this.registry, null, id, handler, new File(this.componentsFolder, id.getUuid().toString() + ".yml"), this); //$NON-NLS-1$
            impl.readConfig();
            
            this.runInContext(ComponentInterface.class, impl, () -> {
                handler.onResume(impl);
            });
            return impl;
        }, brokenComponents);
        
        return new ResumeReport() {
            
            @Override
            public boolean isOk()
            {
                return brokenComponents.isEmpty() && brokenEntities.isEmpty() && brokenSigns.isEmpty() && brokenZones.isEmpty() && brokenObjects.isEmpty();
            }
            
            @Override
            public McException getException(ZoneIdInterface id)
            {
                return brokenZones.get(id);
            }
            
            @Override
            public McException getException(SignIdInterface id)
            {
                return brokenSigns.get(id);
            }
            
            @Override
            public McException getException(EntityIdInterface id)
            {
                return brokenEntities.get(id);
            }
            
            @Override
            public McException getException(ComponentIdInterface id)
            {
                return brokenComponents.get(id);
            }
            
            @Override
            public Iterable<ZoneIdInterface> getBrokenZones()
            {
                return brokenZones.keySet();
            }
            
            @Override
            public Iterable<SignIdInterface> getBrokenSigns()
            {
                return brokenSigns.keySet();
            }
            
            @Override
            public Iterable<EntityIdInterface> getBrokenEntities()
            {
                return brokenEntities.keySet();
            }
            
            @Override
            public Iterable<ComponentIdInterface> getBrokenComponents()
            {
                return brokenComponents.keySet();
            }
            
            @Override
            public Iterable<ObjectIdInterface> getBrokenObjects()
            {
                return brokenObjects.keySet();
            }
            
            @Override
            public McException getException(ObjectIdInterface id)
            {
                return brokenObjects.get(id);
            }
        };
    }
    
    /**
     * @param plugin
     */
    public void onDisable(Plugin plugin)
    {
        final String pluginName = plugin.getName();
        if (this.loadedPlugins.contains(pluginName))
        {
            // remove types
            final Map<String, ComponentTypeId> componentTypes = this.componentTypesByPlugin.remove(pluginName);
            if (componentTypes != null)
            {
                componentTypes.entrySet().forEach(this.componentHandlerClasses::remove);
            }
            final Map<String, EntityTypeId> entityTypes = this.entityTypesByPlugin.remove(pluginName);
            if (entityTypes != null)
            {
                entityTypes.entrySet().forEach(this.entityHandlerClasses::remove);
            }
            final Map<String, SignTypeId> signTypes = this.signTypesByPlugin.remove(pluginName);
            if (signTypes != null)
            {
                signTypes.entrySet().forEach(this.signHandlerClasses::remove);
            }
            final Map<String, ZoneTypeId> zoneTypes = this.zoneTypesByPlugin.remove(pluginName);
            if (zoneTypes != null)
            {
                zoneTypes.entrySet().forEach(this.zoneHandlerClasses::remove);
            }
            
            // pause elements
            this.components.removePlugin(pluginName).forEach((id, persistent) -> {
                final ComponentImpl component = this.components.remove(id);
                component.clearEventRegistrations();
                
                this.runInContext(ComponentInterface.class, component, () -> {
                    component.getHandler().onPause(component);
                    if (persistent)
                    {
                        component.saveConfig();
                    }
                });
            });
            this.entities.removePlugin(pluginName).forEach((id, persistent) -> {
                final EntityImpl entity = this.entities.remove(id);
                entity.clearEventRegistrations();
                
                this.runInContext(EntityInterface.class, entity, () -> {
                    entity.getHandler().onPause(entity);
                    if (persistent)
                    {
                        entity.saveConfig();
                    }
                });
            });
            this.signs.removePlugin(pluginName).forEach((id, persistent) -> {
                final SignImpl sign = this.signs.remove(id);
                sign.clearEventRegistrations();
                
                this.runInContext(SignInterface.class, sign, () -> {
                    sign.getHandler().onPause(sign);
                    if (persistent)
                    {
                        sign.saveConfig();
                    }
                });
            });
            this.zones.removePlugin(pluginName).forEach((id, persistent) -> {
                final ZoneImpl zone = this.zones.remove(id);
                zone.clearEventRegistrations();
                
                this.runInContext(ZoneInterface.class, zone, () -> {
                    zone.getHandler().onPause(zone);
                    if (persistent)
                    {
                        zone.saveConfig();
                    }
                });
            });
            this.objects.removePlugin(pluginName).forEach((id, persistent) -> {
                final ObjectImpl object = this.objects.remove(id);
                object.clearEventRegistrations();
                
                this.runInContext(ObjectInterface.class, object, () -> {
                    object.getHandler().onPause(object);
                    if (persistent)
                    {
                        object.saveConfig();
                    }
                });
            });
            
            this.components.forEach(c -> {
                this.runInContext(ComponentInterface.class, c, () -> {
                    c.onDisable(plugin);
                });
            });
            this.entities.forEach(c -> {
                this.runInContext(EntityInterface.class, c, () -> {
                    c.onDisable(plugin);
                });
            });
            this.signs.forEach(c -> {
                this.runInContext(SignInterface.class, c, () -> {
                    c.onDisable(plugin);
                });
            });
            this.zones.forEach(c -> {
                this.runInContext(ZoneInterface.class, c, () -> {
                    c.onDisable(plugin);
                });
            });
            this.objects.forEach(c -> {
                this.runInContext(ObjectInterface.class, c, () -> {
                    c.onDisable(plugin);
                });
            });
            
            // remove plugin
            this.loadedPlugins.remove(pluginName);
        }
    }
    
    /**
     * @param plugin
     */
    public void onEnable(Plugin plugin)
    {
        // does nothing, waiting for invocation of resumeObjects.
        // maybe in future versions we will do some logic here
    }
    
    @Override
    public ComponentInterface findComponent(Location location)
    {
        final Optional<ComponentImpl> result = this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof ComponentImpl).map(c -> (ComponentImpl) c)
                .filter(c -> c.getLocation().equals(location)).findFirst();
        return result.isPresent() ? result.get() : null;
    }
    
    @Override
    public ComponentInterface findComponent(ComponentIdInterface id)
    {
        return this.components.get((ComponentId) id);
    }
    
    @Override
    public ComponentInterface createComponent(ComponentTypeId type, Location location, ComponentHandlerInterface handler, boolean persist) throws McException
    {
        final Plugin plugin = this.safeGetPlugin(type.name(), type);
        final String pluginName = plugin.getName();
        if (!this.loadedPlugins.contains(pluginName))
        {
            throw new McException(CommonMessages.PluginNotLoaded, pluginName);
        }
        final ComponentHandlerInterface handler2 = handler == null ? safeCreateHandler(pluginName, type) : handler;
        final UUID uuid = UUID.randomUUID();
        final ComponentId id = new ComponentId(pluginName, type.name(), uuid);
        
        // init
        final ComponentImpl impl = new ComponentImpl(plugin, this.registry, location, id, handler2, persist ? new File(this.componentsFolder, uuid.toString() + ".yml") : null, this); //$NON-NLS-1$
        final ComponentCreateEvent createEvent = new ComponentCreateEvent(impl);
        Bukkit.getPluginManager().callEvent(createEvent);
        if (createEvent.isCancelled())
        {
            throw new McException(createEvent.getVetoReason(), createEvent.getVetoReasonArgs());
        }
        
        this.runInContext(ComponentInterface.class, impl, () -> {
            handler2.onCreate(impl);
            
            // store data
            this.components.put(pluginName, id, impl, persist);
            
            // save data
            if (persist)
            {
                this.components.saveIdList(this.componentsFolder);
                impl.saveConfig();
            }
        });
        
        final ComponentCreatedEvent createdEvent = new ComponentCreatedEvent(impl);
        Bukkit.getPluginManager().callEvent(createdEvent);
        return impl;
    }
    
    /**
     * safe get plugin name from enum.
     * 
     * @param name
     * @param enumValue
     * @return plugin name
     * @throws McException
     *             thrown if enum is invalid or not registered
     */
    private String safeGetPluginName(String name, Object enumValue) throws McException
    {
        return this.safeGetPlugin(name, enumValue).getName();
    }
    
    /**
     * safe get plugin from enum.
     * 
     * @param name
     * @param enumValue
     * @return plugin
     * @throws McException
     *             thrown if enum is invalid or not registered
     */
    private Plugin safeGetPlugin(String name, Object enumValue) throws McException
    {
        if (!(enumValue instanceof Enum<?>) || !(enumValue instanceof EnumerationValue))
        {
            throw new McException(CommonMessages.BrokenObjectTypeNotAnEnum, "?", name, enumValue.getClass().getName()); //$NON-NLS-1$
        }
        final EnumerationValue value = (EnumerationValue) enumValue;
        final Plugin plugin = EnumServiceInterface.instance().getPlugin(value);
        if (plugin == null)
        {
            throw new McException(CommonMessages.BrokenObjectTypeEnumNotRegistered, "?", name, enumValue.getClass().getName()); //$NON-NLS-1$
        }
        return plugin;
    }
    
    /**
     * Safe create handler from type.
     * 
     * @param pluginName
     * @param type
     * @return handler
     * @throws McException
     */
    private ComponentHandlerInterface safeCreateHandler(String pluginName, ComponentTypeId type) throws McException
    {
        final Class<? extends ComponentHandlerInterface> clazz = this.componentHandlerClasses.get(type);
        if (clazz == null)
        {
            throw new McException(CommonMessages.BrokenObjectType, pluginName, type.name(), type.getClass().getName());
        }
        try
        {
            return clazz.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            throw new McException(CommonMessages.BrokenObjectType, pluginName, type.name(), type.getClass().getName(), e.getMessage());
        }
    }
    
    @Override
    public EntityInterface findEntity(Entity entity)
    {
        final UUID uuid = entity.getUniqueId();
        if (this.entitiesByUuid.containsKey(uuid))
        {
            final Optional<EntityInterface> result = this.entitiesByUuid.get(uuid).stream().map(this::findEntity).findFirst();
            if (result.isPresent())
            {
                return result.get();
            }
        }
        return null;
    }
    
    @Override
    public EntityInterface findEntity(EntityIdInterface id)
    {
        return this.entities.get((EntityId) id);
    }
    
    @Override
    public EntityInterface createEntity(EntityTypeId type, Entity entity, EntityHandlerInterface handler, boolean persist) throws McException
    {
        final Plugin plugin = this.safeGetPlugin(type.name(), type);
        final String pluginName = plugin.getName();
        if (!this.loadedPlugins.contains(pluginName))
        {
            throw new McException(CommonMessages.PluginNotLoaded, pluginName);
        }
        final EntityHandlerInterface handler2 = handler == null ? safeCreateHandler(pluginName, type) : handler;
        final UUID uuid = UUID.randomUUID();
        final EntityId id = new EntityId(pluginName, type.name(), uuid);
        
        // init
        final EntityImpl impl = new EntityImpl(plugin, entity, id, handler2, persist ? new File(this.entitiesFolder, uuid.toString() + ".yml") : null, this); //$NON-NLS-1$
        final EntityCreateEvent createEvent = new EntityCreateEvent(impl);
        Bukkit.getPluginManager().callEvent(createEvent);
        if (createEvent.isCancelled())
        {
            throw new McException(createEvent.getVetoReason(), createEvent.getVetoReasonArgs());
        }
        
        this.runInContext(EntityInterface.class, impl, () -> {
            handler2.onCreate(impl);
            
            // store data
            this.entities.put(pluginName, id, impl, persist);
            final UUID entityUuid = entity.getUniqueId();
            this.entitiesByUuid.computeIfAbsent(entityUuid, k -> new HashSet<>()).add(id);
            
            // save data
            if (persist)
            {
                this.entities.saveIdList(this.entitiesFolder);
                impl.saveConfig();
            }
        });
        
        final EntityCreatedEvent createdEvent = new EntityCreatedEvent(impl);
        Bukkit.getPluginManager().callEvent(createdEvent);
        return impl;
    }
    
    @Override
    public SignInterface findSign(Location location)
    {
        final Optional<SignImpl> result = this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof SignImpl).map(c -> (SignImpl) c).filter(c -> c.getLocation().equals(location))
                .findFirst();
        return result.isPresent() ? result.get() : null;
    }
    
    @Override
    public SignInterface findSign(SignIdInterface id)
    {
        return this.signs.get((SignId) id);
    }
    
    @Override
    public SignInterface createSign(SignTypeId type, Sign sign, SignHandlerInterface handler, boolean persist) throws McException
    {
        final Plugin plugin = this.safeGetPlugin(type.name(), type);
        final String pluginName = plugin.getName();
        if (!this.loadedPlugins.contains(pluginName))
        {
            throw new McException(CommonMessages.PluginNotLoaded, pluginName);
        }
        final SignHandlerInterface handler2 = handler == null ? safeCreateHandler(pluginName, type) : handler;
        final UUID uuid = UUID.randomUUID();
        final SignId id = new SignId(pluginName, type.name(), uuid);
        
        // init
        final SignImpl impl = new SignImpl(plugin, this.registry, sign, id, handler2, persist ? new File(this.signsFolder, uuid.toString() + ".yml") : null, this); //$NON-NLS-1$
        final SignCreateEvent createEvent = new SignCreateEvent(impl);
        Bukkit.getPluginManager().callEvent(createEvent);
        if (createEvent.isCancelled())
        {
            throw new McException(createEvent.getVetoReason(), createEvent.getVetoReasonArgs());
        }
        
        this.runInContext(SignInterface.class, impl, () -> {
            handler2.onCreate(impl);
            
            // store data
            this.signs.put(pluginName, id, impl, persist);
            
            // save data
            if (persist)
            {
                this.signs.saveIdList(this.signsFolder);
                impl.saveConfig();
            }
        });
        
        final SignCreatedEvent createdEvent = new SignCreatedEvent(impl);
        Bukkit.getPluginManager().callEvent(createdEvent);
        return impl;
    }
    
    /**
     * Safe create handler from type.
     * 
     * @param pluginName
     * @param type
     * @return handler
     * @throws McException
     */
    private SignHandlerInterface safeCreateHandler(String pluginName, SignTypeId type) throws McException
    {
        final Class<? extends SignHandlerInterface> clazz = this.signHandlerClasses.get(type);
        if (clazz == null)
        {
            throw new McException(CommonMessages.BrokenObjectType, pluginName, type.name(), type.getClass().getName());
        }
        try
        {
            return clazz.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            throw new McException(CommonMessages.BrokenObjectType, pluginName, type.name(), type.getClass().getName(), e.getMessage());
        }
    }
    
    /**
     * Safe create handler from type.
     * 
     * @param pluginName
     * @param type
     * @return handler
     * @throws McException
     */
    private EntityHandlerInterface safeCreateHandler(String pluginName, EntityTypeId type) throws McException
    {
        final Class<? extends EntityHandlerInterface> clazz = this.entityHandlerClasses.get(type);
        if (clazz == null)
        {
            throw new McException(CommonMessages.BrokenObjectType, pluginName, type.name(), type.getClass().getName());
        }
        try
        {
            return clazz.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            throw new McException(CommonMessages.BrokenObjectType, pluginName, type.name(), type.getClass().getName(), e.getMessage());
        }
    }
    
    @Override
    public ZoneInterface createZone(ZoneTypeId type, Cuboid cuboid, ZoneHandlerInterface handler, boolean persist) throws McException
    {
        final Plugin plugin = this.safeGetPlugin(type.name(), type);
        final String pluginName = plugin.getName();
        if (!this.loadedPlugins.contains(pluginName))
        {
            throw new McException(CommonMessages.PluginNotLoaded, pluginName);
        }
        final ZoneHandlerInterface handler2 = handler == null ? safeCreateHandler(pluginName, type) : handler;
        final UUID uuid = UUID.randomUUID();
        final ZoneId id = new ZoneId(pluginName, type.name(), uuid);
        
        // init
        final ZoneImpl impl = new ZoneImpl(plugin, this.registry, cuboid, id, handler2, persist ? new File(this.zonesFolder, uuid.toString() + ".yml") : null, this); //$NON-NLS-1$
        final ZoneCreateEvent createEvent = new ZoneCreateEvent(impl);
        Bukkit.getPluginManager().callEvent(createEvent);
        if (createEvent.isCancelled())
        {
            throw new McException(createEvent.getVetoReason(), createEvent.getVetoReasonArgs());
        }
        
        this.runInContext(ZoneInterface.class, impl, () -> {
            handler2.onCreate(impl);
            
            // store data
            this.zones.put(pluginName, id, impl, persist);
            
            // save data
            if (persist)
            {
                this.zones.saveIdList(this.zonesFolder);
                impl.saveConfig();
            }
        });
        
        final ZoneCreatedEvent createdEvent = new ZoneCreatedEvent(impl);
        Bukkit.getPluginManager().callEvent(createdEvent);
        return impl;
    }
    
    /**
     * Safe create handler from type.
     * 
     * @param pluginName
     * @param type
     * @return handler
     * @throws McException
     */
    private ZoneHandlerInterface safeCreateHandler(String pluginName, ZoneTypeId type) throws McException
    {
        final Class<? extends ZoneHandlerInterface> clazz = this.zoneHandlerClasses.get(type);
        if (clazz == null)
        {
            throw new McException(CommonMessages.BrokenObjectType, pluginName, type.name(), type.getClass().getName());
        }
        try
        {
            return clazz.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            throw new McException(CommonMessages.BrokenObjectType, pluginName, type.name(), type.getClass().getName(), e.getMessage());
        }
    }
    
    /**
     * Safe create handler from type.
     * 
     * @param pluginName
     * @param type
     * @return handler
     * @throws McException
     */
    private ObjectHandlerInterface safeCreateHandler(String pluginName, ObjectTypeId type) throws McException
    {
        final Class<? extends ObjectHandlerInterface> clazz = this.objectHandlerClasses.get(type);
        if (clazz == null)
        {
            throw new McException(CommonMessages.BrokenObjectType, pluginName, type.name(), type.getClass().getName());
        }
        try
        {
            return clazz.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            throw new McException(CommonMessages.BrokenObjectType, pluginName, type.name(), type.getClass().getName(), e.getMessage());
        }
    }
    
    @Override
    public ZoneInterface findZone(ZoneIdInterface id)
    {
        return this.zones.get((ZoneId) id);
    }
    
    @Override
    public Collection<ZoneInterface> findZonesWithoutYD(Location location)
    {
        return this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c).filter(c -> c.getCuboid().containsLocWithoutYD(location))
                .collect(Collectors.toList());
    }
    
    @Override
    public ZoneInterface findZoneWithoutYD(Location location)
    {
        final Optional<ZoneImpl> result = this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c)
                .filter(c -> c.getCuboid().containsLocWithoutYD(location)).findFirst();
        return result.isPresent() ? result.get() : null;
    }
    
    @Override
    public Collection<ZoneInterface> findZonesWithoutY(Location location)
    {
        return this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c).filter(c -> c.getCuboid().containsLocWithoutY(location))
                .collect(Collectors.toList());
    }
    
    @Override
    public ZoneInterface findZoneWithoutY(Location location)
    {
        final Optional<ZoneImpl> result = this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c)
                .filter(c -> c.getCuboid().containsLocWithoutY(location)).findFirst();
        return result.isPresent() ? result.get() : null;
    }
    
    @Override
    public Collection<ZoneInterface> findZones(Location location)
    {
        return this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c).filter(c -> c.getCuboid().containsLoc(location))
                .collect(Collectors.toList());
    }
    
    @Override
    public ZoneInterface findZone(Location location)
    {
        final Optional<ZoneImpl> result = this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c)
                .filter(c -> c.getCuboid().containsLoc(location)).findFirst();
        return result.isPresent() ? result.get() : null;
    }
    
    @Override
    public void onDelete(AbstractComponent component) throws McException
    {
        if (component instanceof ComponentImpl)
        {
            this.onDelete((ComponentImpl) component);
        }
        else if (component instanceof ZoneImpl)
        {
            this.onDelete((ZoneImpl) component);
        }
        else if (component instanceof SignImpl)
        {
            this.onDelete((SignImpl) component);
        }
        if (component instanceof ObjectImpl)
        {
            this.onDelete((ObjectImpl) component);
        }
        else if (component instanceof EntityImpl)
        {
            this.onDelete((EntityImpl) component);
        }
    }
    
    /**
     * Deletes a component
     * 
     * @param component
     * @throws McException
     */
    private void onDelete(ComponentImpl component) throws McException
    {
        final ComponentId id = (ComponentId) component.getComponentId();
        this.components.remove(id.getPluginName(), id, this.componentsFolder);
    }
    
    /**
     * Deletes a zone
     * 
     * @param zone
     * @throws McException
     */
    private void onDelete(ZoneImpl zone) throws McException
    {
        final ZoneId id = zone.getZoneId();
        this.zones.remove(id.getPluginName(), id, this.zonesFolder);
    }
    
    /**
     * Deletes a sign
     * 
     * @param sign
     * @throws McException
     */
    private void onDelete(SignImpl sign) throws McException
    {
        final SignId id = (SignId) sign.getSignId();
        this.signs.remove(id.getPluginName(), id, this.signsFolder);
    }
    
    /**
     * Deletes an object
     * 
     * @param obj
     * @throws McException
     */
    private void onDelete(ObjectImpl obj) throws McException
    {
        final ObjectId id = (ObjectId) obj.getObjectId();
        this.objects.remove(id.getPluginName(), id, this.objectsFolder);
    }
    
    /**
     * Deletes an entity
     * 
     * @param ent
     * @throws McException
     */
    private void onDelete(EntityImpl ent) throws McException
    {
        final EntityId id = (EntityId) ent.getEntityId();
        final UUID uuid = ent.getBukkitEntity().getUniqueId();
        if (this.entitiesByUuid.containsKey(uuid))
        {
            this.entitiesByUuid.get(uuid).remove(id);
        }
        this.entities.remove(id.getPluginName(), id, this.entitiesFolder);
    }
    
    @Override
    public Collection<ComponentInterface> findComponents(Location location)
    {
        return this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof ComponentImpl).map(c -> (ComponentImpl) c).filter(c -> c.getLocation().equals(location))
                .collect(Collectors.toList());
    }
    
    @Override
    public Collection<ComponentInterface> findComponents(ComponentTypeId... type)
    {
        final Map<String, Set<String>> perPlugin = new HashMap<>();
        for (final ComponentTypeId typeid : type)
        {
            perPlugin.computeIfAbsent(typeid.getPluginName(), k -> new HashSet<>()).add(typeid.name());
        }
        
        final List<ComponentInterface> result = new ArrayList<>();
        perPlugin.forEach((plugin, ids) -> {
            this.components.getByPlugin(plugin).stream().filter(id -> ids.contains(id.getType())).map(this.components::get).forEach(result::add);
        });
        
        return result;
    }
    
    @Override
    public Collection<EntityInterface> findEntities(Entity entity)
    {
        final UUID uuid = entity.getUniqueId();
        if (this.entitiesByUuid.containsKey(uuid))
        {
            return this.entitiesByUuid.get(uuid).stream().map(this::findEntity).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
    
    @Override
    public Collection<EntityInterface> findEntities(EntityTypeId... type)
    {
        final List<EntityInterface> result = new ArrayList<>();
        final Set<EntityTypeId> types = new HashSet<>();
        for (final EntityTypeId t : type)
        {
            types.add(t);
        }
        this.entitiesByUuid.values().forEach(s -> {
            s.stream().map(this::findEntity).filter(e -> types.contains(e.getTypeId())).forEach(result::add);
        });
        return result;
    }
    
    @Override
    public Collection<SignInterface> findSigns(Location location)
    {
        return this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof SignImpl).map(c -> (SignImpl) c).filter(c -> c.getLocation().equals(location))
                .collect(Collectors.toList());
    }
    
    @Override
    public Collection<SignInterface> findSigns(SignTypeId... type)
    {
        final Map<String, Set<String>> perPlugin = new HashMap<>();
        for (final SignTypeId typeid : type)
        {
            perPlugin.computeIfAbsent(typeid.getPluginName(), k -> new HashSet<>()).add(typeid.name());
        }
        
        final List<SignInterface> result = new ArrayList<>();
        perPlugin.forEach((plugin, ids) -> {
            this.signs.getByPlugin(plugin).stream().filter(id -> ids.contains(id.getType())).map(this.signs::get).forEach(result::add);
        });
        
        return result;
    }
    
    /**
     * Returns a stream for all chunks in given cuboid
     * 
     * @param cuboid
     * @return stream
     */
    private Stream<AbstractComponent> fetchForCuboid(Cuboid cuboid)
    {
        Stream<AbstractComponent> result = Stream.empty();
        final WorldChunk lowChunk = new WorldChunk(cuboid.getLowLoc());
        final WorldChunk highChunk = new WorldChunk(cuboid.getHighLoc());
        for (int x = lowChunk.getX(); x <= highChunk.getX(); x++)
        {
            for (int z = lowChunk.getZ(); z < lowChunk.getZ(); z++)
            {
                final WorldChunk chunk = new WorldChunk(lowChunk.getServerName(), lowChunk.getServerName(), x, z);
                result = Stream.concat(result, this.registry.fetch(chunk).stream());
            }
        }
        return result;
    }
    
    /**
     * @param mode
     * @return test function
     */
    private BiPredicate<ZoneImpl, Cuboid> getTester(CuboidMode mode)
    {
        switch (mode)
        {
            case FindMatching:
                return (z, c) -> z.getCuboid().equals(c);
            case FindChildren:
                return (z, c) -> z.getCuboid().isChild(c);
            case FindParents:
                return (z, c) -> z.getCuboid().isParent(c);
            case FindOverlapping:
                return (z, c) -> z.getCuboid().isOverlapping(c);
            default:
                break;
        }
        return null;
    }
    
    @Override
    public ZoneInterface findZone(Cuboid cuboid, CuboidMode mode)
    {
        final BiPredicate<ZoneImpl, Cuboid> tester = getTester(mode);
        final Optional<ZoneImpl> result = this.fetchForCuboid(cuboid).filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c).filter(z -> tester.test(z, cuboid)).findFirst();
        return result.isPresent() ? result.get() : null;
    }
    
    @Override
    public ZoneInterface findZone(Cuboid cuboid, CuboidMode mode, ZoneTypeId... type)
    {
        final Map<String, Set<String>> perPlugin = new HashMap<>();
        for (final ZoneTypeId typeid : type)
        {
            perPlugin.computeIfAbsent(typeid.getPluginName(), k -> new HashSet<>()).add(typeid.name());
        }
        final BiPredicate<ZoneImpl, Cuboid> tester = getTester(mode);
        final Optional<ZoneImpl> result = this.fetchForCuboid(cuboid).filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c)
                .filter(z -> perPlugin.containsKey(z.getZoneId().getPluginName()) && perPlugin.get(z.getZoneId().getPluginName()).contains(z.getZoneId().getType())).filter(z -> tester.test(z, cuboid))
                .findFirst();
        return result.isPresent() ? result.get() : null;
    }
    
    @Override
    public ZoneInterface findZone(Location location, ZoneTypeId... type)
    {
        final Map<String, Set<String>> perPlugin = new HashMap<>();
        for (final ZoneTypeId typeid : type)
        {
            perPlugin.computeIfAbsent(typeid.getPluginName(), k -> new HashSet<>()).add(typeid.name());
        }
        final Optional<ZoneImpl> result = this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c)
                .filter(z -> perPlugin.containsKey(z.getZoneId().getPluginName()) && perPlugin.get(z.getZoneId().getPluginName()).contains(z.getZoneId().getType()))
                .filter(c -> c.getCuboid().containsLoc(location)).findFirst();
        return result.isPresent() ? result.get() : null;
    }
    
    @Override
    public Collection<ZoneInterface> findZones(Cuboid cuboid, CuboidMode mode)
    {
        final BiPredicate<ZoneImpl, Cuboid> tester = getTester(mode);
        return this.fetchForCuboid(cuboid).filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c).filter(z -> tester.test(z, cuboid)).collect(Collectors.toList());
    }
    
    @Override
    public Collection<ZoneInterface> findZones(Cuboid cuboid, CuboidMode mode, ZoneTypeId... type)
    {
        final Map<String, Set<String>> perPlugin = new HashMap<>();
        for (final ZoneTypeId typeid : type)
        {
            perPlugin.computeIfAbsent(typeid.getPluginName(), k -> new HashSet<>()).add(typeid.name());
        }
        final BiPredicate<ZoneImpl, Cuboid> tester = getTester(mode);
        return this.fetchForCuboid(cuboid).filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c)
                .filter(z -> perPlugin.containsKey(z.getZoneId().getPluginName()) && perPlugin.get(z.getZoneId().getPluginName()).contains(z.getZoneId().getType())).filter(z -> tester.test(z, cuboid))
                .collect(Collectors.toList());
    }
    
    @Override
    public Collection<ZoneInterface> findZones(Location location, ZoneTypeId... type)
    {
        final Map<String, Set<String>> perPlugin = new HashMap<>();
        for (final ZoneTypeId typeid : type)
        {
            perPlugin.computeIfAbsent(typeid.getPluginName(), k -> new HashSet<>()).add(typeid.name());
        }
        return this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c)
                .filter(z -> perPlugin.containsKey(z.getZoneId().getPluginName()) && perPlugin.get(z.getZoneId().getPluginName()).contains(z.getZoneId().getType()))
                .filter(c -> c.getCuboid().containsLoc(location)).collect(Collectors.toList());
    }
    
    @Override
    public ZoneInterface findZoneWithoutY(Location location, ZoneTypeId... type)
    {
        final Map<String, Set<String>> perPlugin = new HashMap<>();
        for (final ZoneTypeId typeid : type)
        {
            perPlugin.computeIfAbsent(typeid.getPluginName(), k -> new HashSet<>()).add(typeid.name());
        }
        final Optional<ZoneImpl> result = this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c)
                .filter(z -> perPlugin.containsKey(z.getZoneId().getPluginName()) && perPlugin.get(z.getZoneId().getPluginName()).contains(z.getZoneId().getType()))
                .filter(c -> c.getCuboid().containsLocWithoutY(location)).findFirst();
        return result.isPresent() ? result.get() : null;
    }
    
    @Override
    public Collection<ZoneInterface> findZonesWithoutY(Location location, ZoneTypeId... type)
    {
        final Map<String, Set<String>> perPlugin = new HashMap<>();
        for (final ZoneTypeId typeid : type)
        {
            perPlugin.computeIfAbsent(typeid.getPluginName(), k -> new HashSet<>()).add(typeid.name());
        }
        return this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c)
                .filter(z -> perPlugin.containsKey(z.getZoneId().getPluginName()) && perPlugin.get(z.getZoneId().getPluginName()).contains(z.getZoneId().getType()))
                .filter(c -> c.getCuboid().containsLocWithoutY(location)).collect(Collectors.toList());
    }
    
    @Override
    public ZoneInterface findZoneWithoutYD(Location location, ZoneTypeId... type)
    {
        final Map<String, Set<String>> perPlugin = new HashMap<>();
        for (final ZoneTypeId typeid : type)
        {
            perPlugin.computeIfAbsent(typeid.getPluginName(), k -> new HashSet<>()).add(typeid.name());
        }
        final Optional<ZoneImpl> result = this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c)
                .filter(z -> perPlugin.containsKey(z.getZoneId().getPluginName()) && perPlugin.get(z.getZoneId().getPluginName()).contains(z.getZoneId().getType()))
                .filter(c -> c.getCuboid().containsLocWithoutYD(location)).findFirst();
        return result.isPresent() ? result.get() : null;
    }
    
    @Override
    public Collection<ZoneInterface> findZonesWithoutYD(Location location, ZoneTypeId... type)
    {
        final Map<String, Set<String>> perPlugin = new HashMap<>();
        for (final ZoneTypeId typeid : type)
        {
            perPlugin.computeIfAbsent(typeid.getPluginName(), k -> new HashSet<>()).add(typeid.name());
        }
        return this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c)
                .filter(z -> perPlugin.containsKey(z.getZoneId().getPluginName()) && perPlugin.get(z.getZoneId().getPluginName()).contains(z.getZoneId().getType()))
                .filter(c -> c.getCuboid().containsLocWithoutYD(location)).collect(Collectors.toList());
    }
    
    @Override
    public Collection<ZoneInterface> findZones(ZoneTypeId... type)
    {
        final Map<String, Set<String>> perPlugin = new HashMap<>();
        for (final ZoneTypeId typeid : type)
        {
            perPlugin.computeIfAbsent(typeid.getPluginName(), k -> new HashSet<>()).add(typeid.name());
        }
        
        final List<ZoneInterface> result = new ArrayList<>();
        perPlugin.forEach((plugin, ids) -> {
            this.zones.getByPlugin(plugin).stream().filter(id -> ids.contains(id.getType())).map(this.zones::get).forEach(result::add);
        });
        
        return result;
    }
    
    @Override
    public ComponentInterface findComponent(Block block)
    {
        return this.findComponent(block.getLocation());
    }
    
    @Override
    public Collection<ComponentInterface> findComponents(Block block)
    {
        return this.findComponents(block.getLocation());
    }
    
    @Override
    public SignInterface findSign(Block block)
    {
        return this.findSign(block.getLocation());
    }
    
    @Override
    public Collection<SignInterface> findSigns(Block block)
    {
        return this.findSigns(block.getLocation());
    }
    
    @Override
    public SignInterface findSign(Sign sign)
    {
        return this.findSign(sign.getLocation());
    }
    
    @Override
    public Collection<SignInterface> findSigns(Sign sign)
    {
        return this.findSigns(sign.getLocation());
    }
    
    @Override
    public McPlayerInterface getPlayer(Player player)
    {
        return this.players.getPlayer(player);
    }
    
    @Override
    public McPlayerInterface getPlayer(OfflinePlayer player)
    {
        return this.players.getPlayer(player);
    }
    
    @Override
    public McPlayerInterface getPlayer(UUID uuid)
    {
        return this.players.getPlayer(uuid);
    }
    
    @Override
    public ComponentTypeId getType(ComponentIdInterface id)
    {
        final ComponentId casted = (ComponentId) id;
        return this.componentTypesByPlugin.containsKey(casted.getPluginName()) ? this.componentTypesByPlugin.get(casted.getPluginName()).get(casted.getType()) : null;
    }
    
    @Override
    public EntityTypeId getType(EntityIdInterface id)
    {
        final EntityId casted = (EntityId) id;
        return this.entityTypesByPlugin.containsKey(casted.getPluginName()) ? this.entityTypesByPlugin.get(casted.getPluginName()).get(casted.getType()) : null;
    }
    
    @Override
    public ZoneTypeId getType(ZoneIdInterface id)
    {
        final ZoneId casted = (ZoneId) id;
        return this.zoneTypesByPlugin.containsKey(casted.getPluginName()) ? this.zoneTypesByPlugin.get(casted.getPluginName()).get(casted.getType()) : null;
    }
    
    @Override
    public SignTypeId getType(SignIdInterface id)
    {
        final SignId casted = (SignId) id;
        return this.signTypesByPlugin.containsKey(casted.getPluginName()) ? this.signTypesByPlugin.get(casted.getPluginName()).get(casted.getType()) : null;
    }
    
    @Override
    public ObjectTypeId getType(ObjectIdInterface id)
    {
        final ObjectId casted = (ObjectId) id;
        return this.objectTypesByPlugin.containsKey(casted.getPluginName()) ? this.objectTypesByPlugin.get(casted.getPluginName()).get(casted.getType()) : null;
    }
    
    @Override
    public ObjectInterface findObject(ObjectIdInterface id)
    {
        return this.objects.get((ObjectId) id);
    }
    
    @Override
    public Collection<ObjectInterface> findObjects(ObjectTypeId... type)
    {
        final Map<String, Set<String>> perPlugin = new HashMap<>();
        for (final ObjectTypeId typeid : type)
        {
            perPlugin.computeIfAbsent(typeid.getPluginName(), k -> new HashSet<>()).add(typeid.name());
        }
        
        final List<ObjectInterface> result = new ArrayList<>();
        perPlugin.forEach((plugin, ids) -> {
            this.objects.getByPlugin(plugin).stream().filter(id -> ids.contains(id.getType())).map(this.objects::get).forEach(result::add);
        });
        
        return result;
    }
    
    @Override
    public ObjectInterface createObject(ObjectTypeId type, ObjectHandlerInterface handler, boolean persist) throws McException
    {
        final Plugin plugin = this.safeGetPlugin(type.name(), type);
        final String pluginName = plugin.getName();
        if (!this.loadedPlugins.contains(pluginName))
        {
            throw new McException(CommonMessages.PluginNotLoaded, pluginName);
        }
        final ObjectHandlerInterface handler2 = handler == null ? safeCreateHandler(pluginName, type) : handler;
        final UUID uuid = UUID.randomUUID();
        final ObjectId id = new ObjectId(pluginName, type.name(), uuid);
        
        // init
        final ObjectImpl impl = new ObjectImpl(plugin, id, handler2, persist ? new File(this.objectsFolder, uuid.toString() + ".yml") : null, this); //$NON-NLS-1$
        final ObjectCreateEvent createEvent = new ObjectCreateEvent(impl);
        Bukkit.getPluginManager().callEvent(createEvent);
        if (createEvent.isCancelled())
        {
            throw new McException(createEvent.getVetoReason(), createEvent.getVetoReasonArgs());
        }
        
        this.runInContext(ObjectInterface.class, impl, () -> {
            handler2.onCreate(impl);
            
            // store data
            this.objects.put(pluginName, id, impl, persist);
            
            // save data
            if (persist)
            {
                this.objects.saveIdList(this.objectsFolder);
                impl.saveConfig();
            }
        });
        
        final ObjectCreatedEvent createdEvent = new ObjectCreatedEvent(impl);
        Bukkit.getPluginManager().callEvent(createdEvent);
        return impl;
    }
    
    /**
     * Runs given method in context and sets given value
     * 
     * @param clazz
     * @param value
     * @param run
     */
    private <T> void runInContext(Class<T> clazz, T value, McRunnable run)
    {
        try
        {
            McLibInterface.instance().runInCopiedContext(() -> {
                McLibInterface.instance().setContext(clazz, value);
                run.run();
            });
        }
        catch (McException ex)
        {
            // TODO logging
        }
    }
    
    @Override
    public VillagerBuilderInterface villager()
    {
        return new VillagerBuilder();
    }

    @Override
    public HumanBuilderInterface human()
    {
        return new HumanBuilder();
    }

    /**
     * Disable objects manager
     */
    public void disable()
    {
        for (final String pname : this.loadedPlugins)
        {
            final Plugin plugin = Bukkit.getPluginManager().getPlugin(pname);
            if (plugin != null)
            {
                this.onDisable(plugin);
            }
        }
    }
    
}
