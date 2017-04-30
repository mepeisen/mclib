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

package de.minigameslib.mclib.api.cli;

import de.minigameslib.mclib.api.ApiVersion;
import de.minigameslib.mclib.api.objects.McPlayerInterface;

/**
 * Representation of smart gui client for players.
 * 
 * @author mepeisen
 */
public interface ClientInterface
{
    
    /**
     * represents the underlying player for this client.
     * 
     * @return underlying player.
     */
    McPlayerInterface getPlayer();
    
    /**
     * the first api version, all versions up to first release, includes minecraft versions up to 1.11.
     */
    int APIVERSION_1_0_0 = 10000;
    
    /**
     * This api version is not yet used; the api version is compatible to {@link #APIVERSION_1_0_0} but may introduce new features or methods. Use this for version checks (getApiVersion &lt;
     * APIVERSION_1_1_0).
     */
    int APIVERSION_1_1_0 = 10100;
    
    /**
     * This api version is not yet used; the api version is incompatible to {@link #APIVERSION_1_0_0}. Use this for version checks (getApiVersion &lt; APIVERSION_2_0_0).
     */
    int APIVERSION_2_0_0 = 20000;
    
    /**
     * Returns the api version of client.
     * 
     * <p>
     * The api version is found with int constants on this interface. The integer is built with following schematic:
     * </p>
     * <ul>
     * <li>major version number</li>
     * <li>minor version number (to digits)</li>
     * <li>fix level (two digits)</li>
     * </ul>
     * 
     * <p>
     * Different fix levels are returned for new minecraft versions. It indicates that the mclib version type enum contains new entries and that MinigamesLib supports a new minecraft version. For most
     * situations you need not take care about fix levels. You need only check for fix levels if you require a special feature found at newest minecraft versions.
     * </p>
     * 
     * <p>
     * The minor version number is changed if MinigamesLib adds new features or methods to the existing API. You will find an informative annotation (@ApiVersion) for methods being present in a
     * specific api version.
     * </p>
     * 
     * <p>
     * The major version number is changed if MinigamesLib API is completly rewritten.
     * </p>
     * 
     * <p>
     * In most situations it is ok to check for any version below the newest major version. (getApiVersion() &lt; APIVERSION_2_0_0).
     * </p>
     * 
     * @return api version.
     * 
     * @see #APIVERSION_1_0_0
     * @see #APIVERSION_1_1_0
     * @see #APIVERSION_2_0_0
     * @see ApiVersion
     */
    int getApiVersion();
    
}
