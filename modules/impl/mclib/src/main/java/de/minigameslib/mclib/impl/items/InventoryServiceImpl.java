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

package de.minigameslib.mclib.impl.items;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.enums.EnumServiceInterface;
import de.minigameslib.mclib.api.enums.EnumerationListener;
import de.minigameslib.mclib.api.items.InventoryDescriptorInterface;
import de.minigameslib.mclib.api.items.InventoryId;
import de.minigameslib.mclib.api.items.InventoryServiceInterface;
import de.minigameslib.mclib.api.items.InventoryTypeId;
import de.minigameslib.mclib.api.locale.LocalizedMessage;
import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;
import de.minigameslib.mclib.api.locale.LocalizedMessages;
import de.minigameslib.mclib.api.locale.MessageComment;
import de.minigameslib.mclib.api.locale.MessageSeverityType;
import de.minigameslib.mclib.impl.comp.AbstractComponent;
import de.minigameslib.mclib.impl.comp.ComponentOwner;
import de.minigameslib.mclib.impl.comp.ComponentRegistry;
import de.minigameslib.mclib.impl.comp.WorldChunk;
import de.minigameslib.mclib.impl.yml.YmlFile;
import de.minigameslib.mclib.shared.api.com.LocationData;

/**
 * Implementation of inventory service.
 * 
 * @author mepeisen
 *
 */
public class InventoryServiceImpl implements InventoryServiceInterface, ComponentOwner, EnumerationListener<InventoryTypeId>
{
    
    /**
     * inventory ids by type.
     */
    final Map<InventoryTypeId, Set<InventoryId>>         inventoryByType        = new HashMap<>();
    
    /**
     * inventory ids by type and name.
     */
    final Map<InventoryTypeId, Map<String, InventoryId>> inventoryByTypeAndName = new HashMap<>();
    
    /**
     * inventories.
     */
    final Map<InventoryId, InventoryComponent>           inventories            = new HashMap<>();
    
    /** component registry for inventory objects. */
    final ComponentRegistry                              objects                = new ComponentRegistry();
    
    /** configuration folder. */
    File                                                 configFolder;
    
    /** main config file. */
    File                                                 configFile;
    
    /** the known inventories. */
    List<InventoryRegistryData>                          inventoryIds           = new ArrayList<>();
    
    /** logger. */
    static final Logger                                  LOGGER                 = Logger.getLogger(InventoryServiceImpl.class.getName());
    
    /**
     * Constructor.
     * 
     * @param configFolder the config folder to be used for inventory storage.
     */
    public InventoryServiceImpl(File configFolder)
    {
        this.configFolder = configFolder;
        if (!this.configFolder.exists())
        {
            this.configFolder.mkdirs();
        }
        this.configFile = new File(this.configFolder, "registry.yml"); //$NON-NLS-1$
        if (this.configFile.exists())
        {
            try
            {
                final YmlFile yml = new YmlFile(this.configFile);
                this.inventoryIds = yml.getFragmentList(InventoryRegistryData.class, "list"); //$NON-NLS-1$
            }
            catch (IOException e)
            {
                LOGGER.log(Level.WARNING, "Problems reading inventory list", e); //$NON-NLS-1$
            }
        }
        
        EnumServiceInterface.instance().registerEnumerationListener((Plugin) McLibInterface.instance(), InventoryTypeId.class, this);
    }
    
    @Override
    public List<InventoryId> getInventoryIds(InventoryTypeId... types)
    {
        final List<InventoryId> result = new ArrayList<>();
        if (types != null)
        {
            for (final InventoryTypeId type : types)
            {
                final Set<InventoryId> set = this.inventoryByType.get(type);
                if (set != null)
                {
                    result.addAll(set);
                }
            }
        }
        return result;
    }
    
    @Override
    public void deleteAllInventories(InventoryTypeId... types) throws McException
    {
        this.deleteInventories(this.getInventoryIds(types).toArray(new InventoryId[0]));
    }
    
    @Override
    public void deleteInventories(InventoryId... ids) throws McException
    {
        if (ids != null)
        {
            for (final InventoryId id : ids)
            {
                final InventoryComponent impl = this.inventories.remove(id);
                if (impl != null)
                {
                    impl.delete();
                    this.inventoryByType.get(impl.getTypeId()).remove(id);
                    this.inventoryByTypeAndName.get(impl.getTypeId()).remove(impl.getStringIdentified());
                }
            }
        }
    }
    
    @Override
    public InventoryId getInventory(InventoryTypeId type, String stringIdentifier)
    {
        final Map<String, InventoryId> map = this.inventoryByTypeAndName.get(type);
        if (map != null)
        {
            return map.get(stringIdentifier);
        }
        return null;
    }
    
    @Override
    public InventoryId getInventory(InventoryTypeId type, Location location)
    {
        final Optional<InventoryComponent> comp = this.objects.fetch(new WorldChunk(location)).stream().map(c -> (InventoryComponent) c).filter(i -> i.getLocations().contains(location))
            .filter(i -> i.getTypeId() == type).findFirst();
        return comp.isPresent() ? comp.get().getId() : null;
    }
    
    @Override
    public InventoryDescriptorInterface getInventory(InventoryId id)
    {
        return this.inventories.get(id);
    }
    
    @Override
    public InventoryId getOrCreateInventory(InventoryTypeId type, int initialSize, boolean fixed, boolean shared, String stringIdentifier) throws McException
    {
        final Map<String, InventoryId> map = this.inventoryByTypeAndName.computeIfAbsent(type, k -> new HashMap<>());
        if (!map.containsKey(stringIdentifier))
        {
            final InventoryIdImpl id = new InventoryIdImpl(UUID.randomUUID());
            final InventoryData data = new InventoryData();
            data.setFixed(fixed);
            data.setId(id);
            data.setIdentifier(stringIdentifier);
            data.setInitialSize(initialSize);
            data.setShared(shared);
            data.setTypeId(type);
            final InventoryComponent comp = new InventoryComponent(this.objects, new File(this.configFolder, "inv-" + id.getUuid() + ".yml"), this, data); //$NON-NLS-1$ //$NON-NLS-2$
            this.inventories.put(comp.getId(), comp);
            this.inventoryIds.add(new InventoryRegistryData(type.getPluginName(), id.getUuid().toString(), type.name()));
            this.inventoryByType.computeIfAbsent(type, t -> new HashSet<>()).add(id);
            this.inventoryByTypeAndName.computeIfAbsent(type, t -> new HashMap<>()).put(stringIdentifier, id);
            final YmlFile yml = new YmlFile();
            yml.setFragmentList("list", this.inventoryIds); //$NON-NLS-1$
            try
            {
                yml.saveFile(this.configFile);
            }
            catch (IOException e)
            {
                throw new McException(CommonMessages.InternalError, e, e.getMessage());
            }
            map.put(stringIdentifier, comp.getId());
            return comp.getId();
        }
        return map.get(stringIdentifier);
    }
    
    @Override
    public InventoryId getOrCreateInventory(InventoryTypeId type, int initialSize, boolean fixed, boolean shared, Location location) throws McException
    {
        InventoryId result = this.getInventory(type, location);
        if (result == null)
        {
            final InventoryIdImpl id = new InventoryIdImpl(UUID.randomUUID());
            final InventoryData data = new InventoryData();
            data.setFixed(fixed);
            data.setId(id);
            data.setInitialSize(initialSize);
            data.setShared(shared);
            data.setTypeId(type);
            data.getLocations().add(new LocationData(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), location.getWorld().getName()));
            final InventoryComponent comp = new InventoryComponent(this.objects, new File(this.configFolder, "inv-" + id.getUuid() + ".yml"), this, data); //$NON-NLS-1$ //$NON-NLS-2$
            this.inventories.put(comp.getId(), comp);
            this.inventoryIds.add(new InventoryRegistryData(type.getPluginName(), id.getUuid().toString(), type.name()));
            this.inventoryByType.computeIfAbsent(type, t -> new HashSet<>()).add(id);
            final YmlFile yml = new YmlFile();
            yml.setFragmentList("list", this.inventoryIds); //$NON-NLS-1$
            try
            {
                yml.saveFile(this.configFile);
            }
            catch (IOException e)
            {
                throw new McException(CommonMessages.InternalError, e, e.getMessage());
            }
            result = comp.getId();
        }
        return result;
    }
    
    @Override
    public void bindInventory(InventoryId id, Location... locations) throws McException
    {
        if (locations != null)
        {
            final InventoryComponent impl = this.inventories.get(id);
            if (impl != null)
            {
                for (final Location location : locations)
                {
                    impl.getData().getLocations().add(new LocationData(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), location.getWorld().getName()));
                }
                impl.saveData();
                impl.changeLocs();
            }
        }
    }
    
    @Override
    public void unbindInventory(InventoryId id, Location... locations) throws McException
    {
        if (locations != null)
        {
            final InventoryComponent impl = this.inventories.get(id);
            if (impl != null)
            {
                for (final Location location : locations)
                {
                    impl.getData().getLocations().remove(new LocationData(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), location.getWorld().getName()));
                }
                impl.saveData();
                impl.changeLocs();
            }
        }
    }
    
    /**
     * the inventory messages.
     */
    @LocalizedMessages(value = "inventory", defaultLocale = "en")
    public enum Messages implements LocalizedMessageInterface
    {
        
        /**
         * Not enough free slots.
         */
        @LocalizedMessage(defaultMessage = "Unable to shrink repository. Not enough free slots.", severity = MessageSeverityType.Error)
        @MessageComment(value = { "Not enough free slots" })
        NotEnoughFreeSlots,
        
        /**
         * Cannot change fixed inventory.
         */
        @LocalizedMessage(defaultMessage = "Cannot change size of fixed inventory", severity = MessageSeverityType.Error)
        @MessageComment(value = { "Cannot change fixed inventory" })
        CannotChangeFixedInventory,
        
    }
    
    @Override
    public void onEnumRegistered(Plugin plugin, Class<? extends InventoryTypeId> clazz, InventoryTypeId[] values)
    {
        new BukkitRunnable() {
            
            @Override
            public void run()
            {
                for (final InventoryTypeId type : values)
                {
                    InventoryServiceImpl.this.inventoryIds.stream().filter(r -> r.getPluginName().equals(type.getPluginName()) && r.getEnumName().equals(type.name())).forEach(r ->
                    {
                        try
                        {
                            final InventoryComponent comp = new InventoryComponent(
                                InventoryServiceImpl.this.objects,
                                new File(InventoryServiceImpl.this.configFolder, "inv-" + r.getUuid() + ".yml"), //$NON-NLS-1$ //$NON-NLS-2$
                                InventoryServiceImpl.this);
                            InventoryServiceImpl.this.inventories.put(comp.getId(), comp);
                            InventoryServiceImpl.this.inventoryByType.computeIfAbsent(type, t -> new HashSet<>()).add(comp.getId());
                            if (comp.getData().getIdentifier() != null)
                            {
                                InventoryServiceImpl.this.inventoryByTypeAndName.computeIfAbsent(type, t -> new HashMap<>()).put(comp.getData().getIdentifier(), comp.getId());
                            }
                        }
                        catch (McException e)
                        {
                            LOGGER.log(Level.WARNING, "Problems reading inventory", e); //$NON-NLS-1$
                        }
                    });
                }
            }
        }.runTaskLater(plugin, 1);
    }
    
    @Override
    public void onEnumRemoved(Plugin plugin, Class<? extends InventoryTypeId> clazz, InventoryTypeId[] values)
    {
        for (final InventoryTypeId type : values)
        {
            this.inventoryByTypeAndName.remove(type);
            final Set<InventoryId> invs = this.inventoryByType.remove(type);
            if (invs != null)
            {
                invs.forEach(this.inventories::remove);
            }
        }
    }
    
    @Override
    public void onDelete(AbstractComponent component) throws McException
    {
        final InventoryComponent inv = (InventoryComponent) component;
        final InventoryTypeId type = inv.getTypeId();
        this.inventoryIds.remove(new InventoryRegistryData(type.getPluginName(), ((InventoryIdImpl) inv.getId()).getUuid().toString(), type.name()));
        final YmlFile yml = new YmlFile();
        yml.setFragmentList("list", this.inventoryIds); //$NON-NLS-1$
        try
        {
            yml.saveFile(this.configFile);
        }
        catch (IOException e)
        {
            throw new McException(CommonMessages.InternalError, e, e.getMessage());
        }
    }
    
}
