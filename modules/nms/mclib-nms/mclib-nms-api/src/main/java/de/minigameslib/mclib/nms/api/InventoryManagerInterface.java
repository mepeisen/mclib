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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * An nms inventory manager.
 * 
 * @author mepeisen
 */
public interface InventoryManagerInterface extends Listener
{
    
    /**
     * Opens an inventory.
     * 
     * @param player
     *            the player that is opening the inventory
     * @param name
     *            the name of the inventory
     * @param items
     *            the items within the inventory
     * @param listener
     *            The listener for inventory events.
     * @return inventory reference.
     */
    InventoryHelper openInventory(Player player, String name, ItemStack[] items, InventoryListener listener);
    
    /**
     * Listener for inventories.
     */
    public interface InventoryListener
    {
        
        /**
         * Invoked upon close of the inventory.
         */
        void onClose();
        
        /**
         * Invoked as soon as an item was clicked.
         * 
         * @param item
         *            clicked item.
         * @param rawSlot
         *            the raw slot number
         * @param slot
         *            the slot number in given inventory
         * @return {@code true} to cancel event
         */
        boolean onClick(ItemStack item, int rawSlot, int slot);
        
        /**
         * Sets an item to given slot.
         * 
         * @param index
         *            slot index
         * @param item
         *            new item for this slot.
         */
        void setItem(int index, ItemStack item);
    }
    
    /**
     * Inventory helper.
     */
    public interface InventoryHelper
    {
        /**
         * Forces the plugin to close.
         */
        void close();
        
        /**
         * Returns the bukkit inventory.
         * 
         * @return bukkit inventory
         */
        Inventory getBukkitInventory();
        
        /**
         * Changes the page items and name
         * 
         * @param name
         *            new page name
         * @param items
         *            new items
         */
        void setNewPage(String name, ItemStack[] items);
    }
    
    /**
     * Returns a colored string that hides data from users view.
     * 
     * @param name
     *            item name
     * @param hiddenString
     *            the hidden string
     * @return colored hidden text
     */
    static String toColorsString(String name, String hiddenString)
    {
        final String target = toHexString(hiddenString);
        final StringBuilder builder = new StringBuilder();
        if (name != null)
        {
            builder.append(name);
        }
        builder.append(' ');
        for (int i = 0; i < target.length(); i++)
        {
            builder.append('§');
            builder.append(target.charAt(i));
        }
        return builder.toString();
    }
    
    /**
     * Strips the string that was originally encoded by toColorsString
     * 
     * @param src
     * @return hiddenString
     */
    static String stripColoredString(String src)
    {
        int index = src.lastIndexOf(' ');
        final StringBuilder hex = new StringBuilder();
        for (int i = index + 1; i < src.length(); i += 2)
        {
            hex.append(src.substring(i + 1, i + 2));
        }
        return fromHexString(hex.toString());
    }
    
    /**
     * Converts given string to hey code.
     * 
     * @param src
     *            source string
     * @return hex string
     */
    static String toHexString(String src)
    {
        final byte[] ba = src.getBytes();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < ba.length; i++)
        {
            str.append(String.format("%02x", ba[i])); //$NON-NLS-1$
        }
        return str.toString();
    }
    
    /**
     * Converts given hex string to a normal string.
     * 
     * @param hex
     *            hex string
     * @return normal string.
     */
    static String fromHexString(String hex)
    {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2)
        {
            str.append((char) Integer.parseInt(hex.substring(i, i + 2), 16));
        }
        return str.toString();
    }
    
}
