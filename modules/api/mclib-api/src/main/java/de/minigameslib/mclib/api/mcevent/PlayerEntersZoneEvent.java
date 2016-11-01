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

package de.minigameslib.mclib.api.mcevent;

import org.bukkit.event.HandlerList;

import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;

/**
 * An event fired before a player enters a zone.
 * 
 * <p>
 * This event can be cancelled. If cancelled the player movement/teleport will be cancelled.
 * </p>
 * 
 * @author mepeisen
 */
public class PlayerEntersZoneEvent extends AbstractVetoEvent
{
    
    /** handlers list. */
    private static final HandlerList handlers = new HandlerList();
    
    /** the zone that was entered. */
    private final ZoneInterface      zone;
    
    /** the player entering the zone. */
    private final McPlayerInterface  player;
    
    /**
     * Constructor.
     * 
     * @param zone
     *            the entered zone.
     * @param player
     *            the player entering the zone
     */
    public PlayerEntersZoneEvent(ZoneInterface zone, McPlayerInterface player)
    {
        this.zone = zone;
        this.player = player;
    }
    
    /**
     * Returns the zone that was entered
     * 
     * @return the entered arena
     */
    public ZoneInterface getZone()
    {
        return this.zone;
    }
    
    /**
     * Returns the player that was entering the zone
     * 
     * @return the entering player
     */
    public McPlayerInterface getPlayer()
    {
        return this.player;
    }
    
    /**
     * Returns the handlers list
     * 
     * @return handlers
     */
    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }
    
    /**
     * Returns the handlers list
     * 
     * @return handlers
     */
    public static HandlerList getHandlerList()
    {
        return handlers;
    }
    
}
