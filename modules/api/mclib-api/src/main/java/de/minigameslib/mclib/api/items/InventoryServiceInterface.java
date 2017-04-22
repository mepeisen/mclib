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
    
    /**
     * Returns inventory ids for given types
     * @param types
     * @return inventories.
     */
    List<InventoryId> getInventoryIds(InventoryTypeId... types);
    
    /**
     * Deletes all inventories of given types
     * @param types
     * @throws McException 
     */
    void deleteAllInventories(InventoryTypeId... types) throws McException;
    
    /**
     * Deletes all inventories of given ids.
     * @param ids
     * @throws McException 
     */
    void deleteInventories(InventoryId... ids) throws McException;
    
    /**
     * Returns global inventory of given type and string id; global inventories are unique within servers.
     * @param type
     * @param stringIdentifier
     * @return global inventory identifies by given string id; {@code null} if it does not exist
     */
    InventoryId getInventory(InventoryTypeId type, String stringIdentifier);
    
    /**
     * Returns global inventory of given type and string id; global inventories are unique within servers.
     * Creates inventory if it does not exist.
     * @param type
     * @param initialSize initial inventory size in slots
     * @param fixed {@code true} if size is fixed
     * @param shared {@code true} if all players share inventory
     * @param stringIdentifier
     * @return global inventory identifies by given string id
     * @throws McException 
     */
    InventoryId getOrCreateInventory(InventoryTypeId type, int initialSize, boolean fixed, boolean shared, String stringIdentifier) throws McException;
    
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
     * @param initialSize initial inventory size in slots
     * @param fixed {@code true} if size is fixed
     * @param shared {@code true} if all players share inventory
     * @param location
     * @return inventory bound to given location
     * @throws McException 
     */
    InventoryId getOrCreateInventory(InventoryTypeId type, int initialSize, boolean fixed, boolean shared, Location location) throws McException;
    
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
    
}
