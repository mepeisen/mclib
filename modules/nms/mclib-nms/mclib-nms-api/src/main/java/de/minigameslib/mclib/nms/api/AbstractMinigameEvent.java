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

import org.bukkit.Location;
import org.bukkit.event.Event;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.api.util.function.FalseStub;
import de.minigameslib.mclib.api.util.function.McOutgoingStubbing;
import de.minigameslib.mclib.api.util.function.McPredicate;
import de.minigameslib.mclib.api.util.function.TrueStub;

/**
 * Base Implementation for minigame events.
 * 
 * @author mepeisen
 * @param <EVT>
 *            minecraft event class
 * @param <MGEVT>
 *            minigame event class
 */
public abstract class AbstractMinigameEvent<EVT extends Event, MGEVT extends MinecraftEvent<EVT, MGEVT>> implements MinecraftEvent<EVT, MGEVT>
{
    
    /** the bukkit event object. */
    private EVT               event;
    
    /** the player for this event. */
    private McPlayerInterface player;
    
    /** the zone interface. */
    private ZoneInterface     zone;
    
    /**
     * Abstract minigame event.
     * 
     * @param event
     *            the event.
     * @param player
     *            player involved in this event
     */
    public AbstractMinigameEvent(EVT event, McPlayerInterface player)
    {
        this.event = event;
        this.player = player;
        this.zone = player == null ? null : this.player.getZone();
    }
    
    /**
     * Abstract minigame event.
     * 
     * @param event
     *            the event.
     * @param zone
     *            zone involved in this event
     */
    public AbstractMinigameEvent(EVT event, ZoneInterface zone)
    {
        this.event = event;
        this.zone = zone;
    }
    
    /**
     * Abstract minigame event.
     * 
     * @param event
     *            the event.
     * @param player
     *            player involved in this event
     * @param zone
     *            zone involved in this event
     */
    public AbstractMinigameEvent(EVT event, McPlayerInterface player, ZoneInterface zone)
    {
        this.event = event;
        this.player = player;
        this.zone = zone;
    }
    
    /**
     * Abstract minigame event.
     * 
     * @param event
     *            the event.
     * @param player
     *            player involved in this event
     * @param location
     *            location involved in this event
     */
    public AbstractMinigameEvent(EVT event, McPlayerInterface player, Location location)
    {
        this.event = event;
        this.player = player;
        this.zone = ObjectServiceInterface.instance().findZone(location);
    }
    
    @Override
    public EVT getBukkitEvent()
    {
        return this.event;
    }
    
    @Override
    public ZoneInterface getZone()
    {
        return this.zone == null && this.player != null ? this.player.getZone() : this.zone;
    }
    
    @Override
    public McPlayerInterface getPlayer()
    {
        return this.player;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public McOutgoingStubbing<MGEVT> when(McPredicate<MGEVT> test) throws McException
    {
        if (test.test((MGEVT) this))
        {
            return new TrueStub<>((MGEVT) this);
        }
        return new FalseStub<>((MGEVT) this);
    }
    
}
