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

package de.minigames.mclib.nms.v185.event;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

import de.minigameslib.mclib.api.event.McInventoryMoveItemEvent;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;
import de.minigameslib.mclib.nms.api.AbstractMinigameEvent;

/**
 * Minigame event implementation.
 * 
 * @author mepeisen
 */
public class MgInventoryMoveItemEvent extends AbstractMinigameEvent<InventoryMoveItemEvent, McInventoryMoveItemEvent> implements McInventoryMoveItemEvent
{
    
    /**
     * Constructor.
     * 
     * @param event
     *            the bukkit event
     */
    public MgInventoryMoveItemEvent(InventoryMoveItemEvent event)
    {
        super(event, getPlayer(event));
    }
    
    /**
     * @param event
     *            the bukkit event
     * @return player
     */
    private static McPlayerInterface getPlayer(InventoryMoveItemEvent event)
    {
        if (event.getInitiator() != null && event.getInitiator().getViewers().size() > 0)
        {
            return ObjectServiceInterface.instance().getPlayer((Player) event.getInitiator().getViewers().get(0)); // TODO correct to invoke get(0)?
        }
        return null;
    }
    
}
