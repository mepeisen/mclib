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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.CommonMessages;
import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.config.ConfigItemStackData;
import de.minigameslib.mclib.api.items.InventoryDescriptorInterface;
import de.minigameslib.mclib.api.items.InventoryId;
import de.minigameslib.mclib.api.items.InventoryTypeId;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.impl.comp.AbstractComponent;
import de.minigameslib.mclib.impl.comp.ComponentOwner;
import de.minigameslib.mclib.impl.comp.ComponentRegistry;
import de.minigameslib.mclib.impl.comp.WorldChunk;
import de.minigameslib.mclib.impl.gui.inv.InventoryGui;
import de.minigameslib.mclib.impl.yml.YmlFile;
import de.minigameslib.mclib.nms.api.CraftInventoryWrapper;
import de.minigameslib.mclib.shared.api.com.LocationDataFragment;

/**
 * Implementation of inventory object.
 * 
 * @author mepeisen
 *
 */
public class InventoryComponent extends AbstractComponent implements InventoryDescriptorInterface
{
    
    /** the inventory data. */
    private InventoryData            data;
    
    /**
     * the shared inventory.
     */
    private InventoryImpl            shared;
    
    /** Logger. */
    static final Logger              LOGGER            = Logger.getLogger(InventoryComponent.class.getName());
    
    /**
     * player inventories.
     */
    private Map<UUID, InventoryImpl> playerInventories = new HashMap<>();
    
    /**
     * Constructor for new inventories.
     * 
     * @param registry
     *            component registry used for location binds
     * @param config
     *            configuration file for this inventory
     * @param owner
     *            component owner
     * @param data
     *            inventory data
     * @throws McException
     *             thrown for io errors writing the initial config
     */
    public InventoryComponent(ComponentRegistry registry, File config, ComponentOwner owner, InventoryData data) throws McException
    {
        super(registry, config, owner);
        this.data = data;
        this.changeLocs();
        this.saveData();
    }
    
    /**
     * Constructor for existing inventories.
     * 
     * @param registry
     *            component registry used for location binds
     * @param config
     *            configuration file for this inventory
     * @param owner
     *            component owner
     * @throws McException
     *             thrown for io errors reading the config
     */
    public InventoryComponent(ComponentRegistry registry, File config, ComponentOwner owner) throws McException
    {
        super(registry, config, owner);
        this.data = this.config.getFragment(InventoryData.class, "data"); //$NON-NLS-1$
        this.changeLocs();
    }
    
    /**
     * Saves the config data.
     * 
     * @throws McException
     *             wraps io errors
     */
    void saveData() throws McException
    {
        this.config.set("data", this.data); //$NON-NLS-1$
        try
        {
            this.config.saveFile(this.configFile);
        }
        catch (IOException e)
        {
            LOGGER.log(Level.WARNING, "Problems saving inventory", e); //$NON-NLS-1$
            throw new McException(CommonMessages.InternalError, e, e.getMessage());
        }
    }
    
    /**
     * Changes the locations depending on the given cuboid.
     */
    void changeLocs()
    {
        final Set<WorldChunk> chunks = new HashSet<>();
        for (final LocationDataFragment loc : this.data.getLocations())
        {
            chunks.add(new WorldChunk(new Location(Bukkit.getWorld(loc.getWorld()), loc.getX(), loc.getY(), loc.getZ())));
        }
        this.setWorldChunks(chunks);
    }
    
    /**
     * Returns the inventory data.
     * 
     * @return the data
     */
    public InventoryData getData()
    {
        return this.data;
    }
    
    @Override
    public InventoryTypeId getTypeId()
    {
        return this.data.getTypeId();
    }
    
    @Override
    public InventoryId getId()
    {
        return this.data.getId();
    }
    
    @Override
    public String getStringIdentified()
    {
        return this.data.getIdentifier();
    }
    
    @Override
    public List<Location> getLocations()
    {
        return this.data.getLocations().stream().map(loc -> new Location(Bukkit.getWorld(loc.getWorld()), loc.getX(), loc.getY(), loc.getZ())).collect(Collectors.toList());
    }
    
    @Override
    public boolean isShared()
    {
        return this.data.isShared();
    }
    
    @Override
    public boolean isFixed()
    {
        return this.data.isFixed();
    }
    
    @Override
    public List<McPlayerInterface> getPlayers()
    {
        if (this.isShared())
        {
            return Collections.emptyList();
        }
        final ObjectServiceInterface osi = ObjectServiceInterface.instance();
        return this.data.getPlayers().stream().map(osi::getPlayer).collect(Collectors.toList());
    }
    
    @Override
    public Inventory getInventory(McPlayerInterface player) throws McException
    {
        if (this.isShared())
        {
            return this.getSharedInventory();
        }
        
        final UUID uuid = player.getPlayerUUID();
        
        // return if already loaded
        if (this.playerInventories.containsKey(uuid))
        {
            return this.playerInventories.get(uuid);
        }
        
        // return null if not known
        final File file = new File(this.configFile.getParentFile(), this.getId() + "-player-" + uuid + " .yml"); //$NON-NLS-1$ //$NON-NLS-2$
        if (!file.exists())
        {
            return null;
        }
        
        // create existing inventory
        final InventoryContentData content = this.getContent(file);
        final InventoryImpl result = new InventoryImpl(content, file, () -> this.playerInventories.remove(uuid));
        this.playerInventories.put(uuid, result);
        return result;
    }
    
    /**
     * Returns the shared inventory.
     * 
     * @return shared inventory
     * @throws McException
     *             thrown for problems reading the content.
     */
    private InventoryImpl getSharedInventory() throws McException
    {
        if (this.shared == null)
        {
            final File file = new File(this.configFile.getParentFile(), this.getId() + "-shared.yml"); //$NON-NLS-1$
            final InventoryContentData content = this.getContent(file);
            this.shared = new InventoryImpl(content, file, () -> this.shared = null);
        }
        return this.shared;
    }
    
    /**
     * Gets content from file.
     * 
     * @param file
     *            data file
     * @return content from file
     * @throws McException
     *             wraps io exceptions
     */
    private InventoryContentData getContent(File file) throws McException
    {
        if (file.exists())
        {
            try
            {
                return new YmlFile(file).getFragment(InventoryContentData.class, "data"); //$NON-NLS-1$
            }
            catch (IOException e)
            {
                LOGGER.log(Level.WARNING, "Problems reading inventory", e); //$NON-NLS-1$
                throw new McException(CommonMessages.InternalError, e, e.getMessage());
            }
        }
        final InventoryContentData result = new InventoryContentData(this.data.getInitialSize(), 64, "Chest"); //$NON-NLS-1$
        return result;
    }
    
    @Override
    public Inventory getOrCreateInventory(McPlayerInterface player) throws McException
    {
        if (this.isShared())
        {
            return this.getSharedInventory();
        }
        
        final UUID uuid = player.getPlayerUUID();
        return getOrCreate(uuid);
    }
    
    /**
     * Returns content from file.
     * 
     * @param uuid
     *            unique id of player
     * @return inventory impl
     * @throws McException
     *             wrapped io exception
     */
    private InventoryImpl getOrCreate(final UUID uuid) throws McException
    {
        InventoryImpl result = this.playerInventories.get(uuid);
        if (result == null)
        {
            final File file = new File(this.configFile.getParentFile(), this.getId() + "-player-" + uuid + " .yml"); //$NON-NLS-1$ //$NON-NLS-2$
            if (!file.exists())
            {
                this.data.getPlayers().add(uuid);
                this.saveData();
            }
            final InventoryContentData content = this.getContent(file);
            result = new InventoryImpl(content, file, () -> this.playerInventories.remove(uuid));
            this.playerInventories.put(uuid, result);
        }
        return result;
    }
    
    @Override
    public void deleteInventory(McPlayerInterface player) throws McException
    {
        if (this.isShared())
        {
            if (this.shared != null)
            {
                this.shared.getViewers().forEach(HumanEntity::closeInventory);
            }
            final File file = new File(this.configFile.getParentFile(), this.getId() + "-shared.yml"); //$NON-NLS-1$
            if (!file.delete())
            {
                LOGGER.log(Level.WARNING, "file deletion of " + file + " failed"); //$NON-NLS-1$ //$NON-NLS-2$
                throw new McException(CommonMessages.InternalError, "file deletion of " + file + " failed"); //$NON-NLS-1$ //$NON-NLS-2$
            }
            this.shared = null;
        }
        else
        {
            final UUID uuid = player.getPlayerUUID();
            if (this.playerInventories.containsKey(uuid))
            {
                this.playerInventories.get(uuid).getViewers().forEach(HumanEntity::closeInventory);
            }
            final File file = new File(this.configFile.getParentFile(), this.getId() + "-player-" + uuid + " .yml"); //$NON-NLS-1$ //$NON-NLS-2$
            if (!file.delete())
            {
                LOGGER.log(Level.WARNING, "file deletion of " + file + " failed"); //$NON-NLS-1$ //$NON-NLS-2$
                throw new McException(CommonMessages.InternalError, "file deletion of " + file + " failed"); //$NON-NLS-1$ //$NON-NLS-2$
            }
            this.data.getPlayers().remove(uuid);
            this.saveData();
        }
    }
    
    @Override
    public void shrinkInventory(int slotAmount) throws McException
    {
        if (this.isFixed())
        {
            throw new McException(InventoryServiceImpl.Messages.CannotChangeFixedInventory);
        }
        final ObjectServiceInterface osi = ObjectServiceInterface.instance();
        
        if (this.isShared())
        {
            final InventoryImpl sharedInventory = this.getSharedInventory();
            sharedInventory.shrink(slotAmount);
            // reopen inventory
            reopenInventory(osi, sharedInventory);
        }
        else
        {
            for (UUID uuid : this.data.getPlayers())
            {
                final InventoryImpl i = this.getOrCreate(uuid);
                i.shrink(slotAmount);
                this.reopenInventory(osi, i);
            }
        }
    }
    
    @Override
    public void shrinkInventory(McPlayerInterface player, int slotAmount) throws McException
    {
        if (this.isFixed())
        {
            throw new McException(InventoryServiceImpl.Messages.CannotChangeFixedInventory);
        }
        final ObjectServiceInterface osi = ObjectServiceInterface.instance();
        
        if (this.isShared())
        {
            final InventoryImpl sharedInventory = this.getSharedInventory();
            sharedInventory.shrink(slotAmount);
            // reopen inventory
            reopenInventory(osi, sharedInventory);
        }
        else
        {
            final UUID uuid = player.getPlayerUUID();
            final InventoryImpl inv = this.getOrCreate(uuid);
            inv.shrink(slotAmount);
            // reopen inventory
            reopenInventory(osi, inv);
        }
    }
    
    @Override
    public void setInventorySize(int slots) throws McException
    {
        if (this.isFixed())
        {
            throw new McException(InventoryServiceImpl.Messages.CannotChangeFixedInventory);
        }
        final ObjectServiceInterface osi = ObjectServiceInterface.instance();
        
        if (this.isShared())
        {
            final InventoryImpl sharedInventory = this.getSharedInventory();
            sharedInventory.setSize(slots);
            // reopen inventory
            reopenInventory(osi, sharedInventory);
        }
        else
        {
            for (UUID uuid : this.data.getPlayers())
            {
                final InventoryImpl i = this.getOrCreate(uuid);
                i.setSize(slots);
                this.reopenInventory(osi, i);
            }
        }
    }
    
    @Override
    public void setInventorySize(McPlayerInterface player, int slots) throws McException
    {
        if (this.isFixed())
        {
            throw new McException(InventoryServiceImpl.Messages.CannotChangeFixedInventory);
        }
        final ObjectServiceInterface osi = ObjectServiceInterface.instance();
        
        if (this.isShared())
        {
            final InventoryImpl sharedInventory = this.getSharedInventory();
            sharedInventory.setSize(slots);
            // reopen inventory
            reopenInventory(osi, sharedInventory);
        }
        else
        {
            final UUID uuid = player.getPlayerUUID();
            final InventoryImpl inv = this.getOrCreate(uuid);
            inv.shrink(slots);
            // reopen inventory
            reopenInventory(osi, inv);
        }
    }
    
    @Override
    public void growInventory(McPlayerInterface player, int slotAmount) throws McException
    {
        if (this.isFixed())
        {
            throw new McException(InventoryServiceImpl.Messages.CannotChangeFixedInventory);
        }
        final ObjectServiceInterface osi = ObjectServiceInterface.instance();
        
        if (this.isShared())
        {
            final InventoryImpl sharedInventory = this.getSharedInventory();
            sharedInventory.grow(slotAmount);
            // reopen inventory
            reopenInventory(osi, sharedInventory);
        }
        else
        {
            final UUID uuid = player.getPlayerUUID();
            final InventoryImpl inv = this.getOrCreate(uuid);
            inv.grow(slotAmount);
            // reopen inventory
            reopenInventory(osi, inv);
        }
    }
    
    @Override
    public void growInventory(int slotAmount) throws McException
    {
        if (this.isFixed())
        {
            throw new McException(InventoryServiceImpl.Messages.CannotChangeFixedInventory);
        }
        final ObjectServiceInterface osi = ObjectServiceInterface.instance();
        
        if (this.isShared())
        {
            final InventoryImpl sharedInventory = this.getSharedInventory();
            sharedInventory.grow(slotAmount);
            // reopen inventory
            reopenInventory(osi, sharedInventory);
        }
        else
        {
            for (UUID uuid : this.data.getPlayers())
            {
                final InventoryImpl i = this.getOrCreate(uuid);
                i.grow(slotAmount);
                // reopen inventory
                reopenInventory(osi, i);
            }
        }
    }
    
    /**
     * Re-open an inventory after changing the inventory size.
     * 
     * @param osi
     *            object service interface
     * @param inventory
     *            the inventory to reopen.
     */
    private void reopenInventory(final ObjectServiceInterface osi, final InventoryImpl inventory)
    {
        inventory.getViewers().stream().forEach(v ->
        {
            v.closeInventory();
            try
            {
                this.openInventory(osi.getPlayer(v.getUniqueId()));
            }
            catch (McException e)
            {
                LOGGER.log(Level.WARNING, "problems reopening inventory", e); //$NON-NLS-1$
            }
        });
    }
    
    @Override
    public void openInventory(McPlayerInterface player) throws McException
    {
        final Inventory inventory = this.getOrCreateInventory(player);
        if (inventory.getSize() > 9 * 6)
        {
            player.openClickGui(new InventoryGui(inventory));
        }
        else
        {
            player.getBukkitPlayer().openInventory(inventory);
        }
    }
    
    /**
     * Implementation of local inventories.
     */
    private class InventoryImpl extends CraftInventoryWrapper implements InventoryListener
    {
        
        /**
         * Inventory content data.
         */
        private InventoryContentData invcontent;
        
        /**
         * File for inventory data.
         */
        private File                 invconfig;
        
        /**
         * On inventory close action.
         */
        private Runnable             onClose;
        
        /**
         * Inventory implementation (inventory content).
         * 
         * @param data
         *            The persistent data
         * @param config
         *            the config file
         * @param onClose
         *            the runnable on closing this inventory
         */
        public InventoryImpl(InventoryContentData data, File config, Runnable onClose)
        {
            super(data.getSlotSize(), data.getMaxStackSize(), data.getSlots().stream().map(ConfigItemStackData::toBukkit).toArray(ItemStack[]::new));
            this.invcontent = data;
            this.invconfig = config;
            this.onClose = onClose;
            if (!config.exists())
            {
                this.saveData();
            }
        }
        
        @Override
        protected ItemStack[] getPersistentSlots()
        {
            return this.invcontent.getSlots().stream().map(ConfigItemStackData::toBukkit).toArray(ItemStack[]::new);
        }
        
        /**
         * Inventory growing.
         * 
         * @param slotAmount
         *            slot amount.
         */
        public void grow(int slotAmount)
        {
            final int size = this.invcontent.getSlotSize();
            final int newSize = size + slotAmount;
            this.invcontent.setSlotSize(newSize);
            for (int i = 0; i < slotAmount; i++)
            {
                this.invcontent.getSlots().add(InventoryContentData.AIR);
            }
            this.saveData();
            this.createMirror();
        }
        
        /**
         * Sets the new inventory size.
         * 
         * @param slots
         *            slot count.
         * @throws McException
         *             thrown if there are too many items.
         */
        public void setSize(int slots) throws McException
        {
            if (this.getSize() < slots)
            {
                this.grow(slots - this.getSize());
            }
            else if (this.getSize() > slots)
            {
                this.shrink(this.getSize() - slots);
            }
        }
        
        /**
         * Inventory shrink.
         * 
         * @param slotAmount
         *            slot count.
         * @throws McException
         *             thrown if there are too many items.
         */
        public void shrink(int slotAmount) throws McException
        {
            final List<ItemStack> stacks = new ArrayList<>();
            for (int i = slotAmount; i > 0; i--)
            {
                final int index = this.getSize() - i;
                if (this.getItem(index) != null && this.getItem(index).getType() != Material.AIR)
                {
                    stacks.add(this.getItem(index));
                }
            }
            
            int freeSlots = 0;
            for (int i = this.getSize() - slotAmount - 1; i >= 0; i++)
            {
                if (this.getItem(i) == null || this.getItem(i).getType() == Material.AIR)
                {
                    freeSlots++;
                }
            }
            
            if (stacks.size() > freeSlots)
            {
                throw new McException(InventoryServiceImpl.Messages.NotEnoughFreeSlots);
            }
            
            // TODO shrink it
        }
        
        @Override
        protected void saveMaxStackSize(int size2)
        {
            this.invcontent.setMaxStackSize(size2);
            this.saveData();
        }
        
        @Override
        protected int getPersistentMaxStackSize()
        {
            return this.invcontent.getMaxStackSize();
        }
        
        @Override
        protected void saveItem(int index, ItemStack item)
        {
            final List<ConfigItemStackData> list = this.invcontent.getSlots();
            if (index >= list.size())
            {
                return;
            }
            
            if (item == null || item.getType() == Material.AIR)
            {
                list.set(index, InventoryContentData.AIR);
            }
            else
            {
                list.set(index, ConfigItemStackData.fromBukkit(item));
            }
            this.saveData();
        }
        
        @Override
        protected void saveItems(ItemStack[] old, ItemStack[] contents)
        {
            this.fillInventorySlotsFromMirror();
            this.saveData();
        }
        
        /**
         * Saves data to inventory.
         * 
         * @throws IllegalStateException
         *             wrapped io exceptions
         */
        void saveData() throws IllegalStateException
        {
            final YmlFile yml = new YmlFile();
            yml.set("data", this.invcontent); //$NON-NLS-1$
            try
            {
                yml.saveFile(this.invconfig);
            }
            catch (IOException e)
            {
                LOGGER.log(Level.WARNING, "Problems saving inventory", e); //$NON-NLS-1$
                throw new IllegalStateException(e);
            }
        }
        
        /**
         * Fills inventory slots from mirror.
         */
        void fillInventorySlotsFromMirror()
        {
            final List<ConfigItemStackData> list = this.invcontent.getSlots();
            for (int i = this.getSize() - 1; i >= 0; i--)
            {
                final ItemStack stack = this.getItem(i);
                list.set(i, stack == null || stack.getType() == Material.AIR ? InventoryContentData.AIR : ConfigItemStackData.fromBukkit(stack));
            }
        }
        
        @Override
        public void handle(InventoryCloseEvent evt)
        {
            if (this.getViewers().isEmpty())
            {
                this.onClose.run();
            }
        }
        
    }
    
}
