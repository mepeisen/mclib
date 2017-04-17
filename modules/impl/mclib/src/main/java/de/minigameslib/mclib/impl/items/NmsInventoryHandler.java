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

package de.minigameslib.mclib.impl.items;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.items.BlockInventory;
import de.minigameslib.mclib.nms.api.NmsInventoryHandlerInterface;

/**
 * @author mepeisen
 *
 */
public class NmsInventoryHandler implements NmsInventoryHandlerInterface
{

    /**
     * @param variantInv
     */
    public NmsInventoryHandler(BlockInventory variantInv)
    {
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.nms.api.NmsInventoryHandlerInterface#onPlace(org.bukkit.Location)
     */
    @Override
    public void onPlace(Location location)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.nms.api.NmsInventoryHandlerInterface#onPostPlace(org.bukkit.Location, org.bukkit.inventory.ItemStack, org.bukkit.entity.Player)
     */
    @Override
    public void onPostPlace(Location location, ItemStack asCraftMirror, Player player)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.nms.api.NmsInventoryHandlerInterface#onBreak(org.bukkit.Location)
     */
    @Override
    public void onBreak(Location location)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.minigameslib.mclib.nms.api.NmsInventoryHandlerInterface#onInteract(org.bukkit.Location, org.bukkit.entity.HumanEntity, boolean, org.bukkit.block.BlockFace, float, float, float)
     */
    @Override
    public boolean onInteract(Location location, HumanEntity bukkitEntity, boolean mainHand, BlockFace blockFace, float hitX, float hitY, float hitZ)
    {
        // TODO Auto-generated method stub
        return false;
    }
    
}
