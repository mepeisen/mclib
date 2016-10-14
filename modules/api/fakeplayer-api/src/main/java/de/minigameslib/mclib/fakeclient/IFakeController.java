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

package de.minigameslib.mclib.fakeclient;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Base interface for controlling the fake player.
 * 
 * @author mepeisen
 */
public interface IFakeController
{
    
    /**
     * Connects to the current local server (must be running in background).
     * 
     * @param plugin
     *            the plugin causing the spawn
     * @param spawn
     *            the world and spawn point
     */
    void connect(Plugin plugin, Location spawn);
    
    /**
     * Disconnects the player.
     */
    void disconnect();
    
    /**
     * Returns the bukkit player (if being online)
     * 
     * @return bukkit player
     */
    Player getPlayer();
    
    /**
     * Returns the offline player.
     * 
     * @return offline player.
     */
    OfflinePlayer getOfflinePlayer();
    
    // interaction
    
    /**
     * Sends a chat message.
     * 
     * @param msg
     */
    void say(String msg);
    
}
