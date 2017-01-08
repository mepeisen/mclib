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
import java.io.IOException;
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
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.enums.EnumServiceInterface;
import de.minigameslib.mclib.api.objects.ComponentHandlerInterface;
import de.minigameslib.mclib.api.objects.ComponentIdInterface;
import de.minigameslib.mclib.api.objects.ComponentInterface;
import de.minigameslib.mclib.api.objects.ComponentTypeId;
import de.minigameslib.mclib.api.objects.Cuboid;
import de.minigameslib.mclib.api.objects.EntityHandlerInterface;
import de.minigameslib.mclib.api.objects.EntityIdInterface;
import de.minigameslib.mclib.api.objects.EntityInterface;
import de.minigameslib.mclib.api.objects.EntityTypeId;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface.CuboidMode;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface.ResumeReport;
import de.minigameslib.mclib.api.objects.SignHandlerInterface;
import de.minigameslib.mclib.api.objects.SignIdInterface;
import de.minigameslib.mclib.api.objects.SignInterface;
import de.minigameslib.mclib.api.objects.SignTypeId;
import de.minigameslib.mclib.api.objects.ZoneHandlerInterface;
import de.minigameslib.mclib.api.objects.ZoneIdInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.api.objects.ZoneTypeId;
import de.minigameslib.mclib.impl.comp.AbstractComponent;
import de.minigameslib.mclib.impl.comp.ComponentId;
import de.minigameslib.mclib.impl.comp.ComponentImpl;
import de.minigameslib.mclib.impl.comp.ComponentOwner;
import de.minigameslib.mclib.impl.comp.ComponentRegistry;
import de.minigameslib.mclib.impl.comp.EntityId;
import de.minigameslib.mclib.impl.comp.EntityImpl;
import de.minigameslib.mclib.impl.comp.SignId;
import de.minigameslib.mclib.impl.comp.SignImpl;
import de.minigameslib.mclib.impl.comp.WorldChunk;
import de.minigameslib.mclib.impl.comp.ZoneId;
import de.minigameslib.mclib.impl.comp.ZoneImpl;
import de.minigameslib.mclib.impl.yml.YmlFile;
import de.minigameslib.mclib.shared.api.com.DataFragment;
import de.minigameslib.mclib.shared.api.com.DataSection;

/**
 * A helper class managing plugin objects.
 * 
 * @author mepeisen
 *
 */
class ObjectsManager implements ComponentOwner
{
    
    /** target data folder. */
    private final File                                                             dataFolder;
    
    /** the components. */
    private final Map<ComponentId, ComponentImpl>                                  components              = new HashMap<>();
    
    /** the entities. */
    private final Map<EntityId, EntityImpl>                                        entities                = new HashMap<>();
    
    /** the signs. */
    private final Map<SignId, SignImpl>                                            signs                   = new HashMap<>();
    
    /** the zones. */
    private final Map<ZoneId, ZoneImpl>                                            zones                   = new HashMap<>();
    
    /** registered components per plugin name. */
    private final Map<String, Map<ComponentId, Boolean>>                           componentsByPlugin      = new HashMap<>();
    
    /** registered entities per plugin name. */
    private final Map<String, Map<EntityId, Boolean>>                              entitiesByPlugin        = new HashMap<>();
    
    /** registered signs per plugin name. */
    private final Map<String, Map<SignId, Boolean>>                                signsByPlugin           = new HashMap<>();
    
    /** registered zones per plugin name. */
    private final Map<String, Map<ZoneId, Boolean>>                                zonesByPlugin           = new HashMap<>();
    
    /** component types per plugin name. */
    private final Map<String, Map<String, ComponentTypeId>>                        componentTypesByPlugin  = new HashMap<>();
    
    /** entity types per plugin name. */
    private final Map<String, Map<String, EntityTypeId>>                           entityTypesByPlugin     = new HashMap<>();
    
    /** sign types per plugin name. */
    private final Map<String, Map<String, SignTypeId>>                             signTypesByPlugin       = new HashMap<>();
    
    /** zone types per plugin name. */
    private final Map<String, Map<String, ZoneTypeId>>                             zoneTypesByPlugin       = new HashMap<>();
    
    /** registered handlers. */
    private final Map<ComponentTypeId, Class<? extends ComponentHandlerInterface>> componentHandlerClasses = new HashMap<>();
    
    /** registered handlers. */
    private final Map<EntityTypeId, Class<? extends EntityHandlerInterface>>       entityHandlerClasses    = new HashMap<>();
    
    /** registered handlers. */
    private final Map<SignTypeId, Class<? extends SignHandlerInterface>>           signHandlerClasses      = new HashMap<>();
    
    /** registered handlers. */
    private final Map<ZoneTypeId, Class<? extends ZoneHandlerInterface>>           zoneHandlerClasses      = new HashMap<>();
    
    /** the component registry handling location specific components (components, zones, signs) */
    private final ComponentRegistry                                                registry                = new ComponentRegistry();
    
    /** the components folder. */
    private final File                                                             componentsFolder;
    
    /** the signs folder. */
    private final File                                                             signsFolder;
    
    /** the entites folder. */
    private final File                                                             entitiesFolder;
    
    /** the zones folder. */
    private final File                                                             zonesFolder;
    
    /** the loaded plugin sets. To detect duplicate loading. */
    private final Set<String>                                                      loadedPlugins           = new HashSet<>();
    
    /**
     * Constructor.
     * 
     * @param dataFolder
     * @throws McException 
     */
    public ObjectsManager(File dataFolder) throws McException
    {
        this.dataFolder = dataFolder;
        this.componentsFolder = new File(this.dataFolder, "components"); //$NON-NLS-1$
        this.signsFolder = new File(this.dataFolder, "signs"); //$NON-NLS-1$
        this.entitiesFolder = new File(this.dataFolder, "entities"); //$NON-NLS-1$
        this.zonesFolder = new File(this.dataFolder, "zones"); //$NON-NLS-1$
        
        if (!this.componentsFolder.exists())
            this.componentsFolder.mkdirs();
        if (!this.signsFolder.exists())
            this.signsFolder.mkdirs();
        if (!this.entitiesFolder.exists())
            this.entitiesFolder.mkdirs();
        if (!this.zonesFolder.exists())
            this.zonesFolder.mkdirs();
        
        this.loadRegistry(this.componentsByPlugin, ComponentId::new, this.componentsFolder);
        this.loadRegistry(this.zonesByPlugin, ZoneId::new, this.zonesFolder);
        this.loadRegistry(this.entitiesByPlugin, EntityId::new, this.entitiesFolder);
        this.loadRegistry(this.signsByPlugin, SignId::new, this.signsFolder);
    }
    
    /**
     * @param map
     * @param factory
     * @param folder
     * @throws McException 
     */
    private <T extends DataFragment> void loadRegistry(Map<String, Map<T, Boolean>> map, Supplier<T> factory, File folder) throws McException
    {
        final File file = new File(folder, "registry.yml"); //$NON-NLS-1$
        if (file.exists())
        {
            try
            {
                final YmlFile config = new YmlFile(file);
                for (final String pluginName : config.getKeys(false))
                {
                    final Map<T, Boolean> idmap = map.computeIfAbsent(pluginName, k -> new HashMap<>());
                    final DataSection pluginSection = config.getSection(pluginName);
                    for (final String idkey : pluginSection.getKeys(false))
                    {
                        final DataSection section = pluginSection.getSection(idkey);
                        final T id = factory.get();
                        id.read(section);
                        idmap.put(id, Boolean.TRUE);
                    }
                }
            }
            catch (IOException ex)
            {
                throw new McException(CommonMessages.InternalError, ex, ex.getMessage());
            }
        }
    }

    /**
     * Registers a component handler.
     * 
     * @param type
     * @param handler
     * @throws McException
     */
    public <T extends ComponentHandlerInterface> void register(ComponentTypeId type, Class<T> handler) throws McException
    {
        this.componentTypesByPlugin.computeIfAbsent(safeGetPluginName(type.name(), type), k -> new HashMap<>()).put(type.name(), type);
        this.componentHandlerClasses.put(type, handler);
    }
    
    /**
     * Registers an entity handler.
     * 
     * @param type
     * @param handler
     * @throws McException
     */
    public <T extends EntityHandlerInterface> void register(EntityTypeId type, Class<T> handler) throws McException
    {
        this.entityTypesByPlugin.computeIfAbsent(safeGetPluginName(type.name(), type), k -> new HashMap<>()).put(type.name(), type);
        this.entityHandlerClasses.put(type, handler);
    }
    
    /**
     * Registers a sign handler.
     * 
     * @param type
     * @param handler
     * @throws McException
     */
    public <T extends SignHandlerInterface> void register(SignTypeId type, Class<T> handler) throws McException
    {
        this.signTypesByPlugin.computeIfAbsent(safeGetPluginName(type.name(), type), k -> new HashMap<>()).put(type.name(), type);
        this.signHandlerClasses.put(type, handler);
    }
    
    /**
     * Registers a zone handler.
     * 
     * @param type
     * @param handler
     * @throws McException
     */
    public <T extends ZoneHandlerInterface> void register(ZoneTypeId type, Class<T> handler) throws McException
    {
        this.zoneTypesByPlugin.computeIfAbsent(safeGetPluginName(type.name(), type), k -> new HashMap<>()).put(type.name(), type);
        this.zoneHandlerClasses.put(type, handler);
    }
    
    /**
     * Tries to resume objects.
     * 
     * @param plugin
     * @return report of resuming components.
     */
    public ResumeReport resumeObjects(Plugin plugin)
    {
        final String pluginName = plugin.getName();
        if (this.loadedPlugins.contains(pluginName))
        {
            final McException dupException = new McException(CommonMessages.PluginLoadedTwice, pluginName);
            final Set<ZoneIdInterface> brokenZones = this.zonesByPlugin.containsKey(pluginName) ? new HashSet<>(this.zonesByPlugin.get(pluginName).keySet()) : Collections.emptySet();
            final Set<ComponentIdInterface> brokenComponents = this.componentsByPlugin.containsKey(pluginName) ? new HashSet<>(this.componentsByPlugin.get(pluginName).keySet()) : Collections.emptySet();
            final Set<EntityIdInterface> brokenEntities = this.entitiesByPlugin.containsKey(pluginName) ? new HashSet<>(this.entitiesByPlugin.get(pluginName).keySet()) : Collections.emptySet();
            final Set<SignIdInterface> brokenSigns = this.signsByPlugin.containsKey(pluginName) ? new HashSet<>(this.signsByPlugin.get(pluginName).keySet()) : Collections.emptySet();
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
            };
        }
        
        this.loadedPlugins.add(pluginName);
        final Map<ZoneIdInterface, McException> brokenZones = new HashMap<>();
        final Map<SignIdInterface, McException> brokenSigns = new HashMap<>();
        final Map<EntityIdInterface, McException> brokenEntities = new HashMap<>();
        final Map<ComponentIdInterface, McException> brokenComponents = new HashMap<>();
        
        if (this.zonesByPlugin.containsKey(pluginName))
        {
            for (final ZoneId id : this.zonesByPlugin.get(pluginName).keySet())
            {
                if (!this.zoneTypesByPlugin.containsKey(pluginName))
                {
                    brokenZones.put(id, new McException(CommonMessages.BrokenObjectType, pluginName, id.getType(), "?")); //$NON-NLS-1$
                    continue;
                }
                
                final ZoneTypeId type = this.zoneTypesByPlugin.get(pluginName).get(id.getType());
                if (type == null)
                {
                    brokenZones.put(id, new McException(CommonMessages.BrokenObjectType, pluginName, id.getType(), "?")); //$NON-NLS-1$
                    continue;
                }
                
                try
                {
                    // init
                    final ZoneHandlerInterface handler = safeCreateHandler(pluginName, type);
                    final ZoneImpl impl = new ZoneImpl(this.registry, null, id, handler, new File(this.zonesFolder, id.getUuid().toString() + ".yml"), this); //$NON-NLS-1$
                    impl.readConfig();
                    handler.onResume(impl);
                    
                    // store data
                    this.zonesByPlugin.computeIfAbsent(pluginName, k -> new HashMap<>()).put(id, Boolean.TRUE);
                    this.zones.put(id, impl);
                }
                catch (McException ex)
                {
                    brokenZones.put(id, ex);
                }
            }
        }
        
        if (this.signsByPlugin.containsKey(pluginName))
        {
            for (final SignId id : this.signsByPlugin.get(pluginName).keySet())
            {
                if (!this.signTypesByPlugin.containsKey(pluginName))
                {
                    brokenSigns.put(id, new McException(CommonMessages.BrokenObjectType, pluginName, id.getType(), "?")); //$NON-NLS-1$
                    continue;
                }
                
                final SignTypeId type = this.signTypesByPlugin.get(pluginName).get(id.getType());
                if (type == null)
                {
                    brokenSigns.put(id, new McException(CommonMessages.BrokenObjectType, pluginName, id.getType(), "?")); //$NON-NLS-1$
                    continue;
                }
                
                try
                {
                    // init
                    final SignHandlerInterface handler = safeCreateHandler(pluginName, type);
                    final SignImpl impl = new SignImpl(this.registry, null, id, handler, new File(this.signsFolder, id.getUuid().toString() + ".yml"), this); //$NON-NLS-1$
                    impl.readConfig();
                    // research sign
                    final Block block = impl.getLocation().getBlock();
                    if (block instanceof Sign)
                    {
                        impl.setSign((Sign) block);
                        handler.onResume(impl);
                    
                        // store data
                        this.signsByPlugin.computeIfAbsent(pluginName, k -> new HashMap<>()).put(id, Boolean.TRUE);
                        this.signs.put(id, impl);
                    }
                    else
                    {
                        throw new McException(CommonMessages.SignNotFoundError);
                    }
                }
                catch (McException ex)
                {
                    brokenSigns.put(id, ex);
                }
            }
        }
        
        if (this.entitiesByPlugin.containsKey(pluginName))
        {
            // TODO entity support
        }
        
        if (this.componentsByPlugin.containsKey(pluginName))
        {
            for (final ComponentId id : this.componentsByPlugin.get(pluginName).keySet())
            {
                if (!this.componentTypesByPlugin.containsKey(pluginName))
                {
                    brokenComponents.put(id, new McException(CommonMessages.BrokenObjectType, pluginName, id.getType(), "?")); //$NON-NLS-1$
                    continue;
                }
                
                final ComponentTypeId type = this.componentTypesByPlugin.get(pluginName).get(id.getType());
                if (type == null)
                {
                    brokenComponents.put(id, new McException(CommonMessages.BrokenObjectType, pluginName, id.getType(), "?")); //$NON-NLS-1$
                    continue;
                }
                
                try
                {
                    // init
                    final ComponentHandlerInterface handler = safeCreateHandler(pluginName, type);
                    final ComponentImpl impl = new ComponentImpl(this.registry, null, id, handler, new File(this.componentsFolder, id.getUuid().toString() + ".yml"), this); //$NON-NLS-1$
                    impl.readConfig();
                    handler.onResume(impl);
                    
                    // store data
                    this.componentsByPlugin.computeIfAbsent(pluginName, k -> new HashMap<>()).put(id, Boolean.TRUE);
                    this.components.put(id, impl);
                }
                catch (McException ex)
                {
                    brokenComponents.put(id, ex);
                }
            }
        }
        
        return new ResumeReport() {
            
            @Override
            public boolean isOk()
            {
                return brokenComponents.isEmpty() && brokenEntities.isEmpty() && brokenSigns.isEmpty() && brokenZones.isEmpty();
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
            final Map<ComponentId, Boolean> cmap = this.componentsByPlugin.remove(pluginName);
            if (cmap != null)
            {
                cmap.forEach((id, persist) -> {
                    final ComponentImpl component = this.components.remove(id);
                    component.getHandler().onPause(component);
                });
            }
            final Map<EntityId, Boolean> emap = this.entitiesByPlugin.remove(pluginName);
            if (emap != null)
            {
                emap.forEach((id, persist) -> {
                    final EntityImpl entity = this.entities.remove(id);
                    entity.getHandler().onPause(entity);
                });
            }
            final Map<SignId, Boolean> smap = this.signsByPlugin.remove(pluginName);
            if (smap != null)
            {
                smap.forEach((id, persist) -> {
                    final SignImpl sign = this.signs.remove(id);
                    sign.getHandler().onPause(sign);
                });
            }
            final Map<ZoneId, Boolean> zmap = this.zonesByPlugin.remove(pluginName);
            if (zmap != null)
            {
                zmap.forEach((id, persist) -> {
                    final ZoneImpl zone = this.zones.remove(id);
                    zone.getHandler().onPause(zone);
                });
            }
            
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
    
    /**
     * @param location
     * @return component or {@code null} if it was not found.
     */
    public ComponentInterface findComponent(Location location)
    {
        final Optional<ComponentImpl> result = this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof ComponentImpl).map(c -> (ComponentImpl) c)
                .filter(c -> c.getLocation().equals(location)).findFirst();
        return result.isPresent() ? result.get() : null;
    }
    
    /**
     * @param id
     * @return component or {@code null} if it was not found.
     */
    public ComponentInterface findComponent(ComponentIdInterface id)
    {
        return this.components.get(id);
    }
    
    /**
     * @param type
     * @param location
     * @param handler
     * @param persist
     * @return new component
     * @throws McException
     */
    public ComponentInterface createComponent(ComponentTypeId type, Location location, ComponentHandlerInterface handler, boolean persist) throws McException
    {
        final String pluginName = this.safeGetPluginName(type.name(), type);
        if (!this.loadedPlugins.contains(pluginName))
        {
            throw new McException(CommonMessages.PluginLoadedTwice, pluginName);
        }
        final ComponentHandlerInterface handler2 = handler == null ? safeCreateHandler(pluginName, type) : handler;
        final UUID uuid = UUID.randomUUID();
        final ComponentId id = new ComponentId(pluginName, type.name(), uuid);
        
        // init
        final ComponentImpl impl = new ComponentImpl(this.registry, location, id, handler2, persist ? new File(this.componentsFolder, uuid.toString() + ".yml") : null, this); //$NON-NLS-1$
        handler2.onCreate(impl);
        
        // store data
        this.componentsByPlugin.computeIfAbsent(pluginName, k -> new HashMap<>()).put(id, Boolean.valueOf(persist));
        this.components.put(id, impl);
        
        // save data
        if (persist)
        {
            this.saveIdList(this.componentsFolder, this.componentsByPlugin);
            impl.saveConfig();
        }
        return impl;
    }
    
    /**
     * @param folder
     * @param map
     * @throws McException
     */
    private <T extends DataFragment> void saveIdList(File folder, Map<String, Map<T, Boolean>> map) throws McException
    {
        final YmlFile fileConfig = new YmlFile();
        map.forEach((k, v) -> {
            final DataSection section = fileConfig.createSection(k);
            int i = 0;
            for (final Map.Entry<T, Boolean> data : v.entrySet())
            {
                if (data.getValue())
                {
                    i++;
                    data.getKey().write(section.createSection("item" + i)); //$NON-NLS-1$
                }
            }
        });
        try
        {
            fileConfig.saveFile(new File(folder, "registry.yml")); //$NON-NLS-1$
        }
        catch (IOException ex)
        {
            throw new McException(CommonMessages.BrokenObjectType, ex);
        }
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
        if (!(enumValue instanceof Enum<?>))
        {
            throw new McException(CommonMessages.BrokenObjectTypeNotAnEnum, "?", name, enumValue.getClass().getName()); //$NON-NLS-1$
        }
        final Enum<?> value = (Enum<?>) enumValue;
        final Plugin plugin = EnumServiceInterface.instance().getPlugin(value);
        if (plugin == null)
        {
            throw new McException(CommonMessages.BrokenObjectTypeEnumNotRegistered, "?", name, enumValue.getClass().getName()); //$NON-NLS-1$
        }
        return plugin.getName();
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
    
    /**
     * @param entity
     * @return entity or {@code null} if it was not found
     */
    public EntityInterface findEntity(Entity entity)
    {
        // TODO entity support
        throw new UnsupportedOperationException("entities not yet supported"); //$NON-NLS-1$
    }
    
    /**
     * @param id
     * @return entity or {@code null} if it was not found
     */
    public EntityInterface findEntity(EntityIdInterface id)
    {
        return this.entities.get(id);
    }
    
    /**
     * @param type
     * @param entity
     * @param handler
     * @param persist
     * @return new entity
     * @throws McException
     */
    public EntityInterface createEntity(EntityTypeId type, Entity entity, EntityHandlerInterface handler, boolean persist) throws McException
    {
        // TODO entity support
        throw new UnsupportedOperationException("entities not yet supported"); //$NON-NLS-1$
    }
    
    /**
     * @param location
     * @return sign or {@code null} if it was not found
     */
    public SignInterface findSign(Location location)
    {
        final Optional<SignImpl> result = this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof SignImpl).map(c -> (SignImpl) c).filter(c -> c.getLocation().equals(location))
                .findFirst();
        return result.isPresent() ? result.get() : null;
    }
    
    /**
     * @param id
     * @return sign or {@code null} if it was not found
     */
    public SignInterface findSign(SignIdInterface id)
    {
        return this.signs.get(id);
    }
    
    /**
     * @param type
     * @param sign
     * @param handler
     * @param persist
     * @return new sign
     * @throws McException
     */
    public SignInterface createSign(SignTypeId type, Sign sign, SignHandlerInterface handler, boolean persist) throws McException
    {
        final String pluginName = this.safeGetPluginName(type.name(), type);
        if (!this.loadedPlugins.contains(pluginName))
        {
            throw new McException(CommonMessages.PluginLoadedTwice, pluginName);
        }
        final SignHandlerInterface handler2 = handler == null ? safeCreateHandler(pluginName, type) : handler;
        final UUID uuid = UUID.randomUUID();
        final SignId id = new SignId(pluginName, type.name(), uuid);
        
        // init
        final SignImpl impl = new SignImpl(this.registry, sign, id, handler2, persist ? new File(this.signsFolder, uuid.toString() + ".yml") : null, this); //$NON-NLS-1$
        handler2.onCreate(impl);
        
        // store data
        this.signsByPlugin.computeIfAbsent(pluginName, k -> new HashMap<>()).put(id, Boolean.valueOf(persist));
        this.signs.put(id, impl);
        
        // save data
        if (persist)
        {
            this.saveIdList(this.zonesFolder, this.zonesByPlugin);
            impl.saveConfig();
        }
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
     * @param type
     * @param cuboid
     * @param handler
     * @param persist
     * @return new zone
     * @throws McException
     */
    public ZoneInterface createZone(ZoneTypeId type, Cuboid cuboid, ZoneHandlerInterface handler, boolean persist) throws McException
    {
        final String pluginName = this.safeGetPluginName(type.name(), type);
        if (!this.loadedPlugins.contains(pluginName))
        {
            throw new McException(CommonMessages.PluginLoadedTwice, pluginName);
        }
        final ZoneHandlerInterface handler2 = handler == null ? safeCreateHandler(pluginName, type) : handler;
        final UUID uuid = UUID.randomUUID();
        final ZoneId id = new ZoneId(pluginName, type.name(), uuid);
        
        // init
        final ZoneImpl impl = new ZoneImpl(this.registry, cuboid, id, handler2, persist ? new File(this.zonesFolder, uuid.toString() + ".yml") : null, this); //$NON-NLS-1$
        handler2.onCreate(impl);
        
        // store data
        this.zonesByPlugin.computeIfAbsent(pluginName, k -> new HashMap<>()).put(id, Boolean.valueOf(persist));
        this.zones.put(id, impl);
        
        // save data
        if (persist)
        {
            this.saveIdList(this.zonesFolder, this.zonesByPlugin);
            impl.saveConfig();
        }
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
     * @param id
     * @return zone or {@code null} if it was not found
     */
    public ZoneInterface findZone(ZoneIdInterface id)
    {
        return this.zones.get(id);
    }
    
    /**
     * @param location
     * @return zone or {@code null} if it was not found
     */
    public Collection<ZoneInterface> findZonesWithoutYD(Location location)
    {
        return this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c).filter(c -> c.getCuboid().containsLocWithoutYD(location))
                .collect(Collectors.toList());
    }
    
    /**
     * @param location
     * @return zone or {@code null} if it was not found
     */
    public ZoneInterface findZoneWithoutYD(Location location)
    {
        final Optional<ZoneImpl> result = this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c)
                .filter(c -> c.getCuboid().containsLocWithoutYD(location)).findFirst();
        return result.isPresent() ? result.get() : null;
    }
    
    /**
     * @param location
     * @return zone or {@code null} if it was not found
     */
    public Collection<ZoneInterface> findZonesWithoutY(Location location)
    {
        return this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c).filter(c -> c.getCuboid().containsLocWithoutY(location))
                .collect(Collectors.toList());
    }
    
    /**
     * @param location
     * @return zone or {@code null} if it was not found
     */
    public ZoneInterface findZoneWithoutY(Location location)
    {
        final Optional<ZoneImpl> result = this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c)
                .filter(c -> c.getCuboid().containsLocWithoutY(location)).findFirst();
        return result.isPresent() ? result.get() : null;
    }
    
    /**
     * @param location
     * @return zone or {@code null} if it was not found
     */
    public Collection<ZoneInterface> findZones(Location location)
    {
        return this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c).filter(c -> c.getCuboid().containsLoc(location))
                .collect(Collectors.toList());
    }
    
    /**
     * @param location
     * @return zone or {@code null} if it was not found
     */
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
        // TODO entity support
//        else if (component instanceof EntityImpl)
//        {
//            this.onDelete((EntityImpl) component);
//        }
    }
    
    /**
     * Deletes a component
     * @param component
     * @throws McException 
     */
    private void onDelete(ComponentImpl component) throws McException
    {
        final ComponentId id = (ComponentId) component.getComponentId();
        final Boolean persistent = this.componentsByPlugin.get(id.getPluginName()).remove(id);
        this.components.remove(id);
        if (persistent)
        {
            this.saveIdList(this.componentsFolder, this.componentsByPlugin);
        }
    }
    
    /**
     * Deletes a zone
     * @param zone
     * @throws McException 
     */
    private void onDelete(ZoneImpl zone) throws McException
    {
        final ZoneId id = zone.getZoneId();
        final Boolean persistent = this.zonesByPlugin.get(id.getPluginName()).remove(id);
        this.zones.remove(id);
        if (persistent)
        {
            this.saveIdList(this.zonesFolder, this.zonesByPlugin);
        }
    }
    
    /**
     * Deletes a sign
     * @param sign
     * @throws McException 
     */
    private void onDelete(SignImpl sign) throws McException
    {
        final SignId id = (SignId) sign.getSignId();
        final Boolean persistent = this.signsByPlugin.get(id.getPluginName()).remove(id);
        this.signs.remove(id);
        if (persistent)
        {
            this.saveIdList(this.signsFolder, this.signsByPlugin);
        }
    }

    /**
     * @param location
     * @return components list
     */
    public Collection<ComponentInterface> findComponents(Location location)
    {
        return this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof ComponentImpl).map(c -> (ComponentImpl) c)
                .filter(c -> c.getLocation().equals(location)).collect(Collectors.toList());
    }

    /**
     * @param type
     * @return components list
     */
    public Collection<ComponentInterface> findComponents(ComponentTypeId[] type)
    {
        final Map<String, Set<String>> perPlugin = new HashMap<>();
        for (final ComponentTypeId typeid : type)
        {
            perPlugin.computeIfAbsent(EnumServiceInterface.instance().getPlugin((Enum<?>) typeid).getName(), k -> new HashSet<>()).add(typeid.name());
        }
        
        final List<ComponentInterface> result = new ArrayList<>();
        perPlugin.forEach((plugin, ids) -> {
            final Map<ComponentId, Boolean> map = this.componentsByPlugin.get(plugin);
            if (map != null) {
                map.keySet().stream().filter(id -> ids.contains(id.getType())).map(this.components::get).forEach(result::add);
            }
        });
        
        return result;
    }

    /**
     * @param entity
     * @return entities
     */
    public Collection<EntityInterface> findEntities(Entity entity)
    {
        // TODO entity support
        throw new UnsupportedOperationException("entities not yet supported"); //$NON-NLS-1$
    }

    /**
     * @param type
     * @return entities
     */
    public Collection<EntityInterface> findEntities(EntityTypeId[] type)
    {
        // TODO entity support
        throw new UnsupportedOperationException("entities not yet supported"); //$NON-NLS-1$
    }

    /**
     * @param location
     * @return collection
     */
    public Collection<SignInterface> findSigns(Location location)
    {
        return this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof SignImpl).map(c -> (SignImpl) c)
                .filter(c -> c.getLocation().equals(location)).collect(Collectors.toList());
    }

    /**
     * @param type
     * @return collection
     */
    public Collection<SignInterface> findSigns(SignTypeId[] type)
    {
        final Map<String, Set<String>> perPlugin = new HashMap<>();
        for (final SignTypeId typeid : type)
        {
            perPlugin.computeIfAbsent(EnumServiceInterface.instance().getPlugin((Enum<?>) typeid).getName(), k -> new HashSet<>()).add(typeid.name());
        }
        
        final List<SignInterface> result = new ArrayList<>();
        perPlugin.forEach((plugin, ids) -> {
            final Map<SignId, Boolean> map = this.signsByPlugin.get(plugin);
            if (map != null) {
                map.keySet().stream().filter(id -> ids.contains(id.getType())).map(this.signs::get).forEach(result::add);
            }
        });
        
        return result;
    }
    
    /**
     * Returns a stream for all chunks in given cuboid
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

    /**
     * @param cuboid
     * @param mode
     * @return collection
     */
    public ZoneInterface findZone(Cuboid cuboid, CuboidMode mode)
    {
        final BiPredicate<ZoneImpl, Cuboid> tester = getTester(mode);
        final Optional<ZoneImpl> result = this.fetchForCuboid(cuboid).filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c)
                .filter(z -> tester.test(z, cuboid)).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    /**
     * @param cuboid
     * @param mode
     * @param type
     * @return collection
     */
    public ZoneInterface findZone(Cuboid cuboid, CuboidMode mode, ZoneTypeId[] type)
    {
        final Map<String, Set<String>> perPlugin = new HashMap<>();
        for (final ZoneTypeId typeid : type)
        {
            perPlugin.computeIfAbsent(EnumServiceInterface.instance().getPlugin((Enum<?>) typeid).getName(), k -> new HashSet<>()).add(typeid.name());
        }
        final BiPredicate<ZoneImpl, Cuboid> tester = getTester(mode);
        final Optional<ZoneImpl> result = this.fetchForCuboid(cuboid).filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c)
                .filter(z -> perPlugin.containsKey(z.getZoneId().getPluginName()) && perPlugin.get(z.getZoneId().getPluginName()).contains(z.getZoneId().getType()))
                .filter(z -> tester.test(z, cuboid)).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    /**
     * @param location
     * @param type
     * @return collection
     */
    public ZoneInterface findZone(Location location, ZoneTypeId[] type)
    {
        final Map<String, Set<String>> perPlugin = new HashMap<>();
        for (final ZoneTypeId typeid : type)
        {
            perPlugin.computeIfAbsent(EnumServiceInterface.instance().getPlugin((Enum<?>) typeid).getName(), k -> new HashSet<>()).add(typeid.name());
        }
        final Optional<ZoneImpl> result = this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c)
                .filter(z -> perPlugin.containsKey(z.getZoneId().getPluginName()) && perPlugin.get(z.getZoneId().getPluginName()).contains(z.getZoneId().getType()))
                .filter(c -> c.getCuboid().containsLoc(location)).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    /**
     * @param cuboid
     * @param mode
     * @return collection
     */
    public Collection<ZoneInterface> findZones(Cuboid cuboid, CuboidMode mode)
    {
        final BiPredicate<ZoneImpl, Cuboid> tester = getTester(mode);
        return this.fetchForCuboid(cuboid).filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c)
                .filter(z -> tester.test(z, cuboid)).collect(Collectors.toList());
    }

    /**
     * @param cuboid
     * @param mode
     * @param type
     * @return collection
     */
    public Collection<ZoneInterface> findZones(Cuboid cuboid, CuboidMode mode, ZoneTypeId[] type)
    {
        final Map<String, Set<String>> perPlugin = new HashMap<>();
        for (final ZoneTypeId typeid : type)
        {
            perPlugin.computeIfAbsent(EnumServiceInterface.instance().getPlugin((Enum<?>) typeid).getName(), k -> new HashSet<>()).add(typeid.name());
        }
        final BiPredicate<ZoneImpl, Cuboid> tester = getTester(mode);
        return this.fetchForCuboid(cuboid).filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c)
                .filter(z -> perPlugin.containsKey(z.getZoneId().getPluginName()) && perPlugin.get(z.getZoneId().getPluginName()).contains(z.getZoneId().getType()))
                .filter(z -> tester.test(z, cuboid)).collect(Collectors.toList());
    }

    /**
     * @param location
     * @param type
     * @return collection
     */
    public Collection<ZoneInterface> findZones(Location location, ZoneTypeId[] type)
    {
        final Map<String, Set<String>> perPlugin = new HashMap<>();
        for (final ZoneTypeId typeid : type)
        {
            perPlugin.computeIfAbsent(EnumServiceInterface.instance().getPlugin((Enum<?>) typeid).getName(), k -> new HashSet<>()).add(typeid.name());
        }
        return this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c)
                .filter(z -> perPlugin.containsKey(z.getZoneId().getPluginName()) && perPlugin.get(z.getZoneId().getPluginName()).contains(z.getZoneId().getType()))
                .filter(c -> c.getCuboid().containsLoc(location)).collect(Collectors.toList());
    }

    /**
     * @param location
     * @param type
     * @return zone
     */
    public ZoneInterface findZoneWithoutY(Location location, ZoneTypeId[] type)
    {
        final Map<String, Set<String>> perPlugin = new HashMap<>();
        for (final ZoneTypeId typeid : type)
        {
            perPlugin.computeIfAbsent(EnumServiceInterface.instance().getPlugin((Enum<?>) typeid).getName(), k -> new HashSet<>()).add(typeid.name());
        }
        final Optional<ZoneImpl> result = this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c)
                .filter(z -> perPlugin.containsKey(z.getZoneId().getPluginName()) && perPlugin.get(z.getZoneId().getPluginName()).contains(z.getZoneId().getType()))
                .filter(c -> c.getCuboid().containsLocWithoutY(location)).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    /**
     * @param location
     * @param type
     * @return collection
     */
    public Collection<ZoneInterface> findZonesWithoutY(Location location, ZoneTypeId[] type)
    {
        final Map<String, Set<String>> perPlugin = new HashMap<>();
        for (final ZoneTypeId typeid : type)
        {
            perPlugin.computeIfAbsent(EnumServiceInterface.instance().getPlugin((Enum<?>) typeid).getName(), k -> new HashSet<>()).add(typeid.name());
        }
        return this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c)
                .filter(z -> perPlugin.containsKey(z.getZoneId().getPluginName()) && perPlugin.get(z.getZoneId().getPluginName()).contains(z.getZoneId().getType()))
                .filter(c -> c.getCuboid().containsLocWithoutY(location)).collect(Collectors.toList());
    }

    /**
     * @param location
     * @param type
     * @return zone
     */
    public ZoneInterface findZoneWithoutYD(Location location, ZoneTypeId[] type)
    {
        final Map<String, Set<String>> perPlugin = new HashMap<>();
        for (final ZoneTypeId typeid : type)
        {
            perPlugin.computeIfAbsent(EnumServiceInterface.instance().getPlugin((Enum<?>) typeid).getName(), k -> new HashSet<>()).add(typeid.name());
        }
        final Optional<ZoneImpl> result = this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c)
                .filter(z -> perPlugin.containsKey(z.getZoneId().getPluginName()) && perPlugin.get(z.getZoneId().getPluginName()).contains(z.getZoneId().getType()))
                .filter(c -> c.getCuboid().containsLocWithoutYD(location)).findFirst();
        return result.isPresent() ? result.get() : null;
    }

    /**
     * @param location
     * @param type
     * @return collection
     */
    public Collection<ZoneInterface> findZonesWithoutYD(Location location, ZoneTypeId[] type)
    {
        final Map<String, Set<String>> perPlugin = new HashMap<>();
        for (final ZoneTypeId typeid : type)
        {
            perPlugin.computeIfAbsent(EnumServiceInterface.instance().getPlugin((Enum<?>) typeid).getName(), k -> new HashSet<>()).add(typeid.name());
        }
        return this.registry.fetch(new WorldChunk(location)).stream().filter(c -> c instanceof ZoneImpl).map(c -> (ZoneImpl) c)
                .filter(z -> perPlugin.containsKey(z.getZoneId().getPluginName()) && perPlugin.get(z.getZoneId().getPluginName()).contains(z.getZoneId().getType()))
                .filter(c -> c.getCuboid().containsLocWithoutYD(location)).collect(Collectors.toList());
    }

    /**
     * @param type
     * @return collection
     */
    public Collection<ZoneInterface> findZones(ZoneTypeId[] type)
    {
        final Map<String, Set<String>> perPlugin = new HashMap<>();
        for (final ZoneTypeId typeid : type)
        {
            perPlugin.computeIfAbsent(EnumServiceInterface.instance().getPlugin((Enum<?>) typeid).getName(), k -> new HashSet<>()).add(typeid.name());
        }
        
        final List<ZoneInterface> result = new ArrayList<>();
        perPlugin.forEach((plugin, ids) -> {
            final Map<ZoneId, Boolean> map = this.zonesByPlugin.get(plugin);
            if (map != null) {
                map.keySet().stream().filter(id -> ids.contains(id.getType())).map(this.zones::get).forEach(result::add);
            }
        });
        
        return result;
    }
    
}
