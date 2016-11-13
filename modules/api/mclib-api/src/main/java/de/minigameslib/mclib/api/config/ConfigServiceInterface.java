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

package de.minigameslib.mclib.api.config;

import de.minigameslib.mclib.api.McContext;

/**
 * Configuration services.
 * 
 * @author mepeisen
 */
public interface ConfigServiceInterface extends McContext
{
    
    /**
     * Returns the configuration services instance.
     * 
     * @return configuration services instance.
     */
    static ConfigServiceInterface instance()
    {
        return ConfigServiceCache.get();
    }
    
    /**
     * Returns the configuration declaring the given configuration value.
     * 
     * @param item
     *            the configuration value; only works on classes that are returned by a plugin or extension provider during initialization.
     * 
     * @return config provider or {@code null} if the class was not declared by any minigame or extension.
     */
    ConfigInterface getConfigFromCfg(ConfigurationValueInterface item);
    
}