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
import de.minigameslib.mclib.api.config.ConfigurationInt;
import de.minigameslib.mclib.api.config.ConfigurationString;
import de.minigameslib.mclib.api.config.ConfigurationStringList;
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
     * The main locales
     */
    @ConfigurationStringList(defaultValue = "en")
    @ConfigComment({
        "The main languages available to the players. Informational option only.",
        "Set the list elements to an ISO 639 alpha-2 or alpha-3 language code.",
        "For example 'en' or 'de'."})
    MainLocales,
    
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
    
    /**
     * The default download url of the resource pack (&lt;1.9)
     */
    @ConfigurationString(defaultValue = "http://www.minigameslib.de/mclib/mclib_core_resources_v1.zip")
    @ConfigComment({"The default download url of the resource pack (<1.9)"})
    ResourcePackDownloadUrlV1,
    
    /**
     * The default download url of the resource pack (1.9 and 1.10)
     */
    @ConfigurationString(defaultValue = "http://www.minigameslib.de/mclib/mclib_core_resources_v2.zip")
    @ConfigComment({"The default download url of the resource pack (1.9 and 1.10)"})
    ResourcePackDownloadUrlV2,
    
    /**
     * The default download url of the resource pack (&gt; 1.10)
     */
    @ConfigurationString(defaultValue = "http://www.minigameslib.de/mclib/mclib_core_resources_v3.zip")
    @ConfigComment({"The default download url of the resource pack (>1.10)"})
    ResourcePackDownloadUrlV3,
    
    /**
     * The auto download flag
     */
    @ConfigurationBool(defaultValue = false)
    @ConfigComment({"True to let the users automatically be notified about the resource pack on join"})
    ResourcePackAutoDownload,
    
    /**
     * The auto download ticks
     */
    @ConfigurationInt(defaultValue = 40)
    @ConfigComment({
        "Number of ticks to wait before sending the resource pack notification to clients",
        "Requires RespourcePackAutoDownload to be set to true"})
    ResourcePackAutoDownloadTicks,
    
    /**
     * maximum size of player registry
     */
    @ConfigurationInt(defaultValue = 10000)
    @ConfigComment({"maximum size of player registry", "lower the value if you have memory problems"})
    PlayerRegistrySize,
    
    /**
     * player registry cache timeout in minutes
     */
    @ConfigurationInt(defaultValue = 30)
    @ConfigComment({"player registry cache timeout in minutes", "lower the value if you have memory problems"})
    PlayerRegistryAccessMinutes,
    
}
