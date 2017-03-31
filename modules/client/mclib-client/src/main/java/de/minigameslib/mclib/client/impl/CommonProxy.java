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

package de.minigameslib.mclib.client.impl;

import de.minigameslib.mclib.client.impl.markers.MarkerInterface;
import net.minecraft.client.Minecraft;

/**
 * Client stuff
 * 
 * @author mepeisen
 */
public abstract class CommonProxy
{
    
    /** minecraft instance. */
    protected static Minecraft        mc          = Minecraft.getMinecraft();
    
    /**
     * Constructor.
     */
    public CommonProxy()
    {
        // empty
    }

    /**
     * @param markerId
     * @param marker
     */
    public abstract void setMarker(String markerId, MarkerInterface marker);

    /**
     * @param markerId
     */
    public abstract void removeMarker(String markerId);

    /**
     * 
     */
    public abstract void resetMarkers();
    
    /**
     * Registers the item renderers
     */
    public abstract void registerItemRenderers();
    
}
