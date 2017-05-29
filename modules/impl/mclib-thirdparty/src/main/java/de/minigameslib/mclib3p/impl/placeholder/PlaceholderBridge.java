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

package de.minigameslib.mclib3p.impl.placeholder;

/**
 * Bridge between mclib and placerholder api.
 * 
 * @author mepeisen
 *
 */
public class PlaceholderBridge
{
    
    /**
     * Check for target plugin.
     * @return {@code true} if target plugin is available.
     */
    public static boolean test()
    {
        // TODO version check etc.
        // should work with 2.0.6 and higher
        return true;
    }
    
    /**
     * Setup Bridge.
     */
    public static void onSetup()
    {
        // TODO using some better hook (in mclib there may be multiple identifiers used)
        new MclibPlaceholders().hook();
    }
    
    /**
     * On disable mclib.
     */
    public static void onDisableMclib()
    {
        // nothing to do
    }
    
    /**
     * On disable target plugin.
     */
    public static void onDisablePlugin()
    {
        // TODO
    }
    
}
