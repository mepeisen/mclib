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

package de.minigameslib.mclib.api.gui;

import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.McException;

/**
 * An interface to build a smart anvil gui to fetch strings.
 * 
 * @author mepeisen
 */
public interface AnvilGuiInterface
{
    
    /**
     * Returns an internal unique id that is used to identify this gui.
     * 
     * @return internal id to identify this gui.
     */
    AnvilGuiId getUniqueId();
    
    /**
     * Returns the item to be edited.
     * 
     * @return item; initial name must be the display name in meta data.
     */
    ItemStack getItem();
    
    /**
     * Returns the initial output item.
     * 
     * @return item; initial name must be the display name in meta data.
     */
    default ItemStack getOutputItem()
    {
        return null;
    }
    
    /**
     * Handles input.
     * 
     * @param input
     *            the string input
     * @throws McException
     *             thrown if the string input is not accepted by this gui
     */
    void onInput(String input) throws McException;
    
    /**
     * Handles a cancel.
     */
    void onCancel();
    
}
