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

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;

import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.mcevent.EntityEnteredZoneEvent;
import de.minigameslib.mclib.api.mcevent.EntityEntersZoneEvent;
import de.minigameslib.mclib.api.mcevent.EntityLeavesZoneEvent;
import de.minigameslib.mclib.api.mcevent.EntityLeftZoneEvent;
import de.minigameslib.mclib.api.mcevent.PlayerCloseGuiEvent;
import de.minigameslib.mclib.api.mcevent.PlayerDisplayGuiPageEvent;
import de.minigameslib.mclib.api.mcevent.PlayerEnteredZoneEvent;
import de.minigameslib.mclib.api.mcevent.PlayerEntersZoneEvent;
import de.minigameslib.mclib.api.mcevent.PlayerGuiClickEvent;
import de.minigameslib.mclib.api.mcevent.PlayerLeavesZoneEvent;
import de.minigameslib.mclib.api.mcevent.PlayerLeftZoneEvent;
import de.minigameslib.mclib.api.mcevent.PlayerOpenGuiEvent;
import de.minigameslib.mclib.api.mcevent.ZoneCreateEvent;
import de.minigameslib.mclib.api.mcevent.ZoneCreatedEvent;
import de.minigameslib.mclib.api.mcevent.ZoneDeleteEvent;
import de.minigameslib.mclib.api.mcevent.ZoneDeletedEvent;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
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
    protected final List<MgEventListener> listeners = new ArrayList<>();
    
    /**
     * Constructor.
     */
    public AbstractEventSystem()
    {
        this.registerHandler(EntityEnteredZoneEvent.class, (evt) -> new MgEntityEnteredZoneEvent(evt));
        this.registerHandler(EntityEntersZoneEvent.class, (evt) -> new MgEntityEntersZoneEvent(evt));
        this.registerHandler(EntityLeavesZoneEvent.class, (evt) -> new MgEntityLeavesZoneEvent(evt));
        this.registerHandler(EntityLeftZoneEvent.class, (evt) -> new MgEntityLeftZoneEvent(evt));
        this.registerHandler(PlayerCloseGuiEvent.class, (evt) -> new MgPlayerCloseGuiEvent(evt));
        this.registerHandler(PlayerDisplayGuiPageEvent.class, (evt) -> new MgPlayerDisplayGuiPageEvent(evt));
        this.registerHandler(PlayerEnteredZoneEvent.class, (evt) -> new MgPlayerEnteredZoneEvent(evt));
        this.registerHandler(PlayerEntersZoneEvent.class, (evt) -> new MgPlayerEntersZoneEvent(evt));
        this.registerHandler(PlayerGuiClickEvent.class, (evt) -> new MgPlayerGuiClickEvent(evt));
        this.registerHandler(PlayerLeavesZoneEvent.class, (evt) -> new MgPlayerLeavesZoneEvent(evt));
        this.registerHandler(PlayerLeftZoneEvent.class, (evt) -> new MgPlayerLeftZoneEvent(evt));
        this.registerHandler(PlayerOpenGuiEvent.class, (evt) -> new MgPlayerOpenGuiEvent(evt));
        this.registerHandler(ZoneCreatedEvent.class, (evt) -> new MgZoneCreatedEvent(evt));
        this.registerHandler(ZoneCreateEvent.class, (evt) -> new MgZoneCreateEvent(evt));
        this.registerHandler(ZoneDeletedEvent.class, (evt) -> new MgZoneDeletedEvent(evt));
        this.registerHandler(ZoneDeleteEvent.class, (evt) -> new MgZoneDeleteEvent(evt));
        // TODO Sign create/delete
        // TODO Entity create/delete
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
    
    /**
     * Event handler for EntityEnteredZoneEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onEntityEnteredZoneEvent(EntityEnteredZoneEvent evt)
    {
        this.getHandler(EntityEnteredZoneEvent.class).handle(evt);
    }
    
    /**
     * Event handler for EntityEntersZoneEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onEntityEntersZoneEvent(EntityEntersZoneEvent evt)
    {
        this.getHandler(EntityEntersZoneEvent.class).handle(evt);
    }
    
    /**
     * Event handler for EntityLeavesZoneEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onEntityLeavesZoneEvent(EntityLeavesZoneEvent evt)
    {
        this.getHandler(EntityLeavesZoneEvent.class).handle(evt);
    }
    
    /**
     * Event handler for EntityLeftZoneEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onEntityLeftZoneEvent(EntityLeftZoneEvent evt)
    {
        this.getHandler(EntityLeftZoneEvent.class).handle(evt);
    }
    
    /**
     * Event handler for PlayerEnteredZoneEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onPlayerEnteredZoneEvent(PlayerEnteredZoneEvent evt)
    {
        this.getHandler(PlayerEnteredZoneEvent.class).handle(evt);
    }
    
    /**
     * Event handler for PlayerEntersZoneEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onPlayerEntersZoneEvent(PlayerEntersZoneEvent evt)
    {
        this.getHandler(PlayerEntersZoneEvent.class).handle(evt);
    }
    
    /**
     * Event handler for PlayerLeavesZoneEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onPlayerLeavesZoneEvent(PlayerLeavesZoneEvent evt)
    {
        this.getHandler(PlayerLeavesZoneEvent.class).handle(evt);
    }
    
    /**
     * Event handler for PlayerLeftZoneEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onPlayerLeftZoneEvent(PlayerLeftZoneEvent evt)
    {
        this.getHandler(PlayerLeftZoneEvent.class).handle(evt);
    }
    
    /**
     * Event handler for ZoneCreateEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onZoneCreate(ZoneCreateEvent evt)
    {
        this.getHandler(ZoneCreateEvent.class).handle(evt);
    }
    
    /**
     * Event handler for ZoneDeletedEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onZoneDeleted(ZoneDeletedEvent evt)
    {
        this.getHandler(ZoneDeletedEvent.class).handle(evt);
    }
    
    /**
     * Event handler for ZoneDeleteEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onZoneDelete(ZoneDeleteEvent evt)
    {
        this.getHandler(ZoneDeleteEvent.class).handle(evt);
    }
    
    /**
     * Event handler for PlayerCloseGuiEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onPlayerCloseGui(PlayerCloseGuiEvent evt)
    {
        this.getHandler(PlayerCloseGuiEvent.class).handle(evt);
    }
    
    /**
     * Event handler for PlayerDisplayGuiPageEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onPlayerDisplayGuiPage(PlayerDisplayGuiPageEvent evt)
    {
        this.getHandler(PlayerDisplayGuiPageEvent.class).handle(evt);
    }
    
    /**
     * Event handler for PlayerGuiClickEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onPlayerGuiClick(PlayerGuiClickEvent evt)
    {
        this.getHandler(PlayerGuiClickEvent.class).handle(evt);
    }
    
    /**
     * Event handler for PlayerOpenGuiEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onPlayerOpenGui(PlayerOpenGuiEvent evt)
    {
        this.getHandler(PlayerOpenGuiEvent.class).handle(evt);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <Evt extends Event, MgEvt extends MinecraftEvent<Evt, MgEvt>> MgEvt createEvent(Evt bukkitEvent)
    {
        return ((MinecraftEventHandler<Evt, MgEvt>) this.getHandler(bukkitEvent.getClass())).createMgEvent(bukkitEvent);
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
            final MgEvt mgevt = this.createMgEvent(evt);
            for (MgEventListener listener : AbstractEventSystem.this.listeners)
            {
                listener.handle(this.cls, mgevt);
            }
            
            final McPlayerInterface player = mgevt.getPlayer();
            if (player instanceof MgEventListener)
            {
                ((MgEventListener) player).handle(this.cls, mgevt);
            }
            
            final ZoneInterface arena = mgevt.getZone();
            if (arena instanceof MgEventListener)
            {
                ((MgEventListener) arena).handle(this.cls, mgevt);
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
