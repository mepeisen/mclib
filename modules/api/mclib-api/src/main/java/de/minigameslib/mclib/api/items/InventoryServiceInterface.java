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

package de.minigameslib.mclib.api.items;

import java.util.List;

import org.bukkit.Location;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.McPlayerInterface;

/**
 * @author mepeisen
 *
 */
public interface InventoryServiceInterface
{
    
    /**
     * Returns the inventory services instance.
     * 
     * @return inventors services instance.
     */
    static InventoryServiceInterface instance()
    {
        return InventoryServiceCache.get();
    }
    
    /** default inventory name */
    String DEFAULT_INVENTORY = "DEFAULT"; //$NON-NLS-1$
    
    /** location prefix for identified */
    String LOC_PREFIX = "LOC$"; //$NON-NLS-1$
    
    /**
     * Returns inventory ids for given types
     * @param types
     * @return inventories.
     */
    List<InventoryId> getInventoryIds(InventoryTypeId... types);
    
    /**
     * Deletes all inventories of given types
     * @param types
     */
    void deleteAllInventories(InventoryTypeId... types);
    
    /**
     * Deletes all inventories of given ids.
     * @param ids
     */
    void deleteInventories(InventoryId... ids);
    
    /**
     * Returns global inventory of given type; global inventories are unique within servers.
     * Identical to {@code getOrCreateInventory(type, DEFAULT_INVENTORY)}.
     * @param type
     * @return global inventory
     */
    InventoryId getDefault(InventoryTypeId type);
    
    /**
     * Returns global inventory of given type and string id; global inventories are unique within servers.
     * @param type
     * @param stringIdentified
     * @return global inventory identifies by given string id; {@code null} if it does not exist
     */
    InventoryId getInventory(InventoryTypeId type, String stringIdentified);
    
    /**
     * Returns global inventory of given type and string id; global inventories are unique within servers.
     * Creates inventory if it does not exist.
     * @param type
     * @param stringIdentified
     * @return global inventory identifies by given string id
     */
    InventoryId getOrCreateInventory(InventoryTypeId type, String stringIdentified);
    
    /**
     * Returns inventory of given type and location
     * @param type
     * @param location
     * @return inventory bound to given location; {@code null} if inventory was not created before
     */
    InventoryId getInventory(InventoryTypeId type, Location location);
    
    /**
     * Returns inventory of given type and location.
     * Creates inventory if it does not exist.
     * @param type
     * @param location
     * @return inventory bound to given location
     */
    InventoryId getOrCreateInventory(InventoryTypeId type, Location location);
    
    /**
     * Creates an inventory for one or multiple locations.
     * @param type
     * @param locations
     * @return inventory
     * @throws McException thrown if there was a problem creating the inventory; thrown if the locations are already in use
     */
    InventoryId createInventory(InventoryTypeId type, Location... locations) throws McException;
    
    /**
     * Bind inventory to new locations
     * @param id
     * @param locations
     * @throws McException thrown if there was a problem binding the inventory; thrown if the locations are already in use
     */
    void bindInventory(InventoryId id, Location... locations) throws McException;
    
    /**
     * Unbind inventory from existing locations
     * @param id
     * @param locations
     * @throws McException thrown if there was a problem unbinding the inventory; thrown if the locations are not in use by given inventory
     */
    void unbindInventory(InventoryId id, Location... locations) throws McException;
    
    /**
     * Returns the inventory descriptor for given inventory
     * @param id
     * @return inventory descriptor; {@code null} if inventory is invalid
     */
    InventoryDescriptorInterface getInventory(InventoryId id);
    
    /**
     * Open inventory GUI for given player
     * @param player
     */
    void openInventory(McPlayerInterface player);
    
}
