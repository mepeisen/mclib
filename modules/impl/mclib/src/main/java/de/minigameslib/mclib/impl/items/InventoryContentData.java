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

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.config.ConfigItemStackData;
import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.PersistentField;

/**
 * Inventory content data.
 * 
 * @author mepeisen
 */
public class InventoryContentData extends AnnotatedDataFragment
{
    
    /** empty stack (air) */
    public static ConfigItemStackData AIR = ConfigItemStackData.fromBukkit(new ItemStack(Material.AIR));
    
    /**
     * Number of slots in this repository
     */
    @PersistentField
    protected int slotSize;
    
    /**
     * The max stack size
     */
    @PersistentField
    protected int maxStackSize;

    /**
     * Inventory name
     */
    @PersistentField
    protected String name;
    
    /**
     * Item stacks
     */
    @PersistentField
    protected List<ConfigItemStackData> slots;

    /**
     * Constructor for reading object
     */
    public InventoryContentData()
    {
        // empty
    }

    /**
     * Constructor for initializing inventory
     * @param slotSize
     * @param maxStackSize
     * @param name 
     */
    public InventoryContentData(int slotSize, int maxStackSize, String name)
    {
        this.slotSize = slotSize;
        this.maxStackSize = maxStackSize;
        this.name = name;
        for (int i = 0; i < slotSize; i++)
        {
            this.slots.add(AIR);
        }
    }

    /**
     * @return the slots
     */
    public List<ConfigItemStackData> getSlots()
    {
        return this.slots;
    }

    /**
     * @return the slotSize
     */
    public int getSlotSize()
    {
        return this.slotSize;
    }

    /**
     * @param slotSize the slotSize to set
     */
    public void setSlotSize(int slotSize)
    {
        this.slotSize = slotSize;
    }

    /**
     * @return the maxStackSize
     */
    public int getMaxStackSize()
    {
        return this.maxStackSize;
    }

    /**
     * @param maxStackSize the maxStackSize to set
     */
    public void setMaxStackSize(int maxStackSize)
    {
        this.maxStackSize = maxStackSize;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    
    
}
