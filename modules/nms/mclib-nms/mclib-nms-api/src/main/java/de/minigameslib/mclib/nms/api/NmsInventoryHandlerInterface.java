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

package de.minigameslib.mclib.nms.api;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * NMS Helper for supporting inventory callbacks.
 * 
 * @author mepeisen
 *
 */
public interface NmsInventoryHandlerInterface
{
    
    /**
     * place handler.
     * 
     * @param location
     *            target location
     */
    void onPlace(Location location);
    
    /**
     * post place handler.
     * 
     * @param location
     *            target location
     * @param stack
     *            item stack to be used
     * @param player
     *            target player.
     */
    void onPostPlace(Location location, ItemStack stack, Player player);
    
    /**
     * break handler.
     * 
     * @param location
     *            target location.
     */
    void onBreak(Location location);
    
    /**
     * right click handler.
     * 
     * @param location
     *            target location.
     * @param bukkitEntity
     *            bukkt entity that interacts with chest
     * @param mainHand
     *            {@coede true} for using main hand and {@code false} for using off hand
     * @param blockFace
     *            the block face
     * @param hitX
     *            x pos for clicking
     * @param hitY
     *            y pos for clicking
     * @param hitZ
     *            z pos for clicking
     * @return {@code true} on success
     */
    boolean onInteract(Location location, HumanEntity bukkitEntity, boolean mainHand, BlockFace blockFace, float hitX, float hitY, float hitZ);
    
}
