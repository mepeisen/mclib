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

import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.McEventHandler;
import de.minigameslib.mclib.api.event.McListener;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.util.function.McConsumer;

/**
 * Minecraft entites controlled by plugins.
 * 
 * @author mepeisen
 */
public interface EntityInterface
{
    
    /**
     * Returns the unique id of this entity.
     * 
     * @return entity id.
     */
    EntityIdInterface getEntityId();
    
    /**
     * Returns the type id.
     * 
     * @return type id of this object
     */
    EntityTypeId getTypeId();
    
    /**
     * Deletes this entity.
     * 
     * @throws McException
     *             thrown if the entity cannot be deleted.
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
     * Returns the bukkit entity.
     * 
     * @return bukkit entity.
     */
    Entity getBukkitEntity();
    
    /**
     * Returns the handler.
     * 
     * @return associated handler.
     */
    EntityHandlerInterface getHandler();
    
    // TODO Controlling entites
    
    // event system
    
    /**
     * Register entity related event handlers only active if this entity is involved in events.
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
     * Registers an event handler object for events on this entity. Methods tagged with McEventHandler are considered as event handlers.
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
