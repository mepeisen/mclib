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

package de.minigameslib.mclib.nms.v111;

import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftInventoryCustom;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.nms.api.InventoryManagerInterface.InventoryListener;

/**
 * A wrapper for inventories allowing to call listeners.
 * 
 * @author mepeisen
 */
public class Inventory1_11 extends CraftInventoryCustom
{
    private InventoryListener listener;
    
    private boolean callListener = false;

    /**
     * @param listener
     * @param owner 
     * @param size 
     * @param title 
     */
    public Inventory1_11(InventoryListener listener, InventoryHolder owner, int size, String title)
    {
        super(owner, size, title);
        this.listener = listener;
    }
    
    @Override
    public void setItem(int index, ItemStack item)
    {
        if (this.callListener)
        {
            this.listener.setItem(index, item);
        }
        super.setItem(index, item);
    }
    
    public void initListener()
    {
        this.callListener = true;
    }
    
}
