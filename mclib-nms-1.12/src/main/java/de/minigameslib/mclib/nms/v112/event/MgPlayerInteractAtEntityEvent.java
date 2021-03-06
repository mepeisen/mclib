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

package de.minigameslib.mclib.nms.v112.event;

import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import de.minigameslib.mclib.api.event.McPlayerInteractAtEntityEvent;
import de.minigameslib.mclib.api.objects.EntityInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.nms.api.AbstractMinigameEvent;

/**
 * Minigame event implementation.
 * 
 * @author mepeisen
 */
public class MgPlayerInteractAtEntityEvent extends AbstractMinigameEvent<PlayerInteractAtEntityEvent, McPlayerInteractAtEntityEvent> implements McPlayerInteractAtEntityEvent
{
    
    /**
     * Constructor.
     * 
     * @param event
     *            the bukkit event
     */
    public MgPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event)
    {
        super(event, ObjectServiceInterface.instance().getPlayer(event.getPlayer()));
    }
    
    @Override
    public EntityInterface getEntity()
    {
        return ObjectServiceInterface.instance().findEntity(this.getBukkitEvent().getRightClicked());
    }
    
    @Override
    public boolean isOffhand()
    {
        return this.getBukkitEvent().getHand() == EquipmentSlot.OFF_HAND;
    }
    
}
