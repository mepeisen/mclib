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

package de.minigameslib.mclib.api.enums;

import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.shared.api.com.EnumerationValue;

/**
 * Listener for watching newly registered or removed enumerations.
 * 
 * @author mepeisen
 * @param <T>
 *            enumeration class
 */
public interface EnumerationListener<T extends EnumerationValue>
{
    
    /**
     * On registered enumeration.
     * 
     * @param plugin
     *            plugin that registered enumerations
     * @param clazz
     *            enumeration interface
     * @param values
     *            registered values
     */
    void onEnumRegistered(Plugin plugin, Class<? extends T> clazz, T[] values);
    
    /**
     * on removed enumeration.
     * 
     * @param plugin
     *            plugin that removed enumerations
     * @param clazz
     *            enumeration interface
     * @param values
     *            removed values
     */
    void onEnumRemoved(Plugin plugin, Class<? extends T> clazz, T[] values);
    
}
