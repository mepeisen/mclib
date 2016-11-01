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

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.minigameslib.mclib.api.objects.ZoneInterface;

/**
 * An event fired after an entity left a zone.
 * 
 * @author mepeisen
 */
public class EntityLeftZoneEvent extends Event
{
    
    /** handlers list. */
    private static final HandlerList handlers = new HandlerList();
    
    /** the zone that was left. */
    private final ZoneInterface      zone;
    
    /** the entity leaving the zone. */
    private final Entity  entity;
    
    /**
     * Constructor.
     * 
     * @param zone
     *            the left zone.
     * @param entity
     *            the entity leaving the zone
     */
    public EntityLeftZoneEvent(ZoneInterface zone, Entity entity)
    {
        this.zone = zone;
        this.entity = entity;
    }
    
    /**
     * Returns the zone that was left
     * 
     * @return the left arena
     */
    public ZoneInterface getZone()
    {
        return this.zone;
    }
    
    /**
     * Returns the entity that was leaving the zone
     * 
     * @return the leaving entity
     */
    public Entity getentity()
    {
        return this.entity;
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
