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

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * A helper to manage holograms.
 * 
 * @author mepeisen
 */
public interface HologramHelperInterface
{
    
    /**
     * Creates a new hologram entity at given location.
     * 
     * @param location
     *            the location where the hologram is displayed.
     * @return the hologram entity.
     */
    HologramEntityInterface create(Location location);
    
    /**
     * Updates visibility list for given player; hides dummy humans.
     * 
     * @param player
     *            target player.
     */
    void playerOnline(Player player);
    
    /**
     * Updates visibility list for given player; hides dummy humans.
     * 
     * @param player
     *            target player
     */
    void playerOffline(Player player);
    
    /**
     * Deletes given entity that was created by {@link #create(Location)}.
     * 
     * @param entity
     *            entity to be deleted
     */
    void delete(HologramEntityInterface entity);
    
    /**
     * The hologram entity.
     */
    public interface HologramEntityInterface
    {
        
        /**
         * Sets the new location.
         * 
         * @param newLocation
         *            new location.
         */
        void setNewLocation(Location newLocation);
        
        /**
         * Hides the hologram expect for given players.
         * 
         * @param whitelist
         *            whitelist.
         */
        void hide(List<UUID> whitelist);
        
        /**
         * Update the lines.
         * 
         * @param lines
         *            new lines
         */
        void updateLines(List<Serializable> lines);
        
        /**
         * Shows the hologram for all players.
         */
        void show();
        
    }
    
}
