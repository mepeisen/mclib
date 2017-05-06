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
import org.bukkit.event.entity.EntityCombustByEntityEvent;

import de.minigameslib.mclib.api.event.McEntityCombustByEntityEvent;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.api.objects.ZoneInterface;
import de.minigameslib.mclib.nms.api.AbstractMinigameEvent;

/**
 * Minigame event implementation
 * 
 * @author mepeisen
 */
public class MgEntityCombustByEntityEvent extends AbstractMinigameEvent<EntityCombustByEntityEvent, McEntityCombustByEntityEvent> implements McEntityCombustByEntityEvent
{
    
    /**
     * Constructor
     * 
     * @param event
     */
    public MgEntityCombustByEntityEvent(EntityCombustByEntityEvent event)
    {
        super(event, player(event), location(event));
    }
    
    /**
     * @param event
     * @return player
     */
    private static McPlayerInterface player(EntityCombustByEntityEvent event)
    {
        final Entity passenger = event.getEntity();
        return passenger instanceof Player ? ObjectServiceInterface.instance().getPlayer((Player) passenger) : null;
    }
    
    /**
     * @param event
     * @return arena
     */
    private static ZoneInterface location(EntityCombustByEntityEvent event)
    {
        final Entity passenger = event.getEntity();
        if (passenger instanceof Player)
        {
            return null; // will force to calculate from player
        }
        return ObjectServiceInterface.instance().findZone(event.getEntity().getLocation());
    }
    
}
