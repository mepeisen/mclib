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

package de.minigameslib.mclib.api.objects;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * A service to register enumerations with plugins.
 * 
 * @author mepeisen
 */
public interface ObjectServiceInterface
{
    
    /**
     * Returns the enumeration services instance.
     * 
     * @return enumeration services instance.
     */
    static ObjectServiceInterface instance()
    {
        return ObjectServiceCache.get();
    }
    
    // player api
    
    /**
     * Returns the player for given bukkit player.
     * 
     * @param player bukkit online player
     * @return arena player.
     */
    McPlayerInterface getPlayer(Player player);
    
    /**
     * Returns the player for given bukkit player.
     * 
     * @param player bukkit offline player
     * @return arena player.
     */
    McPlayerInterface getPlayer(OfflinePlayer player);
    
    /**
     * Returns the player for given bukkit player uuid.
     * 
     * @param uuid player uuid
     * @return arena player.
     */
    McPlayerInterface getPlayer(UUID uuid);
    
}
