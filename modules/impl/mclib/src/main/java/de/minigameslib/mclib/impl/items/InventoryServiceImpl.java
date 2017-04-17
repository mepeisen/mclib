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

import org.bukkit.Location;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.items.InventoryDescriptorInterface;
import de.minigameslib.mclib.api.items.InventoryId;
import de.minigameslib.mclib.api.items.InventoryServiceInterface;
import de.minigameslib.mclib.api.items.InventoryTypeId;

/**
 * Implementation of inventory service.
 * 
 * @author mepeisen
 *
 */
public class InventoryServiceImpl implements InventoryServiceInterface
{

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.items.InventoryServiceInterface#getInventoryIds(de.minigameslib.mclib.api.items.InventoryTypeId[])
     */
    @Override
    public List<InventoryId> getInventoryIds(InventoryTypeId... types)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.items.InventoryServiceInterface#deleteAllInventories(de.minigameslib.mclib.api.items.InventoryTypeId[])
     */
    @Override
    public void deleteAllInventories(InventoryTypeId... types)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.items.InventoryServiceInterface#deleteInventories(de.minigameslib.mclib.api.items.InventoryId[])
     */
    @Override
    public void deleteInventories(InventoryId... ids)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.items.InventoryServiceInterface#getInventory(de.minigameslib.mclib.api.items.InventoryTypeId, java.lang.String)
     */
    @Override
    public InventoryId getInventory(InventoryTypeId type, String stringIdentifier)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.items.InventoryServiceInterface#getOrCreateInventory(de.minigameslib.mclib.api.items.InventoryTypeId, int, boolean, boolean, java.lang.String)
     */
    @Override
    public InventoryId getOrCreateInventory(InventoryTypeId type, int initialSize, boolean fixed, boolean shared, String stringIdentifier)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.items.InventoryServiceInterface#createInventory(de.minigameslib.mclib.api.items.InventoryTypeId, int, boolean, boolean, java.lang.String)
     */
    @Override
    public InventoryId createInventory(InventoryTypeId type, int initialSize, boolean fixed, boolean shared, String stringIdentifier)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.items.InventoryServiceInterface#getInventory(de.minigameslib.mclib.api.items.InventoryTypeId, org.bukkit.Location)
     */
    @Override
    public InventoryId getInventory(InventoryTypeId type, Location location)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.items.InventoryServiceInterface#getOrCreateInventory(de.minigameslib.mclib.api.items.InventoryTypeId, int, boolean, boolean, org.bukkit.Location)
     */
    @Override
    public InventoryId getOrCreateInventory(InventoryTypeId type, int initialSize, boolean fixed, boolean shared, Location location)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.items.InventoryServiceInterface#createInventory(de.minigameslib.mclib.api.items.InventoryTypeId, int, boolean, boolean, org.bukkit.Location[])
     */
    @Override
    public InventoryId createInventory(InventoryTypeId type, int initialSize, boolean fixed, boolean shared, Location... locations) throws McException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.items.InventoryServiceInterface#bindInventory(de.minigameslib.mclib.api.items.InventoryId, org.bukkit.Location[])
     */
    @Override
    public void bindInventory(InventoryId id, Location... locations) throws McException
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.items.InventoryServiceInterface#unbindInventory(de.minigameslib.mclib.api.items.InventoryId, org.bukkit.Location[])
     */
    @Override
    public void unbindInventory(InventoryId id, Location... locations) throws McException
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.api.items.InventoryServiceInterface#getInventory(de.minigameslib.mclib.api.items.InventoryId)
     */
    @Override
    public InventoryDescriptorInterface getInventory(InventoryId id)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
}
