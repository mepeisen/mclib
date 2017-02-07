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

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McLibInterface;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.objects.ComponentInterface;
import de.minigameslib.mclib.api.objects.EntityInterface;
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
    private final Map<Class<? extends Event>, MinecraftEventHandler<?, ?>> eventHandlers = new HashMap<>();
    
    /** common event listeners. */
    public final List<MgEventListener> listeners = new ArrayList<>();
    
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

    /**
     * Returns the minigame event handler for given class.
     * 
     * @param clazz
     *            event class.
     * @return event handler.
     */
    @SuppressWarnings("unchecked")
    protected <T extends Event, MgEvt extends MinecraftEvent<T, MgEvt>> MinecraftEventHandler<T, MgEvt> getHandler(Class<T> clazz)
    {
        return (MinecraftEventHandler<T, MgEvt>) this.eventHandlers.get(clazz);
    }
    
    /**
     * Registers the minigame event handler for given class.
     * 
     * @param clazz
     *            event class.
     * @param factory
     *            the factory to create minigame events.
     */
    protected <T extends Event, MgEvt extends MinecraftEvent<T, MgEvt>> void registerHandler(Class<T> clazz, MinecraftEventFactory<T> factory)
    {
        this.eventHandlers.put(clazz, new MinecraftEventHandler<>(clazz, factory));
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <Evt extends Event, MgEvt extends MinecraftEvent<Evt, MgEvt>> MgEvt createEvent(Evt bukkitEvent)
    {
        return ((MinecraftEventHandler<Evt, MgEvt>) this.getHandler(bukkitEvent.getClass())).createMgEvent(bukkitEvent);
    }
    
    @Override
    public <Evt extends Event & MinecraftEvent<Evt, Evt>> void registerEvent(Plugin plugin, Class<Evt> clazz)
    {
        Bukkit.getPluginManager().registerEvent(clazz, this, EventPriority.NORMAL, new EventExecutor() {
            
            @Override
            public void execute(Listener paramListener, Event paramEvent) throws EventException
            {
                getHandler(clazz).handle(clazz.cast(paramEvent));
            }
        }, plugin);
        this.registerHandler(clazz, evt -> evt);
    }

    /**
     * The minigame event handler.
     * 
     * @author mepeisen
     * @param <T>
     *            event clazz for handling the events.
     * @param <MgEvt>
     *            event clazz for handling the events.
     */
    protected final class MinecraftEventHandler<T extends Event, MgEvt extends MinecraftEvent<T, MgEvt>>
    {
        
        /** the event factory. */
        private MinecraftEventFactory<T> factory;
        /** event class. */
        private Class<T> cls;

        /**
         * Constructor.
         * @param clazz event class
         * @param factory mg event factory
         */
        public MinecraftEventHandler(Class<T> clazz, MinecraftEventFactory<T> factory)
        {
            this.cls = clazz;
            this.factory = factory;
        }
        
        /**
         * Handles minigame event.
         * @param evt
         */
        public void handle(T evt)
        {
            final McLibInterface mclib = McLibInterface.instance();
            final MgEvt mgevt = this.createMgEvent(evt);
            for (MgEventListener listener : AbstractEventSystem.this.listeners)
            {
                try
                {
                    mclib.runInNewContext(() -> {
                        mclib.setContext(Event.class, evt);
                        listener.handle(this.cls, mgevt);
                    });
                }
                catch (McException ex)
                {
                    // TODO logging
                }
            }
            
            for (final McPlayerInterface player : mgevt.getPlayers())
            {
                if (player instanceof MgEventListener)
                {
                    try
                    {
                        mclib.runInNewContext(() -> {
                            mclib.setContext(Event.class, evt);
                            mclib.setContext(McPlayerInterface.class, player);
                            mclib.setContext(ZoneInterface.class, mgevt.getZone());
                            mclib.setContext(ComponentInterface.class, mgevt.getComponent());
                            mclib.setContext(ObjectInterface.class, mgevt.getObject());
                            mclib.setContext(SignInterface.class, mgevt.getSign());
                            mclib.setContext(EntityInterface.class, mgevt.getEntity());
                            ((MgEventListener) player).handle(this.cls, mgevt);
                        });
                    }
                    catch (McException ex)
                    {
                        // TODO logging
                    }
                }
            }
            
            for (final ZoneInterface arena : mgevt.getZones())
            {
                if (arena instanceof MgEventListener)
                {
                    try
                    {
                        mclib.runInNewContext(() -> {
                            mclib.setContext(Event.class, evt);
                            mclib.setContext(McPlayerInterface.class, mgevt.getPlayer());
                            mclib.setContext(ZoneInterface.class, arena);
                            mclib.setContext(ComponentInterface.class, mgevt.getComponent());
                            mclib.setContext(ObjectInterface.class, mgevt.getObject());
                            mclib.setContext(SignInterface.class, mgevt.getSign());
                            mclib.setContext(EntityInterface.class, mgevt.getEntity());
                            ((MgEventListener) arena).handle(this.cls, mgevt);
                        });
                    }
                    catch (McException ex)
                    {
                        // TODO logging
                    }
                }
            }
            
            for (final SignInterface sign : mgevt.getSigns())
            {
                if (sign instanceof MgEventListener)
                {
                    try
                    {
                        mclib.runInNewContext(() -> {
                            mclib.setContext(Event.class, evt);
                            mclib.setContext(McPlayerInterface.class, mgevt.getPlayer());
                            mclib.setContext(ZoneInterface.class, mgevt.getZone());
                            mclib.setContext(ComponentInterface.class, mgevt.getComponent());
                            mclib.setContext(ObjectInterface.class, mgevt.getObject());
                            mclib.setContext(SignInterface.class, sign);
                            mclib.setContext(EntityInterface.class, mgevt.getEntity());
                            ((MgEventListener) sign).handle(this.cls, mgevt);
                        });
                    }
                    catch (McException ex)
                    {
                        // TODO logging
                    }
                }
            }
            
            for (final EntityInterface entity : mgevt.getEntities())
            {
                if (entity instanceof MgEventListener)
                {
                    try
                    {
                        mclib.runInNewContext(() -> {
                            mclib.setContext(Event.class, evt);
                            mclib.setContext(McPlayerInterface.class, mgevt.getPlayer());
                            mclib.setContext(ZoneInterface.class, mgevt.getZone());
                            mclib.setContext(ComponentInterface.class, mgevt.getComponent());
                            mclib.setContext(ObjectInterface.class, mgevt.getObject());
                            mclib.setContext(SignInterface.class, mgevt.getSign());
                            mclib.setContext(EntityInterface.class, entity);
                            ((MgEventListener) entity).handle(this.cls, mgevt);
                        });
                    }
                    catch (McException ex)
                    {
                        // TODO logging
                    }
                }
            }
            
            for (final ComponentInterface comp : mgevt.getComponents())
            {
                if (comp instanceof MgEventListener)
                {
                    try
                    {
                        mclib.runInNewContext(() -> {
                            mclib.setContext(Event.class, evt);
                            mclib.setContext(McPlayerInterface.class, mgevt.getPlayer());
                            mclib.setContext(ZoneInterface.class, mgevt.getZone());
                            mclib.setContext(ComponentInterface.class, comp);
                            mclib.setContext(ObjectInterface.class, mgevt.getObject());
                            mclib.setContext(SignInterface.class, mgevt.getSign());
                            mclib.setContext(EntityInterface.class, mgevt.getEntity());
                            ((MgEventListener) comp).handle(this.cls, mgevt);
                        });
                    }
                    catch (McException ex)
                    {
                        // TODO logging
                    }
                }
            }
            
            for (final ObjectInterface obj : mgevt.getObjects())
            {
                if (obj instanceof MgEventListener)
                {
                    try
                    {
                        mclib.runInNewContext(() -> {
                            mclib.setContext(Event.class, evt);
                            mclib.setContext(McPlayerInterface.class, mgevt.getPlayer());
                            mclib.setContext(ZoneInterface.class, mgevt.getZone());
                            mclib.setContext(ComponentInterface.class, mgevt.getComponent());
                            mclib.setContext(ObjectInterface.class, obj);
                            mclib.setContext(SignInterface.class, mgevt.getSign());
                            mclib.setContext(EntityInterface.class, mgevt.getEntity());
                            ((MgEventListener) obj).handle(this.cls, mgevt);
                        });
                    }
                    catch (McException ex)
                    {
                        // TODO logging
                    }
                }
            }
        }
        
        /**
         * Creates minigame event.
         * @param evt bukkit event
         * @return minigame event.
         */
        @SuppressWarnings("unchecked")
        public MgEvt createMgEvent(T evt)
        {
            return (MgEvt) this.factory.create(evt);
        }
        
    }
    
    /**
     * Interface for creating minigame event classes from given bukkit event.
     *
     * @param <Evt>
     */
    @FunctionalInterface
    public interface MinecraftEventFactory<Evt extends Event>
    {
        
        /**
         * Creates the minigame event.
         * 
         * @param event
         *            bukkit event.
         * @return the minigame event object.
         */
        MinecraftEvent<?, ?> create(Evt event);
        
    }
    
}
