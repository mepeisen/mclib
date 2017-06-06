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

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.McEventHandler;
import de.minigameslib.mclib.api.event.McListener;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.util.function.McConsumer;

/**
 * Minecraft holograms controlled by plugins.
 * 
 * @author mepeisen
 */
public interface HologramInterface
{
    
    /**
     * Returns the unique id of this hologram.
     * 
     * @return object id.
     */
    HologramIdInterface getHologramId();
    
    /**
     * Returns the type id.
     * 
     * @return type id of this hologram
     */
    HologramTypeId getTypeId();
    
    /**
     * Deletes this hologram.
     * 
     * @throws McException
     *             thrown if the hologram cannot be deleted.
     */
    void delete() throws McException;
    
    /**
     * Saves the configuration after handler changed data.
     * 
     * @throws McException
     *             thrown if the config cannot be saved
     */
    void saveConfig() throws McException;
    
    /**
     * Returns the handler.
     * 
     * @return associated handler.
     */
    HologramHandlerInterface getHandler();
    
    /**
     * Returns the hologram location.
     * 
     * @return hologram location.
     */
    Location getLocation();
    
    /**
     * Changes the hologram location
     * 
     * @param location
     *            the new location
     * @throws McException
     *             thrown if the location cannot be changed.
     */
    void setLocation(Location location) throws McException;
    
    /**
     * Updates lines and resent them to clients.
     */
    void updateLines();
    
    /**
     * Replaces lines with given strings.
     * 
     * @param strings
     *            text to display; maybe a String or a localized message.
     */
    void setLines(Serializable... strings);
    
    /**
     * Returns the hologram lines.
     * 
     * @return hologram lines.
     */
    List<Serializable> getLines();
    
    /**
     * Adds a line to be displayed.
     * 
     * @param string
     *            text to display; maybe a String or a localized message.
     */
    void addLine(Serializable string);
    
    /**
     * Removes a line.
     * 
     * @param lineNumber
     *            line to remove; starting with 1.
     */
    void removeLine(int lineNumber);
    
    /**
     * Changes line.
     * 
     * @param lineNumber
     *            line to change; starting with 1.
     * @param string
     *            text to display; maybe a String or a localized message.
     */
    void changeLine(int lineNumber, Serializable string);
    
    /**
     * Removes all hologram lines.
     */
    void clearLines();
    
    /**
     * Adds players to whitelist.
     * 
     * <p>
     * Whitelisted players see holograms that are hidden.
     * </p>
     * 
     * @param uuid
     *            player to whitelist
     * @throws McException 
     *             thrown if config cannot be saved.
     */
    void addPlayerToWhiteList(UUID uuid) throws McException;
    
    /**
     * Remove player from whitelist.
     * 
     * @param uuid
     *            player to be removed.
     * @throws McException 
     *             thrown if config cannot be saved.
     */
    void removePlayerFromWhiteList(UUID uuid) throws McException;
    
    /**
     * Returns the players from whitelist.
     * 
     * @return whitelisted players.
     */
    List<UUID> getWhitelist();
    
    /**
     * Checks if hologram is visible to public.
     * 
     * @return {@code true} if only the players from whitelist may see the hologram; {@code false} if all players see the hologram.
     */
    boolean isHidden();
    
    /**
     * Hides the hologram except for players on the whitelist.
     * 
     * @throws McException
     *             thrown if config cannot be saved.
     */
    void hide() throws McException;
    
    /**
     * Shows hologram for all players.
     * 
     * @throws McException
     *             thrown if config cannot be saved.
     */
    void show() throws McException;
    
    // event system
    
    /**
     * Register hologram related event handlers only active if this hologram is involved in events.
     * 
     * @param <EVT>
     *            event class
     * @param plugin
     *            plugin that registers the handler
     * @param clazz
     *            event class
     * @param handler
     *            handler to be invoked on event
     */
    <EVT extends MinecraftEvent<?, EVT>> void registerHandler(Plugin plugin, Class<EVT> clazz, McConsumer<EVT> handler);
    
    /**
     * Registers an event handler object for events on this hologram. Methods tagged with McEventHandler are considered as event handlers.
     * 
     * @param plugin
     *            plugin that registers the handler
     * @param listener
     *            listener class having methods tagged with {@link McEventHandler}
     */
    void registerHandlers(Plugin plugin, McListener listener);
    
    /**
     * Remove a registered event handler.
     * 
     * @param <EVT>
     *            event class
     * @param plugin
     *            plugin that removes the handler
     * @param clazz
     *            event class
     * @param handler
     *            handler to be invoked on event
     */
    <EVT extends MinecraftEvent<?, EVT>> void unregisterHandler(Plugin plugin, Class<EVT> clazz, McConsumer<EVT> handler);
    
    /**
     * Remove a registered event handler.
     * 
     * @param plugin
     *            plugin that removes the handler
     * @param listener
     *            listener class having methods tagged with {@link McEventHandler}
     */
    void unregisterHandlers(Plugin plugin, McListener listener);
    
}
