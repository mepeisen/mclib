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

package de.minigameslib.mclib.api.ext;

import org.bukkit.plugin.Plugin;

import de.minigameslib.mclib.api.McContext;

/**
 * Extension services.
 * 
 * @author mepeisen
 */
public interface ExtensionServiceInterface extends McContext
{
    
    /**
     * Returns the extension services instance.
     * 
     * @return extension services instance.
     */
    static ExtensionServiceInterface instance()
    {
        return ExtensionServiceCache.get();
    }
    
    /**
     * Registers given extension
     * @param plugin java plugin declaring the extension
     * @param extPoint target extension point
     * @param extension extension to register
     */
    <T extends ExtensionInterface> void register(Plugin plugin, ExtensionPointInterface<T> extPoint, T extension);
    
    /**
     * Removes a registered extension
     * @param plugin java plugin declaring the extension
     * @param extPoint target extension point
     * @param extension extension to remove
     */
    <T extends ExtensionInterface> void remove(Plugin plugin, ExtensionPointInterface<T> extPoint, T extension);
    
    /**
     * Removes all extension points used by given plugin.
     * @param plugin
     */
    void removeAllExtensions(Plugin plugin);
    
    /**
     * Returns the extensions declared by plugins for given extension point.
     * @param extPoint extension point.
     * @return extensions.
     * @param <T> extension class
     */
    <T extends ExtensionInterface> Iterable<T> getExtensions(ExtensionPointInterface<? extends T> extPoint);
    
}
