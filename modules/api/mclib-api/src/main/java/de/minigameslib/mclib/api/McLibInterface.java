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

package de.minigameslib.mclib.api;

import java.util.Locale;

import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.event.McListener;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.gui.RawMessageInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.shared.api.com.CommunicationEndpointId;
import de.minigameslib.mclib.shared.api.com.DataSection;

/**
 * Base interface for Minigame Common library.
 * 
 * @author mepeisen
 */
public interface McLibInterface extends McContext
{
    
    /**
     * Returns the library instance.
     * 
     * @return library instance.
     */
    static McLibInterface instance()
    {
        return McLibCache.get();
    }
    
    /**
     * the first api version, all versions up to first release, includes minecraft versions up to 1.11.
     */
    int APIVERSION_1_0_0 = 10000;
    
    /**
     * Returns the api version of MCLIB.
     * 
     * @return api version.
     */
    int getApiVersion();
    
    /**
     * Returns the minecraft version the server is running.
     * 
     * @return minecraft version.
     */
    MinecraftVersionsType getMinecraftVersion();
    
    /**
     * Returns the default locale used in minigame lib.
     * 
     * @return default locale
     */
    Locale getDefaultLocale();
    
    /**
     * Sets the default locale used in minigame lib.
     * 
     * @param locale
     *            the new locale
     * 
     * @throws McException
     *             thrown if the config could not be saved.
     */
    void setDefaultLocale(Locale locale) throws McException;
    
    /**
     * Checks for debug flag.
     * 
     * @return {@code true} if the library debugging is enabled.
     */
    boolean debug();
    
    /**
     * Sets the debug flag.
     * 
     * @param enabled
     *            {@code true} if the library debugging is enabled.
     */
    void setDebug(boolean enabled);
    
    // communication
    
    /**
     * Sends given data to all clients.
     *
     * @param endpoint
     * @param data
     * 
     * @throws IllegalStateException
     */
    void broadcastClients(CommunicationEndpointId endpoint, DataSection... data);
    
    /**
     * Sends given data to all other servers within bungee network.
     *
     * @param endpoint
     * @param data
     * 
     * @throws IllegalStateException
     */
    void broadcastServers(CommunicationEndpointId endpoint, DataSection... data);
    
    /**
     * Creates a new raw message that can be sent to clients.
     * 
     * @return raw message
     */
    RawMessageInterface createRaw();
    
    // event system
    
    /**
     * Register event handler for given mclib event; simliar to registering single bukkit event handlers.
     * The McLib event will automatically set the execution context (current player/ zone etc.).
     * If you use a event class not supported by the spigot version it will log a warning and silently ignore
     * the registration.
     * @param plugin
     * @param clazz
     * @param handler
     */
    <Evt extends MinecraftEvent<?, Evt>> void registerHandler(Plugin plugin, Class<Evt> clazz, McConsumer<Evt> handler);
    
    /**
     * Registers an event handler object. Methods tagged with McEventHandler are considered as event handlers.
     * @param plugin
     * @param listener
     */
    void registerHandlers(Plugin plugin, McListener listener);
    
    /**
     * Remove a registered event handler.
     * @param plugin
     * @param clazz
     * @param handler
     */
    <Evt extends MinecraftEvent<?, Evt>> void unregisterHandler(Plugin plugin, Class<Evt> clazz, McConsumer<Evt> handler);
    
    /**
     * Remove a registered event handler.
     * @param plugin
     * @param listener
     */
    void unregisterHandlers(Plugin plugin, McListener listener);
    
}
