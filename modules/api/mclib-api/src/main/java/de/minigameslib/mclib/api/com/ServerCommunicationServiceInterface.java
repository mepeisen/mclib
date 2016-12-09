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

package de.minigameslib.mclib.api.com;

import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.shared.api.com.CommunicationEndpointId;

/**
 * Communication services.
 * 
 * @author mepeisen
 */
public interface ServerCommunicationServiceInterface extends CommunicationEndpointId.CommunicationServiceInterface
{
    
    /**
     * Returns the communication services instance.
     * 
     * @return communication services instance.
     */
    static ServerCommunicationServiceInterface instance()
    {
        return ServerCommunicationServiceCache.get();
    }
    
    /**
     * Removes all communication endpoints declared by given plugin.
     * 
     * @param plugin
     */
    void removeAllCommunicationEndpoints(Plugin plugin);
    
    /**
     * Registers a handler for given communication endpoint for sending messages between servers and clients.
     * 
     * @param plugin
     *            owning plugin
     * @param id
     *            endpoint id.
     * @param handler
     *            server handler.
     */
    void registerPeerHandler(Plugin plugin, CommunicationEndpointId id, CommunicationPeerHandler handler);
    
    /**
     * Registers a handler for given communication endpoint for sending messages between bungee servers.
     * 
     * @param plugin
     *            owning plugin
     * @param id
     *            endpoint id.
     * @param handler
     *            server handler.
     */
    void registerBungeeHandler(Plugin plugin, CommunicationEndpointId id, CommunicationBungeeHandler handler);
    
}
