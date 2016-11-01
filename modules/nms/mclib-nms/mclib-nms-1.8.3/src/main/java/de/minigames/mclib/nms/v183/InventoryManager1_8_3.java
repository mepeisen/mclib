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

package de.minigames.mclib.nms.v183;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R2.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.nms.api.InventoryManagerInterface;
import net.minecraft.server.v1_8_R2.EntityPlayer;

/**
 * Inventory manager implementation.
 * 
 * @author mepeisen
 */
public class InventoryManager1_8_3 implements InventoryManagerInterface
{

    @Override
    public Inventory openInventory(Player player, String name, ItemStack[] items)
    {
        final Inventory inventory = Bukkit.createInventory(null, items.length, name);
        inventory.setContents(items);
        final EntityPlayer entity = ((CraftPlayer)player).getHandle();
        if (entity.activeContainer != entity.defaultContainer)
        {
            CraftEventFactory.handleInventoryCloseEvent(entity);
            entity.activeContainer = entity.defaultContainer;
        }
        player.openInventory(inventory);
        return inventory;
    }
    
}
