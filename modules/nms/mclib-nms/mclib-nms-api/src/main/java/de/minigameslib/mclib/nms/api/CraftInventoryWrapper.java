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

package de.minigameslib.mclib.nms.api;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/**
 * Wrapper for craft inventories.
 * 
 * @author mepeisen
 */
public abstract class CraftInventoryWrapper implements Inventory
{

    /**
     * craft inventory mirror.
     */
    private Inventory craftInventory;
    
    /**
     * Constructor to create inventory.
     * @param size size of the inventory.
     * @param maxStackSize the max stack size.
     */
    public CraftInventoryWrapper(int size, int maxStackSize)
    {
        this.createMirror(size, maxStackSize);
    }

    /**
     * Creates bukkit mirror.
     * @param size size of the inventory.
     * @param maxStackSize the max stack size.
     */
    private void createMirror(int size, int maxStackSize)
    {
        this.craftInventory = Bukkit.createInventory(null, size);
        this.craftInventory.setMaxStackSize(maxStackSize);
        final ItemStack[] slots = this.getPersistentSlots();
        for (int i = 0; i < size; i++)
        {
            final ItemStack stack = slots[i];
            this.craftInventory.setItem(i, stack.getType() == Material.AIR ? null : stack);
        }
    }

    /**
     * Mirror.
     */
    protected void createMirror()
    {
        this.createMirror(this.getPersistentSlots().length, this.getPersistentMaxStackSize());
    }

    /**
     * Returns the max stack size from config.
     * @return mas stack size.
     */
    protected abstract int getPersistentMaxStackSize();

    /**
     * Returns the persistent slots.
     * @return persistent slots.
     */
    protected abstract ItemStack[] getPersistentSlots();

    /**
     * Saves max stack size.
     * @param size2 new max stack size.
     */
    protected abstract void saveMaxStackSize(int size2);

    /**
     * Saves item on given index.
     * @param index index
     * @param item new item
     */
    protected abstract void saveItem(int index, ItemStack item);

    @Override
    public int getSize()
    {
        return this.craftInventory.getSize();
    }

    @Override
    public int getMaxStackSize()
    {
        return this.craftInventory.getMaxStackSize();
    }

    @Override
    public void setMaxStackSize(int size)
    {
        this.craftInventory.setMaxStackSize(size);
        this.saveMaxStackSize(size);
    }

    @Override
    public String getName()
    {
        return this.craftInventory.getName();
    }

    @Override
    public ItemStack getItem(int index)
    {
        return this.craftInventory.getItem(index);
    }

    @Override
    public void setItem(int index, ItemStack item)
    {
        this.craftInventory.setItem(index, item);
        this.saveItem(index, item);
    }

    @Override
    public HashMap<Integer, ItemStack> addItem(ItemStack... items) throws IllegalArgumentException
    {
        final ItemStack[] old = this.craftInventory.getContents();
        final HashMap<Integer, ItemStack> result = this.craftInventory.addItem(items);
        this.saveItems(old, this.craftInventory.getContents());
        return result;
    }

    /**
     * Calls save items by comparing old and new item array. 
     * @param old old items
     * @param contents new items
     */
    protected void saveItems(ItemStack[] old, ItemStack[] contents)
    {
        for (int i = 0; i < old.length; i++)
        {
            if (old[i] == null)
            {
                if (contents[i] != null)
                {
                    this.saveItem(i, contents[i]);
                }
                // else null == null
            }
            else
            {
                if (contents[i] == null)
                {
                    this.saveItem(i, contents[i]);
                }
                else if (!contents[i].equals(old[i]))
                {
                    this.saveItem(i, contents[i]);
                }
            }
        }
    }

    @Override
    public HashMap<Integer, ItemStack> removeItem(ItemStack... items) throws IllegalArgumentException
    {
        final ItemStack[] old = this.craftInventory.getContents();
        final HashMap<Integer, ItemStack> result = this.craftInventory.removeItem(items);
        this.saveItems(old, this.craftInventory.getContents());
        return result;
    }

    @Override
    public ItemStack[] getContents()
    {
        return this.craftInventory.getContents();
    }

    @Override
    public void setContents(ItemStack[] items) throws IllegalArgumentException
    {
        final ItemStack[] old = this.craftInventory.getContents();
        this.craftInventory.setContents(items);
        this.saveItems(old, this.craftInventory.getContents());
    }

    @Override
    public ItemStack[] getStorageContents()
    {
        return this.craftInventory.getStorageContents();
    }

    @Override
    public void setStorageContents(ItemStack[] items) throws IllegalArgumentException
    {
        final ItemStack[] old = this.craftInventory.getContents();
        this.craftInventory.setStorageContents(items);
        this.saveItems(old, this.craftInventory.getContents());
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean contains(int materialId)
    {
        return this.craftInventory.contains(materialId);
    }

    @Override
    public boolean contains(Material material) throws IllegalArgumentException
    {
        return this.craftInventory.contains(material);
    }

    @Override
    public boolean contains(ItemStack item)
    {
        return this.craftInventory.contains(item);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean contains(int materialId, int amount)
    {
        return this.craftInventory.contains(materialId, amount);
    }

    @Override
    public boolean contains(Material material, int amount) throws IllegalArgumentException
    {
        return this.craftInventory.contains(material, amount);
    }

    @Override
    public boolean contains(ItemStack item, int amount)
    {
        return this.craftInventory.contains(item, amount);
    }

    @Override
    public boolean containsAtLeast(ItemStack item, int amount)
    {
        return this.craftInventory.containsAtLeast(item, amount);
    }

    @SuppressWarnings("deprecation")
    @Override
    public HashMap<Integer, ? extends ItemStack> all(int materialId)
    {
        return this.craftInventory.all(materialId);
    }

    @Override
    public HashMap<Integer, ? extends ItemStack> all(Material material) throws IllegalArgumentException
    {
        return this.craftInventory.all(material);
    }

    @Override
    public HashMap<Integer, ? extends ItemStack> all(ItemStack item)
    {
        return this.craftInventory.all(item);
    }

    @SuppressWarnings("deprecation")
    @Override
    public int first(int materialId)
    {
        return this.craftInventory.first(materialId);
    }

    @Override
    public int first(Material material) throws IllegalArgumentException
    {
        return this.craftInventory.first(material);
    }

    @Override
    public int first(ItemStack item)
    {
        return this.craftInventory.first(item);
    }

    @Override
    public int firstEmpty()
    {
        return this.craftInventory.firstEmpty();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void remove(int materialId)
    {
        final ItemStack[] old = this.craftInventory.getContents();
        this.craftInventory.remove(materialId);
        this.saveItems(old, this.craftInventory.getContents());
    }

    @Override
    public void remove(Material material) throws IllegalArgumentException
    {
        final ItemStack[] old = this.craftInventory.getContents();
        this.craftInventory.remove(material);
        this.saveItems(old, this.craftInventory.getContents());
    }

    @Override
    public void remove(ItemStack item)
    {
        final ItemStack[] old = this.craftInventory.getContents();
        this.craftInventory.remove(item);
        this.saveItems(old, this.craftInventory.getContents());
    }

    @Override
    public void clear(int index)
    {
        this.craftInventory.clear(index);
        this.saveItem(index, null);
    }

    @Override
    public void clear()
    {
        final ItemStack[] old = this.craftInventory.getContents();
        this.craftInventory.clear();
        this.saveItems(old, this.craftInventory.getContents());
    }

    @Override
    public List<HumanEntity> getViewers()
    {
        return this.craftInventory.getViewers();
    }

    @Override
    public String getTitle()
    {
        return this.craftInventory.getTitle();
    }

    @Override
    public InventoryType getType()
    {
        return this.craftInventory.getType();
    }

    @Override
    public InventoryHolder getHolder()
    {
        return this.craftInventory.getHolder();
    }

    @Override
    public ListIterator<ItemStack> iterator()
    {
        return new IteratorWrapper(this.craftInventory.iterator());
    }

    @Override
    public ListIterator<ItemStack> iterator(int index)
    {
        return new IteratorWrapper(this.craftInventory.iterator(index));
    }

    @Override
    public Location getLocation()
    {
        return this.craftInventory.getLocation();
    }
    
    /**
     * Wrapper for list iterator.
     * 
     * @author mepeisen
     */
    private final class IteratorWrapper implements ListIterator<ItemStack>
    {
        
        /**
         * the delegate list iterator.
         */
        private final ListIterator<ItemStack> delegate;
        
        /**
         * Constructor.
         * @param delegate the delegate list iterator
         */
        IteratorWrapper(ListIterator<ItemStack> delegate)
        {
            this.delegate = delegate;
        }
        
        @Override
        public boolean hasNext()
        {
            return this.delegate.hasNext();
        }
        
        @Override
        public ItemStack next()
        {
            return this.delegate.next();
        }
        
        @Override
        public boolean hasPrevious()
        {
            return this.delegate.hasPrevious();
        }
        
        @Override
        public ItemStack previous()
        {
            return this.delegate.previous();
        }
        
        @Override
        public int nextIndex()
        {
            return this.delegate.nextIndex();
        }
        
        @Override
        public int previousIndex()
        {
            return this.delegate.previousIndex();
        }
        
        @Override
        public void remove()
        {
            final ItemStack[] old = CraftInventoryWrapper.this.getContents();
            this.delegate.remove();
            CraftInventoryWrapper.this.saveItems(old, CraftInventoryWrapper.this.getContents());
        }
        
        @Override
        public void set(ItemStack e)
        {
            final ItemStack[] old = CraftInventoryWrapper.this.getContents();
            this.delegate.set(e);
            CraftInventoryWrapper.this.saveItems(old, CraftInventoryWrapper.this.getContents());
        }
        
        @Override
        public void add(ItemStack e)
        {
            final ItemStack[] old = CraftInventoryWrapper.this.getContents();
            this.delegate.add(e);
            CraftInventoryWrapper.this.saveItems(old, CraftInventoryWrapper.this.getContents());
        }
    }
    
}
