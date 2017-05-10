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

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

/**
 * Support anvil containers.
 * 
 * @author mepeisen
 */
public interface AnvilManagerInterface extends Listener
{
    
    /**
     * Opens an anvil gui.
     * 
     * @param player
     *            player that opens the gui
     * @param stack
     *            the initial item with name
     * @param listener
     *            the listener
     * @return avil inventory helper
     */
    AnvilHelper openGui(Player player, ItemStack stack, AnvilListener listener);
    
    /**
     * Listener for inventories.
     */
    public interface AnvilListener
    {
        /**
         * Invoked upon close of the inventory.
         */
        void onClose();
        
        /**
         * Invoked as soon as the name was commited.
         * 
         * @param name
         *            the new name.
         * @return {@code true} to accept the name and close the inventory; {@code false} to reject the name and leave the inventory open.
         */
        boolean onCommit(String name);
    }
    
    /**
     * Inventory helper.
     */
    public interface AnvilHelper
    {
        /**
         * Forces the plugin to close.
         */
        void close();
    }
    
}
