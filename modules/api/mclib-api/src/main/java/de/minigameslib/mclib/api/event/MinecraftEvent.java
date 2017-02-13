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

package de.minigameslib.mclib.api.event;

import java.util.Collections;

import org.bukkit.event.Event;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.ComponentInterface;
import de.minigameslib.mclib.api.objects.EntityInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.objects.SignInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.api.util.function.McOutgoingStubbing;
import de.minigameslib.mclib.api.util.function.McPredicate;

/**
 * Mc event helper.
 * 
 * @author mepeisen
 * 
 * @param <Evt> Event class
 * @param <MgEvt> Minigame event class
 */
public interface MinecraftEvent<Evt extends Event, MgEvt extends MinecraftEvent<Evt, MgEvt>>
{
    
    /**
     * Returns the original event
     * @return original event this rule 
     */
    Evt getBukkitEvent();
    
    /**
     * Returns the object causing this event.
     * @return object causing this event or {@code null} if this event was not caused by an object.
     */
    default ObjectInterface getObject()
    {
        return null;
    }
    
    /**
     * Returns the zone causing this event.
     * @return zone causing this event or {@code null} if this event was outside any zone.
     */
    default ZoneInterface getZone()
    {
        return null;
    }
    
    /**
     * Returns the player causing this event.
     * @return player causing this event or {@code null} if this event was not caused by any player.
     */
    default McPlayerInterface getPlayer()
    {
        return null;
    }
    
    /**
     * Returns the sign causing this event.
     * @return sign causing this event or {@code null} if this event was not involving a sign.
     */
    default SignInterface getSign()
    {
        return null;
    }
    
    /**
     * Returns the entity causing this event.
     * @return entity causing this event or {@code null} if this event was not involving an entity.
     */
    default EntityInterface getEntity()
    {
        final McPlayerInterface player = this.getPlayer();
        return player == null ? null : ObjectServiceInterface.instance().findEntity(this.getPlayer().getBukkitPlayer());
    }
    
    /**
     * Returns the component causing this event.
     * @return component causing this event or {@code null} if this event was not involving a component.
     */
    default ComponentInterface getComponent()
    {
        return null;
    }
    
    /**
     * Returns the objects associated with this event.
     * @return object list
     */
    default Iterable<ObjectInterface> getObjects()
    {
        final ObjectInterface result = this.getObject();
        if (result == null)
        {
            return Collections.emptyList();
        }
        return Collections.singleton(result);
    }
    
    /**
     * Returns the zones associated with this event.
     * @return zone list
     */
    default Iterable<ZoneInterface> getZones()
    {
        final ZoneInterface result = this.getZone();
        if (result == null)
        {
            return Collections.emptyList();
        }
        return Collections.singleton(result);
    }
    
    /**
     * Returns the signs associated with this event.
     * @return sign list
     */
    default Iterable<SignInterface> getSigns()
    {
        final SignInterface result = this.getSign();
        if (result == null)
        {
            return Collections.emptyList();
        }
        return Collections.singleton(result);
    }
    
    /**
     * Returns the components associated with this event.
     * @return component list
     */
    default Iterable<ComponentInterface> getComponents()
    {
        final ComponentInterface result = this.getComponent();
        if (result == null)
        {
            return Collections.emptyList();
        }
        return Collections.singleton(result);
    }
    
    /**
     * Returns the players associated with this event.
     * @return player list
     */
    default Iterable<McPlayerInterface> getPlayers()
    {
        final McPlayerInterface result = this.getPlayer();
        if (result == null)
        {
            return Collections.emptyList();
        }
        return Collections.singleton(result);
    }
    
    /**
     * Returns the entities associated with this event.
     * @return entity list
     */
    default Iterable<EntityInterface> getEntities()
    {
        final EntityInterface result = this.getEntity();
        if (result == null)
        {
            return Collections.emptyList();
        }
        return Collections.singleton(result);
    }
    
    // stubbing
    
    /**
     * Checks this event for given criteria and invokes either then or else statements.
     * 
     * <p>
     * NOTICE: If the test function throws an exception it will be re thrown and no then or else statement will be invoked.
     * </p>
     * 
     * @param test
     *            test functions for testing the event matching any criteria.
     * 
     * @return the outgoing stub to apply then or else consumers.
     * 
     * @throws McException
     *             will be thrown if either the test function or then/else consumers throw the exception.
     */
    McOutgoingStubbing<MgEvt> when(McPredicate<MgEvt> test) throws McException;
    
}
