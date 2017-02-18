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

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.McStorage;
import de.minigameslib.mclib.api.items.ItemServiceInterface.ResourcePackStatus;
import de.minigameslib.mclib.impl.items.ItemServiceImpl;
import de.minigameslib.mclib.impl.items.ItemServiceImpl.ResourcePackMarker;

/**
 * The listener for resource pack status.
 * 
 * @author mepeisen
 */
class ResourcePackListener implements Listener
{
    
    /** the player registry. */
    PlayerRegistry                                                       players;
    
    /**
     * the item service impl
     */
    ItemServiceImpl                                                      itemService;

    
    /**
     * @param players
     * @param itemService
     */
    public ResourcePackListener(PlayerRegistry players, ItemServiceImpl itemService)
    {
        this.players = players;
        this.itemService = itemService;
    }


    /**
     * Resource pack event
     * 
     * @param evt
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onResourcePack(PlayerResourcePackStatusEvent evt)
    {
        final McPlayerImpl player = this.players.getPlayer(evt.getPlayer());
        final McStorage sessionStorage = player.getSessionStorage();
        final ResourcePackMarker marker = sessionStorage.get(ResourcePackMarker.class);
        if (marker == null)
        {
            sessionStorage.set(ResourcePackMarker.class, new ResourcePackMarker(ResourcePackStatus.valueOf(evt.getStatus().name())));
        }
        else
        {
            marker.setState(ResourcePackStatus.valueOf(evt.getStatus().name()));
            switch (evt.getStatus())
            {
                default:
                case ACCEPTED:
                    break;
                case DECLINED:
                    if (marker.getDeclined() != null)
                    {
                        try
                        {
                            marker.getDeclined().run();
                        }
                        catch (McException ex)
                        {
                            player.sendMessage(ex.getErrorMessage(), ex.getArgs());
                        }
                    }
                    break;
                case FAILED_DOWNLOAD:
                    if (marker.getFailure() != null)
                    {
                        try
                        {
                            marker.getFailure().run();
                        }
                        catch (McException ex)
                        {
                            player.sendMessage(ex.getErrorMessage(), ex.getArgs());
                        }
                    }
                    break;
                case SUCCESSFULLY_LOADED:
                    if (marker.getSuccess() != null)
                    {
                        try
                        {
                            marker.getSuccess().run();
                        }
                        catch (McException ex)
                        {
                            player.sendMessage(ex.getErrorMessage(), ex.getArgs());
                        }
                    }
                    break;
            }
        }
        
        this.itemService.clearTools(player.getBukkitPlayer().getInventory());
    }
    
}
