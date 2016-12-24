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

package de.minigameslib.mclib.shared.api.com;

/**
 * World location fragment.
 * 
 * @author mepeisen
 */
public interface LocationDataFragment extends DataFragment
{

    /**
     * Returns the x coordinate
     * @return the x
     */
    double getX();

    /**
     * Returns the y coordinate
     * @return the y
     */
    double getY();

    /**
     * Returns the z coordinate
     * @return the z
     */
    double getZ();
    
    /**
     * The yaw.
     * @return yaw.
     */
    float getYaw();
    
    /**
     * The pitch.
     * @return pitch.
     */
    float getPitch();
    
    /**
     * Name of the world.
     * @return name of the world.
     */
    String getWorld();
    
}