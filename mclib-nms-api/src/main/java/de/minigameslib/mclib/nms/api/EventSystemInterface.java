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

import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.event.MinecraftEvent;

/**
 * Generic event system.
 * 
 * @author mepeisen
 */
public interface EventSystemInterface extends Listener
{
    
    /**
     * Creates a minigame event from given bukkit event.
     * 
     * @param <EVT>
     *            bukkit event class
     * @param <MGEVT>
     *            mclib event class
     * @param bukkitEvent
     *            bukkit event
     * @return minigame event.
     */
    <EVT extends Event, MGEVT extends MinecraftEvent<EVT, MGEVT>> MGEVT createEvent(EVT bukkitEvent);
    
    /**
     * Adds a new event listener.
     * 
     * @param listener
     *            listener to be added.
     */
    void addEventListener(MgEventListener listener);
    
    /**
     * Registers a new event class for event system
     * 
     * @param <EVT>
     *            bukkit event class
     * @param plugin
     *            plugin ownung the event class.
     * @param clazz
     *            bukkit event class
     */
    <EVT extends Event & MinecraftEvent<EVT, EVT>> void registerEvent(Plugin plugin, Class<EVT> clazz);
    
    /**
     * Creates a new event bus.
     * 
     * @return event bus implementation
     */
    EventBus createEventBus();
    
}
