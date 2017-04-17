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
import org.bukkit.inventory.Inventory;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.McPlayerInterface;

/**
 * A descriptor for inventories
 * 
 * @author mepeisen
 */
public interface InventoryDescriptorInterface
{
    
    /**
     * Returns the type id of this inventory
     * @return type of inventory.
     */
    InventoryTypeId getTypeId();
    
    /**
     * Returns the id of this inventory
     * @return id of inventory.
     */
    InventoryId getId();
    
    /**
     * Returns the unique string id used during creation
     * @return unique string id
     */
    String getStringIdentified();
    
    /**
     * returns the locations this inventory is bound to.
     * @return locations
     */
    List<Location> getLocations();
    
    /**
     * Returns the shared flag
     * @return {@code true} if inventory is shared over all players
     */
    boolean isShared();
    
    /**
     * Returns the fixed flag
     * @return {@code true} if inventory size is fixed
     */
    boolean isFixed();
    
    /**
     * Returns the player list having an inventory instance
     * @return player list for this inventory or empty list for shared inventories
     */
    List<McPlayerInterface> getPlayers();
    
    /**
     * Returns the inventory used for given player; returns a shared inventory if {@link #isShared()} is true
     * @param player
     * @return inventory or {@code null} if there is no inventory
     * @throws McException thrown if there was a technical problem reading the inventory
     */
    Inventory getInventory(McPlayerInterface player) throws McException;
    
    /**
     * Returns or creates an inventory used for given player if needed; returns a shared inventory if {@link #isShared()} is true
     * @param player
     * @return inventory
     * @throws McException thrown if there was a technical problem reading or creating the inventory
     */
    Inventory getOrCreateInventory(McPlayerInterface player) throws McException;
    
    /**
     * Deletes inventory for given player. If {@link #isShared()} is true the inventory will be deleted for all players
     * @param player
     * @throws McException thrown if there was a technical problem deleting the inventory
     */
    void deleteInventory(McPlayerInterface player) throws McException;
    
    /**
     * Tries to shrink inventory of given player
     * @param player
     * @param slotAmount
     * @throws McException thrown if inventory size is fixed or shared
     */
    void shrinkInventory(McPlayerInterface player, int slotAmount) throws McException;
    
    /**
     * Tries to set inventory size of given player to given slot count
     * @param player
     * @param slots
     * @throws McException thrown if inventory size is fixed or shared
     */
    void setInventorySize(McPlayerInterface player, int slots) throws McException;
    
    /**
     * Tries to grow inventory of given player
     * @param player
     * @param slotAmount
     * @throws McException thrown if inventory size is fixed or shared
     */
    void growInventory(McPlayerInterface player, int slotAmount) throws McException;
    
    /**
     * Tries to shrink inventory of all players
     * @param slotAmount
     * @throws McException thrown if inventory size is fixed
     */
    void shrinkInventory(int slotAmount) throws McException;
    
    /**
     * Tries to set inventory size of all players to given slot count
     * @param slots
     * @throws McException thrown if inventory size is fixed
     */
    void setInventorySize(int slots) throws McException;
    
    /**
     * Tries to grow inventory of all players
     * @param slotAmount
     * @throws McException thrown if inventory size is fixed
     */
    void growInventory(int slotAmount) throws McException;
    
    /**
     * Open inventory GUI for given player
     * @param player
     */
    void openInventory(McPlayerInterface player);
    
}
