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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Locale;

import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.event.McListener;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.gui.RawMessageInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.shared.api.com.CommentableDataSection;
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
     * This api version is not yet used; the api version is compatible to {@link #APIVERSION_1_0_0} but
     * may introduce new features or methods. Use this for version checks (getApiVersion &lt; APIVERSION_1_1_0).
     */
    int APIVERSION_1_1_0 = 10100;
    
    /**
     * This api version is not yet used; the api version is incompatible to {@link #APIVERSION_1_0_0}.
     * Use this for version checks (getApiVersion &lt; APIVERSION_2_0_0).
     */
    int APIVERSION_2_0_0 = 20000;
    
    /**
     * Returns the api version of McLib.
     * 
     * <p>
     * The api version is found with int constants on this interface. The integer is built with following schematic:
     * </p>
     * <ul>
     * <li>major version number</li>
     * <li>minor version number (to digits)</li>
     * <li>fix level (two digits)</li>
     * </ul>
     * 
     * <p>
     * Different fix levels are returned for new minecraft versions. It indicates that the mclib version type enum
     * contains new entries and that MinigamesLib supports a new minecraft version. For most situations you need not
     * take care about fix levels. You need only check for fix levels if you require a special feature found at newest
     * minecraft versions.
     * </p>
     * 
     * <p>
     * The minor version number is changed if MinigamesLib adds new features or methods to the existing API.
     * You will find an informative annotation (@ApiVersion) for methods being present in a specific api version.
     * </p>
     * 
     * <p>
     * The major version number is changed if MinigamesLib API is completly rewritten.
     * </p>
     * 
     * <p>
     * In most situations it is ok to check for any version below the newest major version. (getApiVersion() &lt; APIVERSION_2_0_0).
     * </p>
     * 
     * @return api version.
     * 
     * @see #APIVERSION_1_0_0
     * @see #APIVERSION_1_1_0
     * @see #APIVERSION_2_0_0
     * @see ApiVersion
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
     * Returns the main locales available on this server. This is for information only.
     * @return main locales.
     */
    Collection<Locale> getMainLocales();
    
    /**
     * Removes a main locale
     * @param locale
     * @throws McException thrown if config cannot be saved or if this is the last main locale.
     */
    void removeMainLocale(Locale locale) throws McException;
    
    /**
     * Adds a main locale
     * @param locale
     * @throws McException thrown if config cannot be saved
     */
    void addMainLocale(Locale locale) throws McException;
    
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
     * Registers a new event class for internal event bus
     * @param plugin
     * @param clazz
     */
    <Evt extends Event & MinecraftEvent<Evt, Evt>> void registerEvent(Plugin plugin, Class<Evt> clazz);
    
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
    
    // support for yml files
    
    /**
     * Reads a yml file into a data section.
     * @param file
     * @return yml data file; implements {@link CommentableDataSection}.
     * @throws IOException
     */
    DataSection readYmlFile(File file) throws IOException;
    
    /**
     * Reads a yml file into a data section.
     * @param file
     * @return yml data file; implements {@link CommentableDataSection}.
     * @throws IOException
     */
    DataSection readYmlFile(InputStream file) throws IOException;
    
    /**
     * Saves given data section into given file.
     * @param section
     * @param file
     * @throws IOException
     */
    void saveYmlFile(DataSection section, File file) throws IOException;
    
}
