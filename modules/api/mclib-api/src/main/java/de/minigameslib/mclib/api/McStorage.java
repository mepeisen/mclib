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

package de.minigameslib.mclib.api;

import de.minigameslib.mclib.api.config.Configurable;

/**
 * The minigame storage can be used on various objects (for example players) to store temporary or persistent data.
 * 
 * @author mepeisen
 */
public interface McStorage
{
    
    /**
     * Returns a storage variable.
     * 
     * @param clazz
     *            the class of the variable to be returned.
     * @return Storage variable or {@code null} if the variable was not set.
     * @param <T>
     *            Configurable object class
     */
    <T extends Configurable> T get(Class<T> clazz);
    
    /**
     * Sets a storage variable.
     * 
     * @param clazz
     *            the class of the variable
     * @param value
     *            the new value
     * @param <T>
     *            Configurable object class
     */
    <T extends Configurable> void set(Class<T> clazz, T value);
    
}
