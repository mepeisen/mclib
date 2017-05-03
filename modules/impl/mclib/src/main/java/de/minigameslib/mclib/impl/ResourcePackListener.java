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

package de.minigameslib.mclib.impl;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import de.minigameslib.mclib.api.items.ResourceServiceInterface.ResourcePackStatus;
import de.minigameslib.mclib.impl.items.ItemServiceImpl;

/**
 * The listener for resource pack status.
 * 
 * @author mepeisen
 */
class ResourcePackListener extends AbstractResourcePackListener implements Listener
{
    
    /**
     * @param players
     * @param itemService
     */
    public ResourcePackListener(PlayerRegistry players, ItemServiceImpl itemService)
    {
        super(players, itemService);
    }
    
    /**
     * Resource pack event
     * 
     * @param evt
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onResourcePack(PlayerResourcePackStatusEvent evt)
    {
        super.accept(evt.getPlayer(), ResourcePackStatus.valueOf(evt.getStatus().name()));
    }
    
}
