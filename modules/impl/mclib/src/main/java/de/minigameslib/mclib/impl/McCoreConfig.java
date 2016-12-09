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

package de.minigameslib.mclib.impl;

import de.minigameslib.mclib.api.config.ConfigComment;
import de.minigameslib.mclib.api.config.ConfigurationBool;
import de.minigameslib.mclib.api.config.ConfigurationString;
import de.minigameslib.mclib.api.config.ConfigurationValueInterface;
import de.minigameslib.mclib.api.config.ConfigurationValues;

/**
 * Core configuration values.
 * @author mepeisen
 */
@ConfigurationValues(path = "core")
public enum McCoreConfig implements ConfigurationValueInterface
{
    
    /**
     * Default locale of minigames library
     */
    @ConfigurationString(defaultValue = "en")
    @ConfigComment({
        "The default language for all players not having a preferred language.",
        "Set this to an ISO 639 alpha-2 or alpha-3 language code.",
        "For example 'en' or 'de'."})
    DefaultLocale,
    
    /**
     * Debugging flag of minigames library
     */
    @ConfigurationBool(defaultValue = false)
    @ConfigComment({"Flag to activate/deactivate debugging output."})
    Debug,
    
    /**
     * Name of this server within bungee cord networks
     */
    @ConfigurationString(defaultValue = "myself")
    @ConfigComment({"Name of this server within bungeecord networks"})
    BungeeServerName,
    
}
