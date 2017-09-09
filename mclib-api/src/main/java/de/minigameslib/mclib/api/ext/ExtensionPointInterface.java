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

/**
 * An interface for extension points.
 * 
 * <p>
 * Use this in constants.
 * </p>
 * 
 * <p>
 * An extension point can be used for other libraries to add extensions. Common example is the command extension point to inject sub commands for the gui.
 * </p>
 * 
 * @author mepeisen
 * @param <T>
 *            the extension interface to implement.
 */
public interface ExtensionPointInterface<T extends ExtensionInterface>
{
    
    /**
     * Extension point name. Should be made of "&lt;pluginname&gt;/&lt;name&gt;"
     * 
     * @return extension point name.
     */
    String name();
    
    /**
     * Returns the extension class that must be implemented by extensions for this extension point.
     * 
     * @return extension class.
     */
    Class<T> extensionClass();
    
    /**
     * Returns the extensions declared by plugins for this extension point.
     * 
     * @return plugin extensions.
     */
    default Iterable<T> getExtensions()
    {
        return ExtensionServiceInterface.instance().getExtensions(this);
    }
    
}
