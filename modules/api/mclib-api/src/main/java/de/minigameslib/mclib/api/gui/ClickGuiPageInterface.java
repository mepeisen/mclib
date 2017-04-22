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

import java.io.Serializable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.CommonMessages;

/**
 * A single gui page.
 * 
 * @author mepeisen
 *
 */
public interface ClickGuiPageInterface
{
    
    /** the max col count. */
    int COL_COUNT = 9;
    
    /**
     * Returns the name of the inventory.
     * 
     * @return inventory name. Either string or a localized message
     */
    Serializable getPageName();
    
    /**
     * Returns the click items.
     * @return click items; first array dimension is the line; second the column.
     */
    ClickGuiItem[][] getItems();

    /**
     * dummy fill item
     * @param line
     * @param col
     * @return dummy fill icon
     */
    static ClickGuiItem itemFill(int line, int col)
    {
        byte color = 0;
        if ((line * COL_COUNT + col) % 2 == 1) color = 1;
        return new ClickGuiItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, color), CommonMessages.IconFill, (player, session, gui) -> {/*empty*/});
    }
    
    /**
     * Creates an inventory with filler icons
     * @param src
     * @param line
     * @return inventory
     */
    static ClickGuiItem[] withFillers(ClickGuiItem[] src, int line)
    {
        final ClickGuiItem[] result = new ClickGuiItem[COL_COUNT];
        for (int col = 0; col < COL_COUNT; col++)
        {
            if (src != null && col < src.length && src[line] != null)
            {
                result[col] = src[col];
            }
            else
            {
                result[col] = itemFill(line, col);
            }
        }
        return result;
    }
    
    /**
     * Creates an inventory with filler icons
     * @param src
     * @param lineCount
     * @return inventory
     */
    static ClickGuiItem[][] withFillers(ClickGuiItem[][] src, int lineCount)
    {
        final ClickGuiItem[][] result = new ClickGuiItem[lineCount][];
        for (int line = 0; line < lineCount; line++)
        {
            result[line] = withFillers(line < src.length ? src[line] : null, line);
        }
        return result;
    }
    
}
