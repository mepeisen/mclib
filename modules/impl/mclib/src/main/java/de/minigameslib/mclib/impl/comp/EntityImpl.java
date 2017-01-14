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

package de.minigameslib.mclib.impl.comp;

import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.McListener;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.objects.EntityHandlerInterface;
import de.minigameslib.mclib.api.objects.EntityIdInterface;
import de.minigameslib.mclib.api.objects.EntityInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;
import de.minigameslib.mclib.impl.EventBus;
import de.minigameslib.mclib.nms.api.MgEventListener;

/**
 * @author mepeisen
 *
 */
public class EntityImpl implements EntityInterface, MgEventListener
{
    
    /** an event bus to handle events. */
    private final EventBus                      eventBus         = new EventBus();
    
    @Override
    public EntityIdInterface getEntityId()
    {
        // TODO support entities
        return null;
    }
    
    @Override
    public void delete() throws McException
    {
        // TODO support entities
    }
    
    @Override
    public void saveConfig() throws McException
    {
        // TODO support entities
    }

    /**
     * @return handler
     */
    public EntityHandlerInterface getHandler()
    {
        // TODO support entities
        return null;
    }

    /**
     * Clears all event registrations
     */
    public void clearEventRegistrations()
    {
        this.eventBus.clear();
    }
    
    @Override
    public <Evt extends MinecraftEvent<?, Evt>> void registerHandler(Plugin plugin, Class<Evt> clazz, McConsumer<Evt> h)
    {
        this.eventBus.registerHandler(plugin, clazz, h);
    }
    
    @Override
    public void registerHandlers(Plugin plugin, McListener listener)
    {
        this.eventBus.registerHandlers(plugin, listener);
    }
    
    @Override
    public <Evt extends MinecraftEvent<?, Evt>> void unregisterHandler(Plugin plugin, Class<Evt> clazz, McConsumer<Evt> h)
    {
        this.eventBus.unregisterHandler(plugin, clazz, h);
    }
    
    @Override
    public void unregisterHandlers(Plugin plugin, McListener listener)
    {
        this.eventBus.unregisterHandlers(plugin, listener);
    }
    
    @Override
    public <T extends Event, Evt extends MinecraftEvent<T, Evt>> void handle(Class<T> eventClass, Evt event)
    {
        this.eventBus.handle(eventClass, event);
    }
    
    /**
     * Plugin disable
     * @param plugin
     */
    public void onDisable(Plugin plugin)
    {
        this.eventBus.onDisable(plugin);
    }
    
}
