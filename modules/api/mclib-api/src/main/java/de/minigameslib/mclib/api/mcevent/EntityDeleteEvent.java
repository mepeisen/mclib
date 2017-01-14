/*
    Copyright 2016 by minigameslib.de
    All rights reserved.
    If you do not own a hand-Entityed commercial license from minigames.de
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

import de.minigameslib.mclib.api.objects.EntityInterface;

/**
 * An event fired before an existing entity is deleted.
 * 
 * <p>
 * This event can be cancelled. If cancelled the entity will not be deleted.
 * </p>
 * 
 * <p>
 * Notice: This event is related to the McLib objects manager. It does not cover the minecraft entities themselves.
 * </p>
 * 
 * @author mepeisen
 */
public class EntityDeleteEvent extends AbstractVetoEvent
{
    
    /** handlers list. */
    private static final HandlerList handlers = new HandlerList();
    
    /** the zone we deleted. */
    private final EntityInterface     entity;
    
    /**
     * Constructor.
     * 
     * @param entity
     *            the deleted entity.
     */
    public EntityDeleteEvent(EntityInterface entity)
    {
        this.entity = entity;
    }
    
    /**
     * Returns the entity that was deleted
     * 
     * @return the deleted entity
     */
    public EntityInterface getEntity()
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
