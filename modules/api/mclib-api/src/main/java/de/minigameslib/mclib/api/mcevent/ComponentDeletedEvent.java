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

package de.minigameslib.mclib.api.mcevent;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.minigameslib.mclib.api.objects.ComponentInterface;

/**
 * An event showing up that a component was deleted.
 * 
 * @author mepeisen
 */
public class ComponentDeletedEvent extends Event
{
    
    /** handlers list. */
    private static final HandlerList handlers = new HandlerList();
    
    /** the component we created. */
    private final ComponentInterface     component;
    
    /**
     * Constructor.
     * 
     * @param component
     *            the deleted component.
     */
    public ComponentDeletedEvent(ComponentInterface component)
    {
        this.component = component;
    }
    
    /**
     * Returns the component that was deleted
     * 
     * @return the deleted component
     */
    public ComponentInterface getComponent()
    {
        return this.component;
    }
    
    /**
     * Returns the handlers list
     * 
     * @return handlers
     */
    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }
    
    /**
     * Returns the handlers list
     * 
     * @return handlers
     */
    public static HandlerList getHandlerList()
    {
        return handlers;
    }
    
}