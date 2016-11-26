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

import java.util.Locale;

/**
 * Base interface for Minigame Common library.
 * 
 * @author mepeisen
 */
public interface McLibInterface extends McContext
{
    
    /**
     * Returns the library instance.
     * 
     * @return library instance.
     */
    static McLibInterface instance()
    {
        return McLibCache.get();
    }
    
    /**
     * Returns the minecraft version the server is running.
     * 
     * @return minecraft version.
     */
    MinecraftVersionsType getMinecraftVersion();
    
    /**
     * Returns the default locale used in minigame lib.
     * 
     * @return default locale
     */
    Locale getDefaultLocale();
    
    /**
     * Sets the default locale used in minigame lib.
     * 
     * @param locale
     *            the new locale
     * 
     * @throws McException
     *             thrown if the config could not be saved.
     */
    void setDefaultLocale(Locale locale) throws McException;
    
    /**
     * Checks for debug flag.
     * 
     * @return {@code true} if the library debugging is enabled.
     */
    boolean debug();
    
    /**
     * Sets the debug flag.
     * 
     * @param enabled {@code true} if the library debugging is enabled.
     */
    void setDebug(boolean enabled);
    
}
