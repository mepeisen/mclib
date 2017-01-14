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
import de.minigameslib.mclib.api.mcevent.ComponentCreateEvent;
import de.minigameslib.mclib.api.mcevent.ComponentCreatedEvent;
import de.minigameslib.mclib.api.mcevent.ComponentDeleteEvent;
import de.minigameslib.mclib.api.mcevent.ComponentDeletedEvent;
import de.minigameslib.mclib.api.mcevent.ComponentRelocateEvent;
import de.minigameslib.mclib.api.mcevent.ComponentRelocatedEvent;
import de.minigameslib.mclib.api.mcevent.EntityCreateEvent;
import de.minigameslib.mclib.api.mcevent.EntityCreatedEvent;
import de.minigameslib.mclib.api.mcevent.EntityDeleteEvent;
import de.minigameslib.mclib.api.mcevent.EntityDeletedEvent;
import de.minigameslib.mclib.api.mcevent.EntityEnteredZoneEvent;
import de.minigameslib.mclib.api.mcevent.EntityLeftZoneEvent;
import de.minigameslib.mclib.api.mcevent.PlayerCloseGuiEvent;
import de.minigameslib.mclib.api.mcevent.PlayerDisplayGuiPageEvent;
import de.minigameslib.mclib.api.mcevent.PlayerEnteredZoneEvent;
import de.minigameslib.mclib.api.mcevent.PlayerGuiClickEvent;
import de.minigameslib.mclib.api.mcevent.PlayerLeftZoneEvent;
import de.minigameslib.mclib.api.mcevent.PlayerOpenGuiEvent;
import de.minigameslib.mclib.api.mcevent.SignCreateEvent;
import de.minigameslib.mclib.api.mcevent.SignCreatedEvent;
import de.minigameslib.mclib.api.mcevent.SignDeleteEvent;
import de.minigameslib.mclib.api.mcevent.SignDeletedEvent;
import de.minigameslib.mclib.api.mcevent.SignRelocateEvent;
import de.minigameslib.mclib.api.mcevent.SignRelocatedEvent;
import de.minigameslib.mclib.api.mcevent.ZoneCreateEvent;
import de.minigameslib.mclib.api.mcevent.ZoneCreatedEvent;
import de.minigameslib.mclib.api.mcevent.ZoneDeleteEvent;
import de.minigameslib.mclib.api.mcevent.ZoneDeletedEvent;
import de.minigameslib.mclib.api.mcevent.ZoneRelocateEvent;
import de.minigameslib.mclib.api.mcevent.ZoneRelocatedEvent;
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
    public final List<MgEventListener> listeners = new ArrayList<>();
    
    /**
     * Constructor.
     */
    public AbstractEventSystem()
    {
        this.registerHandler(ComponentCreatedEvent.class, (evt) -> new MgComponentCreatedEvent(evt));
        this.registerHandler(ComponentCreateEvent.class, (evt) -> new MgComponentCreateEvent(evt));
        this.registerHandler(ComponentDeletedEvent.class, (evt) -> new MgComponentDeletedEvent(evt));
        this.registerHandler(ComponentDeleteEvent.class, (evt) -> new MgComponentDeleteEvent(evt));
        this.registerHandler(ComponentRelocatedEvent.class, (evt) -> new MgComponentRelocatedEvent(evt));
        this.registerHandler(ComponentRelocateEvent.class, (evt) -> new MgComponentRelocateEvent(evt));
        this.registerHandler(EntityCreatedEvent.class, (evt) -> new MgEntityCreatedEvent(evt));
        this.registerHandler(EntityCreateEvent.class, (evt) -> new MgEntityCreateEvent(evt));
        this.registerHandler(EntityDeletedEvent.class, (evt) -> new MgEntityDeletedEvent(evt));
        this.registerHandler(EntityDeleteEvent.class, (evt) -> new MgEntityDeleteEvent(evt));
        this.registerHandler(EntityEnteredZoneEvent.class, (evt) -> new MgEntityEnteredZoneEvent(evt));
        this.registerHandler(EntityLeftZoneEvent.class, (evt) -> new MgEntityLeftZoneEvent(evt));
        this.registerHandler(PlayerCloseGuiEvent.class, (evt) -> new MgPlayerCloseGuiEvent(evt));
        this.registerHandler(PlayerDisplayGuiPageEvent.class, (evt) -> new MgPlayerDisplayGuiPageEvent(evt));
        this.registerHandler(PlayerEnteredZoneEvent.class, (evt) -> new MgPlayerEnteredZoneEvent(evt));
        this.registerHandler(PlayerGuiClickEvent.class, (evt) -> new MgPlayerGuiClickEvent(evt));
        this.registerHandler(PlayerLeftZoneEvent.class, (evt) -> new MgPlayerLeftZoneEvent(evt));
        this.registerHandler(PlayerOpenGuiEvent.class, (evt) -> new MgPlayerOpenGuiEvent(evt));
        this.registerHandler(SignCreatedEvent.class, (evt) -> new MgSignCreatedEvent(evt));
        this.registerHandler(SignCreateEvent.class, (evt) -> new MgSignCreateEvent(evt));
        this.registerHandler(SignDeletedEvent.class, (evt) -> new MgSignDeletedEvent(evt));
        this.registerHandler(SignDeleteEvent.class, (evt) -> new MgSignDeleteEvent(evt));
        this.registerHandler(SignRelocatedEvent.class, (evt) -> new MgSignRelocatedEvent(evt));
        this.registerHandler(SignRelocateEvent.class, (evt) -> new MgSignRelocateEvent(evt));
        this.registerHandler(ZoneCreatedEvent.class, (evt) -> new MgZoneCreatedEvent(evt));
        this.registerHandler(ZoneCreateEvent.class, (evt) -> new MgZoneCreateEvent(evt));
        this.registerHandler(ZoneDeletedEvent.class, (evt) -> new MgZoneDeletedEvent(evt));
        this.registerHandler(ZoneDeleteEvent.class, (evt) -> new MgZoneDeleteEvent(evt));
        this.registerHandler(ZoneRelocatedEvent.class, (evt) -> new MgZoneRelocatedEvent(evt));
        this.registerHandler(ZoneRelocateEvent.class, (evt) -> new MgZoneRelocateEvent(evt));
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
     * Event handler for ComponentCreatedEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onComponentCreated(ComponentCreatedEvent evt)
    {
        this.getHandler(ComponentCreatedEvent.class).handle(evt);
    }
    
    /**
     * Event handler for ComponentCreateEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onComponentCreate(ComponentCreateEvent evt)
    {
        this.getHandler(ComponentCreateEvent.class).handle(evt);
    }
    
    /**
     * Event handler for ComponentDeletedEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onComponentDeleted(ComponentDeletedEvent evt)
    {
        this.getHandler(ComponentDeletedEvent.class).handle(evt);
    }
    
    /**
     * Event handler for ComponentDeleteEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onComponentDelete(ComponentDeleteEvent evt)
    {
        this.getHandler(ComponentDeleteEvent.class).handle(evt);
    }
    
    /**
     * Event handler for ComponentRelocatedEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onComponentRelocated(ComponentRelocatedEvent evt)
    {
        this.getHandler(ComponentRelocatedEvent.class).handle(evt);
    }
    
    /**
     * Event handler for ComponentRelocateEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onComponentRelocate(ComponentRelocateEvent evt)
    {
        this.getHandler(ComponentRelocateEvent.class).handle(evt);
    }
    
    /**
     * Event handler for EntityCreatedEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onEntityCreated(EntityCreatedEvent evt)
    {
        this.getHandler(EntityCreatedEvent.class).handle(evt);
    }
    
    /**
     * Event handler for EntityCreateEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onEntityCreate(EntityCreateEvent evt)
    {
        this.getHandler(EntityCreateEvent.class).handle(evt);
    }
    
    /**
     * Event handler for EntityDeletedEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onEntityDeleted(EntityDeletedEvent evt)
    {
        this.getHandler(EntityDeletedEvent.class).handle(evt);
    }
    
    /**
     * Event handler for EntityDeleteEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onEntityDelete(EntityDeleteEvent evt)
    {
        this.getHandler(EntityDeleteEvent.class).handle(evt);
    }
    
    /**
     * Event handler for SignCreatedEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onSignCreated(SignCreatedEvent evt)
    {
        this.getHandler(SignCreatedEvent.class).handle(evt);
    }
    
    /**
     * Event handler for SignCreateEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onSignCreate(SignCreateEvent evt)
    {
        this.getHandler(SignCreateEvent.class).handle(evt);
    }
    
    /**
     * Event handler for SignDeletedEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onSignDeleted(SignDeletedEvent evt)
    {
        this.getHandler(SignDeletedEvent.class).handle(evt);
    }
    
    /**
     * Event handler for SignDeleteEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onSignDelete(SignDeleteEvent evt)
    {
        this.getHandler(SignDeleteEvent.class).handle(evt);
    }
    
    /**
     * Event handler for SignRelocatedEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onSignRelocated(SignRelocatedEvent evt)
    {
        this.getHandler(SignRelocatedEvent.class).handle(evt);
    }
    
    /**
     * Event handler for SignRelocateEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onSignRelocate(SignRelocateEvent evt)
    {
        this.getHandler(SignRelocateEvent.class).handle(evt);
    }
    
    /**
     * Event handler for ZoneCreatedEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onZoneCreated(ZoneCreatedEvent evt)
    {
        this.getHandler(ZoneCreatedEvent.class).handle(evt);
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
     * Event handler for ZoneRelocatedEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onZoneRelocated(ZoneRelocatedEvent evt)
    {
        this.getHandler(ZoneRelocatedEvent.class).handle(evt);
    }
    
    /**
     * Event handler for ZoneRelocateEvent event.
     * 
     * @param evt
     *            the event to be passed.
     */
    @EventHandler
    public void onZoneRelocate(ZoneRelocateEvent evt)
    {
        this.getHandler(ZoneRelocateEvent.class).handle(evt);
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
            
            // TODO component etc.
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
