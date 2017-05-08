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

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import de.minigameslib.mclib.api.items.InventoryId;
import de.minigameslib.mclib.api.items.InventoryTypeId;
import de.minigameslib.mclib.shared.api.com.AnnotatedDataFragment;
import de.minigameslib.mclib.shared.api.com.LocationDataFragment;
import de.minigameslib.mclib.shared.api.com.PersistentField;

/**
 * The inventories data.
 * 
 * @author mepeisen
 */
public class InventoryData extends AnnotatedDataFragment
{
    
    /**
     * inventory type id.
     */
    @PersistentField
    protected InventoryTypeId           typeId;
    
    /**
     * inventory identifier.
     */
    @PersistentField
    protected String                    identifier;
    
    /**
     * initial size.
     */
    @PersistentField
    protected int                       initialSize;
    
    /**
     * fixed flag.
     */
    @PersistentField
    protected boolean                   fixed;
    
    /**
     * shared flag.
     */
    @PersistentField
    protected boolean                   shared;
    
    /**
     * the id of this inventory.
     */
    @PersistentField
    protected InventoryId               id;
    
    /**
     * Locations of this inventory.
     */
    @PersistentField
    protected Set<LocationDataFragment> locations = new HashSet<>();
    
    /**
     * players.
     */
    @PersistentField
    protected Set<UUID>                 players   = new HashSet<>();
    
    /**
     * Constructor to create new data.
     * 
     * @param typeId
     *            inventory type id
     * @param identifier
     *            string identifier (optional)
     * @param initialSize
     *            initial inventory size in slots
     * @param fixed
     *            fixed size flag
     * @param shared
     *            shared flag
     * @param id
     *            the inventory id to use
     */
    public InventoryData(InventoryTypeId typeId, String identifier, int initialSize, boolean fixed, boolean shared, InventoryId id)
    {
        this.typeId = typeId;
        this.identifier = identifier;
        this.initialSize = initialSize;
        this.fixed = fixed;
        this.shared = shared;
        this.id = id;
    }
    
    /**
     * Constructor for reading from config.
     */
    public InventoryData()
    {
        // empty
    }
    
    /**
     * Returns the bound locations.
     * 
     * @return the locations
     */
    public Set<LocationDataFragment> getLocations()
    {
        return this.locations;
    }
    
    /**
     * Returns the players having an inventory data instance.
     * 
     * @return the players
     */
    public Set<UUID> getPlayers()
    {
        return this.players;
    }
    
    /**
     * Returns the inventoy id.
     * 
     * @return the id
     */
    public InventoryId getId()
    {
        return this.id;
    }
    
    /**
     * Sets the inventoy id.
     * 
     * @param id
     *            the id to set
     */
    public void setId(InventoryId id)
    {
        this.id = id;
    }
    
    /**
     * Returns the inventory type id.
     * 
     * @return the typeId
     */
    public InventoryTypeId getTypeId()
    {
        return this.typeId;
    }
    
    /**
     * Sets the inventory type id.
     * 
     * @param typeId
     *            the typeId to set
     */
    public void setTypeId(InventoryTypeId typeId)
    {
        this.typeId = typeId;
    }
    
    /**
     * Returns the string identifier of this inventory.
     * 
     * @return the identifier
     */
    public String getIdentifier()
    {
        return this.identifier;
    }
    
    /**
     * Sets the string identifier of this identifier.
     * 
     * @param identifier
     *            the identifier to set
     */
    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }
    
    /**
     * Returns the initial size in slots.
     * 
     * @return the initialSize
     */
    public int getInitialSize()
    {
        return this.initialSize;
    }
    
    /**
     * Sets the initial size in slots.
     * 
     * @param initialSize
     *            the initialSize to set
     */
    public void setInitialSize(int initialSize)
    {
        this.initialSize = initialSize;
    }
    
    /**
     * Returns the fixed flag.
     * 
     * @return the fixed
     */
    public boolean isFixed()
    {
        return this.fixed;
    }
    
    /**
     * Sets the fixed flag.
     * 
     * @param fixed
     *            the fixed to set
     */
    public void setFixed(boolean fixed)
    {
        this.fixed = fixed;
    }
    
    /**
     * Returns the shared flag.
     * 
     * @return the shared
     */
    public boolean isShared()
    {
        return this.shared;
    }
    
    /**
     * Sets the shared flag.
     * 
     * @param shared
     *            the shared to set
     */
    public void setShared(boolean shared)
    {
        this.shared = shared;
    }
    
}
