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

package de.minigameslib.mclib.client.impl.gui.widgets;

import java.util.HashMap;
import java.util.Map;

import de.matthiasmann.twl.Widget;

/**
 * @author mepeisen
 *
 */
public class MultiWidget extends Widget
{
    
    /**
     * the child map.
     */
    private Map<String, Widget> childMap = new HashMap<>();
    
    /**
     * Constructor
     */
    public MultiWidget()
    {
        this.setTheme("-defaults"); //$NON-NLS-1$
    }
    
    /**
     * Adds a widget with given window id
     * @param winId
     * @param child
     */
    public void addWidget(String winId, Widget child)
    {
        this.childMap.put(winId, child);
        this.add(child);
    }
    
    /**
     * Removes a widget with given window id
     * @param winId
     */
    public void removeWidget(String winId)
    {
        final Widget widget = this.childMap.remove(winId);
        if (widget != null)
        {
            this.removeChild(widget);
        }
    }
    
    /**
     * Returns a widget with given id
     * @param winId
     * @return widget or {@code null} if it was not found
     */
    public Widget getWidget(String winId)
    {
        return this.childMap.get(winId);
    }
    
}
