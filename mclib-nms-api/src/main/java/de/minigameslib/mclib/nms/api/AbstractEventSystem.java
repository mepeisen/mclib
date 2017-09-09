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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.objects.ComponentInterface;
import de.minigameslib.mclib.api.objects.EntityInterface;
import de.minigameslib.mclib.api.objects.HologramInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectInterface;
import de.minigameslib.mclib.api.objects.SignInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;

/**
 * Abstract base class for event systems.
 * 
 * @author mepeisen
 */
public abstract class AbstractEventSystem implements EventSystemInterface
{
    
    /**
     * The common event handlers per event class.
     */
    private final Map<Class<? extends Event>, MinecraftEventHandler<?, ?>>           eventHandlers = new HashMap<>();
    
    /** common event listeners. */
    public final List<MgEventListener>                                               listeners     = new ArrayList<>();
    
    /**
     * the event executors.
     */
    private final Map<Class<? extends Event>, EventExecutor>                         executors     = new ConcurrentHashMap<>();
    
    /**
     * mapping from mclib event classes to bukkit event classes.
     */
    private final Map<Class<? extends MinecraftEvent<?, ?>>, Class<? extends Event>> classMap      = new HashMap<>();
    
    /** logger. */
    protected static final Logger                                                    LOGGER        = Logger.getLogger(AbstractEventSystem.class.getName());
    
    /**
     * Constructor.
     */
    public AbstractEventSystem()
    {
        // empty
    }
    
    @Override
    public void addEventListener(MgEventListener listener)
    {
        this.listeners.add(listener);
    }
    
    @Override
    public EventBus createEventBus()
    {
        return new EventBus(this);
    }
    
    /**
     * Register class mapping.
     * 
     * @param src
     *            source class (mclib event class)
     * @param target
     *            target class (bukkit/spigot event class)
     */
    protected void registerMapping(Class<? extends MinecraftEvent<?, ?>> src, Class<? extends Event> target)
    {
        this.classMap.put(src, target);
    }
    
    /**
     * Register an event class.
     * 
     * @param <EVT>
     *            minecraft event class
     * @param clazz
     *            minecraft event class
     */
    <EVT extends MinecraftEvent<?, EVT>> void registerEventClass(Class<EVT> clazz)
    {
        this.registerEventClassEx(this.classMap.get(clazz));
    }
    
    /**
     * Registers an event class for listening.
     * 
     * @param clazz
     *            bukkit event class.
     */
    void registerEventClassEx(Class<? extends Event> clazz)
    {
        this.executors.computeIfAbsent(clazz, c ->
        {
            final EventExecutor executor = new EventExecutor() {
                
                @Override
                public void execute(Listener listener, Event event) throws EventException
                {
                    if (!clazz.isInstance(event))
                    {
                        // some event classes inherit listeners from parent event.
                        // for example: Registering PlayerDeathEvent actually uses handlers from EntityDeathEvent
                        // and therefor it catches "EntityDeathEvent" object although not being interested in them.
                        return;
                    }
                    this.handle(event);
                }
                
                private <T extends Event, MGEVT extends MinecraftEvent<T, MGEVT>> void handle(T event)
                {
                    @SuppressWarnings("unchecked")
                    final Class<T> clazz2 = (Class<T>) clazz;
                    final MinecraftEventHandler<T, MGEVT> handler = AbstractEventSystem.this.getHandler(clazz2);
                    handler.handle(event);
                }
                
            };
            Bukkit.getPluginManager().registerEvent(clazz, this, EventPriority.HIGHEST, executor, (JavaPlugin) McLibInterface.instance(), true);
            return executor;
        });
    }
    
    /**
     * Returns the minigame event handler for given class.
     * 
     * @param <T>
     *            minecraft event class
     * @param <MGEVT>
     *            mclib event class
     * @param clazz
     *            event class.
     * @return event handler.
     */
    @SuppressWarnings("unchecked")
    protected <T extends Event, MGEVT extends MinecraftEvent<T, MGEVT>> MinecraftEventHandler<T, MGEVT> getHandler(Class<T> clazz)
    {
        return (MinecraftEventHandler<T, MGEVT>) this.eventHandlers.get(clazz);
    }
    
    /**
     * Registers the minigame event handler for given class.
     * 
     * @param <T>
     *            event class.
     * @param <MGEVT>
     *            minecraft event class
     * @param clazz
     *            event class.
     * @param mgclazz
     *            minecraft event class
     * @param factory
     *            the factory to create minigame events.
     */
    protected <T extends Event, MGEVT extends MinecraftEvent<T, MGEVT>> void registerHandler(Class<T> clazz, Class<MGEVT> mgclazz, MinecraftEventFactory<T> factory)
    {
        this.classMap.put(mgclazz, clazz);
        this.eventHandlers.put(clazz, new MinecraftEventHandler<>(clazz, mgclazz, factory));
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <EVT extends Event, MGEVT extends MinecraftEvent<EVT, MGEVT>> MGEVT createEvent(EVT bukkitEvent)
    {
        return ((MinecraftEventHandler<EVT, MGEVT>) this.getHandler(bukkitEvent.getClass())).createMgEvent(bukkitEvent);
    }
    
    @Override
    public <EVT extends Event & MinecraftEvent<EVT, EVT>> void registerEvent(Plugin plugin, Class<EVT> clazz)
    {
        // Bukkit.getPluginManager().registerEvent(clazz, this, EventPriority.NORMAL, new EventExecutor() {
        //
        // @Override
        // public void execute(Listener paramListener, Event paramEvent) throws EventException
        // {
        // getHandler(clazz).handle(clazz.cast(paramEvent));
        // }
        // }, plugin);
        this.registerHandler(clazz, clazz, evt -> evt);
    }
    
    /**
     * The minigame event handler.
     * 
     * @author mepeisen
     * @param <T>
     *            event clazz for handling the events.
     * @param <MGEVT>
     *            event clazz for handling the events.
     */
    protected final class MinecraftEventHandler<T extends Event, MGEVT extends MinecraftEvent<T, MGEVT>>
    {
        
        /** the event factory. */
        private MinecraftEventFactory<T> factory;
        /** event class. */
        @SuppressWarnings("unused")
        private Class<T>                 cls;
        /** event class. */
        private Class<MGEVT>             mgcls;
        
        /**
         * Constructor.
         * 
         * @param clazz
         *            event class
         * @param mgclazz
         *            mclib event class
         * @param factory
         *            mg event factory
         */
        public MinecraftEventHandler(Class<T> clazz, Class<MGEVT> mgclazz, MinecraftEventFactory<T> factory)
        {
            this.cls = clazz;
            this.mgcls = mgclazz;
            this.factory = factory;
        }
        
        /**
         * Handles minigame event.
         * 
         * @param evt
         *            minecraft event
         */
        public void handle(T evt)
        {
            final McLibInterface mclib = McLibInterface.instance();
            final MGEVT mgevt = this.createMgEvent(evt);
            for (MgEventListener listener : AbstractEventSystem.this.listeners)
            {
                try
                {
                    mclib.runInNewContext(() ->
                    {
                        mclib.setContext(Event.class, evt);
                        listener.handle(this.mgcls, mgevt);
                    });
                }
                catch (McException ex)
                {
                    LOGGER.log(Level.WARNING, "Unhandled exception while distributing event " + evt + " to " + listener, ex); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
            
            for (final McPlayerInterface player : mgevt.getPlayers())
            {
                if (player instanceof MgEventListener)
                {
                    try
                    {
                        mclib.runInNewContext(() ->
                        {
                            mclib.setContext(Event.class, evt);
                            mclib.setContext(McPlayerInterface.class, player);
                            mclib.setContext(ZoneInterface.class, mgevt.getZone());
                            mclib.setContext(ComponentInterface.class, mgevt.getComponent());
                            mclib.setContext(ObjectInterface.class, mgevt.getObject());
                            mclib.setContext(SignInterface.class, mgevt.getSign());
                            mclib.setContext(EntityInterface.class, mgevt.getEntity());
                            ((MgEventListener) player).handle(this.mgcls, mgevt);
                        });
                    }
                    catch (McException ex)
                    {
                        LOGGER.log(Level.WARNING, "Unhandled exception while distributing event " + evt + " to " + player, ex); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                }
            }
            
            for (final ZoneInterface arena : mgevt.getZones())
            {
                if (arena instanceof MgEventListener)
                {
                    try
                    {
                        mclib.runInNewContext(() ->
                        {
                            mclib.setContext(Event.class, evt);
                            mclib.setContext(McPlayerInterface.class, mgevt.getPlayer());
                            mclib.setContext(ZoneInterface.class, arena);
                            mclib.setContext(ComponentInterface.class, mgevt.getComponent());
                            mclib.setContext(ObjectInterface.class, mgevt.getObject());
                            mclib.setContext(SignInterface.class, mgevt.getSign());
                            mclib.setContext(EntityInterface.class, mgevt.getEntity());
                            ((MgEventListener) arena).handle(this.mgcls, mgevt);
                        });
                    }
                    catch (McException ex)
                    {
                        LOGGER.log(Level.WARNING, "Unhandled exception while distributing event " + evt + " to " + arena, ex); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                }
            }
            
            for (final SignInterface sign : mgevt.getSigns())
            {
                if (sign instanceof MgEventListener)
                {
                    try
                    {
                        mclib.runInNewContext(() ->
                        {
                            mclib.setContext(Event.class, evt);
                            mclib.setContext(McPlayerInterface.class, mgevt.getPlayer());
                            mclib.setContext(ZoneInterface.class, mgevt.getZone());
                            mclib.setContext(ComponentInterface.class, mgevt.getComponent());
                            mclib.setContext(ObjectInterface.class, mgevt.getObject());
                            mclib.setContext(SignInterface.class, sign);
                            mclib.setContext(EntityInterface.class, mgevt.getEntity());
                            ((MgEventListener) sign).handle(this.mgcls, mgevt);
                        });
                    }
                    catch (McException ex)
                    {
                        LOGGER.log(Level.WARNING, "Unhandled exception while distributing event " + evt + " to " + sign, ex); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                }
            }
            
            for (final EntityInterface entity : mgevt.getEntities())
            {
                if (entity instanceof MgEventListener)
                {
                    try
                    {
                        mclib.runInNewContext(() ->
                        {
                            mclib.setContext(Event.class, evt);
                            mclib.setContext(McPlayerInterface.class, mgevt.getPlayer());
                            mclib.setContext(ZoneInterface.class, mgevt.getZone());
                            mclib.setContext(ComponentInterface.class, mgevt.getComponent());
                            mclib.setContext(ObjectInterface.class, mgevt.getObject());
                            mclib.setContext(SignInterface.class, mgevt.getSign());
                            mclib.setContext(EntityInterface.class, entity);
                            ((MgEventListener) entity).handle(this.mgcls, mgevt);
                        });
                    }
                    catch (McException ex)
                    {
                        LOGGER.log(Level.WARNING, "Unhandled exception while distributing event " + evt + " to " + entity, ex); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                }
            }
            
            for (final ComponentInterface comp : mgevt.getComponents())
            {
                if (comp instanceof MgEventListener)
                {
                    try
                    {
                        mclib.runInNewContext(() ->
                        {
                            mclib.setContext(Event.class, evt);
                            mclib.setContext(McPlayerInterface.class, mgevt.getPlayer());
                            mclib.setContext(ZoneInterface.class, mgevt.getZone());
                            mclib.setContext(ComponentInterface.class, comp);
                            mclib.setContext(ObjectInterface.class, mgevt.getObject());
                            mclib.setContext(SignInterface.class, mgevt.getSign());
                            mclib.setContext(EntityInterface.class, mgevt.getEntity());
                            ((MgEventListener) comp).handle(this.mgcls, mgevt);
                        });
                    }
                    catch (McException ex)
                    {
                        LOGGER.log(Level.WARNING, "Unhandled exception while distributing event " + evt + " to " + comp, ex); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                }
            }
            
            for (final ObjectInterface obj : mgevt.getObjects())
            {
                if (obj instanceof MgEventListener)
                {
                    try
                    {
                        mclib.runInNewContext(() ->
                        {
                            mclib.setContext(Event.class, evt);
                            mclib.setContext(McPlayerInterface.class, mgevt.getPlayer());
                            mclib.setContext(ZoneInterface.class, mgevt.getZone());
                            mclib.setContext(ComponentInterface.class, mgevt.getComponent());
                            mclib.setContext(ObjectInterface.class, obj);
                            mclib.setContext(SignInterface.class, mgevt.getSign());
                            mclib.setContext(EntityInterface.class, mgevt.getEntity());
                            ((MgEventListener) obj).handle(this.mgcls, mgevt);
                        });
                    }
                    catch (McException ex)
                    {
                        LOGGER.log(Level.WARNING, "Unhandled exception while distributing event " + evt + " to " + obj, ex); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                }
            }
            
            for (final HologramInterface hologram : mgevt.getHolograms())
            {
                if (hologram instanceof MgEventListener)
                {
                    try
                    {
                        mclib.runInNewContext(() ->
                        {
                            mclib.setContext(Event.class, evt);
                            mclib.setContext(McPlayerInterface.class, mgevt.getPlayer());
                            mclib.setContext(ZoneInterface.class, mgevt.getZone());
                            mclib.setContext(ComponentInterface.class, mgevt.getComponent());
                            mclib.setContext(HologramInterface.class, hologram);
                            mclib.setContext(SignInterface.class, mgevt.getSign());
                            mclib.setContext(EntityInterface.class, mgevt.getEntity());
                            ((MgEventListener) hologram).handle(this.mgcls, mgevt);
                        });
                    }
                    catch (McException ex)
                    {
                        LOGGER.log(Level.WARNING, "Unhandled exception while distributing event " + evt + " to " + hologram, ex); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                }
            }
        }
        
        /**
         * Creates minigame event.
         * 
         * @param evt
         *            bukkit event
         * @return minigame event.
         */
        @SuppressWarnings("unchecked")
        public MGEVT createMgEvent(T evt)
        {
            return (MGEVT) this.factory.create(evt);
        }
        
    }
    
    /**
     * Interface for creating minigame event classes from given bukkit event.
     *
     * @param <EVT>
     *            minecraft event class
     */
    @FunctionalInterface
    public interface MinecraftEventFactory<EVT extends Event>
    {
        
        /**
         * Creates the minigame event.
         * 
         * @param event
         *            bukkit event.
         * @return the minigame event object.
         */
        MinecraftEvent<?, ?> create(EVT event);
        
    }
    
}
