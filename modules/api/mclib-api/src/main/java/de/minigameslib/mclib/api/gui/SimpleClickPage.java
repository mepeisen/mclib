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

import de.minigameslib.mclib.api.locale.LocalizedMessageInterface;

/**
 * A simple click gui interface.
 * 
 * @author mepeisen
 */
public class SimpleClickPage implements ClickGuiPageInterface
{
    
    /** the page name. */
    private final LocalizedMessageInterface pageName;
    
    /** the page items. */
    private final ClickGuiItem[][] items;

    /**
     * Constructor
     * @param pageName
     * @param rowCount
     */
    public SimpleClickPage(LocalizedMessageInterface pageName, int rowCount)
    {
        this.pageName = pageName;
        if (rowCount < 1 || rowCount > 6) throw new IllegalArgumentException("rowCount out of range"); //$NON-NLS-1$
        this.items = new ClickGuiItem[rowCount][];
        for (int i = 0; i < rowCount; i++)
        {
            this.items[i] = new ClickGuiItem[9]; 
        }
    }

    @Override
    public LocalizedMessageInterface getPageName()
    {
        return this.pageName;
    }
    
    @Override
    public ClickGuiItem[][] getItems()
    {
        return this.items;
    }
    
    /**
     * Sets the item at given position
     * @param rowNum row position (between 0 and rowCount-1) 
     * @param colNum column position (between 0 and 8)
     * @param item the item to set
     * @return this object for chaining
     */
    public SimpleClickPage setItem(int rowNum, int colNum, ClickGuiItem item)
    {
        this.items[rowNum][colNum] = item;
        return this;
    }
    
}
