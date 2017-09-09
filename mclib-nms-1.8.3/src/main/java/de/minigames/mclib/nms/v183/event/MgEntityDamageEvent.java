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

package de.minigames.mclib.nms.v183.event;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import de.minigameslib.mclib.api.event.McEntityDamageEvent;
import de.minigameslib.mclib.api.objects.EntityInterface;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.nms.api.AbstractMinigameEvent;

/**
 * Minigame event implementation.
 * 
 * @author mepeisen
 */
public class MgEntityDamageEvent extends AbstractMinigameEvent<EntityDamageEvent, McEntityDamageEvent> implements McEntityDamageEvent
{
    
    /**
     * Constructor.
     * 
     * @param event
     *            the bukkit event
     */
    public MgEntityDamageEvent(EntityDamageEvent event)
    {
        super(event, player(event), location(event));
    }
    
    /**
     * @param event
     *            the bukkit event
     * @return player
     */
    private static McPlayerInterface player(EntityDamageEvent event)
    {
        final Entity passenger = event.getEntity();
        if (passenger instanceof Player)
        {
            return ObjectServiceInterface.instance().isHuman((Player) passenger) ? ObjectServiceInterface.instance().getPlayer((Player) passenger) : null;
        }
        return null;
    }
    
    @Override
    public EntityInterface getEntity()
    {
        return ObjectServiceInterface.instance().findEntity(this.getBukkitEvent().getEntity());
    }
    
    /**
     * @param event
     *            the bukkit event
     * @return arena
     */
    private static ZoneInterface location(EntityDamageEvent event)
    {
        final Entity passenger = event.getEntity();
        if (passenger instanceof Player)
        {
            return null; // will force to calculate from player
        }
        return ObjectServiceInterface.instance().findZone(event.getEntity().getLocation());
    }
    
}
