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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.McEventHandler;
import de.minigameslib.mclib.api.event.McListener;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.util.function.McConsumer;

/**
 * Helper class for event dispatching.
 * 
 * @author mepeisen
 */
public class EventBus
{
    
    /** event handlers per plugin. */
    private final Map<String, Set<EventHandler<?>>>             eventHandlersPerPlugin = new HashMap<>();
    
    /** event handlers per class. */
    private final Map<Class<?>, Set<EventHandler<?>>>           eventHandlersPerClass  = new HashMap<>();
    
    /** the event descriptors per class. */
    private static final Map<Class<?>, Set<EventDescriptor<?>>> eventDescriptors       = new HashMap<>();
    
    /** the abstract event system owning this event bus */
    private final AbstractEventSystem                           eventSystem;
    
    /**
     * Secured constructor
     * 
     * @param eventSystem
     *            the abstract event system
     */
    EventBus(AbstractEventSystem eventSystem)
    {
        this.eventSystem = eventSystem;
    }
    
    /**
     * Clears tegistered handlers.
     */
    public void clear()
    {
        this.eventHandlersPerClass.clear();
        this.eventHandlersPerPlugin.clear();
    }
    
    /**
     * Registers handler
     * 
     * @param plugin
     * @param clazz
     * @param handler
     */
    public <Evt extends MinecraftEvent<?, Evt>> void registerHandler(Plugin plugin, Class<Evt> clazz, McConsumer<Evt> handler)
    {
        final EventHandler<Evt> eventHandler = new EventHandler<>(plugin.getName(), clazz, handler, null);
        this.eventHandlersPerPlugin.computeIfAbsent(eventHandler.pluginName, k -> new HashSet<>()).add(eventHandler);
        this.eventHandlersPerClass.computeIfAbsent(eventHandler.eventClass, k -> new HashSet<>()).add(eventHandler);
        this.eventSystem.registerEventClass(clazz);
    }
    
    /**
     * Calculates the descriptors
     * 
     * @param clazz
     * @return descriptors
     */
    private Set<EventDescriptor<?>> calculateDescriptors(Class<?> clazz)
    {
        Set<Method> methods;
        try
        {
            Method[] publicMethods = clazz.getMethods();
            Method[] privateMethods = clazz.getDeclaredMethods();
            methods = new HashSet<>(publicMethods.length + privateMethods.length, 1.0f);
            for (Method method : publicMethods)
            {
                methods.add(method);
            }
            for (Method method : privateMethods)
            {
                methods.add(method);
            }
        }
        catch (NoClassDefFoundError e)
        {
            throw new IllegalStateException(e);
        }
        final Set<EventDescriptor<?>> result = new HashSet<>();
        for (final Method method : methods)
        {
            final McEventHandler eh = method.getAnnotation(McEventHandler.class);
            if (eh == null)
                continue;
            // Do not register bridge or synthetic methods to avoid event duplication
            // Fixes SPIGOT-893
            if (method.isBridge() || method.isSynthetic())
            {
                continue;
            }
            final Class<?> checkClass;
            if (method.getParameterTypes().length != 1 || !MinecraftEvent.class.isAssignableFrom(checkClass = method.getParameterTypes()[0]))
            {
                throw new IllegalStateException("attempted to register an invalid EventHandler method signature \"" + method.toGenericString() + "\" in " + clazz); //$NON-NLS-1$//$NON-NLS-2$
            }
            @SuppressWarnings("rawtypes")
            final Class<? extends MinecraftEvent> eventClass = checkClass.asSubclass(MinecraftEvent.class);
            method.setAccessible(true);
            
            result.add(new EventDescriptor<>(eventClass, method));
        }
        return result;
    }
    
    /**
     * registers with given listener
     * 
     * @param plugin
     * @param desc
     * @param listener
     */
    private <Evt extends MinecraftEvent<?, Evt>> void register(Plugin plugin, EventDescriptor<Evt> desc, McListener listener)
    {
        final EventHandler<Evt> eventHandler = new EventHandler<>(plugin.getName(), desc.eventClass, evt -> desc.handle(listener, evt), listener);
        this.eventHandlersPerPlugin.computeIfAbsent(eventHandler.pluginName, k -> new HashSet<>()).add(eventHandler);
        this.eventHandlersPerClass.computeIfAbsent(eventHandler.eventClass, k -> new HashSet<>()).add(eventHandler);
        this.eventSystem.registerEventClass(desc.eventClass);
    }
    
    /**
     * Registers handler
     * 
     * @param plugin
     * @param listener
     */
    public void registerHandlers(Plugin plugin, McListener listener)
    {
        for (final EventDescriptor<?> desc : eventDescriptors.computeIfAbsent(listener.getClass(), this::calculateDescriptors))
        {
            this.register(plugin, desc, listener);
        }
    }
    
    /**
     * Unregisters handler
     * 
     * @param plugin
     * @param clazz
     * @param handler
     */
    public <Evt extends MinecraftEvent<?, Evt>> void unregisterHandler(Plugin plugin, Class<Evt> clazz, McConsumer<Evt> handler)
    {
        final EventHandler<Evt> eventHandler = new EventHandler<>(plugin.getName(), clazz, handler, null);
        this.eventHandlersPerPlugin.computeIfAbsent(eventHandler.pluginName, k -> new HashSet<>()).remove(eventHandler);
        this.eventHandlersPerClass.computeIfAbsent(eventHandler.eventClass, k -> new HashSet<>()).remove(eventHandler);
        // TODO this.eventSystem.unregisterEventClass
    }
    
    /**
     * Unregisters handler
     * 
     * @param plugin
     * @param listener
     */
    public void unregisterHandlers(Plugin plugin, McListener listener)
    {
        final String pluginName = plugin.getName();
        if (this.eventHandlersPerPlugin.containsKey(pluginName))
        {
            final List<EventHandler<?>> handlers = this.eventHandlersPerPlugin.get(pluginName).stream().filter(h -> h.listener == listener).collect(Collectors.toList());
            for (final EventHandler<?> handler : handlers)
            {
                this.eventHandlersPerPlugin.get(pluginName).remove(handler);
                this.eventHandlersPerClass.computeIfAbsent(handler.eventClass, k -> new HashSet<>()).remove(handler);
            }
        }
        // TODO this.eventSystem.unregisterEventClass
    }
    
    /**
     * Handles event
     * 
     * @param eventClass
     * @param event
     */
    @SuppressWarnings("unchecked")
    public <T extends Event, Evt extends MinecraftEvent<T, Evt>> void handle(Class<Evt> eventClass, Evt event)
    {
        final Set<EventHandler<?>> handlers = this.eventHandlersPerClass.get(eventClass);
        if (handlers != null)
        {
            for (final EventHandler<?> handler : handlers)
            {
                try
                {
                    ((McConsumer<Evt>) handler.handler).accept(event);
                }
                catch (McException e)
                {
                    // TODO Auto-generated catch block
                }
            }
        }
    }
    
    /**
     * event descriptor helper.
     * 
     * @author mepeisen
     * @param <Evt>
     */
    private static final class EventDescriptor<Evt extends MinecraftEvent<?, Evt>>
    {
        /** the event class. */
        public final Class<Evt> eventClass;
        /** java reflection method. */
        public final Method     method;
        
        /**
         * @param eventClass
         * @param method
         */
        public EventDescriptor(Class<Evt> eventClass, Method method)
        {
            this.eventClass = eventClass;
            this.method = method;
        }
        
        /**
         * Handles given event.
         * 
         * @param listener
         * @param event
         */
        public void handle(McListener listener, Evt event)
        {
            try
            {
                this.method.invoke(listener, event);
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
            {
                // TODO Auto-generated catch block
            }
        }
        
    }
    
    /**
     * Event handler impl.
     * 
     * @author mepeisen
     * @param <Evt>
     */
    private static final class EventHandler<Evt extends MinecraftEvent<?, Evt>>
    {
        /** the plugin name. */
        public final String          pluginName;
        /** the event class. */
        public final Class<Evt>      eventClass;
        /** the event handler. */
        public final McConsumer<Evt> handler;
        /** the minecraft listener object. */
        public final McListener      listener;
        
        /**
         * @param pluginName
         * @param eventClass
         * @param handler
         * @param listener
         */
        public EventHandler(String pluginName, Class<Evt> eventClass, McConsumer<Evt> handler, McListener listener)
        {
            this.pluginName = pluginName;
            this.eventClass = eventClass;
            this.handler = handler;
            this.listener = listener;
        }
        
        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((this.eventClass == null) ? 0 : this.eventClass.hashCode());
            result = prime * result + ((this.handler == null) ? 0 : this.handler.hashCode());
            result = prime * result + ((this.pluginName == null) ? 0 : this.pluginName.hashCode());
            return result;
        }
        
        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            EventHandler<?> other = (EventHandler<?>) obj;
            if (this.eventClass == null)
            {
                if (other.eventClass != null)
                    return false;
            }
            else if (!this.eventClass.equals(other.eventClass))
                return false;
            if (this.handler == null)
            {
                if (other.handler != null)
                    return false;
            }
            else if (!this.handler.equals(other.handler))
                return false;
            if (this.pluginName == null)
            {
                if (other.pluginName != null)
                    return false;
            }
            else if (!this.pluginName.equals(other.pluginName))
                return false;
            return true;
        }
    }
    
    /**
     * Plugin disable
     * 
     * @param plugin
     */
    public void onDisable(Plugin plugin)
    {
        final String pluginName = plugin.getName();
        // TODO remove class descriptors...
        if (this.eventHandlersPerPlugin.containsKey(pluginName))
        {
            for (final EventHandler<?> handler : this.eventHandlersPerPlugin.get(pluginName))
            {
                if (this.eventHandlersPerClass.containsKey(handler.eventClass))
                {
                    final Set<EventHandler<?>> set = this.eventHandlersPerClass.get(handler.eventClass);
                    set.remove(handler);
                }
            }
            this.eventHandlersPerPlugin.remove(pluginName);
        }
        
    }
    
}
