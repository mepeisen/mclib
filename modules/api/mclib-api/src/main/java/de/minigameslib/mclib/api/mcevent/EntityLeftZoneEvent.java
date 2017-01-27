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

import java.util.Collection;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.event.MinecraftEvent;
import de.minigameslib.mclib.api.objects.EntityInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.api.util.function.FalseStub;
import de.minigameslib.mclib.api.util.function.McOutgoingStubbing;
import de.minigameslib.mclib.api.util.function.McPredicate;
import de.minigameslib.mclib.api.util.function.TrueStub;

/**
 * An event fired after an entity left a zone.
 * 
 * @author mepeisen
 */
public class EntityLeftZoneEvent extends Event implements MinecraftEvent<EntityLeftZoneEvent, EntityLeftZoneEvent>
{
    
    /** handlers list. */
    private static final HandlerList handlers = new HandlerList();
    
    /** the zone that was left. */
    private final ZoneInterface      zone;
    
    /** the entity leaving the zone. */
    private final Entity  entity;
    
    /** mclib entity */
    private final EntityInterface mcEntity;
    
    /** mclib entities */
    private final Collection<EntityInterface> mcEntities;
    
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
        this.mcEntities = ObjectServiceInterface.instance().findEntities(entity);
        this.mcEntity = this.mcEntities.isEmpty() ? null : this.mcEntities.iterator().next();
    }
    
    @Override
    public ZoneInterface getZone()
    {
        return this.zone;
    }
    
    /**
     * Returns the entity that was leaving the zone
     * 
     * @return the leaving entity
     */
    public Entity getBukkitEntity()
    {
        return this.entity;
    }
    
    @Override
    public EntityInterface getEntity()
    {
        return this.mcEntity;
    }

    @Override
    public Iterable<EntityInterface> getEntities()
    {
        return this.mcEntities;
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

    @Override
    public EntityLeftZoneEvent getBukkitEvent()
    {
        return this;
    }

    @Override
    public McOutgoingStubbing<EntityLeftZoneEvent> when(McPredicate<EntityLeftZoneEvent> test) throws McException
    {
        if (test.test(this))
        {
            return new TrueStub<>(this);
        }
        return new FalseStub<>(this);
    }
    
}
