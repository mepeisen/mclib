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

import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.nms.api.CraftInventoryWrapper;
import de.minigameslib.mclib.nms.api.InventoryManagerInterface.InventoryListener;

/**
 * A wrapper for inventories allowing to call listeners.
 * 
 * @author mepeisen
 */
public class InventoryForListener extends CraftInventoryWrapper
{
    /** the listener. */
    InventoryListener listener;
    
    /** true forcalling listener. */
    boolean           callListener = false;
    
    /** the inventory title. */
    private String    title;
    
    /**
     * Coonstructor.
     * 
     * @param listener
     *            the listener
     * @param size
     *            initial size.
     * @param title
     *            the title.
     */
    public InventoryForListener(InventoryListener listener, int size, String title)
    {
        super(size, 64, new ItemStack[size]);
        this.listener = listener;
        this.title = title;
    }
    
    /**
     * Activate inventory listener.
     */
    public void initListener()
    {
        this.callListener = true;
    }
    
    @Override
    protected int getPersistentMaxStackSize()
    {
        return 64;
    }
    
    @Override
    protected org.bukkit.inventory.ItemStack[] getPersistentSlots()
    {
        return new org.bukkit.inventory.ItemStack[this.getSize()];
    }
    
    @Override
    protected void saveMaxStackSize(int size2)
    {
        // not used
    }
    
    @Override
    public String getTitle()
    {
        return this.title;
    }
    
    @Override
    protected void saveItem(int index, org.bukkit.inventory.ItemStack item)
    {
        if (this.callListener)
        {
            this.listener.setItem(index, item);
        }
    }
    
}
