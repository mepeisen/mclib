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

import org.bukkit.Location;
import org.bukkit.event.HandlerList;

import de.minigameslib.mclib.api.objects.SignInterface;

/**
 * An event fired before a sign is relocated.
 * 
 * <p>
 * This event can be cancelled. If cancelled the sign will not be relocated.
 * </p>
 * 
 * @author mepeisen
 */
public class SignRelocateEvent extends AbstractVetoEvent
{
    
    /** handlers list. */
    private static final HandlerList handlers = new HandlerList();
    
    /** the sign we created. */
    private final SignInterface     sign;
    
    /** the old location. */
    private final Location oldLocation;
    
    /** the new location. */
    private final Location newLocation;
    
    /**
     * Constructor.
     * 
     * @param sign
     *            the relocated sign.
     * @param oldLocation 
     * @param newLocation 
     */
    public SignRelocateEvent(SignInterface sign, final Location oldLocation, final Location newLocation)
    {
        this.sign = sign;
        this.oldLocation = oldLocation;
        this.newLocation = newLocation;
    }
    
    /**
     * Returns the sign that was relocated
     * 
     * @return the created sign
     */
    public SignInterface getSign()
    {
        return this.sign;
    }
    
    /**
     * Returns the old location.
     * @return the oldLocation
     */
    public Location getOldCuboid()
    {
        return this.oldLocation;
    }

    /**
     * Returns the new location.
     * @return the newLocation
     */
    public Location getNewCuboid()
    {
        return this.newLocation;
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
